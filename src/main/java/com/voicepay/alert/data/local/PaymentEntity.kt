package com.voicepay.alert.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: String,
    val sender: String,
    val appName: String,
    val timestamp: Long
)
