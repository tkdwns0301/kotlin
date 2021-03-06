package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.room.Room
import com.example.calculator.model.History
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {
    private val tvExpression: TextView by lazy {
        findViewById<TextView>(R.id.tvExpression)
    }

    private val tvResult: TextView by lazy {
        findViewById<TextView>(R.id.tvResult)
    }

    private var isOperator = false
    private var hasOperator = false

    private val historyLayout: View by lazy {
        findViewById(R.id.historyLayout)
    }
    private val historyLinearLayout: LinearLayout by lazy {
        findViewById(R.id.historyLinearLayout)
    }

    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "historyDB"
        ).build()
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
        if (isOperator) {
            tvExpression.append(" ")
        }

        isOperator = false

        val expressionText = tvExpression.text.split(" ")
        if (expressionText.isNotEmpty() && expressionText.last().length > 15) {
            Toast.makeText(this, "15??????????????? ????????? ??? ????????????.", Toast.LENGTH_SHORT).show()
            return
        } else if (expressionText.last().isEmpty() && number == "0") {
            Toast.makeText(this, "0??? ?????? ?????? ??? ??? ????????????.", Toast.LENGTH_SHORT).show()
        }

        tvExpression.append(number)
        tvResult.text = calculateExpression()

    }

    private fun operatorButtonClicked(operator: String) {
        if (tvExpression.text.isEmpty()) {
            return
        }

        when {
            isOperator -> {
                tvExpression.text = tvExpression.text.toString().dropLast(1) + operator
            }
            hasOperator -> {
                Toast.makeText(this, "???????????? ????????? ????????? ??? ????????????.", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                tvExpression.append(" $operator")
            }
        }

        val ssb = SpannableStringBuilder(tvExpression.text)
        ssb.setSpan(
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
        val expressionTexts = tvExpression.text.split(" ")

        if (tvExpression.text.isEmpty() || expressionTexts.size == 1) {
            return
        }
        if (expressionTexts.size != 3 && hasOperator) {
            Toast.makeText(this, "?????? ???????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show()
            return
        }
        if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            Toast.makeText(this, "????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return
        }

        val expressionText = tvExpression.text.toString()
        val resultText = calculateExpression()

        //TODO ????????? ???????????? ??????
        Thread(Runnable {
            db.historyDao().insertHistory(History(null, expressionText, resultText))
        }).start()

        tvResult.text = ""
        tvExpression.text = resultText

        isOperator = false
        hasOperator = false
    }

    private fun calculateExpression(): String {
        val expressionTexts = tvExpression.text.split(" ")

        if (hasOperator.not() || expressionTexts.size != 3) {
            return ""
        } else if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            return ""
        }

        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()
        val op = expressionTexts[1]

        return when (op) {
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "*" -> (exp1 * exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }
    }

    fun clearButtonClicked(v: View) {
        tvExpression.text = ""
        tvResult.text = ""
        isOperator = false
        hasOperator = false
    }

    fun historyButtonClicked(v: View) {
        historyLayout.isVisible = true
        historyLinearLayout.removeAllViews()

        //TODO ???????????? ?????? ?????? ????????????
        //TODO ????????? ?????? ?????? ????????????
        Thread(Runnable {
            db.historyDao().getAll().reversed().forEach {
                runOnUiThread {
                    val historyView = LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
                    historyView.findViewById<TextView>(R.id.tvExpression).text = it.expression
                    historyView.findViewById<TextView>(R.id.tvResult).text = "= ${it.result}"

                    historyLinearLayout.addView(historyView)
                }
            }
        }).start()


    }

    fun historyClearButtonClicked(v: View) {
        //TODO ????????? ?????? ?????? ??????
        historyLinearLayout.removeAllViews()

        //TODO ???????????? ?????? ?????? ??????
        Thread(Runnable {
            db.historyDao().deleteAll()
        }).start()

    }

    fun closeHistoryButtonClicked(v: View) {
        historyLayout.isVisible = false
    }
}

fun String.isNumber(): Boolean {
    return try {
        this.toBigInteger()
        return true
    } catch (e: NumberFormatException) {
        return false
    }
}