package com.voicepay.alert.ui

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.voicepay.alert.App
import com.voicepay.alert.R
import com.voicepay.alert.utils.Constants

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val switchVoice = findViewById<Switch>(R.id.switchVoice)
        val switchSpeakSender = findViewById<Switch>(R.id.switchSpeakSender)
        val btnTestVoice = findViewById<Button>(R.id.btnTestVoice)

        val prefs = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

        switchVoice.isChecked = prefs.getBoolean(Constants.KEY_VOICE_ENABLED, true)
        switchSpeakSender.isChecked = prefs.getBoolean(Constants.KEY_SPEAK_SENDER, true)

        switchVoice.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(Constants.KEY_VOICE_ENABLED, isChecked).apply()
        }

        switchSpeakSender.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(Constants.KEY_SPEAK_SENDER, isChecked).apply()
        }

        btnTestVoice.setOnClickListener {
            val ttsManager = (application as App).ttsManager
            if (switchSpeakSender.isChecked) {
                ttsManager.speak("Received 500 rupees from Test User on VoicePay")
            } else {
                ttsManager.speak("Received 500 rupees on VoicePay")
            }
        }
    }
}
