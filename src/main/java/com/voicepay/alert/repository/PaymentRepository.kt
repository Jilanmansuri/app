package com.voicepay.alert.repository

import com.voicepay.alert.data.local.PaymentDao
import com.voicepay.alert.data.local.PaymentEntity
import kotlinx.coroutines.flow.Flow

class PaymentRepository(private val paymentDao: PaymentDao) {

    val allPayments: Flow<List<PaymentEntity>> = paymentDao.getAllPayments()

    suspend fun insertPayment(payment: PaymentEntity) {
        paymentDao.insertPayment(payment)
    }
}
