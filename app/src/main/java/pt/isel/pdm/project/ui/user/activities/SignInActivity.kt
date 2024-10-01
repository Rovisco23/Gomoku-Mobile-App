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
import pt.isel.pdm.project.ui.user.screens.SignInScreen
import pt.isel.pdm.project.ui.common.Loaded
import pt.isel.pdm.project.ui.user.UserViewModel

class SignInActivity : ComponentActivity() {
    private val app by lazy { application as GomokuApplication }
    private val viewModel by viewModels<UserViewModel>(
        factoryProducer = { UserViewModel.factory(app.usersService, app.userInfoRepository) }
    )

    companion object {
        fun navigateTo(origin: ComponentActivity) {
            val intent = Intent(origin, SignInActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(ContentValues.TAG, "SignInView.onCreate() called")
        lifecycleScope.launch {
            viewModel.signedIn.collect {
                if (it is Loaded) {
                    LoginActivity.navigateTo(this@SignInActivity)
                }
            }
        }
        setContent {
            val error by viewModel.error.collectAsState(initial = Idle)
            SignInScreen(
                errorState = error,
                onSignIn = { viewModel.signIn(it) },
                onBackRequested = { finish() },
                onErrorDismiss = { viewModel.errorToIdle() }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(ContentValues.TAG, "SignInActivity.onStart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.v(ContentValues.TAG, "SignInActivity.onStop() called")
        viewModel.resetSignedIn()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(ContentValues.TAG, "SignInActivity.onDestroy() called")
    }
}