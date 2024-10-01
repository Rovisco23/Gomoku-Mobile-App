package pt.isel.pdm.project.domainmodel

data class GamePlayInputModel(val row: Int, val col: Char)

data class GameCreateInputModel(val userId: Int, val rule: String, val boardSize: Int, val token: String)

data class GameReloadInputModel(val gameId: String, val token: String)

data class LobbyInputModel(val userId: Int, val token: String)

data class GiveUpInputModel(val gameId: String, val token: String)

data class UserLoginCredentialInputModel(val username: String, val password: String)

data class UserSignInCredentialInputModel(
    val username: String,
    val email: String,
    val password: String
)
