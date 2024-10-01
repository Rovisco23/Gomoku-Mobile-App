package pt.isel.pdm.project.domain

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import pt.isel.pdm.project.domainmodel.Board
import pt.isel.pdm.project.domainmodel.Game
import pt.isel.pdm.project.domainmodel.GameState
import pt.isel.pdm.project.domainmodel.validateUserInfoParts
import java.util.UUID

class DomainTest {
    private var fixedUuid = UUID.randomUUID()
    @Test
    fun create_game_with_valid_arguments_succeeds() {
        Game(
            UUID.randomUUID(),  Board.fromString(
            "-------------------------------------------------------------------------------------------------------" +
                    "--------------------------------------------------------------------------------------------------" +
                    "------------------------/15/B"), GameState.ON_GOING, 1, 1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun create_game_with_wrong_board_size() {
        Game(fixedUuid,  Board.fromString(
            "-------------------------------------------------------------------------------------------------------" +
                    "--------------------------------------------------------------------------------------------------" +
                    "------------------------/22/B"), GameState.ON_GOING, 1, 1)
    }

    @Test
    fun get_turn_from_game() {
        val game = Game(fixedUuid,  Board.fromString(
            "-------------------------------------------------------------------------------------------------------" +
                    "--------------------------------------------------------------------------------------------------" +
                    "------------------------/15/B"), GameState.ON_GOING, 1, 1)
        val turn = game.board.getCurrTurn()
        assertTrue(turn == 'B')
    }

    @Test
    fun get_size_from_game() {
        val game = Game(fixedUuid,  Board.fromString(
            "-------------------------------------------------------------------------------------------------------" +
                    "--------------------------------------------------------------------------------------------------" +
                    "------------------------/15/B"), GameState.ON_GOING, 1, 1)
        val size = game.board.getSize()
        assertTrue(size == 15)
    }

    @Test
    fun get_number_of_cells() {
        val game = Game(fixedUuid,  Board.fromString(
            "W------------------------W---------------------------------------B-------------------------------------" +
                    "------------------------B-------------------------------------------------------------------------" +
                    "------------------------/15/B"), GameState.ON_GOING, 1, 1)
        val ncells = game.board.getNumberOfCells()
        assertTrue(ncells == 4)
    }

    @Test
    fun validate_user_info_parts() {
        val valid = validateUserInfoParts(1, "username", "token")
        assertTrue(valid)
    }

    @Test
    fun validate_user_info_with_wrong_id() {
        val valid = validateUserInfoParts(-1, "username", "token")
        assertFalse(valid)
    }

    @Test
    fun validate_user_info_with_wrong_username() {
        val valid = validateUserInfoParts(1, "", "token")
        assertFalse(valid)
    }

    @Test
    fun validate_user_info_with_wrong_token() {
        val valid = validateUserInfoParts(1, "username", "")
        assertFalse(valid)
    }
}