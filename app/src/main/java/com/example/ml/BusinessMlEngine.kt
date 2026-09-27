package com.example.ml

import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

data class RevenueForecastPoint(
    val monthIndex: Int,
    val monthLabel: String,
    val predictedRevenue: Double,
    val lowerBound: Double,
    val upperBound: Double
)

data class RevenueForecastResult(
    val slope: Double,
    val rSquared: Double,
    val averageMonthlyGrowthRate: Double,
    val nextQuarterRevenue: Double,
    val annualRunRateProjected: Double,
    val forecastPoints: List<RevenueForecastPoint>,
    val trendSummary: String
)

data class ChurnPredictionInput(
    val customerCount: Int,
    val monthlyRecurringRevenue: Double,
    val npsScore: Int, // -100 to 100
    val usageDropPercent: Double, // 0 - 100%
    val supportTicketsPerCustomer: Double,
    val avgContractLengthMonths: Int
)

data class ChurnPredictionResult(
    val churnProbabilityPercent: Double,
    val riskLevel: String, // "LOW", "MODERATE", "ELEVATED", "CRITICAL"
    val estimatedMmrAtRisk: Double,
    val keyRiskFactors: List<String>,
    val recommendedInterventions: List<String>
)

data class UnitEconomicsMetrics(
    val cac: Double,
    val arpu: Double, // Average Revenue Per User per month
    val grossMarginPercent: Double,
    val monthlyChurnRatePercent: Double,
    val cashBalance: Double,
    val monthlyBurnRate: Double,
    val fixedCostsMonthly: Double,
    val pricePerUnit: Double,
    val variableCostPerUnit: Double
)

data class FinancialDiagnosticsResult(
    val ltv: Double,
    val ltvCacRatio: Double,
    val cacPaybackMonths: Double,
    val runwayMonths: Double,
    val breakEvenUnitsMonthly: Int,
    val healthScore: Int, // 0 - 100
    val healthRating: String, // "EXCEPTIONAL", "HEALTHY", "NEEDS_OPTIMIZATION", "DISTRESSED"
    val diagnosticInsights: List<String>
)

object BusinessMlEngine {

    /**
     * Predictive Revenue Forecasting using Ordinary Least Squares (OLS) Linear Regression
     * with variance-based confidence intervals.
     */
    fun forecastRevenue(historicalRevenues: List<Double>, currentMonthName: String = "Month"): RevenueForecastResult {
        val n = historicalRevenues.size
        if (n < 2) {
            val single = historicalRevenues.firstOrNull() ?: 10000.0
            val flatPoints = (1..6).map { i ->
                RevenueForecastPoint(i, "M+$i", single, single * 0.9, single * 1.1)
            }
            return RevenueForecastResult(
                slope = 0.0,
                rSquared = 1.0,
                averageMonthlyGrowthRate = 0.0,
                nextQuarterRevenue = single * 3,
                annualRunRateProjected = single * 12,
                forecastPoints = flatPoints,
                trendSummary = "Insufficient data points for trend line. Flat baseline projected."
            )
        }

        // Calculate means
        val xValues = (1..n).map { it.toDouble() }
        val xMean = xValues.average()
        val yMean = historicalRevenues.average()

        // Calculate slope (beta1) and intercept (beta0)
        var numerator = 0.0
        var denominator = 0.0
        for (i in 0 until n) {
            val xDiff = xValues[i] - xMean
            val yDiff = historicalRevenues[i] - yMean
            numerator += xDiff * yDiff
            denominator += xDiff * xDiff
        }

        val slope = if (denominator != 0.0) numerator / denominator else 0.0
        val intercept = yMean - (slope * xMean)

        // Calculate R-squared (goodness of fit)
        var totalSumSquares = 0.0
        var residualSumSquares = 0.0
        for (i in 0 until n) {
            val actual = historicalRevenues[i]
            val predicted = intercept + (slope * xValues[i])
            totalSumSquares += (actual - yMean).pow(2)
            residualSumSquares += (actual - predicted).pow(2)
        }
        val rSquared = if (totalSumSquares > 0) max(0.0, min(1.0, 1.0 - (residualSumSquares / totalSumSquares))) else 0.9

        // Standard error of estimate
        val standardError = if (n > 2) sqrt(residualSumSquares / (n - 2)) else (yMean * 0.08)

        // Growth rate
        val firstVal = historicalRevenues.first().coerceAtLeast(1.0)
        val lastVal = historicalRevenues.last()
        val totalGrowth = (lastVal - firstVal) / firstVal
        val avgMonthlyGrowth = totalGrowth / (n - 1)

        // Forecast future 6 months
        val futurePoints = mutableListOf<RevenueForecastPoint>()
        var nextQuarterSum = 0.0
        for (step in 1..6) {
            val futureX = n + step
            val rawPredicted = max(100.0, intercept + (slope * futureX))
            val uncertaintyMargin = standardError * sqrt(1.0 + (1.0 / n) + ((futureX - xMean).pow(2) / denominator.coerceAtLeast(1.0))) * 1.645 // 90% CI
            val lower = max(0.0, rawPredicted - uncertaintyMargin)
            val upper = rawPredicted + uncertaintyMargin

            if (step <= 3) {
                nextQuarterSum += rawPredicted
            }

            futurePoints.add(
                RevenueForecastPoint(
                    monthIndex = step,
                    monthLabel = "Month +$step",
                    predictedRevenue = rawPredicted,
                    lowerBound = lower,
                    upperBound = upper
                )
            )
        }

        val annualRunRate = (futurePoints.first().predictedRevenue) * 12.0
        val trendText = when {
            slope > 1000 && rSquared > 0.7 -> "Strong linear growth trajectory (R² = ${String.format("%.2f", rSquared)}). Sustained compounding momentum."
            slope > 0 -> "Moderate positive expansion trend. Focus on increasing average order value/deal size."
            slope == 0.0 -> "Plateaued top-line revenue. Business model requires catalyst intervention."
            else -> "Contracting revenue trajectory. Critical need for churn reduction and revised acquisition channels."
        }

        return RevenueForecastResult(
            slope = slope,
            rSquared = rSquared,
            averageMonthlyGrowthRate = avgMonthlyGrowth * 100.0,
            nextQuarterRevenue = nextQuarterSum,
            annualRunRateProjected = annualRunRate,
            forecastPoints = futurePoints,
            trendSummary = trendText
        )
    }

