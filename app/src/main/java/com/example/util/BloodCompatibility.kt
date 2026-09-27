package com.example.util

object BloodCompatibility {
    val ALL_BLOOD_GROUPS = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    val ALL_COMPONENTS = listOf("WHOLE_BLOOD", "PLASMA", "PLATELETS", "RBC")

    fun getCompatibleDonorsForRecipient(recipientGroup: String): List<String> {
        return when (recipientGroup) {
            "A+" -> listOf("A+", "A-", "O+", "O-")
            "A-" -> listOf("A-", "O-")
            "B+" -> listOf("B+", "B-", "O+", "O-")
            "B-" -> listOf("B-", "O-")
            "AB+" -> ALL_BLOOD_GROUPS
            "AB-" -> listOf("AB-", "A-", "B-", "O-")
            "O+" -> listOf("O+", "O-")
            "O-" -> listOf("O-")
            else -> listOf(recipientGroup)
        }
    }

    fun isDonorCompatible(donorGroup: String, recipientGroup: String): Boolean {
        return getCompatibleDonorsForRecipient(recipientGroup).contains(donorGroup)
    }

    fun getDonorCooldownRemainingDays(lastDonationEpochMs: Long?): Long {
        if (lastDonationEpochMs == null) return 0L
        val cooldownPeriodMs = 90L * 24L * 60L * 60L * 1000L
        val elapsed = System.currentTimeMillis() - lastDonationEpochMs
        if (elapsed >= cooldownPeriodMs) return 0L
        val remainingMs = cooldownPeriodMs - elapsed
        return (remainingMs / (24L * 60L * 60L * 1000L)) + 1
    }

    fun isCooldownActive(lastDonationEpochMs: Long?): Boolean {
        return getDonorCooldownRemainingDays(lastDonationEpochMs) > 0
    }
}
