package com.example.util

import java.security.MessageDigest

object SecurityUtil {
    private const val SALT = "BloodBridge_Secure_Salt_2026#"

    fun hashPassword(password: String): String {
        val input = "$SALT$password"
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun verifyPassword(password: String, hash: String): Boolean {
        return hashPassword(password) == hash
    }

    fun maskPhoneNumber(phone: String): String {
        val trimmed = phone.trim()
        if (trimmed.length < 4) return "••••"
        val visibleDigits = trimmed.takeLast(3)
        return "••••••$visibleDigits"
    }

    fun getFirstName(fullName: String): String {
        return fullName.trim().split(" ").firstOrNull() ?: fullName
    }
}
