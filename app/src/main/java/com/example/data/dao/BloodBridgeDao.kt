package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.ActivityLogEntity
import com.example.data.entity.BloodBankEntity
import com.example.data.entity.BloodStockEntity
import com.example.data.entity.DonationAlertEntity
import com.example.data.entity.DonorProfileEntity
import com.example.data.entity.EmergencyRequestEntity
import com.example.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BloodBridgeDao {

    // Users
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Long): UserEntity?

    @Query("SELECT * FROM users ORDER BY created_at DESC")
    fun getAllUsersFlow(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET account_status = :status, updated_at = :updatedAt WHERE id = :userId")
    suspend fun updateUserStatus(userId: Long, status: String, updatedAt: Long)

    // Donor Profiles
    @Query("SELECT * FROM donor_profiles WHERE user_id = :userId LIMIT 1")
    suspend fun getDonorProfileByUserId(userId: Long): DonorProfileEntity?

    @Query("SELECT * FROM donor_profiles WHERE user_id = :userId LIMIT 1")
    fun getDonorProfileByUserIdFlow(userId: Long): Flow<DonorProfileEntity?>

    @Query("SELECT * FROM donor_profiles WHERE id = :id LIMIT 1")
    suspend fun getDonorProfileById(id: Long): DonorProfileEntity?

    @Query("SELECT * FROM donor_profiles ORDER BY created_at DESC")
    fun getAllDonorProfilesFlow(): Flow<List<DonorProfileEntity>>

    @Query("SELECT * FROM donor_profiles")
    suspend fun getAllDonorProfilesList(): List<DonorProfileEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonorProfile(profile: DonorProfileEntity): Long

    @Update
    suspend fun updateDonorProfile(profile: DonorProfileEntity)

    @Query("UPDATE donor_profiles SET is_available = :isAvailable, updated_at = :updatedAt WHERE user_id = :userId")
    suspend fun updateDonorAvailability(userId: Long, isAvailable: Boolean, updatedAt: Long)

    @Query("UPDATE donor_profiles SET verified_by_admin = :isVerified, updated_at = :updatedAt WHERE id = :donorId")
    suspend fun updateDonorVerification(donorId: Long, isVerified: Boolean, updatedAt: Long)

    // Blood Banks
    @Query("SELECT * FROM blood_banks ORDER BY name ASC")
    fun getAllBloodBanksFlow(): Flow<List<BloodBankEntity>>

    @Query("SELECT * FROM blood_banks ORDER BY name ASC")
    suspend fun getAllBloodBanksList(): List<BloodBankEntity>

    @Query("SELECT * FROM blood_banks WHERE id = :id LIMIT 1")
    suspend fun getBloodBankById(id: Long): BloodBankEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBloodBank(bloodBank: BloodBankEntity): Long

    @Update
    suspend fun updateBloodBank(bloodBank: BloodBankEntity)

    // Blood Stock
    @Query("SELECT * FROM blood_stock ORDER BY blood_bank_id, blood_group, component")
    fun getAllBloodStockFlow(): Flow<List<BloodStockEntity>>

    @Query("SELECT * FROM blood_stock WHERE blood_bank_id = :bankId ORDER BY blood_group, component")
    fun getStockForBankFlow(bankId: Long): Flow<List<BloodStockEntity>>

    @Query("SELECT * FROM blood_stock WHERE blood_bank_id = :bankId AND blood_group = :group AND component = :component LIMIT 1")
    suspend fun findStock(bankId: Long, group: String, component: String): BloodStockEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBloodStock(stock: BloodStockEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBloodStock(stockList: List<BloodStockEntity>)

    @Query("UPDATE blood_stock SET units_available = :units, last_updated_by = :updatedBy, updated_at = :updatedAt WHERE id = :id")
    suspend fun updateStockUnits(id: Long, units: Int, updatedBy: String, updatedAt: Long)

    // Emergency Requests
    @Query("SELECT * FROM emergency_requests ORDER BY created_at DESC")
    fun getAllEmergencyRequestsFlow(): Flow<List<EmergencyRequestEntity>>

    @Query("SELECT * FROM emergency_requests WHERE requested_by_user_id = :userId ORDER BY created_at DESC")
    fun getRequestsByUserFlow(userId: Long): Flow<List<EmergencyRequestEntity>>

    @Query("SELECT * FROM emergency_requests WHERE id = :id LIMIT 1")
    suspend fun getEmergencyRequestById(id: Long): EmergencyRequestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyRequest(request: EmergencyRequestEntity): Long

    @Query("UPDATE emergency_requests SET status = :status, reviewed_by_admin_id = :adminId, updated_at = :updatedAt WHERE id = :id")
    suspend fun updateEmergencyRequestStatus(id: Long, status: String, adminId: Long?, updatedAt: Long)

    // Donation Alerts
    @Query("SELECT * FROM donation_alerts WHERE donor_id = :donorId ORDER BY created_at DESC")
    fun getAlertsForDonorFlow(donorId: Long): Flow<List<DonationAlertEntity>>

    @Query("SELECT * FROM donation_alerts ORDER BY created_at DESC")
    fun getAllAlertsFlow(): Flow<List<DonationAlertEntity>>

    @Query("SELECT COUNT(*) FROM donation_alerts WHERE emergency_request_id = :requestId AND donor_id = :donorId")
    suspend fun countAlertsForRequestAndDonor(requestId: Long, donorId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonationAlert(alert: DonationAlertEntity): Long

    @Query("UPDATE donation_alerts SET status = :status, responded_at = :respondedAt WHERE id = :id")
    suspend fun updateAlertStatus(id: Long, status: String, respondedAt: Long)

    // Activity Log
    @Query("SELECT * FROM activity_log ORDER BY created_at DESC")
    fun getAllActivityLogsFlow(): Flow<List<ActivityLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityLog(log: ActivityLogEntity): Long
}
