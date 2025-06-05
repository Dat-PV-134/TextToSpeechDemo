package com.rekoj134.texttospeechdemo

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var speak: TextToSpeech? = null
    private lateinit var ed1: EditText
    private lateinit var textviewLanguage: TextView
    private lateinit var b1: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ed1 = findViewById(R.id.editText)
        b1 = findViewById(R.id.button)
        textviewLanguage = findViewById(R.id.tv_lang)

        speak = TextToSpeech(applicationContext) { status ->
            if (status != TextToSpeech.ERROR) {
                speak?.language = Locale.ENGLISH
            }
        }

        b1.setOnClickListener {
//            val toSpeak = ed1.text.toString()
//            Toast.makeText(applicationContext, toSpeak, Toast.LENGTH_SHORT).show()
//            speak?.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
            startActivity(Intent(this@MainActivity, SpeechToTextActivity::class.java))
        }
    }

    override fun onPause() {
        speak?.stop()
        speak?.shutdown()
        super.onPause()
    }
}