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
import pt.isel.pdm.project.domainmodel.LobbyInputModel
import pt.isel.pdm.project.ui.common.Error
import pt.isel.pdm.project.ui.common.Idle
import pt.isel.pdm.project.ui.game.screens.MatchMakingScreen
import pt.isel.pdm.project.ui.common.Loaded
import pt.isel.pdm.project.ui.common.get
import pt.isel.pdm.project.ui.common.activities.MainActivity
import pt.isel.pdm.project.ui.game.GameViewModel

private const val USER_ID = "UserId"
private const val USER_TOKEN = "Token"

class MatchMakingActivity : ComponentActivity() {
    private val app by lazy { application as GomokuApplication }
    private val viewModel by viewModels<GameViewModel>(
        factoryProducer = { GameViewModel.factory(app.gamesService) }
    )

    companion object {
        fun navigateTo(ctx: Context, user: Int, token: String) {
            ctx.startActivity(createIntent(ctx, user, token))
        }

        private fun createIntent(ctx: Context, user: Int, token: String): Intent {
            val intent = Intent(ctx, MatchMakingActivity::class.java)
            intent.putExtra(USER_ID, user)
            intent.putExtra(USER_TOKEN, token)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(ContentValues.TAG, "MatchMakingActivity.onStart() called")
        val userId = intent.getIntExtra(USER_ID, -1)
        val token = intent.getStringExtra(USER_TOKEN)
        if (token != null) {
            viewModel.checkLobby(LobbyInputModel(userId, token))
            lifecycleScope.launch {
                viewModel.gameId.collect {
                    if (it is Loaded) {
                        val rsp = it.get()
                        GameActivity.navigateTo(
                            this@MatchMakingActivity,
                            rsp,
                            userId,
                            token
                        )
                    }
                }
            }
            setContent {
                val error by viewModel.error.collectAsState(initial = Idle)
                MatchMakingScreen(
                    errorState = error,
                    onBackRequested = { finish() },
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
        Log.v(ContentValues.TAG, "MatchMakingActivity.onStart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.v(ContentValues.TAG, "MatchMakingActivity.onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(ContentValues.TAG, "MatchMakingActivity.onDestroy() called")
    }
}