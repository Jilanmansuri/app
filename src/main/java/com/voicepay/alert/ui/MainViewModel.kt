package com.voicepay.alert.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.voicepay.alert.App
import com.voicepay.alert.data.local.PaymentEntity
import kotlinx.coroutines.flow.Flow

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as App).repository
    val allPayments = repository.allPayments.asLiveData()
}
