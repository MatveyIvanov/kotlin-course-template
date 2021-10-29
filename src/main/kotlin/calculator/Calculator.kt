package calculator

import java.io.CharConversionException
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import java.util.Stack
import kotlin.math.pow


val OPERATIONS = mapOf('+' to 1, '-' to 1, '*' to 2, '/' to 2, '^' to 3) // Map of operations and their priorities

class Calculator {

    fun calculate(expr: String) : Double = calculatePolishNotation(getPolishNotation(expr)) // Calculation of math expression

    private fun getPolishNotation(expression: String) : String {
        /*
        *   Method that takes math expression as a parameter and transforms it to the polish notation
        */
        val expr: String = expression.replace(" ", "") // Delete all spaces

        if (expr.isEmpty())
            throw IllegalArgumentException("Expression cannot be empty")
        if (expr[0] in OPERATIONS.keys.filter { key -> OPERATIONS[key]!! > 1 })
            throw IllegalArgumentException("Expression cannot start with binary operation")
        if (expr[expr.length - 1] in OPERATIONS.keys)
            throw IllegalArgumentException("Expression cannot end with operation")

        val stack = Stack<Char>() // Stack for brackets and operations
        var polishNotation = ""
        var bracketCounter = 0 /* If 0 then no brackets or all opened brackets have been closed.
                                  If >0 then there is at least 1 opened bracket
                                  If <0 then there is closed bracket without opened one
                                */
        var pi = false // True if 'p' in expression. If true and 'i' in expression, then add pi constant to the expression
        var pointed = false // True if '.' of current number already in polish notation
        expr.forEachIndexed { index, it ->
            when {
                it in OPERATIONS.keys -> {
                    pointed = false
                    polishNotation += processMathOperation(expr, it, index, stack)
                }
                it.isDigit() -> {
                    polishNotation += it // Just add digit of current number to polish notation
                }
                it == '.' && !pointed -> { // if '.' and current number does not have floating point
                    pointed = true
                    polishNotation += it // Add floating point to current number
                }
                it == '.' && pointed -> { // If '.' and current number already has floating point
                    throw NumberFormatException("Number can have only one floating point")
                }
                it == '(' -> {
                    pointed = false
                    bracketCounter++ // Increase bracket counter because opening bracket was added
                    processOpeningBracket(expr, index, stack)
                }
                it == ')' -> {
                    pointed = false
                    bracketCounter-- // Decrease bracket counter because closing bracket was added

                    // Bracket counter cannot be negative, otherwise there is closing bracket w/o opening one
                    if (bracketCounter < 0)
                        throw IllegalArgumentException("There must be an opening bracket before closing one")
                    polishNotation += processClosingBracket(expr, index, stack)
                }
                it == 'e' -> { // E constant
                    pointed = false
                    polishNotation += processEConstant(expr, index)
                }
                it == 'p' -> { // Potential pi constant
                    pointed = false
                    pi = true // Mark that pi constant may appear
                }
                it == 'i' && pi -> { // If previous char was 'p' and current is 'i' then add pi constant to polish notation
                    pointed = false
                    pi = false
                    polishNotation += processPiConstant(expr, index)
                }
                else -> { // Unsupported symbol appeared in expression
                    throw CharConversionException("Unsupported symbol '$it'")
                }
            }
        }
        if (bracketCounter > 0) // If not all opening brackets were closed
            throw IllegalArgumentException("Missing closing bracket")

        // Get remaining operations from stack
        while (!stack.empty()) {
            polishNotation += stack.pop()
        }

        return polishNotation
    }

    private fun processMathOperation(expr: String, chr: Char, index: Int, stack: Stack<Char>): String {
        if (charArrayOf('+', '-').contains(chr) && (index == 0 || expr[index - 1] == '(' || expr[index - 1] in OPERATIONS.keys)) {
            return chr.toString() // Unary operation
        }
        if (expr[index + 1] in OPERATIONS.keys.filter { key -> OPERATIONS[key]!! > 1 }) {
            throw IllegalArgumentException("Operation cannot be followed by binary operation")
        }
        var toAdd = "," // Add comma to detect end of the number in polish notation

        /*
        *   Pop all operations from stack which priority not lower than current operation
        */
        while (!stack.empty() && stack.peek() in OPERATIONS.keys && OPERATIONS[stack.peek()]!! >= OPERATIONS[chr]!!) {
            toAdd += stack.pop()
        }
        stack.push(chr) // Push current operation to the stack

        return toAdd
    }

