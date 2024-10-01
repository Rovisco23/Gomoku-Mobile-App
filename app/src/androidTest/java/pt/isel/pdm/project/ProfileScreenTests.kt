package pt.isel.pdm.project

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.project.ui.common.NavigateBackTestTag
import pt.isel.pdm.project.ui.common.screens.BackgroundVisibilityTestTag
import pt.isel.pdm.project.ui.user.screens.ProfileScreen
import pt.isel.pdm.project.ui.user.screens.SignInScreenTestTag

class ProfileScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun profile_on_back_request_test(){
        var backRequested = false
        composeTestRule.setContent {
            ProfileScreen(
                profile = null,
                onBackRequested = { backRequested = true }
            )
        }
        composeTestRule.onNodeWithTag(NavigateBackTestTag).performClick()
        composeTestRule.onNodeWithTag(SignInScreenTestTag).assertExists().assertIsDisplayed()
        Assert.assertTrue(backRequested)
    }
}