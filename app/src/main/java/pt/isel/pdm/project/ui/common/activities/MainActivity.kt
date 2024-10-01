package pt.isel.pdm.project.ui.common.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pt.isel.pdm.project.GomokuApplication
import pt.isel.pdm.project.ui.game.activities.LobbyActivity
import pt.isel.pdm.project.ui.user.activities.LoginActivity
import pt.isel.pdm.project.ui.user.activities.ProfileActivity
import pt.isel.pdm.project.ui.user.activities.RankingActivity
import pt.isel.pdm.project.ui.user.activities.SignInActivity
import pt.isel.pdm.project.ui.common.screens.MainScreen
import pt.isel.pdm.project.ui.common.Idle
import pt.isel.pdm.project.ui.common.get
import pt.isel.pdm.project.ui.user.UserViewModel

class MainActivity : ComponentActivity() {

    private val app by lazy { application as GomokuApplication }
    private val viewModel by viewModels<UserViewModel>(
        factoryProducer = { UserViewModel.factory(app.usersService, app.userInfoRepository) }
    )
    companion object {
        fun navigateTo(origin: ComponentActivity) {
            val intent = Intent(origin, MainActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "MainActivity.onCreate() called")
        viewModel.getUserInfo()
        setContent {
            val state by viewModel.userInfo.collectAsState(initial = Idle)
            val error by viewModel.error.collectAsState(initial = Idle)
            MainScreen(
                isLogged = state,
                errorState = error,
                onPlayRequested = { LobbyActivity.navigateTo(this, state.get()) },
                onRanksRequested = { RankingActivity.navigateTo(this) },
                onInfoRequested = { AboutActivity.navigateTo(this) },
                onLoginRequested = { LoginActivity.navigateTo(this) },
                onSignInRequested = { SignInActivity.navigateTo(this) },
                onProfileRequested = { ProfileActivity.navigateTo(this, state.get()) },
                onLogoutRequested = { viewModel.logout(state.get()?.token ?: "") },
                onErrorDismiss = { viewModel.errorToIdle() }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "MainActivity.onStart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.v(TAG, "MainActivity.onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "MainActivity.onDestroy() called")
    }
}

