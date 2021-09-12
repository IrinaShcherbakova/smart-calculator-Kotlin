package calculator

import java.math.BigInteger
import kotlin.Exception

object Calculator {
    private val variables = mutableMapOf<String, BigInteger>()

    fun calculate(input: String) {
        try {
            when {
                input.isEmpty() -> return
                isVariable(input) -> printVariableValue(input)
                isAssignment(input) -> processAssignment(input)
                else -> println(processExpression(input))
            }
        } catch(e: Exception){
            println(e.message)
            return
        }
    }

    private fun processExpression(input: String) : BigInteger {
        val symbols = try { convertInfixToPostfix(input) } catch(e: Exception){ throw Exception(e.message) }
        val stack = mutableListOf<BigInteger>()

        symbols.forEach{
            if(it is Operand){
                if(it.value is BigInteger) stack.add(it.value)
                else{
                    val value = variables[it.value] ?: throw Exception("Unknown variable")
                    stack.add(value)
                }
            } else if(it is Operator){
                val second = stack.removeLast()
                val first = stack.removeLast()
                val res = applyOp(first, it.op, second)
                stack.add(res)
            }
        }
        if(stack.size == 2)    return stack[1]
        return stack.first()
    }

    private fun convertInfixToPostfix(input: String) : List<Symbol>{
        val output = mutableListOf<Symbol>()
        output.add(Operand("0"))
        val operatorStack = mutableListOf<Operator>()
        val inputWithSpaces = insertSpacesInInput(input)
        val elements = inputWithSpaces.split(" ")

        elements.forEach {
            val trimmedElement = it.trim()
            when {
                trimmedElement.isEmpty() -> {}
                isNumeric(trimmedElement) -> output.add(Operand(trimmedElement))
                isVariable(trimmedElement) -> output.add(Operand(trimmedElement))
                else -> {
                    val currentOp = Operator(trimmedElement)
                    if(currentOp.op == '(' || operatorStack.isEmpty() || operatorStack.last().op == '('){
                        operatorStack.add(currentOp)
                    } else{
                        if(currentOp.op == ')'){
                            while(operatorStack.isNotEmpty() && operatorStack.last().op != '('){
                                output.add(operatorStack.removeLast())
                            }
                            if(operatorStack.isEmpty()) throw Exception("Invalid expression")
                            operatorStack.removeLast() //remove '('
                        } else{
                            if(currentOp.precedence > operatorStack.last().precedence){
                                operatorStack.add(currentOp)
                            } else{
                                while(operatorStack.isNotEmpty() &&
                                    (operatorStack.last().op != '(' || operatorStack.last().precedence <= currentOp.precedence)){
                                    output.add(operatorStack.removeLast())
                                }
                                operatorStack.add(currentOp)
                            }
                        }
                    }
                }
            }
        }

        if(operatorStack.any { it.op == '('})   throw Exception("Invalid expression")

        while(operatorStack.isNotEmpty()){
            output.add(operatorStack.removeLast())
        }

        return output
    }

    // insert whitespaces between numbers/variables and operators
    private fun insertSpacesInInput(input: String) : String{
        val output = StringBuilder()
        input.forEachIndexed { index, char ->
            val prevChar = if(index-1 >= 0) input[index-1] else ' '
            val nextChar = if(index+1 < input.length) input[index+1] else ' '
            if((isOp(char) && prevChar.isLetterOrDigit()) || char == '(' || char == ')'){
                output.append(" ")
            }
            output.append(char)
            if((isOp(char) && nextChar.isLetterOrDigit()) || char == '(' || char == ')'){
                output.append(" ")
            }
        }
        return output.toString()
    }

    private fun applyOp(left: BigInteger, op: Char, right: BigInteger): BigInteger {
        return when(op){
            '+' -> left.plus(right)
            '-' -> left.minus(right)
            '*' -> left.times(right)
            '/' -> left.div(right)
            else -> right
        }
    }

    private fun processAssignment(input: String) {
        if(input.isEmpty()) return
        val parts = input.split("=")
        if(parts.size != 2) throw Exception("Invalid assignment")

        val identifier = parts[0].trim(); val value = parts[1].trim()
        if(!isIdentifier(identifier)) throw Exception("Invalid identifier")

        //check the case when value is a variable, expression or numeric value
        when {
            isVariable(value) -> {
                if (!variables.containsKey(value)) throw Exception("Invalid assignment")
                variables[identifier] = variables[value]!!
            }
            else -> variables[identifier] = processExpression(value)
        }
    }

    private fun printVariableValue(input: String){
        println(if(variables.containsKey(input)) variables[input] else "Unknown variable")
    }

}

