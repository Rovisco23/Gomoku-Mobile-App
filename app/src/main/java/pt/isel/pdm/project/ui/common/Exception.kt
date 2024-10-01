package pt.isel.pdm.project.ui.common

class UpdateGameException(message: String, cause: Throwable? = null) : Exception(message, cause)
class CreateGameException(message: String, cause: Throwable? = null) : Exception(message, cause)
class GetLobbiesException(message: String, cause: Throwable? = null) : Exception(message, cause)
class GetRankingsException(message: String, cause: Throwable? = null) : Exception(message, cause)
class GetUserException(message: String, cause: Throwable? = null) : Exception(message, cause)
class LoginException(message: String, cause: Throwable? = null) : Exception(message, cause)
class LogoutException(message: String, cause: Throwable? = null) : Exception(message, cause)
class SignInException(message: String, cause: Throwable? = null) : Exception(message, cause)