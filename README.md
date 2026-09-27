# BizAdvisor AI – AI-Powered Business Consultant using Machine Learning and Generative AI

> **"Executive Management Consulting, Predictive Analytics, and Strategic Intelligence at Your Fingertips."**

BizAdvisor AI is an intelligent business consultant platform that fuses **Predictive Machine Learning algorithms** and **Gemini Generative AI** to guide founders, executives, and business operators through growth inflection points, financial diagnostics, customer churn mitigation, and strategic planning.

---

## 1. Core Architecture & Modules

### 1. Predictive Machine Learning Engine (On-Device & Hybrid ML)
- **Time-Series Revenue Forecasting (OLS Regression)**:
  - Fits historical monthly financial data using Ordinary Least Squares (OLS) linear regression.
  - Computes velocity slope ($\beta_1$), intercept ($\beta_0$), and $R^2$ goodness-of-fit correlation.
  - Projects monthly revenue for future quarters with 90% confidence uncertainty intervals.
- **Probabilistic Churn Risk Classifier (Logistic Modeling)**:
  - Analyzes customer behavioral telemetry: Net Promoter Score (NPS), 30-day usage drop-off rate, support ticket frequency, and contract longevity.
  - Computes exact Churn Probability Score (0% – 100%) and quantifies Monthly Recurring Revenue (MRR) at risk.
  - Prescribes prioritized, actionable retention interventions.
- **Unit Economics & Financial Diagnostics**:
  - Customer Lifetime Value (CLV): $CLV = \frac{ARPU \times GrossMargin\%}{MonthlyChurn\%}$
  - LTV/CAC Ratio with venture threshold benchmarks (optimal $\ge 3.0x$).
  - CAC Payback Period (in months).
  - Cash Runway and Monthly Net Burn Rate.
  - Break-Even Analysis ($BreakEven = \frac{FixedCosts}{PricePerUnit - VariableCostPerUnit}$).

### 2. Generative AI Strategic Advisory (Gemini Generative AI)
- **Interactive Multi-Turn Advisory Chat**:
  - Context-aware consultation across 5 core corporate practices:
    1. *Strategy & Competitive Moats*
    2. *Go-To-Market & Pricing Models*
    3. *Unit Economics & Capital Runway*
    4. *Product-Market Fit & Retention*
    5. *Operations, Hiring & Scaling*
  - Uses `gemini-2.5-flash` with automatic fallback to on-device expert heuristic synthesis when offline or running prototype keys.
- **9-Box Osterwalder Business Model Canvas Generator**:
  - Synthesizes Value Propositions, Customer Segments, Channels, Customer Relationships, Revenue Streams, Key Activities, Key Resources, Key Partnerships, and Cost Structures.
- **SWOT & Competitive Moats Analysis**:
  - Evaluates internal Strengths & Weaknesses alongside external Opportunities & Threats.
- **30-60-90 Day Execution Roadmaps**:
  - Phased milestone roadmaps: Phase 1 (Foundation & Unit Economics), Phase 2 (Growth Acceleration & Expansion), Phase 3 (Scale & Capital Readiness), complete with target KPIs.

### 3. Pre-Loaded Executive Business Profiles
- **Apex AI Systems**: B2B SaaS / Enterprise AI ($85k MRR, scaling phase).
- **GreenRoots Organics**: E-Commerce / D2C Sustainable Brands ($42k revenue, customer acquisition optimization).
- **HealthPulse Network**: Concierge Healthcare & Clinical Network ($115k revenue, multi-location expansion).
- **Custom Profile Builder**: Input your own startup/business parameters for personalized ML and AI consulting.

---

## 2. Technology Stack

- **Platform**: Android (minSdk 24, targetSdk 36)
- **Language**: Kotlin 2.2.10
- **UI Framework**: Jetpack Compose with Material Design 3 (M3)
- **Architecture**: MVVM + Repository Pattern
- **Local Persistence**: Room Database (SQLite) with Coroutines and StateFlow
- **AI & Networking**: Gemini REST API + OkHttp + Retrofit + Secrets Gradle Plugin (`BuildConfig.GEMINI_API_KEY`)

---

## 3. How to Configure the Gemini API Key

1. Open the **Secrets panel** in the Google AI Studio UI.
2. Add your secret key named `GEMINI_API_KEY`.
3. The platform automatically injects this key into `BuildConfig.GEMINI_API_KEY` via `.env`.
4. If running locally without an external key, BizAdvisor AI seamlessly employs its built-in expert heuristics engine so all advisory features remain fully operational.
