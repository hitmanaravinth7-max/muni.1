package com.example

import android.app.Application
import com.example.data.repository.BloodBridgeRepository

class BloodBridgeApp : Application() {
    lateinit var repository: BloodBridgeRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = BloodBridgeRepository.getInstance(this)
    }
}
