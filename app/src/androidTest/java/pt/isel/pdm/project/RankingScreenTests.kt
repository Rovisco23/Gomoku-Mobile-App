package pt.isel.pdm.project

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.project.domainmodel.Ranking
import pt.isel.pdm.project.ui.common.NavigateBackTestTag
import pt.isel.pdm.project.ui.common.screens.BackgroundVisibilityTestTag
import pt.isel.pdm.project.ui.user.screens.RankingScreen
import pt.isel.pdm.project.ui.user.screens.RankingScreenTestTag

class RankingScreenTests {
    private val rankings = listOf(
        Ranking(1,"User", 0, 0, 0, 0, 0.0),
        Ranking(1,"Usuario", 1, 0, 1, 0, 0.0),
        Ranking(1,"Utilizador", 1, 0, 1, 0, 0.0),
    )
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun ranking_on_back_request_test(){
        var backRequested = false
        composeTestRule.setContent {
            RankingScreen(
                rankings = rankings,
                onBackRequested = { backRequested = true }
            )
        }
        composeTestRule.onNodeWithTag(NavigateBackTestTag).performClick()
        composeTestRule.onNodeWithTag(RankingScreenTestTag).assertExists().assertIsDisplayed()
        Assert.assertTrue(backRequested)
    }
}