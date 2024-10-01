package pt.isel.pdm.project

import pt.isel.pdm.project.ui.common.NavigateBackTestTag
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.project.ui.common.screens.AboutScreen
import pt.isel.pdm.project.ui.common.screens.AuthorInfoTestTag
import pt.isel.pdm.project.ui.common.screens.CheckAuthorInfoTestTag
import pt.isel.pdm.project.ui.common.screens.CreatorInfo
import pt.isel.pdm.project.ui.common.screens.SendEmailTestTag

class AboutScreenTests {
    private val authors = listOf(
        CreatorInfo("João Mota", "a49508@alunos.isel.pt", R.drawable.author),
        CreatorInfo("Ricardo Rovisco", "a49487@alunos.isel.pt", R.drawable.author),
        CreatorInfo("Daniel Antunes", "a48337@alunos.isel.pt", R.drawable.author)
    )
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun about_click_on_back_navigation_calls_callback_test() {
        var backRequested = false
        composeTestRule.setContent {
            AboutScreen(
                onBackRequested = { backRequested = true },
                authors = emptyList()
            )
        }
        composeTestRule.onNodeWithTag(NavigateBackTestTag).performClick()
        assertTrue(backRequested)
    }

    @Test
    fun about_click_on_author_info_calls_onSendEmailRequested_test() {
        var sendEmailRequested = false
        composeTestRule.setContent {
            AboutScreen(
                onSendEmailRequested = { sendEmailRequested = true },
                authors = emptyList()
            )
        }
        composeTestRule.onNodeWithTag(SendEmailTestTag).performClick()
        assertTrue(sendEmailRequested)
    }

    @Test
    fun about_click_on_check_author_info_visible_test() {
        composeTestRule.setContent {
            AboutScreen(
                authors = authors
            )
        }
        composeTestRule.onAllNodesWithTag(CheckAuthorInfoTestTag).apply {
            fetchSemanticsNodes().forEachIndexed{ i , _ ->
                get(i).performClick()
            }
        }
        composeTestRule.onAllNodesWithTag(AuthorInfoTestTag).assertCountEquals(3)
    }

    @Test
    fun about_click_on_check_author_info_not_visible_test() {
        composeTestRule.setContent {
            AboutScreen(
                authors = authors
            )
        }
        composeTestRule.onAllNodes(hasTestTag(AuthorInfoTestTag)).assertCountEquals(0)
    }
}