import java.lang.IllegalArgumentException

enum class RPSResult(val resultPoints: Int) {
    WIN(6) {
        override fun whatToPlayAgainst(move: RPSMove) = when (move) {
            RPSMove.ROCK -> RPSMove.PAPER
            RPSMove.PAPER -> RPSMove.SCISSORS
            RPSMove.SCISSORS -> RPSMove.ROCK
        }
    },

    TIE(3) {
        override fun whatToPlayAgainst(move: RPSMove) = move
    },

    LOSS(0) {
        override fun whatToPlayAgainst(move: RPSMove) = when (move) {
            RPSMove.ROCK -> RPSMove.SCISSORS
            RPSMove.PAPER -> RPSMove.ROCK
            RPSMove.SCISSORS -> RPSMove.PAPER
        }
    };

    abstract fun whatToPlayAgainst(move: RPSMove): RPSMove

    companion object {
        fun fromString(string: String): RPSResult = when (string.lowercase()) {
            "x" -> LOSS
            "y" -> TIE
            "z" -> WIN
            else -> throw IllegalArgumentException("Not a result: $string")
        }
    }
}

enum class RPSMove(val movePoints: Int) {
    ROCK(1) {
        override fun resultVersus(other: RPSMove) = when (other) {
            PAPER -> RPSResult.LOSS
            SCISSORS -> RPSResult.WIN
            ROCK -> RPSResult.TIE
        }
    },

    PAPER(2) {
        override fun resultVersus(other: RPSMove) = when (other) {
            SCISSORS -> RPSResult.LOSS
            ROCK -> RPSResult.WIN
            PAPER -> RPSResult.TIE
        }
    },

    SCISSORS(3) {
        override fun resultVersus(other: RPSMove) = when (other) {
            ROCK -> RPSResult.LOSS
            PAPER -> RPSResult.WIN
            SCISSORS -> RPSResult.TIE
        }
    };

    abstract fun resultVersus(other: RPSMove): RPSResult

    companion object {
        fun fromString(string: String): RPSMove = when (string.lowercase()) {
            "a", "x" -> ROCK
            "b", "y" -> PAPER
            "c", "z" -> SCISSORS
            else -> throw IllegalArgumentException("Not a valid move: $string")
        }
    }
}

fun main() {
    fun part1(inputs: List<String>): Int {
        var totalScore = 0
        for (rpsRound in inputs) {
            val (theirMove, myMove) = rpsRound.split(" ").map(RPSMove::fromString)
            totalScore += myMove.movePoints + myMove.resultVersus(theirMove).resultPoints
        }

        return totalScore
    }

    fun part2(inputs: List<String>): Int {
        var totalScore = 0
        for (rpsRound in inputs) {
            val (theirMoveStr, myResultStr) = rpsRound.split(" ")
            val theirMove = RPSMove.fromString(theirMoveStr)
            val myResult = RPSResult.fromString(myResultStr)
            val myMove = myResult.whatToPlayAgainst(theirMove)

            totalScore += myMove.movePoints + myResult.resultPoints
        }

        return totalScore
    }

    // Verify the sample input works
    val inputs = readInput("Day02_test")
    check(part1(inputs) == 15)
    check(part2(inputs) == 12)

    val finalInputs = readInput("Day02")
    println(part1(finalInputs))
    println(part2(finalInputs))
}