    private fun processOpeningBracket(expr: String, index: Int, stack: Stack<Char>) {
        /*
        *  Opening bracket can be placed:
        *   - As first symbol in expression
        *   - Before digit, constant, opening bracket or unary operation
        *   - After opening bracket or operation
        */
        if (index + 1 < expr.length &&
            (expr[index + 1].isDigit() || expr[index + 1] == '(' || charArrayOf('+', '-', 'e', 'p').contains(expr[index + 1])) &&
            (index == 0 || expr[index - 1] == '(' || expr[index - 1] in OPERATIONS.keys)
        ) {
            stack.push('(')
        }
        else { // Wrong opening bracket placement
            throw IllegalArgumentException("Invalid expression syntax")
        }
    }

    private fun processClosingBracket(expr: String, index: Int, stack: Stack<Char>): String {
        /*
        *  Closing bracket can be placed:
        *   - As last symbol in expression
        *   - Before operation or closing bracket
        *   - After digit, closing bracket or constant
        */
        var toAdd = ""
        if ((expr[index - 1].isDigit() || expr[index - 1] == ')' || charArrayOf('e', 'i').contains(expr[index - 1])) &&
            (index == expr.length - 1 || (index + 1 < expr.length && (expr[index + 1] == ')' || expr[index + 1] in OPERATIONS.keys)))
        ) {
            // Get all operation from stack until opening bracket appears
            while (!stack.empty() && stack.peek() != '(') {
                toAdd += stack.pop() // Add operation to polish notation
            }
            /*
            * Theoretically, stack cannot be empty, otherwise there is wrong bracket placement in text,
            * which is checked before this block. But it's decided to check stack to prevent crashes
            */
            if (!stack.empty())
                stack.pop() // Delete opening bracket from stack

            return toAdd
        }
        else { // Wrong closing bracket placement
            throw IllegalArgumentException("Invalid expression syntax")
        }
    }

    private fun processEConstant(expr: String, index: Int): String {
        /*
        * E constant can be placed:
        *   - As a first symbol with operation after it
        *   - As a last symbol with operation before it
        *   - After operation (binary/unary) or opening bracket, but before operation or closing bracket
        */
        if ((index == 0 && expr[index + 1] in OPERATIONS.keys) ||
            (index == expr.length - 1 && expr[index - 1] in OPERATIONS.keys) ||
            ((expr[index - 1] in OPERATIONS.keys || expr[index - 1] == '(') &&
                    (expr[index + 1] in OPERATIONS.keys || expr[index + 1] == ')'))
        ) {
            return "e"
        }
        else { // Wrong e constant placement
            throw IllegalArgumentException("Invalid expression syntax")
        }
    }

    private fun processPiConstant(expr: String, index: Int): String {
        /*
        * Pi constant can be placed:
        *   - As a first symbol with operation after it
        *   - As a last symbol with operation before it (index - 2 because 'p' is before 'i')
        *   - After operation (binary/unary) or opening bracket, but before operation or closing bracket
        */
        if ((index == 0 && expr[index + 1] in OPERATIONS.keys) ||
            (index == expr.length - 1 && expr[index - 2] in OPERATIONS.keys) ||
            ((expr[index - 2] in OPERATIONS.keys || expr[index - 2] == '(') &&
                    (expr[index + 1] in OPERATIONS.keys || expr[index + 1] == ')'))
        ) {
            return "p"
        }
        else { // Wrong pi constant placement
            throw IllegalArgumentException("Invalid expression syntax")
        }
    }

    private fun calculatePolishNotation(expr: String): Double {
        val stack = Stack<Double>() // Stack for intermediate results
        var curNum = "" // Current number
        expr.forEachIndexed { index, it ->
            when {
                it.isDigit() || it == '.' -> { // Get the next digit of current number ('.' if number is double)
                    curNum += it
                }
                (it == '+' || it == '-') && curNum == "" && index + 1 < expr.length &&
                        (expr[index + 1].isDigit() || charArrayOf('e', 'p').contains(expr[index + 1])) -> { // Unary operation
                    curNum += it
                }
                it == 'e' -> { // E constant
                    curNum += Math.E.toString()
                }
                it == 'p' -> { // Pi constant
                    curNum += Math.PI.toString()
                }
                it in OPERATIONS.keys -> { // Math operation
                    // Push current number to the stack if it exists
                    if (curNum != "") {
                        stack.push(curNum.toDouble())
                        curNum = ""
                    }
                    // Get first two numbers from stack
                    val a = stack.pop()
                    val b = stack.pop()
                    // Push new number to the stack depending on current operation
                    when (it) {
                        '+' -> { stack.push(a + b) }
                        '-' -> { stack.push(b - a) }
                        '*' -> { stack.push(a * b) }
                        '/' -> { stack.push(b / a) }
                        '^' -> { stack.push(b.pow(a)) }
                    }
                }
                it == ',' -> { // Delimiter appeared
                    if (curNum != "") { // If current number is not empty then push it to the stack
                        stack.push(curNum.toDouble())
                        curNum = ""
                    }
                }
            }
        }
        if (stack.empty() && curNum != "")
            return curNum.toDouble()
        return stack.peek()
    }
}