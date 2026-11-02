package com.example.ioscalculator

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : Activity() {

    private val engine = CalculatorEngine()
    private var tvDisplay: TextView? = null
    private var tvPending: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bind()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // 资源限定符：竖屏用 layout/，横屏自动用 layout-land/
        setContentView(R.layout.activity_main)
        bind()
    }

    private fun bind() {
        tvDisplay = findViewById(R.id.tvDisplay)
        tvPending = findViewById(R.id.tvPending)
        refresh()
    }

    private fun refresh() {
        tvDisplay?.text = engine.display()
        tvPending?.text = engine.pendingLine()
    }

    fun onKey(v: View) {
        val t = v.tag as? String ?: return
        when {
            t.length == 1 && t[0] in '0'..'9' -> engine.inputDigit(t[0] - '0')
            t == "." -> engine.inputDot()
            t == "+" || t == "−" || t == "×" || t == "÷" -> engine.setOperator(t)
            t == "=" -> engine.equals()
            t == "AC" -> engine.reset()
            t == "±" -> engine.negate()
            t == "%" -> engine.percent()
            t == "(" -> engine.openParen()
            t == ")" -> engine.closeParen()
            t == "π" || t == "e" -> engine.constant(t)
            t == "rand" -> engine.rand()
            t == "EE" -> engine.ee()
            else -> engine.unary(t) // sin cos tan ln log √ x² x³ ⅟x
        }
        refresh()
    }
}
