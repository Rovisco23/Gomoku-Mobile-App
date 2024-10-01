package pt.isel.pdm.project.infrastructure

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import pt.isel.pdm.project.domainmodel.UserInfo
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit

private const val ID_KEY = "id"
private const val USERNAME_KEY = "username"
private const val TOKEN_KEY = "token"

class UserInfoDataStore(private val store: DataStore<Preferences>) : UserInfoRepository {

    private val idKey = intPreferencesKey(ID_KEY)
    private val usernameKey = stringPreferencesKey(USERNAME_KEY)
    private val tokenKey = stringPreferencesKey(TOKEN_KEY)

    override suspend fun getUserInfo(): UserInfo? {
        val preferences = store.data.first()
        val id = preferences[idKey]
        val username = preferences[usernameKey]
        val token = preferences[tokenKey]
        return if (id != null && username != null && token != null) UserInfo(
            id,
            username,
            token
        ) else null
    }

    override suspend fun updateUserInfo(userInfo: UserInfo?) {
        userInfo?.let {
            store.edit { preferences ->
                preferences[idKey] = it.id
                preferences[usernameKey] = it.username
                preferences[tokenKey] = it.token
            }
        }
    }

    override suspend fun clearUserInfo() {
        store.edit { preferences ->
            preferences.remove(idKey)
            preferences.remove(usernameKey)
            preferences.remove(tokenKey)
        }
    }
}