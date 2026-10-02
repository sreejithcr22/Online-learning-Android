package com.codit.interview.aptitude.presentation.calculator

import androidx.compose.runtime.Immutable

/**
 * Pure calculator engine.
 *
 * Extracted from `CalcFragment`, where the arithmetic lived inside `onClick`
 * handlers mutating a `TextView` directly. Making it a pure function means the keypad
 * UI is a thin rendering layer and the arithmetic is unit-testable.
 *
 * @param input      what the user has typed so far
 * @param output     the running expression, e.g. `"12 + 3 x"`
 * @param leftOperand the first operand of a pending operation, `null` when idle
 * @param pendingOperator the operator awaiting its right operand
 * @param hasResult  `true` once `=` has produced a result
 * @param history    every completed calculation, newest last
 */
@Immutable
data class CalculatorState(
    val input: String = "0",
    val output: String = "",
    val leftOperand: String? = null,
    val pendingOperator: String? = null,
    val hasResult: Boolean = false,
    val history: String = "",
    val isInvalid: Boolean = false,
) {
    val historyEntries: List<String>
        get() = history.lineSequence().filter { it.isNotBlank() }.toList()

    val hasHistory: Boolean get() = historyEntries.isNotEmpty()
}

/** Result of pressing a calculator key. */
sealed interface CalculatorAction {
    data class Digit(val digit: Char) : CalculatorAction
    data object Decimal : CalculatorAction
    data object Clear : CalculatorAction
    data object Backspace : CalculatorAction
    data object Negate : CalculatorAction
    data class Operator(val symbol: Char) : CalculatorAction
    data object Equals : CalculatorAction
    data object ClearHistory : CalculatorAction
}

/** Stateless calculator reducer. */
object CalculatorEngine {

    private val SYMBOLS = mapOf('+' to " + ", '-' to " - ", 'x' to " x ", '÷' to " ÷ ")

    fun reduce(state: CalculatorState, action: CalculatorAction): CalculatorState = when (action) {
        is CalculatorAction.Digit -> appendDigit(state, action.digit)
        CalculatorAction.Decimal -> appendDecimal(state)
        CalculatorAction.Clear -> CalculatorState()
        CalculatorAction.Backspace -> backspace(state)
        CalculatorAction.Negate -> negate(state)
        is CalculatorAction.Operator -> applyOperator(state, action.symbol)
        CalculatorAction.Equals -> resolve(state)
        CalculatorAction.ClearHistory -> state.copy(history = "")
    }

    private fun appendDigit(state: CalculatorState, digit: Char): CalculatorState {
        if (state.isInvalid || state.hasResult) {
            return state.copy(
                input = digit.toString(),
                output = "",
                hasResult = false,
                isInvalid = false,
            )
        }
        // A leading zero is replaced rather than extended, so "0" + "5" is "5".
        val next = if (state.input == "0" || state.input == "-") {
            if (state.input == "-") "-$digit" else digit.toString()
        } else {
            state.input + digit
        }
        return state.copy(input = next)
    }

    private fun appendDecimal(state: CalculatorState): CalculatorState {
        if (state.isInvalid || state.hasResult) {
            return state.copy(
                input = "0.",
                output = "",
                hasResult = false,
                isInvalid = false,
            )
        }
        if (state.input.contains('.')) return state
        return state.copy(input = state.input + ".")
    }

    private fun backspace(state: CalculatorState): CalculatorState = when {
        state.isInvalid -> state
        state.hasResult -> state.copy(input = "0", hasResult = false)
        state.input.length <= 1 -> state.copy(input = "0")
        else -> state.copy(input = state.input.dropLast(1))
    }

    private fun negate(state: CalculatorState): CalculatorState = when {
        state.input == "0" -> state.copy(input = "-")
        state.input == "-" -> state.copy(input = "0")
        state.input.startsWith("-") -> state.copy(input = state.input.drop(1))
        else -> state.copy(input = "-" + state.input)
    }

    private fun applyOperator(state: CalculatorState, symbol: Char): CalculatorState {
        if (state.isInvalid || state.input.isEmpty()) return state
        val operator = SYMBOLS.getValue(symbol)

        // No pending operation yet: start one.
        if (state.leftOperand == null || state.pendingOperator == null) {
            return state.copy(
                leftOperand = state.input,
                pendingOperator = operator,
                output = state.input + operator,
                input = "0",
                hasResult = false,
            )
        }

        // An operator was already pending: fold the current input in first, which is
        // what makes repeated operator presses chain (2 + 3 x 4 -> 5 x).
        val result = calculate(state.leftOperand, state.input, state.pendingOperator)
            ?: return invalid(state)
        return state.copy(
            leftOperand = result,
            pendingOperator = operator,
            output = result + operator,
            input = "0",
            hasResult = false,
        )
    }

    private fun resolve(state: CalculatorState): CalculatorState {
        val left = state.leftOperand ?: return state.copy(input = state.input, hasResult = true)
        val operator = state.pendingOperator ?: return state.copy(hasResult = true)
        val result = calculate(left, state.input, operator) ?: return invalid(state)

        val entry = "${state.output}${state.input} =$result"
        return state.copy(
            input = result,
            output = "",
            leftOperand = null,
            pendingOperator = null,
            hasResult = true,
            history = (state.history + entry + "\n").trimStart('\n'),
        )
    }

    private fun invalid(state: CalculatorState) = state.copy(
        input = "INVALID",
        output = "",
        leftOperand = null,
        pendingOperator = null,
        isInvalid = true,
    )

    /** Returns the formatted result, or `null` for a division by zero / bad input. */
    fun calculate(left: String, right: String, operator: String): String? {
        val a = left.toDoubleOrNull() ?: return null
        val b = right.toDoubleOrNull() ?: return null
        val result = when (operator.trim()) {
            "+" -> a + b
            "-" -> a - b
            "x" -> a * b
            "÷" -> if (b == 0.0) return null else a / b
            else -> return null
        }
        if (result.isNaN() || result.isInfinite()) return null
        return format(result)
    }

    private fun format(value: Double): String =
        if (value % 1.0 == 0.0) value.toLong().toString() else "%.9f".format(value).trimEnd('0').trimEnd('.')
}
