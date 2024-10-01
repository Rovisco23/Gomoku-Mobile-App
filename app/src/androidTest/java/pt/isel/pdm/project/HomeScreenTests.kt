package pt.isel.pdm.project

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.project.domainmodel.UserInfo
import pt.isel.pdm.project.ui.common.screens.BackgroundVisibilityTestTag
import pt.isel.pdm.project.ui.common.screens.ButtonVisibilityTestTag
import pt.isel.pdm.project.ui.common.screens.MainScreen
import pt.isel.pdm.project.ui.common.screens.LogoVisibilityTestTag
import pt.isel.pdm.project.ui.common.Loaded
import kotlin.Result.Companion.success

class HomeScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun home_content_visible_while_logged() {
        composeTestRule.setContent {
            MainScreen(
                isLogged = Loaded(success(UserInfo(1, "test", "token")))
            )
        }
        composeTestRule.onNodeWithTag(BackgroundVisibilityTestTag).assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithTag(LogoVisibilityTestTag).assertExists().assertIsDisplayed()
        composeTestRule.onAllNodesWithTag(ButtonVisibilityTestTag).assertCountEquals(5)
    }

    @Test
    fun home_content_visible_while_not_logged() {
        composeTestRule.setContent {
            MainScreen()
        }
        composeTestRule.onNodeWithTag(BackgroundVisibilityTestTag).assertExists().assertIsDisplayed()
    }

    @Test
    fun home_click_on_common_buttons_while_logged_test(){
        var ranks = false
        var info = false
        composeTestRule.setContent {
            MainScreen(
                isLogged = Loaded(success(UserInfo(1, "test", "token"))),
                onRanksRequested = { ranks = true },
                onInfoRequested = { info = true }
            )
        }
        composeTestRule.onAllNodesWithTag(ButtonVisibilityTestTag).apply {
            fetchSemanticsNodes().forEachIndexed{ i , _ ->
                get(i).performClick()
            }
        }
        assertTrue(ranks)
        assertTrue(info)
    }

    @Test
    fun home_click_on_common_buttons_while_not_logged_test(){
        var ranks = false
        var info = false
        composeTestRule.setContent {
            MainScreen(
                isLogged = Loaded(success(null)),
                onRanksRequested = { ranks = true },
                onInfoRequested = { info = true }
            )
        }
        composeTestRule.onAllNodesWithTag(ButtonVisibilityTestTag).apply {
            fetchSemanticsNodes().forEachIndexed{ i , _ ->
                get(i).performClick()
            }
        }
        assertTrue(ranks)
        assertTrue(info)
    }

    @Test
    fun home_click_on_logged_only_buttons_test(){
        var play = false
        var profile = false
        var logout = false
        composeTestRule.setContent {
            MainScreen(
                isLogged = Loaded(success(UserInfo(1, "test", "token"))),
                onPlayRequested = { play = true },
                onProfileRequested = { profile = true },
                onLogoutRequested = { logout = true }
            )
        }
        composeTestRule.onAllNodesWithTag(ButtonVisibilityTestTag).apply {
            fetchSemanticsNodes().forEachIndexed{ i , _ ->
                get(i).performClick()
            }
        }
        assertTrue(play)
        assertTrue(profile)
        assertTrue(logout)
    }

    @Test
    fun home_click_on_not_logged_only_buttons_test(){
        var login = false
        var signin = false
        composeTestRule.setContent {
            MainScreen(
                isLogged = Loaded(success(null)),
                onLoginRequested = { login = true },
                onSignInRequested = { signin = true }
            )
        }
        composeTestRule.onAllNodesWithTag(ButtonVisibilityTestTag).apply {
            fetchSemanticsNodes().forEachIndexed{ i , _ ->
                get(i).performClick()
            }
        }
        assertTrue(login)
        assertTrue(signin)
    }
}