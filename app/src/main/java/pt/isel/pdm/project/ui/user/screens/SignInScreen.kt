package pt.isel.pdm.project.ui.user.screens

import pt.isel.pdm.project.ui.common.NavigationHandlers
import pt.isel.pdm.project.ui.common.TopBar
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.project.R
import pt.isel.pdm.project.domainmodel.UserSignInCredentialInputModel
import pt.isel.pdm.project.ui.common.Error
import pt.isel.pdm.project.ui.common.Idle
import pt.isel.pdm.project.ui.common.LoadState
import pt.isel.pdm.project.ui.common.Loaded
import pt.isel.pdm.project.ui.common.get
import pt.isel.pdm.project.ui.common.screens.ButtonVisibilityTestTag
import pt.isel.pdm.project.ui.common.theme.GomokuTheme

const val SignInScreenTestTag = "SignInScreenTestTag"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    errorState: LoadState<String> = Idle,
    onSignIn: (input: UserSignInCredentialInputModel) -> Unit = { },
    onBackRequested: () -> Unit = { },
    onErrorDismiss: () -> Unit = { }
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    GomokuTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(SignInScreenTestTag),
            topBar = {
                TopBar(NavigationHandlers(onBackRequested = onBackRequested), "SignIn")
            }
        ) { innerPadding ->
            if (errorState is Loaded) {
                val errorMsg = errorState.get()
                Error(
                    title = "Ups!",
                    message = errorMsg,
                    buttonText = "Ok",
                    onDismiss = onErrorDismiss
                )
            }
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .paint(
                        painterResource(id = R.drawable.background_wood),
                        contentScale = ContentScale.FillBounds
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    singleLine = true,
                    label = { Text("Username") },
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier
                        .width(232.dp)
                        .border(2.dp, Color.Black)
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    singleLine = true,
                    label = { Text("Email") },
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .width(232.dp)
                        .border(2.dp, Color.Black)
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    singleLine = true,
                    label = { Text("Password") },
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .width(232.dp)
                        .border(2.dp, Color.Black),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Button(
                    modifier = Modifier
                        .width(120.dp)
                        .testTag(ButtonVisibilityTestTag),
                    onClick = {
                        onSignIn(
                            UserSignInCredentialInputModel(
                                username = username,
                                email = email,
                                password = password
                            )
                        )
                    }
                ) {
                    Text(text = "SignIn")
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SignInScreenPreview() {
    SignInScreen()
}
