package pt.isel.pdm.project.ui.common.screens

import pt.isel.pdm.project.ui.common.NavigationHandlers
import pt.isel.pdm.project.ui.common.TopBar
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.pdm.project.R
import pt.isel.pdm.project.ui.common.theme.GomokuTheme

const val AboutScreenTestTag = "AboutScreenTestTag"
const val CheckAuthorInfoTestTag = "CheckAuthorInfoTestTag"
const val AuthorInfoTestTag = "AuthorInfoTestTag"
const val SendEmailTestTag = "SendEmailTestTag"

data class CreatorInfo(val name: String, val email: String, @DrawableRes val imageId: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBackRequested: () -> Unit = { },
    onSendEmailRequested: () -> Unit = { },
    authors: List<CreatorInfo>
) {
    GomokuTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(AboutScreenTestTag),
            topBar = {
                TopBar(NavigationHandlers(onBackRequested = onBackRequested), "About Us")
            }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .paint(
                        painterResource(id = R.drawable.background_wood),
                        contentScale = ContentScale.FillBounds
                    ),
            ) {
                Authors(socials = authors, onSendEmailRequested = onSendEmailRequested)
            }
        }
    }
}

/**
 * Composable used to display information about the author of the application
 */
@Composable
private fun Authors(
    socials: List<CreatorInfo>,
    onSendEmailRequested: () -> Unit = { }
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        socials.forEach { creatorInfo ->
            AuthorInfo(id = creatorInfo.imageId, name = creatorInfo.name, email = creatorInfo.email)
            Spacer(modifier = Modifier.height(20.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        OutlinedButton(
            modifier = Modifier.testTag(SendEmailTestTag),
            onClick = onSendEmailRequested
        ) {
            Text(text = "Contact Us", fontSize = 22.sp)
        }
    }
}

@Composable
private fun AuthorInfo(@DrawableRes id: Int, name: String, email: String) {
    var extended by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = id),
            contentDescription = null,
            modifier = Modifier
                .testTag(CheckAuthorInfoTestTag)
                .sizeIn(maxWidth = 120.dp)
                .clickable { extended = !extended }
        )
        AnimatedVisibility(visible = extended) {
            Text(
                modifier = Modifier.testTag(AuthorInfoTestTag),
                text = "$name\n$email",
                fontSize = 22.sp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InfoScreenPreview() {
    val authorsPreview = listOf(
        CreatorInfo("João Mota", "a49508@alunos.isel.pt", R.drawable.author),
        CreatorInfo("Ricardo Rovisco", "a49487@alunos.isel.pt", R.drawable.author),
        CreatorInfo("Daniel Antunes", "a48337@alunos.isel.pt", R.drawable.author)
    )
    AboutScreen(authors = authorsPreview)
}