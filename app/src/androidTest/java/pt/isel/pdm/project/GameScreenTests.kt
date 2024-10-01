package pt.isel.pdm.project

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.project.domainmodel.GamePlayInputModel
import pt.isel.pdm.project.ui.common.NavigateBackTestTag
import pt.isel.pdm.project.ui.game.screens.GamePlayTestTag
import pt.isel.pdm.project.ui.game.screens.GameScreen
import pt.isel.pdm.project.ui.game.screens.GameScreenTestTag
import pt.isel.pdm.project.ui.game.screens.GameTurnTestTag

class GameScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun game_on_forfeit_request_test(){
        var forfeitRequested = false
        composeTestRule.setContent {
            GameScreen(
                player = 1,
                onForfeit = { forfeitRequested = true }
            )
        }
        composeTestRule.onNodeWithTag(NavigateBackTestTag).performClick()
        composeTestRule.onNodeWithTag(GameTurnTestTag).assertExists().assertIsDisplayed()
        assertTrue(forfeitRequested)
    }
}