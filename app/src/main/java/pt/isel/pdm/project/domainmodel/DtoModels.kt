package pt.isel.pdm.project.domainmodel

import java.text.DecimalFormat
import java.util.UUID

data class SirenMapDtoModel(
    val properties: Map<String, Any>
) {
    fun toUserInfo(): UserInfo {
        val properties = this.properties
        val id = properties["userId"] as Double
        return UserInfo(
            id.toInt(),
            properties["username"] as String,
            properties["token"] as String
        )
    }

    fun toUser(): User {
        val properties = this.properties
        val id = properties["id"] as Double
        return User(
            id.toInt(),
            properties["username"] as String,
            properties["email"] as String
        )
    }

    fun toGame(): Game {
        val properties = this.properties
        val id = properties["id"] as String
        val winner = properties["winner"] as Double
        return Game(
            UUID.fromString(id),
            makeBoard(properties["board"] as Map<String, *>),
            GameState.fromString(properties["state"] as String),
            (properties["playerB"] as Double).toInt(),
            (properties["playerW"] as Double).toInt(),
            winner.toInt()
        )
    }
}

private fun makeBoard(board: Map<String, *>): Board {
    val size = (board["size"] as Double).toInt()
    return Board(
        Board.toCells(board["cells"] as String, size),
        size,
        (board["turn"] as String).first()
    )
}

data class SirenListDtoModel(
    val properties: List<Any>
) {
    fun toRankingDtoModel(): List<Ranking> {
        val properties = this.properties
        val list = mutableListOf<Ranking>()
        properties.forEach {
            val property = it as Map<String, *>
            val winRate = it["win_rate"] as Double
            val decimalFormat = DecimalFormat("#.##")
            decimalFormat.maximumFractionDigits = 2
            list.add(
                Ranking(
                    (property["userId"] as Double).toInt(),
                    property["username"] as String,
                    (property["games_played"] as Double).toInt(),
                    (property["games_won"] as Double).toInt(),
                    (property["games_drawn"] as Double).toInt(),
                    (property["games_played"] as Double).toInt() -
                            (property["games_won"] as Double).toInt() -
                            (property["games_drawn"] as Double).toInt(),
                    decimalFormat.format(winRate).toDouble()
                )
            )
        }
        return list
    }
}

data class SirenMessageDtoModel(
    val properties: String
) {
    fun toMessageDtoModel(): String {
        return this.properties
    }
}

fun main() {
    val s = "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"
    val size = 15
    val turn = 'B'
    val board = Board(
        Board.toCells(s, size),
        size,
        turn
    )
    println(board)
}