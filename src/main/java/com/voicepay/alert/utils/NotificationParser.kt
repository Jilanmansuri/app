package com.voicepay.alert.utils

object NotificationParser {
    
    val supportedApps = listOf(
        "com.phonepe.app",
        "com.google.android.apps.nbu.paisa.user",
        "net.one97.paytm",
        "in.org.npci.upiapp",
        "in.amazon.mShop.android.shopping"
    )

    fun getAppName(packageName: String): String {
        return when (packageName) {
            "com.phonepe.app" -> "PhonePe"
            "com.google.android.apps.nbu.paisa.user" -> "Google Pay"
            "net.one97.paytm" -> "Paytm"
            "in.org.npci.upiapp" -> "BHIM"
            "in.amazon.mShop.android.shopping" -> "Amazon Pay"
            else -> "Unknown App"
        }
    }

    data class PaymentInfo(val amount: String, val sender: String)

    fun parsePaymentNotification(packageName: String, title: String?, text: String?): PaymentInfo? {
        if (title == null || text == null) return null
        
        val fullText = "$title $text".lowercase()
        
        // Keywords detection
        if (fullText.contains("received") || fullText.contains("credited") || 
            fullText.contains("paid you")) {
            
            val amount = extractAmount(fullText)
            val sender = extractSender(fullText)
            
            if (amount != null) {
                return PaymentInfo(amount, sender ?: "Someone")
            }
        }
        return null
    }

    private fun extractAmount(text: String): String? {
        val regex = Regex("(?:rs\\.?|inr|₹)\\s*([0-9,]+(?:\\.[0-9]{1,2})?)", RegexOption.IGNORE_CASE)
        val match = regex.find(text)
        return match?.groupValues?.get(1)?.replace(",", "")
    }

    private fun extractSender(text: String): String? {
        if (text.contains("from ")) {
            val afterFrom = text.substringAfter("from ").substringBefore(" ")
            if (afterFrom.isNotBlank()) return afterFrom
        }
        if (text.contains("paid you")) {
            val beforePaid = text.substringBefore(" paid you").trim().split(" ").last()
            if (beforePaid.isNotBlank()) return beforePaid
        }
        return null
    }
}
