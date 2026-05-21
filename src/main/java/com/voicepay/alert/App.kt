package com.voicepay.alert

import android.app.Application
import com.voicepay.alert.data.local.PaymentDatabase
import com.voicepay.alert.repository.PaymentRepository
import com.voicepay.alert.service.TtsManager

class App : Application() {
    
    val database by lazy { PaymentDatabase.getDatabase(this) }
    val repository by lazy { PaymentRepository(database.paymentDao()) }
    val ttsManager by lazy { TtsManager(this) }

    override fun onCreate() {
        super.onCreate()
    }
}
