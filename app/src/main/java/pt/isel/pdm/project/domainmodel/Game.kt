package pt.isel.pdm.project.domainmodel

import java.util.*

enum class GameState {
    STARTING,
    ON_GOING,
    FINISHED;

    override fun toString(): String {
        return when {
            this == STARTING -> "Starting"
            this == ON_GOING -> "On Going"
            this == FINISHED -> "Finished"
            else -> throw IllegalArgumentException("Invalid value for GameState")
        }
    }
    companion object {
        fun fromString(s: String) = when (s) {
            "Starting" -> STARTING
            "On Going" -> ON_GOING
            "Finished" -> FINISHED
            else -> throw IllegalArgumentException("Invalid value for GameState")
        }
    }
}

enum class CellState(val char: Char) {
    EMPTY('-'),
    PLAYER_B('B'),
    PLAYER_W('W');

    override fun toString() =
        if (this == PLAYER_B) "Black Player" else if (this == PLAYER_W) "White Player" else "Empty"

    fun getNextTurn() = if (this == PLAYER_B) 'W' else 'B'

    companion object {
        fun fromChar(c: Char) = when (c) {
            '-' -> EMPTY
            'B' -> PLAYER_B
            'W' -> PLAYER_W
            else -> throw IllegalArgumentException("Invalid value for Board.State")
        }
    }
}

data class Board(
    private val cells: Array<Array<CellState>>,
    private val size: Int,
    private val turn: Char
) {
    fun get(row: Int, col: Int) = cells[row][col]
    fun mutate(currTurn: CellState, nextTurn: Char, row: Int, col: Int): Board {
        val newBoardCells = Array(size) { r -> Array(size) { c -> cells[r][c] } }
        newBoardCells[row][col] = currTurn
        return Board(newBoardCells, size, nextTurn)
    }

    fun getSize() = size
    fun getCurrTurn() = turn

    companion object {

        fun create(size: Int) =
            Board(Array(size) { Array(size) { CellState.EMPTY } }, size, 'B')

        fun fromString(s: String): Board {
            val boardValues = s.split('/')
            val size = boardValues[1].toInt()
            require(s.split('/')[0].length == size * size)
            val boardState = Array(size) { Array(size) { CellState.EMPTY } }
            for (row in 0 until size)
                for (col in 0 until size)
                    boardState[row][col] = CellState.fromChar(s[row * size + col])
            return Board(
                boardState,
                size,
                boardValues[2].first()
            )
        }

        fun toCells(boardCells: String, size: Int): Array<Array<CellState>> {
            val boardState = Array(size) { Array(size) { CellState.EMPTY } }
            for (row in 0 until size)
                for (col in 0 until size)
                    boardState[row][col] = CellState.fromChar(boardCells[row * size + col])
            return boardState
        }
    }

    override fun toString(): String =
        cells.flatMap { row -> row.map { it.char } }.joinToString("") + "/$size/$turn"

    fun getNumberOfCells (): Int {
        var totalCells = 0
        cells.forEach { it.forEach { cell-> if (cell != CellState.EMPTY) totalCells++ } }
        return totalCells
    }
}

data class Game(
    val id: UUID,
    val board: Board,
    val state: GameState,
    val playerB: Int,
    val playerW: Int,
    val winner: Int? = null
)