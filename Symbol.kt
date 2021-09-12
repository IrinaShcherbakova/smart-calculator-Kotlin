package calculator

private val addition = "[+]+".toRegex()
private val subtraction = "[-]+".toRegex()
private val multiplication = "\\*".toRegex()
private val division = "/".toRegex()
private val leftParenthesis = "\\(".toRegex()
private val rightParenthesis = "\\)".toRegex()

abstract class Symbol {
    abstract fun isOperator() : Boolean
    abstract fun isOperand() : Boolean
}

class Operator(val input: String) : Symbol(){
    val op = try{processOperatorSequence(input)} catch (e: Exception){throw Exception(e.message)}
    val precedence = if(op == '+' || op == '-') 0 else 1

    override fun isOperand() : Boolean = true
    override fun isOperator() : Boolean = false

    private fun processOperatorSequence(input: String) : Char {
        if(input.length == 1 && isOp(input[0])) return input[0]
        return when{
            addition.matches(input) -> '+'
            subtraction.matches(input) -> if(input.length%2 == 0) '+' else '-'
            multiplication.matches(input) -> '*'
            division.matches(input) -> '/'
            leftParenthesis.matches(input) -> '('
            rightParenthesis.matches(input) -> ')'
            else -> throw Exception("Invalid expression")
        }
    }
}

class Operand(val input: String) : Symbol(){
    val value = if(isNumeric(input)) input.toBigInteger() else input

    override fun isOperand() : Boolean = false
    override fun isOperator() : Boolean = true
}