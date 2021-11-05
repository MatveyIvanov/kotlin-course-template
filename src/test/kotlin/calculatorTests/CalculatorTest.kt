package calculatorTests

import org.junit.Assert
import org.junit.Test
import calculator.Calculator
import java.io.CharConversionException


const val PRECISION = 0.00001

class CalculatorTest {
    private val calculator = Calculator()

    @Test
    fun test_operations() {
        Assert.assertEquals(40.0, calculator.calculate("(2 + 3) * (8 / 4) ^ 3"), PRECISION)
    }

    @Test
    fun test_extra_but_correct_brackets() {
        Assert.assertEquals(18.0, calculator.calculate("((2 + 4 ^ 2))"), PRECISION)
    }

    @Test
    fun test_unary_minus() {
        Assert.assertEquals(-15.75, calculator.calculate("(4 + 3) * 9 / -4"), PRECISION)
    }

    @Test
    fun test_unary_plus() {
        Assert.assertEquals(253.0, calculator.calculate("-3 + 4 ^ +4"), PRECISION)
    }

    @Test
    fun test_constants() {
        Assert.assertEquals(2 * Math.PI + Math.E / 2, calculator.calculate("2*pi + e / 2"), PRECISION)
    }

    @Test
    fun test_empty_expression_exception() {
        try {
            calculator.calculate("")
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Expression cannot be empty", e.message)
        }
    }

    @Test
    fun test_expression_starts_with_binary_operation_exception() {
        try {
            calculator.calculate("*2 - 1")
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Expression cannot start with binary operation", e.message)
        }
    }

    @Test
    fun test_expression_ends_with_operation_exception() {
        try {
            calculator.calculate("2 + 3 -")
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Expression cannot end with operation", e.message)
        }
    }

    @Test
    fun test_multiple_floating_points_exception() {
        try {
            calculator.calculate("2.1.2 * 3")
        } catch (e: NumberFormatException) {
            Assert.assertEquals("Number can have only one floating point", e.message)
        }
    }

    @Test
    fun test_closing_bracket_before_opening_one_exception() {
        try {
            calculator.calculate("(2 + 3)) * 2")
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("There must be an opening bracket before closing one", e.message)
        }
    }
    @Test
    fun test_unsupported_symbols_exception() {
        try {
            calculator.calculate("2 * asd")
        } catch (e: CharConversionException) {
            Assert.assertEquals("Unsupported symbol 'a'", e.message)
        }
    }

    @Test
    fun test_closing_bracket_not_closed_exception() {
        try {
            calculator.calculate("(4 / 2 ^ 5")
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Missing closing bracket", e.message)
        }
    }

    @Test
    fun test_binary_operation_after_operation_exception() {
        try {
            calculator.calculate("2 */ 3")
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Operation cannot be followed by binary operation", e.message)
        }
    }

    @Test
    fun test_wrong_opening_bracket_placement_exception() {
        try {
            calculator.calculate("(2 + 3)(1 * 4)")
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Invalid expression syntax", e.message)
        }
    }

    @Test
    fun test_wrong_closing_bracket_placement_exception() {
        try {
            calculator.calculate("()*(1 * 4)")
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Invalid expression syntax", e.message)
        }
    }

    @Test
    fun test_wrong_e_constant_placement_exception() {
        try {
            calculator.calculate("2e")
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Invalid expression syntax", e.message)
        }
    }

    @Test
    fun test_wrong_pi_constant_placement_exception() {
        try {
            calculator.calculate("2pi")
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Invalid expression syntax", e.message)
        }
    }
}