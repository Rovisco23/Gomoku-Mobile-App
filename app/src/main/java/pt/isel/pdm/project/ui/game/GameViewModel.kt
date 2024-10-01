package pt.isel.pdm.project.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.project.domainmodel.Game
import pt.isel.pdm.project.domainmodel.GameCreateInputModel
import pt.isel.pdm.project.domainmodel.GamePlayInputModel
import pt.isel.pdm.project.domainmodel.GameReloadInputModel
import pt.isel.pdm.project.domainmodel.GiveUpInputModel
import pt.isel.pdm.project.domainmodel.LobbyInputModel
import pt.isel.pdm.project.ui.common.CreateGameException
import pt.isel.pdm.project.ui.common.GetLobbiesException
import pt.isel.pdm.project.ui.common.Idle
import pt.isel.pdm.project.ui.common.LoadState
import pt.isel.pdm.project.ui.common.UpdateGameException
import pt.isel.pdm.project.ui.common.complete
import pt.isel.pdm.project.ui.common.loading
import pt.isel.pdm.project.ui.common.resetToIdle
import kotlin.Result.Companion.success

class GameViewModel(private val service: GamesService) : ViewModel() {

    companion object {
        fun factory(service: GamesService) = viewModelFactory {
            initializer { GameViewModel(service) }
        }
    }

    private val errorFlow = MutableStateFlow<LoadState<String>>(Idle)

    private val createGameFlow: MutableStateFlow<LoadState<String>> = MutableStateFlow(Idle)

    private val gameFlow: MutableStateFlow<LoadState<Game>> = MutableStateFlow(Idle)

    private val gameIdFlow: MutableStateFlow<LoadState<String>> = MutableStateFlow(Idle)

    val error: Flow<LoadState<String>>
        get() = errorFlow.asStateFlow()

    val createGame: Flow<LoadState<String>>
        get() = createGameFlow.asStateFlow()

    val game: Flow<LoadState<Game>>
        get() = gameFlow.asStateFlow()

    val gameId: Flow<LoadState<String>>
        get() = gameIdFlow.asStateFlow()

    fun updateGame(input: GamePlayInputModel, userId: Int, gameId: String, token: String) {
        gameFlow.value = gameFlow.value.loading()
        viewModelScope.launch {
            try {
                val rsp = service.play(input, userId, gameId, token)
                gameFlow.value = gameFlow.value.complete(success(rsp))
            } catch (e: UpdateGameException) {
                gameFlow.value = gameFlow.value.resetToIdle()
                val msg = e.message ?: "Something went wrong"
                errorFlow.value = errorFlow.value.complete(success(msg))
            }
        }
    }

    fun startGame(input: GameCreateInputModel) {
        createGameFlow.value = createGameFlow.value.loading()
        viewModelScope.launch {
            try {
                val rsp = service.create(input)
                createGameFlow.value = createGameFlow.value.complete(success(rsp))
            } catch (e: CreateGameException) {
                createGameFlow.value = createGameFlow.value.resetToIdle()
                val msg = e.message ?: "Something went wrong"
                errorFlow.value = errorFlow.value.complete(success(msg))
            }
        }
    }

    fun checkLobby(input: LobbyInputModel) {
        gameIdFlow.value = gameIdFlow.value.loading()
        viewModelScope.launch {
            try {
                while (true) {
                    delay(3000)
                    val result = service.checkLobby(input)
                    if (result != "Lobby is not full") {
                        gameIdFlow.value = gameIdFlow.value.complete(success(result))
                        break
                    }
                }
            } catch (e: GetLobbiesException) {
                gameIdFlow.value = gameIdFlow.value.resetToIdle()
                val msg = e.message ?: "Something went wrong"
                errorFlow.value = errorFlow.value.complete(success(msg))
            }
        }
    }

    fun reloadGame(input: GameReloadInputModel) {
        gameFlow.value = gameFlow.value.loading()
        viewModelScope.launch {
            try {
                val rsp = service.reload(input)
                gameFlow.value = gameFlow.value.complete(success(rsp))
            } catch (e: UpdateGameException) {
                gameFlow.value = gameFlow.value.resetToIdle()
                val msg = e.message ?: "Something went wrong"
                errorFlow.value = errorFlow.value.complete(success(msg))
            }
        }
    }

    fun giveUp(input: GiveUpInputModel) {
        gameFlow.value = gameFlow.value.loading()
        viewModelScope.launch {
            try {
                val rsp = service.forfeit(input)
                gameFlow.value = gameFlow.value.complete(success(rsp))
            } catch (e: UpdateGameException) {
                gameFlow.value = gameFlow.value.resetToIdle()
                val msg = e.message ?: "Something went wrong"
                errorFlow.value = errorFlow.value.complete(success(msg))
            }
        }
    }

    fun resetStartGame() {
        createGameFlow.value = createGameFlow.value.resetToIdle()
    }

    fun errorToIdle() {
        errorFlow.value = errorFlow.value.resetToIdle()
    }
}

