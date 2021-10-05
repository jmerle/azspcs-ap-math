package com.jaspervanmerle.apmath;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Grid class stores a hexagonal grid in which cells can be marked.
 * The grid looks like this:
 * <pre>
 *   ⬢ ⬡ ⬢
 *  ⬡ ⬡ ⬡ ⬡
 * ⬡ ⬡ ⬡ ⬡ ⬡
 *  ⬡ ⬡ ⬡ ⬡
 *   ⬢ ⬡ ⬢
 * </pre>
 * <p>
 * Every cell has (x, y) coordinates like this:
 * <pre>
 *        (0, -2) (1, -2) (2, -2)
 *    (-1, -1) (0, -1) (1, -1) (2, -1)
 * (-2, 0) (-1, 0) (0, 0) (1, 0) (2, 0)
 *     (-2, 1) (-1, 1) (0, 1) (1, 1)
 *        (-2, 2) (-1, 2) (0, 2)
 * </pre>
 */
public class Grid {
    private final int n;

    // The number of cells the grid spans to the left/right/top/bottom of the center cell
    private final int span;

    // The number of cells in the center row and the number of rows
    private final int size;

    private final boolean[] markedCells;
    private final boolean[] blockedCells;

    private int score = 0;

    public Grid(int n) {
        this.n = n;

        span = n - 1;
        size = span * 2 + 1;

        markedCells = new boolean[size * size];
        blockedCells = new boolean[size * size];
    }

    public Grid(Grid other) {
        n = other.n;

        span = other.span;
        size = other.size;

        markedCells = other.markedCells.clone();
        blockedCells = other.blockedCells.clone();
    }

    public boolean isOnGrid(int x, int y) {
        if (x < -span || x > span || y < -span || y > span) {
            return false;
        }

        int startX = y < 0 ? -(span + y) : -span;
        int rowWidth = size - Math.abs(y);

        return x >= startX && x < startX + rowWidth;
    }

    public boolean markCell(int x, int y) {
        if (!isOnGrid(x, y) || isMarked(x, y) || isBlocked(x, y)) {
            return false;
        }

        setMarked(x, y, true);
        score++;

        for (int i = -span; i <= span; i++) {
            for (int j = -span; j <= span; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                updateBlocked(x + i, y + j, x - i, y - j);
                updateBlocked(x + i, y + j, x + 2 * i, y + 2 * j);
            }
        }

        return true;
    }

    private void updateBlocked(int x2, int y2, int x3, int y3) {
        if (!isOnGrid(x2, y2) || !isOnGrid(x3, y3)) {
            return;
        }

        if (isMarked(x2, y2)) {
            setBlocked(x3, y3, true);
        } else if (isMarked(x3, y3)) {
            setBlocked(x2, y2, true);
        }
    }

    public int getN() {
        return n;
    }

    public int getSpan() {
        return span;
    }

    public int getSize() {
        return size;
    }

    public boolean isMarked(int x, int y) {
        return markedCells[getCellsIndex(x, y)];
    }

    private void setMarked(int x, int y, boolean state) {
        markedCells[getCellsIndex(x, y)] = state;
    }

    public boolean isBlocked(int x, int y) {
        return blockedCells[getCellsIndex(x, y)];
    }

    private void setBlocked(int x, int y, boolean state) {
        blockedCells[getCellsIndex(x, y)] = state;
    }

    public int getScore() {
        return score;
    }

    public String getSubmission() {
        StringBuilder sb = new StringBuilder();

        for (int y = -span; y <= span; y++) {
            int rowWidth = size - Math.abs(y);
            int startX = y < 0 ? -(span + y) : -span;

            List<Integer> markedIndices = new ArrayList<>(size);

            for (int x = startX, xMax = startX + rowWidth; x < xMax; x++) {
                if (isMarked(x, y)) {
                    markedIndices.add(x - startX);
                }
            }

            sb.append('{');
            sb.append(markedIndices.stream().map(String::valueOf).collect(Collectors.joining(", ")));
            sb.append('}');

            if (y != span) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int y = -span; y <= span; y++) {
            int startX = y < 0 ? -(span + y) : -span;
            int rowWidth = size - Math.abs(y);

            sb.append(" ".repeat(Math.abs(y)));

            for (int x = startX, xMax = startX + rowWidth; x < xMax; x++) {
                sb.append(isMarked(x, y) ? '⬢' : '⬡');

                if (x != xMax - 1) {
                    sb.append(' ');
                }
            }

            if (y != span) {
                sb.append('\n');
            }
        }

        return sb.toString();
    }

    private int getCellsIndex(int x, int y) {
        return (y + span) * size + (x + span);
    }
}
