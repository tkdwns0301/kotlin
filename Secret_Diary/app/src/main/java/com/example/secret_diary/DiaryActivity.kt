package com.example.secret_diary

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity : AppCompatActivity() {
    private val etDiary: EditText by lazy {
        findViewById<EditText>(R.id.etDiary)
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)


        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)

        etDiary.setText(detailPreferences.getString("detail", ""))


        val runnable = Runnable {
            val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE).edit {
                putString("detail", etDiary.text.toString())
            }
        }

        etDiary.addTextChangedListener {
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 500)
        }
    }
}