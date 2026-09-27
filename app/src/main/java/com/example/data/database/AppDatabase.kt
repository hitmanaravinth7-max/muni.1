package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.BizAdvisorDao
import com.example.data.dao.BloodBridgeDao
import com.example.data.entity.ActivityLogEntity
import com.example.data.entity.BloodBankEntity
import com.example.data.entity.BloodStockEntity
import com.example.data.entity.BusinessProfileEntity
import com.example.data.entity.ConsultingMessageEntity
import com.example.data.entity.DonationAlertEntity
import com.example.data.entity.DonorProfileEntity
import com.example.data.entity.EmergencyRequestEntity
import com.example.data.entity.FinancialMetricEntity
import com.example.data.entity.StrategicPlanEntity
import com.example.data.entity.UserEntity

@Database(
    entities = [
        BusinessProfileEntity::class,
        FinancialMetricEntity::class,
        ConsultingMessageEntity::class,
        StrategicPlanEntity::class,
        UserEntity::class,
        DonorProfileEntity::class,
        BloodBankEntity::class,
        BloodStockEntity::class,
        EmergencyRequestEntity::class,
        DonationAlertEntity::class,
        ActivityLogEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bizAdvisorDao(): BizAdvisorDao
    abstract fun bloodBridgeDao(): BloodBridgeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bizadvisor_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
