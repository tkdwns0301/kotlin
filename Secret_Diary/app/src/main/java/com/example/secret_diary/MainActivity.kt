package com.example.secret_diary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {
    private val npOne: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.npOne)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }
    private val npTwo: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.npTwo)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }
    private val npThree: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.npThree)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val btnOpen: Button by lazy {
        findViewById<AppCompatButton>(R.id.btnOpen)
    }

    private val btnChangePassword: Button by lazy {
        findViewById<AppCompatButton>(R.id.btnChangePassword)
    }

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        npOne
        npTwo
        npThree

        btnOpen.setOnClickListener {
            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호를 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

            val passwordFromUser: String = "${npOne.value}${npTwo.value}${npThree.value}"

            if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                startActivity(Intent(this, DiaryActivity::class.java))
            } else {
                //실패
                showErrorAlertDialog()
            }
        }

        btnChangePassword.setOnClickListener {
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

            if (changePasswordMode) {
                //번호를 저장하는 기능
                passwordPreferences.edit(true) {
                    putString("password", "${npOne.value}${npTwo.value}${npThree.value}")
                }

                changePasswordMode = false
                btnChangePassword.setBackgroundColor(Color.BLACK)
            } else {
                //changePasswordMode가 활성화 :: 비밀번호가 맞는지 체크
                val passwordFromUser: String = "${npOne.value}${npTwo.value}${npThree.value}"

                if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show()

                    btnChangePassword.setBackgroundColor(Color.RED)
                } else {
                    //실패
                    showErrorAlertDialog()
                }
            }
        }
    }

    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패!")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인") { _, _ -> }
            .create()
            .show()
    }
}