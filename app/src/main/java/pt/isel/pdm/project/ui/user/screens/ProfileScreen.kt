package pt.isel.pdm.project.ui.user.screens

import pt.isel.pdm.project.ui.common.NavigationHandlers
import pt.isel.pdm.project.ui.common.TopBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.pdm.project.R
import pt.isel.pdm.project.domainmodel.User
import pt.isel.pdm.project.ui.common.Error
import pt.isel.pdm.project.ui.common.Idle
import pt.isel.pdm.project.ui.common.LoadState
import pt.isel.pdm.project.ui.common.Loaded
import pt.isel.pdm.project.ui.common.get
import pt.isel.pdm.project.ui.common.screens.BackgroundVisibilityTestTag
import pt.isel.pdm.project.ui.common.theme.GomokuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    errorState: LoadState<String> = Idle,
    profile: User?,
    onBackRequested: () -> Unit = { },
    onErrorDismiss: () -> Unit = { }
) {
    GomokuTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(SignInScreenTestTag),
            topBar = {
                TopBar(NavigationHandlers(onBackRequested = onBackRequested), "Profile")
            }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .paint(
                        painterResource(id = R.drawable.background_wood),
                        contentScale = ContentScale.FillBounds
                    ).testTag(BackgroundVisibilityTestTag)
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
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        if (profile != null) {
                            Image(
                                painter = painterResource(id = R.drawable.author),
                                contentDescription = null,
                                modifier = Modifier
                                    .sizeIn(maxWidth = 200.dp, maxHeight = 200.dp)
                            )
                            Text(
                                text = profile.username,
                                fontSize = 24.sp,
                                style = TextStyle(color = Color.White, fontSize = 13.sp),
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            )
                            Text(
                                text = profile.email,
                                fontSize = 24.sp,
                                style = TextStyle(color = Color.White, fontSize = 13.sp),
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun ProfileScreenPreview() {
    ProfileScreen(
        profile = User(1, "user", "user@gmail.com")
    )
}

@Composable
@Preview
fun ProfileScreenNullPreview() {
    ProfileScreen(
        profile = null
    )
}

