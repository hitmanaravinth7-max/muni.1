package com.example.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "password_hash") val passwordHash: String,
    @ColumnInfo(name = "phone_number") val phoneNumber: String,
    @ColumnInfo(name = "role") val role: String = "USER", // "USER" or "ADMIN"
    @ColumnInfo(name = "account_status") val accountStatus: String = "ACTIVE", // "ACTIVE", "BLOCKED"
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "donor_profiles",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["user_id"], unique = true)]
)
data class DonorProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "blood_group") val bloodGroup: String, // A+, A-, B+, B-, AB+, AB-, O+, O-
    @ColumnInfo(name = "date_of_birth") val dateOfBirth: String, // YYYY-MM-DD
    @ColumnInfo(name = "gender") val gender: String, // MALE, FEMALE, OTHER
    @ColumnInfo(name = "weight_kg") val weightKg: Double,
    @ColumnInfo(name = "address_line") val addressLine: String,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "state") val state: String,
    @ColumnInfo(name = "pincode") val pincode: String,
    @ColumnInfo(name = "latitude") val latitude: Double = 0.0,
    @ColumnInfo(name = "longitude") val longitude: Double = 0.0,
    @ColumnInfo(name = "last_donation_date") val lastDonationDate: Long? = null,
    @ColumnInfo(name = "is_available") val isAvailable: Boolean = true,
    @ColumnInfo(name = "has_chronic_illness") val hasChronicIllness: Boolean = false,
    @ColumnInfo(name = "on_medication") val onMedication: Boolean = false,
    @ColumnInfo(name = "recent_surgery") val recentSurgery: Boolean = false,
    @ColumnInfo(name = "total_donations") val totalDonations: Int = 0,
    @ColumnInfo(name = "consent_given") val consentGiven: Boolean = true,
    @ColumnInfo(name = "verified_by_admin") val verifiedByAdmin: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "blood_banks")
data class BloodBankEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "state") val state: String,
    @ColumnInfo(name = "pincode") val pincode: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "contact_phone") val contactPhone: String,
    @ColumnInfo(name = "license_number") val licenseNumber: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "blood_stock",
    foreignKeys = [
        ForeignKey(
            entity = BloodBankEntity::class,
            parentColumns = ["id"],
            childColumns = ["blood_bank_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["blood_bank_id", "blood_group", "component"], unique = true)]
)
data class BloodStockEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "blood_bank_id") val bloodBankId: Long,
    @ColumnInfo(name = "blood_group") val bloodGroup: String, // A+, A-, B+, B-, AB+, AB-, O+, O-
    @ColumnInfo(name = "component") val component: String, // WHOLE_BLOOD, PLASMA, PLATELETS, RBC
    @ColumnInfo(name = "units_available") val unitsAvailable: Int = 0,
    @ColumnInfo(name = "last_updated_by") val lastUpdatedBy: String = "System Admin",
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "emergency_requests",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["requested_by_user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["requested_by_user_id"])]
)
data class EmergencyRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "requested_by_user_id") val requestedByUserId: Long,
    @ColumnInfo(name = "patient_name") val patientName: String,
    @ColumnInfo(name = "blood_group") val bloodGroup: String,
    @ColumnInfo(name = "component") val component: String,
    @ColumnInfo(name = "units_needed") val unitsNeeded: Int,
    @ColumnInfo(name = "blood_bank_id") val bloodBankId: Long? = null,
    @ColumnInfo(name = "hospital_name") val hospitalName: String,
    @ColumnInfo(name = "urgency_level") val urgencyLevel: String, // CRITICAL, URGENT, NORMAL
    @ColumnInfo(name = "contact_phone") val contactPhone: String,
    @ColumnInfo(name = "additional_notes") val additionalNotes: String = "",
    @ColumnInfo(name = "status") val status: String = "PENDING", // PENDING, APPROVED, REJECTED, FULFILLED, EXPIRED, CANCELLED
    @ColumnInfo(name = "reviewed_by_admin_id") val reviewedByAdminId: Long? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "donation_alerts",
    foreignKeys = [
        ForeignKey(
            entity = EmergencyRequestEntity::class,
            parentColumns = ["id"],
            childColumns = ["emergency_request_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DonorProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["donor_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["emergency_request_id", "donor_id"], unique = true),
        Index(value = ["donor_id"])
    ]
)
data class DonationAlertEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "emergency_request_id") val emergencyRequestId: Long,
    @ColumnInfo(name = "donor_id") val donorId: Long,
    @ColumnInfo(name = "status") val status: String = "PENDING", // PENDING, ACCEPTED, DECLINED
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "responded_at") val respondedAt: Long? = null
)

@Entity(tableName = "activity_log")
data class ActivityLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_id") val userId: Long? = null,
    @ColumnInfo(name = "action") val action: String,
    @ColumnInfo(name = "details") val details: String,
    @ColumnInfo(name = "ip_address") val ipAddress: String = "127.0.0.1",
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)
