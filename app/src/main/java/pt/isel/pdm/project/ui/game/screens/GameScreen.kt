package pt.isel.pdm.project.ui.game.screens

import pt.isel.pdm.project.ui.common.NavigationHandlers
import pt.isel.pdm.project.ui.common.TopBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import pt.isel.pdm.project.domainmodel.Game
import pt.isel.pdm.project.ui.game.view.GameView
import pt.isel.pdm.project.domainmodel.GamePlayInputModel
import pt.isel.pdm.project.ui.common.Idle
import pt.isel.pdm.project.ui.common.LoadState
import pt.isel.pdm.project.ui.common.Loaded
import pt.isel.pdm.project.ui.common.get
import pt.isel.pdm.project.ui.common.theme.GomokuTheme
import pt.isel.pdm.project.ui.common.Error

const val GameScreenTestTag = "GameScreenTestTag"
const val GameTurnTestTag = "GameTurnTestTag"
const val GamePlayTestTag = "GamePlayTestTag"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    player: Int,
    gameState: LoadState<Game> = Idle,
    errorState: LoadState<String> = Idle,
    onPlay: (GamePlayInputModel) -> Unit = {},
    onForfeit: () -> Unit = {},
    onErrorDismiss: () -> Unit = {}
) {
    var game by remember { mutableStateOf<Game?>(null) }
    if (gameState is Loaded) game = gameState.get()
    GomokuTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(GameTurnTestTag),
            topBar = {
                TopBar(NavigationHandlers(onBackRequested = onForfeit), "Gomoku Royale")
            },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                if (errorState is Loaded) {
                    val errorMsg = errorState.get()
                    Error(
                        title = "Ups!",
                        message = errorMsg,
                        buttonText = "Ok",
                        onDismiss = onErrorDismiss
                    )
                }
                if (game == null) CircularProgressIndicator()
                game?.let {
                    GameView(player, it) { input -> onPlay(input) }
                }
            }
        }
    }
}
