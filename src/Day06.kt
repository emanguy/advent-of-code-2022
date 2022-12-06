fun main() {
    fun firstUniqueWindowIndex(input: String, windowSize: Int): Int {
        for (window in input.asSequence().withIndex().windowed(windowSize)) {
            val uniqueCharacterSet = window.map { it.value }.toSet()
            if (uniqueCharacterSet.size == windowSize) {
                return window.last().index + 1
            }
        }

        return -1
    }

    fun part1(input: String): Int {
        return firstUniqueWindowIndex(input, 4)
    }

    fun part2(input: String): Int {
        return firstUniqueWindowIndex(input, 14)
    }

    // Verify the sample input works
    val inputs = readInput("Day06_test")
    val expectedPart1Values = listOf(7, 5, 6, 10, 11)
    val expectedPart2Values = listOf(19, 23, 23, 29, 26)
    inputs.zip(expectedPart1Values).forEach { (input, expectedValue) ->
        check(part1(input) == expectedValue) { "Failed on input $input " }
    }
    inputs.zip(expectedPart2Values).forEach { (input, expectedValue) ->
        check(part2(input) == expectedValue) { "Failed on input $input" }
    }

    val finalInputs = readInput("Day06")
    println(part1(finalInputs.first()))
    println(part2(finalInputs.first()))
}
