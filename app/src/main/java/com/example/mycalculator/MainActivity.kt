package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.security.InvalidParameterException

class MainActivity : AppCompatActivity() {

    private var tvDisplay: TextView? = null
    private var lastNumeric = true
    private var lastDot = false
    private var lastLeftParenthesis = false

    private val inputAsPostfix = mutableListOf<String>()

    private var btnClear: Button? = null
    private var btnBackspace: Button? = null
    private var btnLeftParenthesis: Button? = null
    private var btnRightParenthesis: Button? = null
    private var btnDot: Button? = null
    private var btnEqual: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDisplay = findViewById(R.id.tvDisplay)
        btnClear = findViewById(R.id.btnCLR)
        btnBackspace = findViewById(R.id.btnBackspace)
        btnLeftParenthesis = findViewById(R.id.btnLeftParenthesis)
        btnRightParenthesis = findViewById(R.id.btnRightParenthesis)
        btnDot = findViewById(R.id.btnDot)
        btnEqual = findViewById(R.id.btnEqual)

        btnClear?.setOnClickListener {
            tvDisplay?.text = "0"
            lastNumeric = true
            lastDot = false
        }

        btnBackspace?.setOnClickListener {
            val tvDisplayTextLength = tvDisplay?.text?.length ?: 0
            tvDisplay?.text = if (tvDisplayTextLength > 1) {
                tvDisplay?.text?.dropLast(1)
            } else "0"
        }

        btnLeftParenthesis?.setOnClickListener {
            if (tvDisplay?.text?.toString() == "0") {
                tvDisplay?.text = "("
            } else if (!lastNumeric || lastLeftParenthesis) {
                tvDisplay?.append(btnLeftParenthesis?.text)
            }
            lastLeftParenthesis = true

        }

        btnRightParenthesis?.setOnClickListener {
            tvDisplay?.append(btnRightParenthesis?.text)
        }

        btnDot?.setOnClickListener {
            if (lastNumeric && !lastDot && !lastLeftParenthesis) {

                tvDisplay?.append(btnDot?.text)
                lastNumeric = false
                lastDot = true
            }
        }

        btnEqual?.setOnClickListener {
            try {
                val inputAsList = getInputAsList(tvDisplay?.text.toString())

                inputAsPostfix.addAll(toPostfix(inputAsList))

                val result = "=${getResult(inputAsPostfix)}"
                tvDisplay?.append(result)


            } catch (e:InvalidParameterException) {
                Toast.makeText(this, "Wrong expression", Toast.LENGTH_SHORT).show()
            }

        }

    }

    fun onDigit(view: View) {
        val btnText = (view as Button).text
        if (tvDisplay?.text.toString() == "0") {

            tvDisplay?.text = btnText
        } else {
            tvDisplay?.append(btnText)
        }

        lastNumeric = true
        lastLeftParenthesis = false
    }

    fun onOperator(view: View) {
        if (lastNumeric) {
            if (tvDisplay?.text.toString() == "0" && (view as Button).text == "-") {
                tvDisplay?.text = "-"
                lastNumeric = false
            } else {
                tvDisplay?.append((view as Button).text)
                lastNumeric = false
                lastDot = false
                lastLeftParenthesis = false
            }
        }
    }
}