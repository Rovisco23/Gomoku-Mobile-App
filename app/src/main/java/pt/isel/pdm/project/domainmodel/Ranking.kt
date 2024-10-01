package pt.isel.pdm.project.domainmodel

data class Ranking(
    val userId: Int,
    val username: String,
    val gamesPlayed: Int,
    val gamesWon: Int,
    val gamesDrawn: Int,
    val gamesLost: Int = gamesPlayed - gamesWon - gamesDrawn,
    val winRate: Double,
) {
    val points: Int = gamesWon * 3 + gamesDrawn - gamesLost * 2
}