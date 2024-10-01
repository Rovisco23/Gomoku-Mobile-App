package pt.isel.pdm.project.ui.common.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.isel.pdm.project.R
import pt.isel.pdm.project.domainmodel.UserInfo
import pt.isel.pdm.project.ui.common.Error
import pt.isel.pdm.project.ui.common.Idle
import pt.isel.pdm.project.ui.common.LoadState
import pt.isel.pdm.project.ui.common.Loaded
import pt.isel.pdm.project.ui.common.get
import pt.isel.pdm.project.ui.common.getOrNull

const val BackgroundVisibilityTestTag = "BackgroundVisibilityTestTag"
const val LogoVisibilityTestTag = "LogoVisibilityTestTag"
const val ButtonVisibilityTestTag = "ButtonVisibilityTestTag"

@Composable
fun MainScreen(
    isLogged: LoadState<UserInfo?> = Idle,
    errorState: LoadState<String> = Idle,
    onPlayRequested: () -> Unit = { },
    onRanksRequested: () -> Unit = { },
    onLoginRequested: () -> Unit = { },
    onSignInRequested: () -> Unit = { },
    onLogoutRequested: () -> Unit = { },
    onProfileRequested: () -> Unit = { },
    onInfoRequested: () -> Unit = { },
    onErrorDismiss: () -> Unit = { }
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.background_wood),
                contentScale = ContentScale.FillBounds
            )
            .testTag(BackgroundVisibilityTestTag),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
        if (isLogged is Loaded) {
            Image(
            painter = painterResource(id = R.drawable.gomoku_logo),
            contentDescription = "Gomoku Logo",
            modifier = Modifier
                .size(150.dp)
                .testTag(LogoVisibilityTestTag)
        )
            if (isLogged.getOrNull() != null) {
                AuthButtons(onPlayRequested, onProfileRequested)
                CommonButtons(onRanksRequested, onInfoRequested)
                MakeButton("Logout") { onLogoutRequested() }
            } else {
                NonAuthButtons(onLoginRequested, onSignInRequested)
                CommonButtons(onRanksRequested, onInfoRequested)
            }
        }
        else {
            CircularProgressIndicator()
        }
    }
}


@Composable
fun NonAuthButtons(onLogin: () -> Unit, onSignIn: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MakeButton("Login") { onLogin() }
        MakeButton("SignIn") { onSignIn() }
    }
}

@Composable
fun AuthButtons(onPlayRequested: () -> Unit, onProfileRequested: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MakeButton("Play") { onPlayRequested() }
        MakeButton("Profile") { onProfileRequested() }
    }
}

@Composable
fun CommonButtons(onRankRequested: () -> Unit, onInfoRequested: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MakeButton("Rankings") { onRankRequested() }
        MakeButton("About") { onInfoRequested() }
    }
}

@Composable
fun MakeButton(text: String, size: Dp = 120.dp, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .width(size)
            .testTag(ButtonVisibilityTestTag),
        onClick = onClick
    ) {
        Text(text = text)
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    MainScreen()
}