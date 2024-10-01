package pt.isel.pdm.project

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.project.ui.common.NavigateBackTestTag
import pt.isel.pdm.project.ui.common.screens.ButtonVisibilityTestTag
import pt.isel.pdm.project.ui.game.screens.LobbyScreen

class LobbyScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun lobby_components_visibility_test() {
        composeTestRule.setContent { LobbyScreen() }
        composeTestRule.onAllNodesWithTag(ButtonVisibilityTestTag).assertCountEquals(6)
        composeTestRule.onNodeWithTag(NavigateBackTestTag).assertExists().assertIsDisplayed()
    }

    @Test
    fun lobby_on_back_request_test(){
        var backRequested = false
        composeTestRule.setContent {
            LobbyScreen(
                onBackRequested = { backRequested = true }
            )
        }
        composeTestRule.onNodeWithTag(NavigateBackTestTag).performClick()
        assertTrue(backRequested)
    }

    @Test
    fun lobby_on_search_request_test(){
        var search = false
        composeTestRule.setContent {
            LobbyScreen(
                onSearchRequest = { _, _ -> search = true }
            )
        }
        composeTestRule.onAllNodesWithTag(ButtonVisibilityTestTag).onLast().performClick()
        assertTrue(search)
    }
}