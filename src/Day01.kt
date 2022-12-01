fun main() {
    fun part1(input: List<String>): Int {
        var maxSum = 0
        var currentSum = 0
        for (calorieCount in input) {
            if (calorieCount.isBlank()) {
                if (currentSum > maxSum) maxSum = currentSum
                currentSum = 0
                continue
            }

            currentSum += calorieCount.toInt()
        }

        if (currentSum != 0 && maxSum < currentSum) maxSum = currentSum

        return maxSum
    }

    fun addSumToQueue(listToModify: MutableList<Int>, newValue: Int) {
        listToModify += newValue
        listToModify.sort()
        if (listToModify.size > 3) listToModify.removeFirst()
    }

    fun part2(input: List<String>): Int {
        val topSums = mutableListOf<Int>()
        var currentSum = 0
        for (calorieCount in input) {
            if (calorieCount.isBlank()) {
                addSumToQueue(topSums, currentSum)
                currentSum = 0
                continue
            }

            currentSum += calorieCount.toInt()
        }

        if (currentSum != 0) addSumToQueue(topSums, currentSum)

        println("Part 2 top sums: $topSums")
        return topSums.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
