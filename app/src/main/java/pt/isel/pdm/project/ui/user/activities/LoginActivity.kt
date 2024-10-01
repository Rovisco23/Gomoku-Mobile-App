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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.pdm.project.GomokuApplication
import pt.isel.pdm.project.ui.common.Idle
import pt.isel.pdm.project.ui.user.screens.LoginScreen
import pt.isel.pdm.project.ui.common.Loaded
import pt.isel.pdm.project.ui.common.getOrNull
import pt.isel.pdm.project.ui.common.activities.MainActivity
import pt.isel.pdm.project.ui.user.UserViewModel

class LoginActivity : ComponentActivity() {

    private val app by lazy { application as GomokuApplication }
    private val viewModel by viewModels<UserViewModel>(
        factoryProducer = { UserViewModel.factory(app.usersService, app.userInfoRepository) }
    )

    companion object {
        fun navigateTo(origin: ComponentActivity) {
            val intent = Intent(origin, LoginActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(ContentValues.TAG, "LoginActivity.onCreate() called")
        lifecycleScope.launch {
            viewModel.userInfo.collect {
                if (it is Loaded) {
                    if (it.getOrNull() != null) {
                        MainActivity.navigateTo(this@LoginActivity)
                    }
                }
            }
        }
        setContent {
            val error by viewModel.error.collectAsState(initial = Idle)
            LoginScreen(
                errorState = error,
                onLogin = { viewModel.login(it) },
                onBackRequested = {
                    finish()
                    MainActivity.navigateTo(this@LoginActivity)
                },
                onErrorDismiss = {
                    viewModel.errorToIdle()
                }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(ContentValues.TAG, "LoginActivity.onStart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.v(ContentValues.TAG, "LoginActivity.onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(ContentValues.TAG, "LoginActivity.onDestroy() called")
    }
}