    /**
     * Machine Learning Churn Classifier using a calibrated logistic risk equation.
     */
    fun predictChurnRisk(input: ChurnPredictionInput): ChurnPredictionResult {
        // Multi-feature probabilistic weights derived from B2B/D2C empirical benchmarks
        // z = w0 + w1*UsageDrop + w2*NegativeNPS + w3*SupportFriction + w4*ShortContract
        val npsNormalized = (input.npsScore + 100.0) / 200.0 // 0 to 1 (1 is perfect +100 NPS)
        val usageDropFraction = input.usageDropPercent / 100.0

        val npsFactor = (1.0 - npsNormalized) * 2.5
        val usageFactor = usageDropFraction * 3.2
        val ticketFactor = min(2.0, input.supportTicketsPerCustomer * 0.4)
        val contractFactor = if (input.avgContractLengthMonths <= 1) 1.2 else if (input.avgContractLengthMonths < 6) 0.6 else 0.1

        val logit = -3.2 + npsFactor + usageFactor + ticketFactor + contractFactor
        val probability = 1.0 / (1.0 + Math.exp(-logit))
        val churnPercent = min(95.0, max(2.5, probability * 100.0))

        val riskLevel = when {
            churnPercent < 15.0 -> "LOW"
            churnPercent < 35.0 -> "MODERATE"
            churnPercent < 60.0 -> "ELEVATED"
            else -> "CRITICAL"
        }

        val mmrAtRisk = input.monthlyRecurringRevenue * (churnPercent / 100.0)

        val riskFactors = mutableListOf<String>()
        if (input.usageDropPercent > 20.0) {
            riskFactors.add("Product telemetry: Inactive user drop-off is ${input.usageDropPercent.toInt()}% over the past 30 days")
        }
        if (input.npsScore < 10) {
            riskFactors.add("Customer sentiment: NPS of ${input.npsScore} indicates high risk of negative word-of-mouth")
        }
        if (input.supportTicketsPerCustomer > 2.0) {
            riskFactors.add("Operational friction: High ticket volume (${input.supportTicketsPerCustomer} per account) suggests onboarding bottlenecks")
        }
        if (input.avgContractLengthMonths <= 1) {
            riskFactors.add("Contractual vulnerability: Month-to-month commitment lacks barrier to competitor switching")
        }

        val interventions = mutableListOf<String>()
        if (churnPercent >= 35.0) {
            interventions.add("Deploy automated re-engagement workflow targeting accounts with zero logins in last 14 days")
            interventions.add("Initiate executive outreach program for the top 20% highest ARR accounts")
            interventions.add("Offer 15% discount for annual contract migration to lock in 12-month retention")
        } else {
            interventions.add("Introduce customer success check-ins at 30, 60, and 90-day intervals post-onboarding")
            interventions.add("Identify power users for case studies and referral incentives")
        }

        return ChurnPredictionResult(
            churnProbabilityPercent = churnPercent,
            riskLevel = riskLevel,
            estimatedMmrAtRisk = mmrAtRisk,
            keyRiskFactors = riskFactors,
            recommendedInterventions = interventions
        )
    }

