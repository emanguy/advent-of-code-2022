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
    val stacks = mutableListOf<Stack<Char>>()
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
            } else { // Otherwise there should be no additional crates, and we can set the input string blank
                ""
            }
            stackIndex++
        }
    }

    // Parse the instructions
    val instructionRegex = """^move (\d+) from (\d+) to (\d+)$""".toRegex()
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

fun topsOfStacks(stacks: List<Stack<Char>>) = stacks
    .filter { it.isNotEmpty() }
    .map { it.peek() }
    .fold("") { finalString, currentChar -> finalString + currentChar }

fun main() {

    fun part1(inputs: List<String>): String {
        val (stacks, instructions) = parseInput(inputs)

        for (instruction in instructions) {
            var amountToMove = instruction.quantity
            while (amountToMove > 0 && stacks[instruction.sourceIdx].isNotEmpty()) {
                stacks[instruction.destinationIdx].push(stacks[instruction.sourceIdx].pop())
                amountToMove--
            }
        }

        return topsOfStacks(stacks)
    }

    fun part2(inputs: List<String>): String {
        val (stacks, instructions) = parseInput(inputs)

        for (instruction in instructions) {
            var amountToMove = instruction.quantity
            val bufferStack = Stack<Char>()

            while (amountToMove > 0 && stacks[instruction.sourceIdx].isNotEmpty()) {
                bufferStack.push(stacks[instruction.sourceIdx].pop())
                amountToMove--
            }

            while (bufferStack.isNotEmpty()) {
                stacks[instruction.destinationIdx].push(bufferStack.pop())
            }
        }

        return topsOfStacks(stacks)
    }

    // Verify the sample input works
    val inputs = readInput("Day05_test")
    check(part1(inputs) == "CMZ")
    check(part2(inputs) == "MCD")

    val finalInputs = readInput("Day05")
    println(part1(finalInputs))
    println(part2(finalInputs))
}
