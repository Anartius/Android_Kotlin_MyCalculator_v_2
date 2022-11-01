package com.example.mycalculator

class Stack {
    private val operators = mapOf("+" to 0, "-" to 0,
        "*" to 1, "/" to 1, "^" to 1, "(" to 1, ")" to 1)

    private val stack = mutableListOf<Pair<String, Int>>()

    fun isEmpty() = stack.isEmpty()

    fun getTopValue() = stack.last().first

    fun getPriority(value: String) = operators.getValue(value)

    fun getTopPriority() = stack.last().second

    fun push(value: String) {
        if (operators.containsKey(value)) {
            stack.add(value to operators.getValue(value))
        } else stack.add(value to 0)
    }

    fun pop() : String {
        val result = "" + stack.last().first
        stack.removeLast()
        return result
    }
}