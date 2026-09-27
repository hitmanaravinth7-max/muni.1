package com.example

import android.app.Application
import com.example.data.repository.BizAdvisorRepository
import com.example.data.repository.BloodBridgeRepository

class BloodBridgeApp : Application() {
    lateinit var repository: BloodBridgeRepository
        private set

    lateinit var bizAdvisorRepository: BizAdvisorRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = BloodBridgeRepository.getInstance(this)
        bizAdvisorRepository = BizAdvisorRepository.getInstance(this)
    }
}
