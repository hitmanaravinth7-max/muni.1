package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.BusinessProfileEntity
import com.example.data.entity.ConsultingMessageEntity
import com.example.data.entity.FinancialMetricEntity
import com.example.data.entity.StrategicPlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BizAdvisorDao {

    // Business Profiles
    @Query("SELECT * FROM business_profiles ORDER BY id ASC")
    fun getAllProfilesFlow(): Flow<List<BusinessProfileEntity>>

    @Query("SELECT * FROM business_profiles WHERE id = :id LIMIT 1")
    suspend fun getProfileById(id: Long): BusinessProfileEntity?

    @Query("SELECT * FROM business_profiles WHERE id = :id LIMIT 1")
    fun getProfileByIdFlow(id: Long): Flow<BusinessProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: BusinessProfileEntity): Long

    @Update
    suspend fun updateProfile(profile: BusinessProfileEntity)

    // Financial Metrics
    @Query("SELECT * FROM financial_metrics WHERE business_id = :businessId ORDER BY month_index ASC")
    fun getMetricsForBusinessFlow(businessId: Long): Flow<List<FinancialMetricEntity>>

    @Query("SELECT * FROM financial_metrics WHERE business_id = :businessId ORDER BY month_index ASC")
    suspend fun getMetricsForBusinessList(businessId: Long): List<FinancialMetricEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetric(metric: FinancialMetricEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetrics(metrics: List<FinancialMetricEntity>)

    // Consulting Messages
    @Query("SELECT * FROM consulting_messages WHERE business_id = :businessId ORDER BY timestamp ASC")
    fun getMessagesForBusinessFlow(businessId: Long): Flow<List<ConsultingMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ConsultingMessageEntity): Long

    @Query("DELETE FROM consulting_messages WHERE business_id = :businessId")
    suspend fun clearMessagesForBusiness(businessId: Long)

    // Strategic Plans
    @Query("SELECT * FROM strategic_plans WHERE business_id = :businessId ORDER BY created_at DESC")
    fun getPlansForBusinessFlow(businessId: Long): Flow<List<StrategicPlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: StrategicPlanEntity): Long
}
