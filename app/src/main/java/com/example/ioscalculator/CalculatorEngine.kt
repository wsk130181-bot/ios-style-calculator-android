package com.example.ioscalculator

import java.math.BigDecimal
import java.math.MathContext
import java.util.ArrayDeque
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.PI
import kotlin.math.E
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

/**
 * 复刻 iOS 计算器的"即时运算"模型：
 * - 竖屏经典版：AC / ± / % / ÷ × − + =、小数点
 * - 横屏科学版：额外 sin cos tan ln log √ x² x³ ⅟x π e rand EE 以及括号
 */
class CalculatorEngine {

    private var current = "0"            // 当前输入/结果的字符串
    private var pendingOp: String? = null // 待处理运算符
    private var acc = 0.0                // 待处理运算的累加器
    private var startedTyping = true     // 是否正在输入数字
    private var justEvaluated = false    // 刚按过 =
    private var lastOp: String? = null   // 用于 = 重复
    private var lastRhs = 0.0

    private data class Frame(val acc: Double, val pendingOp: String?, val startedTyping: Boolean)

    private val stack = ArrayDeque<Frame>()

    // ---------- 对外状态 ----------

    fun reset() {
        current = "0"; pendingOp = null; acc = 0.0
        startedTyping = true; justEvaluated = false
        lastOp = null; lastRhs = 0.0; stack.clear()
    }

    fun display(): String = current

    /** 顶部小字：如 "12 +" 表示待处理运算 */
    fun pendingLine(): String {
        val op = pendingOp ?: return ""
        return formatDouble(acc) + " " + op
    }

    // ---------- 输入 ----------

    fun inputDigit(d: Int) {
        if (current == "错误") current = "0"
        if (justEvaluated || !startedTyping) { current = "0"; startedTyping = true; justEvaluated = false }
        if (current == "0") current = d.toString()
        else if (current.length < 15) current += d.toString()
    }

    fun inputDot() {
        if (current == "错误") current = "0"
        if (justEvaluated || !startedTyping) { current = "0"; startedTyping = true; justEvaluated = false }
        if (!current.contains('.')) current += "."
    }

    fun setOperator(op: String) {
        if (pendingOp != null && startedTyping) {
            evaluatePending()
        }
        acc = currentValue()
        pendingOp = op
        startedTyping = false
        justEvaluated = false
    }

    fun equals() {
        when {
            pendingOp != null && startedTyping -> {
                // iOS：5 + =  → 10（用当前值重复）
                evaluatePending()
            }
            pendingOp != null -> evaluatePending()
            justEvaluated && lastOp != null -> {
                val lhs = currentValue()
                val r = applyOp(lastOp!!, lhs, lastRhs)
                setCurrent(formatDouble(r))
                startedTyping = false
                justEvaluated = true
            }
        }
    }

    // ---------- 单目 / 常量 ----------

    fun negate() {
        if (current == "错误" || current == "0") return
        setCurrent(formatDouble(-currentValue()))
        startedTyping = false; justEvaluated = false
    }

    fun percent() {
        if (current == "错误") return
        setCurrent(formatDouble(currentValue() / 100.0))
        startedTyping = false; justEvaluated = false
    }

    fun unary(fn: String) {
        val v = currentValue()
        // iOS 三角函数默认角度制
        val r = when (fn) {
            "sin" -> sin(v * PI / 180.0)
            "cos" -> cos(v * PI / 180.0)
            "tan" -> tan(v * PI / 180.0)
            "ln"  -> if (v <= 0) Double.NaN else ln(v)
            "log" -> if (v <= 0) Double.NaN else log10(v)
            "√"   -> if (v < 0) Double.NaN else sqrt(v)
            "x²"  -> v * v
            "x³"  -> v * v * v
            "⅟x"  -> if (v == 0.0) Double.NaN else 1.0 / v
            else   -> v
        }
        setCurrent(formatDouble(r))
        startedTyping = false; justEvaluated = true
    }

    fun constant(c: String) {
        when (c) {
            "π" -> setCurrent(formatDouble(PI))
            "e" -> setCurrent(formatDouble(E))
        }
        startedTyping = false; justEvaluated = true
    }

    fun rand() {
        setCurrent(formatDouble(ThreadLocalRandom.current().nextDouble()))
        startedTyping = false; justEvaluated = true
    }

    fun ee() {
        if (current == "错误") return
        if (!current.contains('E')) {
            current = formatDouble(currentValue()) + "E"
            startedTyping = true; justEvaluated = false
        }
    }

    // ---------- 括号（横屏科学版） ----------

    fun openParen() {
        stack.push(Frame(acc, pendingOp, startedTyping))
        acc = 0.0; pendingOp = null
        current = "0"; startedTyping = true; justEvaluated = false
    }

    fun closeParen() {
        if (stack.isEmpty()) return
        if (pendingOp != null) evaluatePending()
        val f = stack.pop()
        val result = currentValue()
        acc = f.acc
        pendingOp = f.pendingOp
        current = formatDouble(result)
        startedTyping = false
        justEvaluated = true
    }

    // ---------- 内部 ----------

    private fun currentValue(): Double = current.toDoubleOrNull() ?: 0.0

    private fun evaluatePending() {
        val rhs = currentValue()
        val lhs = acc
        lastOp = pendingOp
        lastRhs = rhs
        val r = applyOp(pendingOp, lhs, rhs)
        setCurrent(formatDouble(r))
        pendingOp = null
        startedTyping = false
        justEvaluated = true
    }

    private fun applyOp(op: String?, lhs: Double, rhs: Double): Double = when (op) {
        "+" -> lhs + rhs
        "−" -> lhs - rhs
        "×" -> lhs * rhs
        "÷" -> if (rhs == 0.0) Double.NaN else lhs / rhs
        else -> rhs
    }

    private fun setCurrent(s: String) { current = s }

    private fun formatDouble(v: Double): String {
        if (v.isNaN() || v.isInfinite()) return "错误"
        if (v == floor(v) && abs(v) < 1e15) {
            return v.toLong().toString()
        }
        return BigDecimal(v, MathContext(12)).stripTrailingZeros().toPlainString()
    }

    private fun floor(v: Double): Double = kotlin.math.floor(v)
}
