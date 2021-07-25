package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val tvExpression: TextView by lazy {
        findViewById<TextView>(R.id.tvExpression)
    }

    private val tvResult: TextView by lazy {
        findViewById<TextView>(R.id.tvResult)
    }

    private var isOperator = false
    private var hasOperator = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun buttonClicked(v: View) {
        when (v.id) {
            R.id.btn0 -> numButtonClicked("0")
            R.id.btn1 -> numButtonClicked("1")
            R.id.btn2 -> numButtonClicked("2")
            R.id.btn3 -> numButtonClicked("3")
            R.id.btn4 -> numButtonClicked("4")
            R.id.btn5 -> numButtonClicked("5")
            R.id.btn6 -> numButtonClicked("6")
            R.id.btn7 -> numButtonClicked("7")
            R.id.btn8 -> numButtonClicked("8")
            R.id.btn9 -> numButtonClicked("9")

            R.id.btnPlus -> operatorButtonClicked("+")
            R.id.btnMinus -> operatorButtonClicked("-")
            R.id.btnMul -> operatorButtonClicked("*")
            R.id.btnDiv -> operatorButtonClicked("/")
            R.id.btnModulo -> operatorButtonClicked("%")
        }
    }

    private fun numButtonClicked(number: String) {
        if(isOperator) {
            tvExpression.append(" ")
        }

        isOperator = false

        val expressionText = tvExpression.text.split(" ")
        if (expressionText.isNotEmpty() && expressionText.last().length > 15) {
            Toast.makeText(this, "15자리까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        } else if(expressionText.last().isEmpty() && number == "0") {
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }

        tvExpression.append(number)
        //TODO resultTextView 실시간으로 계산 결과를 넣어야 하는 기능
    }

    private fun operatorButtonClicked(operator: String) {
        if(tvExpression.text.isEmpty()) {
            return
        }

        when {
            isOperator -> {
                tvExpression.text = tvExpression.text.toString().dropLast(1) + operator
            }
            hasOperator -> {
                Toast.makeText(this, "연산자는 한번만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                tvExpression.append(" $operator")
            }
        }

        val ssb = SpannableStringBuilder(tvExpression.text)
        ssb.setSpan (
            ForegroundColorSpan(getColor(R.color.green)),
            tvExpression.text.length - 1,
            tvExpression.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvExpression.text = ssb

        isOperator = true
        hasOperator = true
    }

    fun resultButtonClicked(v: View) {

    }

    fun historyButtonClicked(v: View) {

    }

    fun clearButtonClicked(v: View) {

    }
}