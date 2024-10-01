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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import pt.isel.pdm.project.R
import pt.isel.pdm.project.ui.common.Error
import pt.isel.pdm.project.ui.common.Idle
import pt.isel.pdm.project.ui.common.LoadState
import pt.isel.pdm.project.ui.common.Loaded
import pt.isel.pdm.project.ui.common.get
import pt.isel.pdm.project.ui.common.theme.GomokuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchMakingScreen(
    errorState: LoadState<String> = Idle,
    onBackRequested: () -> Unit = { },
    onErrorDismiss: () -> Unit = { }
) {
    GomokuTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(GameTurnTestTag),
            topBar = {
                TopBar(NavigationHandlers(onBackRequested = onBackRequested), "")
            },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .paint(
                        painterResource(id = R.drawable.background_wood),
                        contentScale = ContentScale.FillBounds
                    ),
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
                Text(text = "Searching For Opponents", fontSize = 30.sp, color = Color.White)
                CircularProgressIndicator()
            }
        }
    }
}

@Preview
@Composable
fun MatchMakingScreenPreview() {
    MatchMakingScreen()
}