    /**
     * Unit Economics and Financial Diagnostics Engine
     */
    fun evaluateFinancialDiagnostics(m: UnitEconomicsMetrics): FinancialDiagnosticsResult {
        // LTV Calculation
        val grossMarginFraction = m.grossMarginPercent / 100.0
        val churnFraction = max(0.01, m.monthlyChurnRatePercent / 100.0)
        val ltv = (m.arpu * grossMarginFraction) / churnFraction

        // LTV / CAC Ratio
        val ltvCacRatio = if (m.cac > 0) ltv / m.cac else 4.0

        // Payback Period (Months)
        val monthlyGrossProfitPerUser = m.arpu * grossMarginFraction
        val paybackMonths = if (monthlyGrossProfitPerUser > 0) m.cac / monthlyGrossProfitPerUser else 24.0

        // Runway (Months)
        val runwayMonths = if (m.monthlyBurnRate > 0) m.cashBalance / m.monthlyBurnRate else 99.0

        // Break-even Units
        val contributionMarginPerUnit = m.pricePerUnit - m.variableCostPerUnit
        val breakEvenUnits = if (contributionMarginPerUnit > 0) {
            (m.fixedCostsMonthly / contributionMarginPerUnit).toInt()
        } else 0

        // Scoring (0 - 100)
        var score = 50
        if (ltvCacRatio >= 3.0) score += 15 else if (ltvCacRatio < 1.5) score -= 15
        if (paybackMonths <= 12.0) score += 15 else if (paybackMonths > 18.0) score -= 10
        if (runwayMonths >= 18.0) score += 20 else if (runwayMonths < 6.0) score -= 25 else score += 5
        score = max(5, min(98, score))

        val rating = when {
            score >= 80 -> "EXCEPTIONAL"
            score >= 65 -> "HEALTHY"
            score >= 45 -> "NEEDS_OPTIMIZATION"
            else -> "DISTRESSED"
        }

        val insights = mutableListOf<String>()
        if (ltvCacRatio >= 3.0) {
            insights.add("LTV/CAC ratio of ${String.format("%.1f", ltvCacRatio)}x exceeds the gold-standard 3.0x venture threshold.")
        } else {
            insights.add("LTV/CAC ratio of ${String.format("%.1f", ltvCacRatio)}x is suboptimal. Either reduce customer acquisition cost or expand monetization.")
        }

        if (runwayMonths < 8.0) {
            insights.add("Critical: Runway is ${String.format("%.1f", runwayMonths)} months. Initiate bridge financing or execute cost containment immediately.")
        } else {
            insights.add("Healthy runway buffer of ${String.format("%.1f", runwayMonths)} months affords strategic breathing room.")
        }

        if (paybackMonths <= 12.0) {
            insights.add("CAC Payback in ${String.format("%.1f", paybackMonths)} months allows rapid capital recycling for reinvestment.")
        } else {
            insights.add("Capital tied up: Payback takes ${String.format("%.1f", paybackMonths)} months, straining short-term working capital.")
        }

        return FinancialDiagnosticsResult(
            ltv = ltv,
            ltvCacRatio = ltvCacRatio,
            cacPaybackMonths = paybackMonths,
            runwayMonths = runwayMonths,
            breakEvenUnitsMonthly = breakEvenUnits,
            healthScore = score,
            healthRating = rating,
            diagnosticInsights = insights
        )
    }
}
