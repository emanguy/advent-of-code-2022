data class ElfRange(val start: Int, val stop: Int) {
    operator fun contains(other: ElfRange) = start <= other.start && stop >= other.stop
    infix fun partiallyIn(other: ElfRange) = start in other.start..other.stop || stop in other.start..other.stop
}

fun main() {
    fun parseInput(inputs: List<String>): List<Pair<ElfRange, ElfRange>> =
        inputs.map {
            val (firstAssignment, secondAssignment) = it.split(",")
            val (firstAssignmentStart, firstAssignmentEnd) = firstAssignment.split("-").map(String::toInt)
            val (secondAssignmentStart, secondAssignmentEnd) = secondAssignment.split("-").map(String::toInt)

            Pair(
                ElfRange(firstAssignmentStart, firstAssignmentEnd),
                ElfRange(secondAssignmentStart, secondAssignmentEnd)
            )
        }

    fun part1(inputs: List<String>): Int {
        return parseInput(inputs).count { it.first in it.second || it.second in it.first }
    }

    fun part2(inputs: List<String>): Int {
        return parseInput(inputs).count { it.first partiallyIn it.second || it.second partiallyIn it.first }
    }

    // Verify the sample input works
    val inputs = readInput("Day04_test")
    check(part1(inputs) == 2)
    check(part2(inputs) == 4)

    val finalInputs = readInput("Day04")
    println(part1(finalInputs))
    println(part2(finalInputs))
}
