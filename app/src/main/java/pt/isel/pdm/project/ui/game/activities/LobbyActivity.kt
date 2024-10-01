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
import kotlinx.coroutines.launch
import pt.isel.pdm.project.GomokuApplication
import pt.isel.pdm.project.domainmodel.GameCreateInputModel
import pt.isel.pdm.project.domainmodel.UserInfo
import pt.isel.pdm.project.ui.common.Error
import pt.isel.pdm.project.ui.common.Idle
import pt.isel.pdm.project.ui.game.screens.LobbyScreen
import pt.isel.pdm.project.ui.common.Loaded
import pt.isel.pdm.project.ui.common.get
import pt.isel.pdm.project.ui.common.activities.MainActivity
import pt.isel.pdm.project.ui.game.GameViewModel

private const val USER_ID = "UserId"
private const val USER_TOKEN = "Token"

class LobbyActivity : ComponentActivity() {

    private val app by lazy { application as GomokuApplication }
    private val viewModel by viewModels<GameViewModel>(
        factoryProducer = { GameViewModel.factory(app.gamesService) }
    )

    companion object {
        fun navigateTo(ctx: Context, user: UserInfo?) {
            if (user == null) {
                return
            }
            ctx.startActivity(createIntent(ctx, user))
        }

        private fun createIntent(ctx: Context, user: UserInfo): Intent {
            val intent = Intent(ctx, LobbyActivity::class.java)
            intent.putExtra(USER_ID, user.id)
            intent.putExtra(USER_TOKEN, user.token)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(ContentValues.TAG, "LobbyActivity.onCreate() called")
        val userId = intent.getIntExtra(USER_ID, -1)
        val token = intent.getStringExtra(USER_TOKEN)
        if (token != null) {
            lifecycleScope.launch {
                viewModel.createGame.collect {
                    if (it is Loaded) {
                        val rsp = it.get()
                        if (rsp == "Lobby Created") {
                            MatchMakingActivity.navigateTo(this@LobbyActivity, userId, token)
                        } else {
                            GameActivity.navigateTo(
                                this@LobbyActivity,
                                rsp,
                                userId,
                                token
                            )
                        }
                    }
                }
            }
            setContent {
                val error by viewModel.error.collectAsState(initial = Idle)
                LobbyScreen(
                    errorState = error,
                    onBackRequested = { finish() },
                    onSearchRequest = { rule, size ->
                        viewModel.startGame(
                            GameCreateInputModel(
                                userId,
                                rule,
                                size,
                                token
                            )
                        )
                    },
                    onErrorDismiss = { viewModel.errorToIdle() }
                )
            }
        } else {
            setContent {
                Error(title = "Ups!", message = "Something went wrong", buttonText = "Ok") {
                    MainActivity.navigateTo(this)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(ContentValues.TAG, "LobbyActivity.onStart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.v(ContentValues.TAG, "LobbyActivity.onStop() called")
        viewModel.resetStartGame()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(ContentValues.TAG, "LobbyActivity.onDestroy() called")
    }
}