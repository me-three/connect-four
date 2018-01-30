package com.me_three.games;

import java.util.StringJoiner;

public class ConnectFour {

    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final int WIN_LENGTH = 4;

    private final int[] board = new int[ROWS * COLUMNS];

    ConnectFour() {
        reset();
    }

    void reset() {
        for (int row = 0; row < ROWS; ++row) {
            for (int column = 0; column < COLUMNS; ++column) {
                setCell(row, column, 0);
            }
        }
    }

    void playGame() {
        System.out.println("Connect Four\n");
        reset();
        System.out.println(toString());
        int turn = 0;
        while (turn < ROWS * COLUMNS) {
            final int player = turn % 2 + 1;
            final int column = getUserInput(player) - 1;
            final int row = dropPiece(column, player);
            System.out.println("");
            System.out.println(toString());
            if (isWinIfPlaced(row, column, player)) {
                System.out.println(getPlayerLabel(player) + " wins!");
                return;
            }
            ++turn;
        }
        System.out.println("The game ends in a draw!");
    }

    private int getUserInput(final int player) {
        while (true) {
            System.out.print(getPlayerLabel(player) + " - choose column (" + getColumnRangeLabel() + "): ");
            final String input = System.console().readLine();
            if ("q".equals(input)) {
                System.out.println("Bye.");
                System.exit(0);
            }
            try {
                final int column = Integer.valueOf(input);
                if (isInRange(column)) {
                    return column;
                }
                System.out.println("Please choose a column in the range " + getColumnRangeLabel() + ".");
            } catch (final NumberFormatException e) {
                System.out.println("I don't understand. Please try again.");
            }
        }
    }

    private boolean isInRange(final int column) {
        return column >= 1 && column <= COLUMNS && cell(0, column - 1) == 0;
    }

    String getColumnRangeLabel() {
        final StringJoiner sj = new StringJoiner(",");
        int count = 0;
        for (int column = 0; column < COLUMNS; ++column) {
            if (cell(0, column) == 0) {
                sj.add(String.valueOf(column + 1));
                ++count;
            }
        }
        return count == COLUMNS ? "1-" + COLUMNS : sj.toString();
    }

    static String getPlayerLabel(final int player) {
        switch (player) {
            case 1:
                return "Player 1 [RED]";
            case 2:
                return "Player 2 [GREEN]";
            default:
                return "?";
        }
    }

    int cell(final int row, final int column) {
        return board[row * COLUMNS + column];
    }

    void setCell(final int row, final int column, final int player) {
        board[row * COLUMNS + column] = player;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < ROWS; ++row) {
            for (int column = 0; column < COLUMNS; ++column) {
                sb.append("|").append(playerGlyph(cell(row, column)));
            }
            sb.append("|\n");
        }
        return sb.toString();
    }

    private static String playerGlyph(final int value) {
        switch (value) {
            case 1:
                return "R";
            case 2:
                return "G";
            default:
                return " ";
        }
    }

    int dropPiece(final int column, final int player) {
        final int row = getLastEmptyRowInColumn(column);
        if (row >= 0) {
            setCell(row, column, player);
        }
        return row;
    }

    int getLastEmptyRowInColumn(final int column) {
        for (int depth = 0; depth < ROWS; ++depth) {
            if (cell(depth, column) > 0) {
                return depth - 1;
            }
        }
        return ROWS - 1;
    }

    boolean isWinIfPlaced(final int row, final int column, final int player) {
        return getLeft(row, column, player) + 1 + getRight(row, column, player) >= WIN_LENGTH
                || getDown(row, column, player) + 1 >= WIN_LENGTH
                || getDownLeft(row, column, player) + 1 + getUpRight(row, column, player) >= WIN_LENGTH
                || getDownRight(row, column, player) + 1 + getUpLeft(row, column, player) >= WIN_LENGTH;
    }

    final int getLeft(final int row, final int column, final int player) {
        return getCount(row, column, player, 0, -1);
    }

    final int getRight(final int row, final int column, final int player) {
        return getCount(row, column, player, 0, 1);
    }

    final int getDown(final int row, final int column, final int player) {
        return getCount(row, column, player, 1, 0);
    }

    final int getDownLeft(final int row, final int column, final int player) {
        return getCount(row, column, player, 1, -1);
    }

    final int getDownRight(final int row, final int column, final int player) {
        return getCount(row, column, player, 1, 1);
    }

    final int getUpLeft(final int row, final int column, final int player) {
        return getCount(row, column, player, -1, -1);
    }

    final int getUpRight(final int row, final int column, final int player) {
        return getCount(row, column, player, -1, 1);
    }

    private int getCount(int row, int column, final int player, final int deltaRow, final int deltaColumn) {
        int count = 0;
        row += deltaRow;
        column += deltaColumn;
        while (row >= 0 && row < ROWS && column >= 0 && column < COLUMNS) {
            if (cell(row, column) != player) {
                break;
            }
            ++count;
            row += deltaRow;
            column += deltaColumn;
        }
        return count;
    }
}
