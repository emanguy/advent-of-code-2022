import java.util.Stack

data class Instruction(
    val quantity: Int,
    val sourceIdx: Int,
    val destinationIdx: Int,
)
data class ParsedInput(
    val stacks: MutableList<Stack<Char>>,
    val instructions: List<Instruction>
)

fun MutableList<Stack<Char>>.addCrate(stackIdx: Int, value: Char) {
    while (this.size <= stackIdx) {
        this.add(Stack())
    }

    this[stackIdx].push(value)
}

fun parseInput(inputs: List<String>): ParsedInput {
    val lineIterator = inputs.iterator()
    val stackLines = mutableListOf<String>()

    // Read the contents of the stacks
    var currentLine = lineIterator.next()
    do {
        stackLines += currentLine
        currentLine = lineIterator.next()
    } while (currentLine != "")

    // Drop the stack "titles"
    stackLines.removeLast()

    // Reverse the input & build the stacks
    var stacks = mutableListOf<Stack<Char>>()
    for (stackInput in stackLines.reversed()) {
        var workingInput = stackInput

        var stackIndex = 0
        while (workingInput.isNotEmpty()) {
            // If the first character of the string is a bracket we have a value for the stack
            if (workingInput[0] == '[') {
                // The second character is our letter, so push that onto a stack
                stacks.addCrate(stackIndex, workingInput[1])
            }

            // If possible, truncate the first 4 letters to move on to the next crate
            workingInput = if (workingInput.length > 3) {
                workingInput.substring(4)
            } else {
                ""
            }
            stackIndex++
        }
    }

    // Parse the instructions
    var instructionRegex = """^move (\d+) from (\d+) to (\d+)$""".toRegex()
    val instructions = mutableListOf<Instruction>()

    // The iterator should only have the instructions left
    for (instructionLine in lineIterator) {
        val parsedString = instructionRegex.matchEntire(instructionLine) ?: throw IllegalArgumentException("Got a bad instruction: $instructionLine")
        val (_, quantity, source, destination) = parsedString.groupValues
        instructions += Instruction(
            quantity.toInt(),
            source.toInt() - 1, // We want 0 indexed
            destination.toInt() - 1,
        )
    }

    return ParsedInput(stacks, instructions)
}

fun main() {
    fun part1(inputs: List<String>): String {
        val stacksAndInstructions = parseInput(inputs)

        for (instruction in stacksAndInstructions.instructions) {
            var amountToMove = instruction.quantity
            while (amountToMove > 0 && stacksAndInstructions.stacks[instruction.sourceIdx].isNotEmpty()) {
                stacksAndInstructions.stacks[instruction.destinationIdx].push(stacksAndInstructions.stacks[instruction.sourceIdx].pop())
            }
        }

        return stacksAndInstructions.stacks.map { it.peek() }.fold("") { finalString, currentChar -> finalString + currentChar }
    }

    fun part2(inputs: List<String>): Int {
        TODO()
    }

    // Verify the sample input works
    val inputs = readInput("Day05_test")
    println(part1(inputs))
//    check(part1(inputs) == 0)
//    check(part2(inputs) == 0)

//    val finalInputs = readInput("DayXX")
//    part1(finalInputs)
//    part2(finalInputs)
}
