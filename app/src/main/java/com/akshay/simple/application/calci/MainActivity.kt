package com.akshay.simple.application.calci

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var tvInput: TextView? = null
    private var tvResult: TextView? = null
    private var isLastNumeric: Boolean = false
    private var isResultAdded: Boolean = false
    private var isLastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInput = findViewById(R.id.tvInput)
        tvResult = findViewById(R.id.tvResult)
    }

    private fun numericAndDot(numeric: Boolean, dot: Boolean): Unit {
        isLastNumeric = numeric
        isLastDot = dot
    }

    fun onDigit(view: View) {
        tvInput?.append((view as Button).text)
        numericAndDot(numeric = true, dot = false)
    }

    fun onClearAll(view: View) {
        tvInput?.text = ""
        tvResult?.text = ""
        numericAndDot(numeric = false, dot = false)
    }

    fun onClear(view: View) {
        if (tvInput?.text?.length == 0) return

        val startIndex = 0
        val endIndex = tvInput?.text?.length?.dec()

        val lastValue = tvInput?.text?.substring((endIndex as Int) + 1)
        val operators = "+ - / *"
        if (operators.contains(lastValue as String)) {
            numericAndDot(numeric = true, dot = false)
        }
        tvInput?.text = tvInput?.text?.substring(startIndex, endIndex as Int)
        if (tvInput?.text?.length == 0) {
            isResultAdded = false
            numericAndDot(numeric = false, dot = false)
        }
    }

    fun onDecimalPoint(view: View) {
        if (isLastNumeric && !isLastDot) {
            tvInput?.append((view as Button).text)
            numericAndDot(numeric = false, dot = true)
        }
    }

    fun onOperator(view: View) {
        if (tvResult?.text?.length as Int >= 1 && tvInput?.text?.length as Int == 0 && !isResultAdded) {
            tvInput?.append(tvResult?.text)
            isResultAdded = true
            numericAndDot(numeric = true, dot = true)
        }
        tvInput?.text?.let {
            if (isLastNumeric && !isOperatorAdded(it.toString())) {
                tvInput?.append((view as Button)?.text)
                numericAndDot(numeric = false, dot = false)
            }
        }
    }

    private fun isOperatorAdded(value: String): Boolean {
        return if (value.startsWith("-")) false else (value.contains("/") || value.contains("+") || value.contains(
            "*"
        ) || value.contains("-"))
    }

    fun onEqual(view: View) {
        if (!isLastNumeric) return

        var tvValue = tvInput?.text.toString()
        var prefix = ""

        try {
            if (tvValue.startsWith("-")) {
                prefix = "-"
                tvValue = tvValue.substring(1)
            }

            if (tvValue.contains("-")) {
                subtract(tvValue, prefix)
            } else if (tvValue.contains("+")) {
                add(tvValue, prefix)
            } else if (tvValue.contains("*")) {
                multiply(tvValue, prefix)
            } else if (tvValue.contains("/")) {
                divide(tvValue, prefix)
            }

            tvInput?.text = ""
            isResultAdded = false
            numericAndDot(numeric = true, dot = false)

        } catch (exception: ArithmeticException) {
            exception.printStackTrace()
        } catch (exception: NumberFormatException) {
            exception.printStackTrace()
        }
    }

    private fun subtract(tvValue: String, prefix: String) {
        val splitValue = tvValue.split("-")

        var beforeOperatorValue =
            (if (prefix.isNotEmpty()) prefix + splitValue[0] else splitValue[0]).toDouble()
        var afterOperatorValue = splitValue[1].toDouble()

        tvResult?.text = (beforeOperatorValue - afterOperatorValue).toBigDecimal().toPlainString()
    }

    private fun add(tvValue: String, prefix: String) {
        val splitValue = tvValue.split("+")

        var beforeOperatorValue =
            (if (prefix.isNotEmpty()) prefix + splitValue[0] else splitValue[0]).toDouble()
        var afterOperatorValue = splitValue[1].toDouble()

        tvResult?.text = (beforeOperatorValue + afterOperatorValue).toBigDecimal().toPlainString()
    }

    private fun multiply(tvValue: String, prefix: String) {
        val splitValue = tvValue.split("*")

        var beforeOperatorValue =
            (if (prefix.isNotEmpty()) prefix + splitValue[0] else splitValue[0]).toDouble()
        var afterOperatorValue = splitValue[1].toDouble()

        tvResult?.text = (beforeOperatorValue * afterOperatorValue).toBigDecimal().toPlainString()
    }

    private fun divide(tvValue: String, prefix: String) {
        val splitValue = tvValue.split("/")

        var beforeOperatorValue =
            (if (prefix.isNotEmpty()) prefix + splitValue[0] else splitValue[0]).toDouble()
        var afterOperatorValue = splitValue[1].toDouble()

        tvResult?.text = (beforeOperatorValue / afterOperatorValue).toBigDecimal().toPlainString()
    }
}