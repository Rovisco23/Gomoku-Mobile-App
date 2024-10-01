package pt.isel.pdm.project.ui.game.screens

import pt.isel.pdm.project.ui.common.NavigationHandlers
import pt.isel.pdm.project.ui.common.TopBar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.pdm.project.domainmodel.OpeningRule
import pt.isel.pdm.project.domainmodel.OpeningRule.*
import pt.isel.pdm.project.R
import pt.isel.pdm.project.ui.common.Error
import pt.isel.pdm.project.ui.common.Idle
import pt.isel.pdm.project.ui.common.LoadState
import pt.isel.pdm.project.ui.common.Loaded
import pt.isel.pdm.project.ui.common.get
import pt.isel.pdm.project.ui.common.screens.ButtonVisibilityTestTag
import pt.isel.pdm.project.ui.common.screens.MakeButton
import pt.isel.pdm.project.ui.common.theme.GomokuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(
    errorState: LoadState<String> = Idle,
    onBackRequested: () -> Unit = { },
    onSearchRequest: (rule: String, size: Int) -> Unit = { _, _ -> },
    onErrorDismiss: () -> Unit = { }
) {
    var boardSize by remember { mutableIntStateOf(15) }
    var openingRule by remember { mutableStateOf(FREE_STYLE) }
    GomokuTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopBar(
                    NavigationHandlers(onBackRequested = onBackRequested),
                    "Choose the rules for your match"
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .paint(
                        painterResource(id = R.drawable.background_wood),
                        contentScale = ContentScale.FillBounds
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

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
                BoardSizeButtons(boardSize) { clickedSize -> boardSize = clickedSize }
                OpeningRuleButtons(openingRule) { clickedRule -> openingRule = clickedRule }
                MakeButton(text = "Search for a match", size = 200.dp) {
                    onSearchRequest(openingRule.toString(), boardSize)
                }
            }
        }
    }
}


@Composable
fun BoardSizeButtons(currSize: Int, onClick: (Int) -> Unit) {
    Text(
        text = "Board Size",
        color = White,
        style = TextStyle(fontSize = 23.sp, fontWeight = Bold)
    )
    Row {
        ClickableButton(currSize, 15, "15x15") { onClick(it as Int) }
        Spacer(modifier = Modifier.padding(2.dp))
        ClickableButton(currSize, 19, "19x19") { onClick(it as Int) }
    }
    Spacer(modifier = Modifier.padding(10.dp))
}

@Composable
fun OpeningRuleButtons(currRule: OpeningRule, onClick: (OpeningRule) -> Unit) {
    Text(
        text = "Opening Rule",
        color = White,
        style = TextStyle(fontSize = 23.sp, fontWeight = Bold)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ClickableButton(currRule, FREE_STYLE, "Free Style") { onClick(it as OpeningRule) }
        Spacer(modifier = Modifier.padding(2.dp))
        ClickableButton(currRule, PRO, "Pro") { onClick(it as OpeningRule) }
        Spacer(modifier = Modifier.padding(2.dp))
        ClickableButton(currRule, LONG_PRO, "Long Pro") { onClick(it as OpeningRule) }
    }
    Spacer(modifier = Modifier.padding(5.dp))
}

@Composable
fun ClickableButton(value: Any, expected: Any, text: String, onClick: (Any) -> Unit) {
    Button(
        onClick = { onClick(expected) },
        border = if (value != expected) null else BorderStroke(4.dp, Black),
        modifier = Modifier.testTag(ButtonVisibilityTestTag)
    ) {
        Text(text = text, fontSize = 20.sp)
    }
}


@Composable
@Preview
fun LobbyScreenPreview() {
    LobbyScreen()
}