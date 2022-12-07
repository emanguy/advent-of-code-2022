import java.lang.IllegalArgumentException
import java.util.Stack

sealed class FSEntity {
    data class Directory(
        val name: String,
        val parent: Directory? = null,
        val children: MutableList<FSEntity> = mutableListOf()
    ) : FSEntity() {

        private var cachedSize: Long? = null

        fun size(): Long {
            if (cachedSize == null) {
                cachedSize = children.sumOf {
                    when (it) {
                        is Directory -> it.size()
                        is File -> it.size
                    }
                }
            }

            return cachedSize!!
        }
    }

    data class File(val name: String, val size: Long) : FSEntity()
}


fun main() {
    fun parseInput(inputs: List<String>): FSEntity.Directory {
        var workingDirectory = FSEntity.Directory("/")

        for (input in inputs) {
            // The initial cd / and ls commands are effectively no-ops in terms of parsing
            if (input == "$ cd /" || input == "$ ls") continue
            // Doing a command
            if (input.startsWith("$")) { // Handle a directory change
                val targetDirectory = input.removePrefix("$ cd ")

                workingDirectory = if (targetDirectory == "..") {
                    // We can't go higher than root
                    workingDirectory.parent ?: workingDirectory
                } else {
                    workingDirectory.children.find { it is FSEntity.Directory && it.name == targetDirectory } as FSEntity.Directory?
                        ?: throw IllegalArgumentException("Cannot enter nonexistent directory: $targetDirectory")
                }
            } else if (input.startsWith("dir ")) { // Handling a directory
                workingDirectory.children += FSEntity.Directory(input.removePrefix("dir "), workingDirectory)
            } else { // Handling a file
                val (size, name) = input.split(" ")
                workingDirectory.children += FSEntity.File(name, size.toLong())
            }
        }

        while (workingDirectory.parent != null) {
            workingDirectory = workingDirectory.parent!!
        }

        return workingDirectory
    }

    fun allDirectoriesInFs(directoryRoot: FSEntity.Directory): MutableList<FSEntity.Directory> {
        val allDirectories = mutableListOf(directoryRoot)
        val directoriesToCheck = Stack<FSEntity.Directory>()
        directoriesToCheck += directoryRoot

        while (directoriesToCheck.isNotEmpty()) {
            val currentDirectory = directoriesToCheck.pop()
            val childDirectories = currentDirectory.children.filterIsInstance<FSEntity.Directory>()
            allDirectories.addAll(childDirectories)
            directoriesToCheck.addAll(childDirectories)
        }
        return allDirectories
    }

    fun part1(inputs: List<String>): Long {
        val directoryRoot = parseInput(inputs)
        val allDirectories = allDirectoriesInFs(directoryRoot)

        return allDirectories.filter { it.size() <= 100_000 }.sumOf { it.size() }
    }

    fun part2(inputs: List<String>): Long {
        val directoryRoot = parseInput(inputs)
        val allDirectories = allDirectoriesInFs(directoryRoot)

        val totalDiskSize = 70_000_000L
        val requiredEmptySpace = 30_000_000L
        val currentDiskUsage = directoryRoot.size()
        val currentFreeSpace = totalDiskSize - currentDiskUsage
        val amountToDelete = requiredEmptySpace - currentFreeSpace

        allDirectories.sortBy { it.size() }
        return allDirectories.first { it.size() >= amountToDelete }.size()
    }

    // Verify the sample input works
    val inputs = readInput("Day07_test")
    check(part1(inputs) == 95437L)
    check(part2(inputs) == 24933642L)

    val finalInputs = readInput("Day07")
    println(part1(finalInputs))
    println(part2(finalInputs))
}
