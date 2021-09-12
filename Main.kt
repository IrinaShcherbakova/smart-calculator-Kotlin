package calculator


fun main() {
    val commandRegex = "/.*".toRegex()
    while(true){
        val input = readLine()!!.trim()
        when {
            input == "/help" -> println("The program calculates the sum of numbers")
            input == "/exit" -> break
            input.matches(commandRegex) -> println("Unknown command")
            else -> Calculator.calculate(input)
        }
    }

    println("Bye!")
}








