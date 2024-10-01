package pt.isel.pdm.project.ui.user.activities

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
import pt.isel.pdm.project.GomokuApplication
import pt.isel.pdm.project.domainmodel.UserInfo
import pt.isel.pdm.project.ui.user.screens.ProfileScreen
import pt.isel.pdm.project.ui.common.Idle
import pt.isel.pdm.project.ui.common.Loaded
import pt.isel.pdm.project.ui.common.get
import pt.isel.pdm.project.ui.user.UserViewModel

private const val USER_ID = "UserId"

class ProfileActivity : ComponentActivity() {
    private val app by lazy { application as GomokuApplication }
    private val viewModel by viewModels<UserViewModel>(
        factoryProducer = { UserViewModel.factory(app.usersService, app.userInfoRepository) }
    )

    companion object {
        fun navigateTo(ctx: Context, user: UserInfo?) {
            if (user == null) {
                return
            }
            ctx.startActivity(createIntent(ctx, user))
        }

        private fun createIntent(ctx: Context, user: UserInfo): Intent {
            val intent = Intent(ctx, ProfileActivity::class.java)
            intent.putExtra(USER_ID, user.id)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(ContentValues.TAG, "ProfileActivity.onCreate() called")
        val userId = intent.getIntExtra(USER_ID, -1)
        Log.v(ContentValues.TAG, "ProfileActivity.onCreate() called userId = $userId")
        viewModel.getProfile(userId)
        setContent {
            val error by viewModel.error.collectAsState(initial = Idle)
            val profile = viewModel.profile.collectAsState(initial = Idle).value
            val playerProfile = if (profile is Loaded) {
                profile.get()
            } else {
                null
            }
            ProfileScreen(
                errorState = error,
                profile = playerProfile,
                onBackRequested = { finish() },
                onErrorDismiss = { viewModel.errorToIdle() }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        Log.v(ContentValues.TAG, "ProfileActivity.onStart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.v(ContentValues.TAG, "ProfileActivity.onStop() called")
        viewModel.resetProfile()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(ContentValues.TAG, "ProfileActivity.onDestroy() called")
    }
}