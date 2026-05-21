package com.voicepay.alert.service

import android.app.Notification
import android.content.Context
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.voicepay.alert.App
import com.voicepay.alert.data.local.PaymentEntity
import com.voicepay.alert.repository.PaymentRepository
import com.voicepay.alert.utils.Constants
import com.voicepay.alert.utils.NotificationParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentNotificationListener : NotificationListenerService() {

    private lateinit var ttsManager: TtsManager
    private lateinit var repository: PaymentRepository

    override fun onCreate() {
        super.onCreate()
        ttsManager = (applicationContext as App).ttsManager
        repository = (applicationContext as App).repository
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn?.let {
            val packageName = it.packageName
            
            if (NotificationParser.supportedApps.contains(packageName)) {
                val extras = it.notification.extras
                val title = extras.getString(Notification.EXTRA_TITLE)
                val text = extras.getString(Notification.EXTRA_TEXT)
                
                Log.d("PaymentNotification", "Package: $packageName, Title: $title, Text: $text")

                val paymentInfo = NotificationParser.parsePaymentNotification(packageName, title, text)
                
                if (paymentInfo != null) {
                    val appName = NotificationParser.getAppName(packageName)
                    
                    val prefs = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
                    val voiceEnabled = prefs.getBoolean(Constants.KEY_VOICE_ENABLED, true)
                    val speakSender = prefs.getBoolean(Constants.KEY_SPEAK_SENDER, true)
                    
                    var announcement = "Received ${paymentInfo.amount} rupees"
                    if (speakSender && paymentInfo.sender.isNotBlank() && paymentInfo.sender != "Someone") {
                        announcement += " from ${paymentInfo.sender}"
                    }
                    announcement += " on $appName"
                    
                    if (voiceEnabled) {
                        ttsManager.speak(announcement)
                    }

                    // Save to Room
                    val entity = PaymentEntity(
                        amount = paymentInfo.amount,
                        sender = paymentInfo.sender,
                        appName = appName,
                        timestamp = System.currentTimeMillis()
                    )
                    
                    CoroutineScope(Dispatchers.IO).launch {
                        repository.insertPayment(entity)
                    }
                }
            }
        }
    }
}
