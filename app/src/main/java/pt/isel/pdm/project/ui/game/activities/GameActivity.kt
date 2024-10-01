package pt.isel.pdm.project.ui.game.activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.isel.pdm.project.ui.game.GameViewModel
import pt.isel.pdm.project.GomokuApplication
import pt.isel.pdm.project.domainmodel.GameReloadInputModel
import pt.isel.pdm.project.domainmodel.GameState
import pt.isel.pdm.project.domainmodel.GiveUpInputModel
import pt.isel.pdm.project.ui.common.Error
import pt.isel.pdm.project.ui.game.screens.GameScreen
import pt.isel.pdm.project.ui.common.Idle
import pt.isel.pdm.project.ui.common.Loaded
import pt.isel.pdm.project.ui.common.Loading
import pt.isel.pdm.project.ui.common.activities.MainActivity
import pt.isel.pdm.project.ui.common.get

private const val GAMEID_INFO = "GameId"
private const val USER_ID = "UserId"
private const val USER_TOKEN = "UserToken"

class GameActivity : ComponentActivity() {

    private val app by lazy { application as GomokuApplication }
    private val viewModel by viewModels<GameViewModel>(
        factoryProducer = { GameViewModel.factory(app.gamesService) }
    )

    companion object {
        fun navigateTo(ctx: Context, gameId: String, userId: Int, token: String) {
            ctx.startActivity(createIntent(ctx, gameId, userId, token))
        }

        private fun createIntent(ctx: Context, gameId: String, userId: Int, token: String): Intent {
            val intent = Intent(ctx, GameActivity::class.java)
            intent.putExtra(GAMEID_INFO, gameId)
            intent.putExtra(USER_ID, userId)
            intent.putExtra(USER_TOKEN, token)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(ContentValues.TAG, "GameActivity.onCreate() called")
        val userId = intent.getIntExtra(USER_ID, -1)
        val gameId = intent.getStringExtra(GAMEID_INFO)
        val token = intent.getStringExtra(USER_TOKEN)
        if (gameId == null || token == null) {
            setContent {
                Error(title = "Ups!", message = "Something went wrong", buttonText = "Ok") {
                    MainActivity.navigateTo(this)
                }
            }
        } else {
            lifecycleScope.launch {
                viewModel.game.collect {
                    while (true) {
                        delay(5000)
                        if (it is Loaded && it.get().state == GameState.FINISHED) {
                            break
                        }
                        if (it !is Loading) viewModel.reloadGame(
                            GameReloadInputModel(
                                gameId,
                                token
                            )
                        )
                    }
                }
            }
            setContent {
                val game by viewModel.game.collectAsState(initial = Idle)
                val error by viewModel.error.collectAsState(initial = Idle)

                GameScreen(
                    player = userId,
                    gameState = game,
                    errorState = error,
                    onPlay = { viewModel.updateGame(it, userId, gameId, token) },
                    onForfeit = {
                        if (game is Loaded && game.get().winner == 0
                            && game.get().state != GameState.FINISHED) {
                            viewModel.giveUp(GiveUpInputModel(gameId, token))
                        }
                        finish()
                        MainActivity.navigateTo(this)
                    },
                    onErrorDismiss = {
                        viewModel.errorToIdle()
                    }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(ContentValues.TAG, "GameActivity.onStart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.v(ContentValues.TAG, "GameActivity.onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(ContentValues.TAG, "GameActivity.onDestroy() called")
    }
}
