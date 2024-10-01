package pt.isel.pdm.project

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.project.ui.common.NavigateBackTestTag
import pt.isel.pdm.project.ui.game.screens.MatchMakingScreen

class MatchMakingScreenTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun matchmaking_on_back_request_test(){
        var backRequested = false
        composeTestRule.setContent {
            MatchMakingScreen(
                onBackRequested = { backRequested = true }
            )
        }
        composeTestRule.onNodeWithTag(NavigateBackTestTag).performClick()
        Assert.assertTrue(backRequested)
    }
}