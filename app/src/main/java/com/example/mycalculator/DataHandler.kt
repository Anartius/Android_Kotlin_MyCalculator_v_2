package com.example.mycalculator

import java.math.BigDecimal
import java.math.MathContext
import java.security.InvalidParameterException

fun getInputAsList(input: String) : MutableList<String> {

    val regex = """(^-*\d+\.\d+)|(^-*\d+)|(~?\d+\.\d+)|(~?\d+)|\)|\(|[-+*/^]""".toRegex()

    val inputString = input.replace("\\(-".toRegex(), "(~")

    val inputList = regex.findAll(inputString).map { it.value }.toMutableList()
    for (i in inputList.indices) {
        inputList[i] = inputList[i].replace("~", "-")
    }
    if (inputList[0] == "-") inputList.add(0, "0")

    val amountOfDigital = inputList.count { """(-?\d+\.\d+)|(-?\d+)""".toRegex().matches(it) }
    val amountOfOperators = inputList.count { "[-+*/^]".toRegex().matches(it) }
    val amountOfLeftPar = inputList.count { it == "(" }
    val amountOfRightPar = inputList.count { it == ")" }

    if (amountOfDigital != amountOfOperators + 1 ||
        amountOfLeftPar != amountOfRightPar) throw InvalidParameterException()

    return inputList
}

fun toPostfix(infix: MutableList<String>) : MutableList<String> {
    val stack = Stack()
    val postfix = mutableListOf<String>()

    for (i in infix.indices) {

        if (infix[i].matches("""(-?\d+\.\d+)|(-?\d+)""".toRegex())) {
            postfix.add(infix[i])

        } else if (infix[i].matches("^[-+*/^()]$".toRegex())) {
            val currentItemPriority = stack.getPriority(infix[i])

            if (infix[i] != ")" && (stack.isEmpty() ||
                        stack.getTopPriority() < currentItemPriority)) {

                stack.push(infix[i])

            } else if (infix[i] == "(") {
                stack.push(infix[i])

            } else if (infix[i] == ")") {
                if (stack.getTopValue() != "(") {
                    while (stack.getTopValue() != "(") {
                        postfix.add(stack.pop())
                    }
                    stack.pop()
                }

            } else if ((stack.getTopPriority() >= currentItemPriority)) {
                if (stack.getTopValue() != "(") {
                    while (!stack.isEmpty() && stack.getTopValue() != "(" &&
                        (stack.getTopPriority() >= currentItemPriority)) {

                        postfix.add(stack.pop())
                    }
                    stack.push(infix[i])

                } else {
                    stack.push(infix[i])
                }
            }
        }

        if (i == infix.size - 1) {
            while (!stack.isEmpty()) postfix.add(stack.pop())
        }
    }

    return postfix
}

fun getResult(inputAsList: MutableList<String>) : String {
    val stack = Stack()
    var a: BigDecimal
    var b: BigDecimal

    for (i in inputAsList.indices) {
        if ("""(-?\d+\.\d+)|(-?\d+)""".toRegex().matches(inputAsList[i])) {
            stack.push(inputAsList[i])

        } else {
            b = stack.pop().toBigDecimal()
            a = stack.pop().toBigDecimal()
            when (inputAsList[i]) {
                "-" -> stack.push((a.subtract(b)).toString())
                "+" -> stack.push((a.add(b)).toString())
                "*" -> stack.push((a.multiply(b)).toString())
                "/" -> {
                    if (b == BigDecimal.ZERO) {
                        throw InvalidParameterException()
                    } else stack.push(((a.divide(b, MathContext(7))).toString()))
                }
                else -> stack.push((a.pow(b.toInt())).toString())
            }
        }
    }
    return stack.pop()
}