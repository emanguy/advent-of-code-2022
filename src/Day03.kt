fun itemPriority(item: Char): Int = when(item) {
    in 'a'..'z' -> item - 'a' + 1
    in 'A'..'Z' -> item - 'A' + 27
    else -> throw IllegalArgumentException("Not an item: $item")
}

fun main() {
    fun part1(inputs: List<String>): Int {
        var totalValue = 0
        inputLoop@ for (input in inputs) {
            val firstCompartment = input.take(input.length / 2)
            val secondCompartment = input.takeLast(input.length / 2)

            val firstCompartmentContents = firstCompartment.asSequence().toSet()

            for (character in secondCompartment.asSequence()) {
                if (character in firstCompartmentContents) {
                    totalValue += itemPriority(character)
                    continue@inputLoop
                }
            }
        }

        return totalValue
    }

    fun part2(inputs: List<String>): Int {
        var totalValue = 0
        for (inputChunk in inputs.chunked(3)) {
            val (bag1, bag2, bag3) = inputChunk
            val bag1Contents = bag1.asSequence().toSet()
            val bag2Contents = bag2.asSequence().toSet()
            val bag3Contents = bag3.asSequence().toSet()

            val commonItemSet = bag1Contents intersect bag2Contents intersect bag3Contents
            // Assuming there is exactly one item in common
            totalValue += itemPriority(commonItemSet.first())
        }

        return totalValue
    }

    // Verify the sample input works
    val inputs = readInput("Day03_test")
    check(part1(inputs) == 157)
    check(part2(inputs) == 70)

    val finalInputs = readInput("Day03")
    println(part1(finalInputs))
    println(part2(finalInputs))
}
