package pt.isel.pdm.project

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.pdm.project.infrastructure.UserInfoRepository
import pt.isel.pdm.project.ui.game.GameService
import pt.isel.pdm.project.ui.game.GamesService
import pt.isel.pdm.project.ui.user.UserService
import pt.isel.pdm.project.ui.user.UsersService
import androidx.datastore.preferences.core.Preferences
import pt.isel.pdm.project.infrastructure.UserInfoDataStore

interface DependenciesContainer {
    val userInfoRepository: UserInfoRepository
}

class GomokuApplication : Application(), DependenciesContainer {
    private val ngrokURL = "https://a461-109-49-138-120.ngrok-free.app"
    private val httpClient: OkHttpClient = OkHttpClient.Builder().build()
    private val gson: Gson = Gson()
    val gamesService: GamesService = GameService(httpClient, gson, ngrokURL)
    val usersService: UsersService = UserService(httpClient, gson, ngrokURL)
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")
    override val userInfoRepository: UserInfoRepository get() = UserInfoDataStore(dataStore)
}