package com.rekoj134.texttospeechdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable
import net.gotev.speech.SpeechUtil
import net.gotev.speech.SupportedLanguagesListener
import net.gotev.speech.TextToSpeechCallback
import net.gotev.speech.UnsupportedReason
import net.gotev.speech.ui.SpeechProgressView
import java.util.Locale

class SpeechToTextActivity : AppCompatActivity() {
    private lateinit var progressView: SpeechProgressView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech_to_text)


        // Khởi tạo Speech
        Speech.init(this, packageName)

//        Speech.getInstance().setLocale(Locale("vi", "VN"))
        Log.d("Speechsdasdsdsad", "Current STT Locale: ${Speech.getInstance().speechToTextLanguage}")

        Speech.getInstance().getSupportedSpeechToTextLanguages(object : SupportedLanguagesListener {
            override fun onSupportedLanguages(supportedLanguages: List<String?>?) {
                supportedLanguages?.forEach {
                    Log.i("Speechsdasdsdsad", "Supported language: $it")
                }
            }

            override fun onNotSupported(reason: UnsupportedReason?) {
                Log.i("Speechsdasdsdsad", "$reason")
            }
        })

        progressView = findViewById(R.id.progress)

        // Tùy chỉnh màu cột progress
        val colors = intArrayOf(
            ContextCompat.getColor(this, android.R.color.holo_blue_dark),
            ContextCompat.getColor(this, android.R.color.holo_green_light),
            ContextCompat.getColor(this, android.R.color.holo_orange_light),
            ContextCompat.getColor(this, android.R.color.holo_red_light),
            ContextCompat.getColor(this, android.R.color.holo_purple)
        )
        progressView.setColors(colors)

        // Tùy chỉnh chiều cao cột
        val heights = intArrayOf(60, 76, 58, 80, 55)
        progressView.setBarMaxHeightsInDp(heights)

        // Bắt sự kiện button start listening
        findViewById<Button>(R.id.btn_start_listening).setOnClickListener {
            startListening()
        }

        // Bắt sự kiện button speak text
        findViewById<Button>(R.id.btn_speak).setOnClickListener {
            speakText("Xin chào! Đây là text to speech bằng Kotlin.")
        }
    }

    private fun startListening() {
        try {
            Speech.getInstance().startListening(progressView, object : SpeechDelegate {
                override fun onStartOfSpeech() {
                    Log.i("Speech", "Speech recognition started")
                }

                override fun onSpeechRmsChanged(value: Float) {
                    Log.d("Speech", "RMS: $value")
                }

                override fun onSpeechPartialResults(results: MutableList<String>?) {
                    Log.i("Speech", "Partial: ${results?.joinToString(" ")}")
                }

                override fun onSpeechResult(result: String?) {
                    Log.i("Speech", "Final result: $result")
                    Toast.makeText(this@SpeechToTextActivity, "Result: $result", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: SpeechRecognitionNotAvailable) {
            Toast.makeText(this, "Speech recognition not available", Toast.LENGTH_LONG).show()
            SpeechUtil.redirectUserToGoogleAppOnPlayStore(this)
        } catch (e: GoogleVoiceTypingDisabledException) {
            Toast.makeText(this, "Google voice typing disabled", Toast.LENGTH_LONG).show()
        }
    }

    private fun speakText(text: String) {
        Speech.getInstance().say(text, object : TextToSpeechCallback {
            override fun onStart() {
                Log.i("Speech", "TTS started")
            }

            override fun onCompleted() {
                Log.i("Speech", "TTS completed")
            }

            override fun onError() {
                Log.i("Speech", "TTS error")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Speech.getInstance().shutdown()
    }
}