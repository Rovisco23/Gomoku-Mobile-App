package pt.isel.pdm.project.domainmodel

data class UserInfo(val id: Int, val username: String, val token: String) {
    init {
        require(validateUserInfoParts(id, username, token))
    }
}

fun validateUserInfoParts(id: Int, username: String, token: String): Boolean {
    return id > 0 && username.isNotBlank() && token.isNotBlank()
}