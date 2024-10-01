package pt.isel.pdm.project.ui.user.screens

import pt.isel.pdm.project.ui.common.NavigationHandlers
import pt.isel.pdm.project.ui.common.TopBar
import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.project.R
import pt.isel.pdm.project.domainmodel.Ranking
import pt.isel.pdm.project.ui.common.Error
import pt.isel.pdm.project.ui.common.Idle
import pt.isel.pdm.project.ui.common.LoadState
import pt.isel.pdm.project.ui.common.Loaded
import pt.isel.pdm.project.ui.common.get
import pt.isel.pdm.project.ui.common.theme.GomokuTheme
import kotlin.random.Random

const val RankingScreenTestTag = "RankingScreenTestTag"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingScreen(
    errorState: LoadState<String> = Idle,
    rankings: List<Ranking>,
    onBackRequested: () -> Unit = { },
    onErrorDismiss: () -> Unit = { }
) {
    GomokuTheme {
        Log.v(ContentValues.TAG, "Inside RankingScreen")
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(RankingScreenTestTag),
            topBar = {
                TopBar(NavigationHandlers(onBackRequested = onBackRequested), "Global Ranking")
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
                ListRankings(rankings)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListRankings(rankings: List<Ranking>) {
    var searchText by remember { mutableStateOf("") }
    var searchRank by remember { mutableStateOf("") }
    Box {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .width(232.dp)
                        .border(2.dp, Black),
                    placeholder = { Text("Search players by name") }
                )
                TextField(
                    value = searchRank,
                    onValueChange = { searchRank = it },
                    modifier = Modifier
                        .width(160.dp)
                        .border(2.dp, Black),
                    placeholder = { Text("Search By Rank") }
                )
            }
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 32.dp)
            ) {
                val filteredRankings = rankings.filter { ranking ->
                    ranking.username.startsWith(searchText, ignoreCase = true) &&
                            (rankings.indexOf(ranking) + 1).toString()
                                .contains(searchRank, ignoreCase = true)
                }
                this.items(filteredRankings) {
                    val rank = rankings.indexOf(it) + 1
                    PlayerRankingView(it, rank)
                }
            }
        }
    }
}

@Composable
fun PlayerRankingView(
    player: Ranking,
    rank: Int
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = when (rank) {
                1 -> Color(0xFFB8860B)
                2 -> Color(0xFFC0C0C0)
                3 -> Color(0xFF8B4513)
                else -> MaterialTheme.colorScheme.primaryContainer
            }
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Rank: $rank - ${player.username} | Win Rate: ${player.winRate}%",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Played: ${player.gamesPlayed} | Wins: ${player.gamesWon} | Losses: " +
                        "${player.gamesLost} | Draws: ${player.gamesDrawn}",
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 8.dp),
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun RankingScreenPreview() {
    RankingScreen(rankings = players)
}

private val players = buildList {
    repeat(30) {
        val gamesPlayed = Random.nextInt(1, 101)
        val gamesWon = Random.nextInt(0, gamesPlayed + 1)
        val gamesDrawn = Random.nextInt(0, gamesPlayed - gamesWon + 1)
        add(
            Ranking(
                userId = it,
                username = "User $it",
                gamesPlayed = gamesPlayed,
                gamesWon = gamesWon,
                gamesDrawn = gamesDrawn,
                gamesLost = gamesPlayed - gamesWon - gamesDrawn,
                winRate = String.format(
                    "%.1f",
                    ((gamesWon.toDouble() / gamesPlayed.toDouble()) * 100.0)
                ).toDouble()
            )
        )
    }
}.sortedWith(compareByDescending(Ranking::points).thenComparing(Ranking::winRate))