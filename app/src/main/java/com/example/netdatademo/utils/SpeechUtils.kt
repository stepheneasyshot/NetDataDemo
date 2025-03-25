package com.example.netdatademo.utils

import android.speech.tts.TextToSpeech
import android.util.Log
import com.example.netdatademo.MainStateHolder.Companion.TAG
import com.example.netdatademo.appContext
import java.util.Locale

object SpeechUtils {

    private lateinit var textToSpeech: TextToSpeech

    private const val TEST_IDENTIFIER = "test"

    private const val TEST_HELLO = "Hi, How are you? I'm fine. Thank you. And you?"

    private var isConnected = false

    val ttsConnectedListener = TextToSpeech.OnInitListener { status ->
        Log.d(TAG, "OnInitListener status: $status")
        isConnected = status == TextToSpeech.SUCCESS
    }

    fun init() {
        textToSpeech = TextToSpeech(appContext, ttsConnectedListener)
    }

    fun speak(text: String = TEST_HELLO, locale: Locale = Locale.US) {
        Log.d(TAG, "==========>speak<=========")
        if (isConnected) {
            textToSpeech.language = locale
            textToSpeech.speak(
                text,
                TextToSpeech.QUEUE_ADD,
                null,
                TEST_IDENTIFIER
            )
        } else {
            Log.d(TAG, "==========>TTS is not connected!<=========")
        }
    }

    fun stop() {
        textToSpeech.stop()
    }
}