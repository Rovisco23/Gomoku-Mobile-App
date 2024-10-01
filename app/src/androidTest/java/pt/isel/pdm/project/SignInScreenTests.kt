package pt.isel.pdm.project

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.project.ui.common.NavigateBackTestTag
import pt.isel.pdm.project.ui.user.screens.RankingScreen
import pt.isel.pdm.project.ui.user.screens.RankingScreenTestTag
import pt.isel.pdm.project.ui.user.screens.SignInScreen
import pt.isel.pdm.project.ui.user.screens.SignInScreenTestTag

class SignInScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun sing_in_on_back_request_test(){
        var backRequested = false
        composeTestRule.setContent {
            SignInScreen(
                onBackRequested = { backRequested = true }
            )
        }
        composeTestRule.onNodeWithTag(NavigateBackTestTag).performClick()
        composeTestRule.onNodeWithTag(SignInScreenTestTag).assertExists().assertIsDisplayed()
        Assert.assertTrue(backRequested)
    }
}