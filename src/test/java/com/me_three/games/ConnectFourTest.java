package com.me_three.games;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConnectFourTest {

    private ConnectFour game = new ConnectFour();

    @Before
    public void before() {
        game.reset();
    }

    @Test
    public void checkPlayerLabel() {
        assertThat(ConnectFour.getPlayerLabel(1), is("Player 1 [RED]"));
        assertThat(ConnectFour.getPlayerLabel(2), is("Player 2 [GREEN]"));
        assertThat(ConnectFour.getPlayerLabel(0), is("?"));
    }

    @Test
    public void checkEmptyBoard() {
        final String actual = game.toString();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; ++i) {
            sb.append("| | | | | | | |\n");
        }
        final String expected = sb.toString();
        assertThat(actual, is(expected));
    }

    @Test
    public void checkGetColumnRangeLabel() {
        setupBoard(
                "| | | | | | | |",
                "| | | | | | | |",
                "| | | | | | | |",
                "| | | | | | | |",
                "| | | | | | | |",
                "| | | | | | | |"
        );
        assertThat(game.getColumnRangeLabel(), is("1-7"));
        setupBoard(
                "| |G| | |R|R| |",
                "| | | | | | | |",
                "| | | | | | | |",
                "| | | | | | | |",
                "| | | | | | | |",
                "| | | | | | | |"
        );
        assertThat(game.getColumnRangeLabel(), is("1,3,4,7"));
    }

    @Test
    public void checkGetLastEmptyRowInColumn() {
        setupBoard(
                "| | | | |G| | |",
                "| | | |R| | | |",
                "| | |G| | | | |",
                "| | | | | | | |",
                "| | | | | | | |",
                "| |R| | | | | |"
        );
        assertThat(game.getLastEmptyRowInColumn(0), is(5));
        assertThat(game.getLastEmptyRowInColumn(1), is(4));
        assertThat(game.getLastEmptyRowInColumn(2), is(1));
        assertThat(game.getLastEmptyRowInColumn(3), is(0));
        assertThat(game.getLastEmptyRowInColumn(4), is(-1));
    }

    @Test
    public void checkDropPiece() {
        game.dropPiece(0, 1);
        assertThat(game.cell(5, 0), is(1));
        game.dropPiece(0, 1);
        assertThat(game.cell(4, 0), is(1));
        for (int i = 0; i < 4; ++i) {
            int row = game.dropPiece(0, 1);
            assertThat(row, not(is(-1)));
        }
        assertThat(game.cell(0, 0), is(1));
        int row = game.dropPiece(0, 1);
        assertThat(row, is(-1));
        game.dropPiece(4, 2);
        assertThat(game.cell(5, 4), is(2));
    }

    @Test
    public void checkCounts() {
        setupBoard(
                "| | | | | |R| |",
                "| | |R| |R| | |",
                "|R|R|R| | | | |",
                "| |G|R| |R| | |",
                "| |R|G| |G|R| |",
                "| | | | | | |G|"
        );
        assertThat(game.getLeft(2, 3, 1), is(3));
        assertThat(game.getLeft(3, 3, 1), is(1));
        assertThat(game.getLeft(4, 3, 1), is(0));
        assertThat(game.getLeft(4, 3, 1), is(0));
        assertThat(game.getRight(2, 3, 1), is(0));
        assertThat(game.getRight(3, 3, 1), is(1));
        assertThat(game.getRight(4, 3, 1), is(0));
        assertThat(game.getDownLeft(1, 3, 1), is(1));
        assertThat(game.getDownLeft(2, 3, 1), is(2));
        assertThat(game.getDownRight(1, 3, 1), is(0));
        assertThat(game.getDownRight(2, 3, 1), is(2));
        assertThat(game.getUpLeft(3, 3, 1), is(1));
        assertThat(game.getUpLeft(4, 3, 1), is(2));
        assertThat(game.getUpRight(2, 3, 1), is(2));
        assertThat(game.getUpRight(5, 3, 1), is(0));
        assertThat(game.getDown(0, 2, 1), is(3));
        assertThat(game.getDown(1, 1, 1), is(1));
    }

    @Test
    public void checkWinIfPlaced() {
        setupBoard(
                "| | | | | | | |",
                "| | | | | | | |",
                "| | | |G| | | |",
                "| | |G|G| |G| |",
                "| | |G| | | | |",
                "| |G|G|G| | | |"
        );
        assertThat(game.isWinIfPlaced(5, 0, 2), is(true));
        assertThat(game.isWinIfPlaced(5, 4, 2), is(true));
        assertThat(game.isWinIfPlaced(5, 6, 2), is(false));
        assertThat(game.isWinIfPlaced(3, 1, 2), is(false));
        assertThat(game.isWinIfPlaced(3, 4, 2), is(true));
        assertThat(game.isWinIfPlaced(2, 2, 2), is(true));
        assertThat(game.isWinIfPlaced(1, 3, 2), is(false));
        assertThat(game.isWinIfPlaced(4, 3, 2), is(false)); // !
    }

    private void setupBoard(final String... rows) {
        game.reset();
        for (int r = 0; r < rows.length; ++r) {
            final String[] columns = rows[r].substring(1, 14).split("\\|");
            for (int c = 0; c < columns.length; ++c) {
                game.setCell(r, c, getPlayerIndex(columns[c]));
            }
        }
    }

    private static int getPlayerIndex(final String glyph) {
        switch (glyph) {
            case "R":
                return 1;
            case "G":
                return 2;
            default:
                return 0;
        }
    }
}
