package pt.isel.pdm.project.ui.user.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pt.isel.pdm.project.GomokuApplication
import pt.isel.pdm.project.ui.user.screens.RankingScreen
import pt.isel.pdm.project.ui.common.Idle
import pt.isel.pdm.project.ui.common.Loaded
import pt.isel.pdm.project.ui.common.get
import pt.isel.pdm.project.ui.user.UserViewModel

class RankingActivity : ComponentActivity() {

    private val app by lazy { application as GomokuApplication }
    private val viewModel by viewModels<UserViewModel>(
        factoryProducer = { UserViewModel.factory(app.usersService, app.userInfoRepository) }
    )

    companion object {
        fun navigateTo(origin: ComponentActivity) {
            val intent = Intent(origin, RankingActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(ContentValues.TAG, "RankingActivity.onCreate() called")
        setContent {
            val error by viewModel.error.collectAsState(initial = Idle)
            val rankings = viewModel.rankings.collectAsState(initial = Idle).value
            if (rankings is Idle) viewModel.getRankings()
            val playerRankings = if (rankings is Loaded) {
                rankings.get()
            } else {
                emptyList()
            }
            RankingScreen(
                errorState = error,
                rankings = playerRankings,
                onBackRequested = { finish() },
                onErrorDismiss = { viewModel.errorToIdle() }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(ContentValues.TAG, "RankingActivity.onStart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.v(ContentValues.TAG, "RankingActivity.onStop() called")
        viewModel.rankingToIdle()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(ContentValues.TAG, "RankingActivity.onDestroy() called")
    }
}