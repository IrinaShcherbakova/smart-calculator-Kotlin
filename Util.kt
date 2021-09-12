package calculator

private val identifierRegex = "\\b[a-zA-Z]+\\b".toRegex()
//private val identifierRegex = "[a-zA-Z]+\\s*".toRegex()
private val valueRegex = "\\s*[0-9]+".toRegex()
private val expressionRegex = ".*[+-/*].*".toRegex()
//val assignmentRegex = "[a-zA-Z]+\\s*=\\s*[0-9]+".toRegex()
private val operands = listOf('*', '+', '-', '/')

fun isNumeric(input: String) = input.all { it.isDigit()}

fun isAssignment(input: String) = input.contains("=")

fun isVariable(input: String) = identifierRegex.matches(input)

fun isExpression(input: String) = expressionRegex.matches(input)

fun isOp(input: Char) = operands.any { it == input }

fun isIdentifier(input: String) = identifierRegex.matches(input)

fun isValue(input: String) = valueRegex.matches(input)