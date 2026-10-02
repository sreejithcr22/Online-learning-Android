package com.codit.interview.aptitude.presentation.calculator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for the calculator engine.
 *
 * These cover the regression that shipped as a crash: digit keys were being routed to
 * [CalculatorAction.Operator], which threw `NoSuchElementException` on lookup.
 */
class CalculatorEngineTest {

    private fun press(vararg actions: CalculatorAction): CalculatorState =
        actions.fold(CalculatorState()) { state, action -> CalculatorEngine.reduce(state, action) }

    private fun digits(value: String): List<CalculatorAction> =
        value.map { CalculatorAction.Digit(it) }

    @Test
    fun `digits build up the input`() {
        assertEquals("1234", press(*digits("1234").toTypedArray()).input)
    }

    @Test
    fun `leading zero is replaced rather than appended`() {
        assertEquals("5", press(CalculatorAction.Digit('5')).input)
        assertEquals("50", press(CalculatorAction.Digit('5'), CalculatorAction.Digit('0')).input)
    }

    @Test
    fun `addition and evaluation`() {
        val state = press(
            *digits("12").toTypedArray(),
            CalculatorAction.Operator('+'),
            *digits("3").toTypedArray(),
            CalculatorAction.Equals,
        )
        assertEquals("15", state.input)
        assertTrue(state.hasResult)
    }

    @Test
    fun `operators chain left to right`() {
        // 12 + 3 x 4 evaluates as (12 + 3) * 4 = 60, matching the legacy calculator.
        val state = press(
            *digits("12").toTypedArray(),
            CalculatorAction.Operator('+'),
            *digits("3").toTypedArray(),
            CalculatorAction.Operator('x'),
            *digits("4").toTypedArray(),
            CalculatorAction.Equals,
        )
        assertEquals("60", state.input)
    }

    @Test
    fun `repeated operator folds the previous result`() {
        val state = press(
            *digits("10").toTypedArray(),
            CalculatorAction.Operator('-'),
            *digits("3").toTypedArray(),
            CalculatorAction.Operator('-'),
            *digits("2").toTypedArray(),
            CalculatorAction.Equals,
        )
        assertEquals("5", state.input)
    }

    @Test
    fun `division works and by zero is invalid`() {
        assertEquals(
            "4",
            press(
                *digits("8").toTypedArray(),
                CalculatorAction.Operator('÷'),
                *digits("2").toTypedArray(),
                CalculatorAction.Equals,
            ).input,
        )
        assertNull(CalculatorEngine.calculate("8", "0", "÷"))
    }

    @Test
    fun `fractions keep up to nine decimal places`() {
        assertEquals("0.333333333", CalculatorEngine.calculate("1", "3", "÷"))
    }

    @Test
    fun `whole results drop the decimal point`() {
        assertEquals("12", CalculatorEngine.calculate("4", "3", "x"))
    }

    @Test
    fun `decimal input allows only one separator`() {
        val state = press(
            CalculatorAction.Digit('1'),
            CalculatorAction.Decimal,
            CalculatorAction.Digit('5'),
            CalculatorAction.Decimal,
        )
        assertEquals("1.5", state.input)
    }

    @Test
    fun `negate toggles the sign of the current input`() {
        assertEquals("-5", press(CalculatorAction.Digit('5'), CalculatorAction.Negate).input)
        assertEquals("5", press(CalculatorAction.Digit('5'), CalculatorAction.Negate, CalculatorAction.Negate).input)
    }

    @Test
    fun `negate from zero and bare minus stay well formed`() {
        assertEquals("-", press(CalculatorAction.Negate).input)
        assertEquals("0", press(CalculatorAction.Negate, CalculatorAction.Negate).input)
        assertEquals("-12", press(*digits("12").toTypedArray(), CalculatorAction.Negate).input)
    }

    @Test
    fun `backspace and clear`() {
        assertEquals("12", press(*digits("123").toTypedArray(), CalculatorAction.Backspace).input)
        assertEquals("0", press(*digits("123").toTypedArray(), CalculatorAction.Backspace, CalculatorAction.Backspace, CalculatorAction.Backspace).input)
        assertEquals("0", press(*digits("123").toTypedArray(), CalculatorAction.Clear).input)
    }

    @Test
    fun `typing after a result starts a new calculation`() {
        val state = press(
            *digits("12").toTypedArray(),
            CalculatorAction.Operator('+'),
            *digits("3").toTypedArray(),
            CalculatorAction.Equals,
            CalculatorAction.Digit('7'),
        )
        assertEquals("7", state.input)
    }

    @Test
    fun `history accumulates one entry per evaluation`() {
        val state = press(
            *digits("2").toTypedArray(),
            CalculatorAction.Operator('+'),
            *digits("2").toTypedArray(),
            CalculatorAction.Equals,
            CalculatorAction.Operator('+'),
            *digits("3").toTypedArray(),
            CalculatorAction.Equals,
        )
        assertEquals(listOf("2 + 2 =4", "4 + 3 =7"), state.historyEntries)
    }

    @Test
    fun `history can be cleared independently of the current calculation`() {
        val state = press(
            *digits("2").toTypedArray(),
            CalculatorAction.Operator('+'),
            *digits("2").toTypedArray(),
            CalculatorAction.Equals,
            CalculatorAction.ClearHistory,
        )
        assertEquals("", state.history)
        assertEquals("4", state.input)
    }
}