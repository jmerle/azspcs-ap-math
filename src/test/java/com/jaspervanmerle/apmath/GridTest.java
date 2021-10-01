package com.jaspervanmerle.apmath;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GridTest {
    @ParameterizedTest
    @CsvSource({
            "0,0,true", "1,0,true", "0,1,true", "-1,1,true", "-1,0,true", "0,-1,true", "1,-1,true",
            "2,0,false", "1,1,false", "0,2,false", "-1,2,false", "-2,2,false", "-2,1,false", "-2,0,false",
            "-1,-1,false", "0,-2,false", "1,-2,false", "2,-2,false", "2,-1,false"
    })
    void isOnGridReturnsWhetherCellIsPartOfTheGrid(int x, int y, boolean expected) {
        Grid grid = new Grid(2);

        assertEquals(expected, grid.isOnGrid(x, y));
    }

    @Test
    void markCellReturnsFalseWhenNotOnGrid() {
        Grid grid = new Grid(2);

        assertFalse(grid.markCell(1, 1));
    }

    @Test
    void markCellReturnsFalseWhenAlreadyMarked() {
        Grid grid = new Grid(2);

        assertTrue(grid.markCell(0, 0));
        assertFalse(grid.markCell(0, 0));
    }

    @ParameterizedTest
    @MethodSource("provideArithmeticProgressionTestCases")
    void markCellReturnsFalseWhenMarkingCreatesArithmeticProgression(int n, int[] cell1, int[] cell2, int[] cell3) {
        Grid grid = new Grid(n);

        assertTrue(grid.markCell(cell1[0], cell1[1]));
        assertTrue(grid.markCell(cell2[0], cell2[1]));
        assertFalse(grid.markCell(cell3[0], cell3[1]));
    }

    private static Stream<Arguments> provideArithmeticProgressionTestCases() {
        return Stream.of(
                // Description picture 1, arithmetic progression in the top-left
                //     ⬡ ⬡ ⬡ ⬡ ⬡
                //    ⬡ ⬡ ⬡ ⬡ ⬡ ⬡
                //   ⬡ 3 ⬡ ⬡ ⬡ ⬡ ⬡
                //  ⬡ 2 ⬡ ⬡ ⬡ ⬡ ⬡ ⬡
                // ⬡ 1 ⬡ ⬡ ⬡ ⬡ ⬡ ⬡ ⬡
                //  ⬡ ⬡ ⬡ ⬡ ⬡ ⬡ ⬡ ⬡
                //   ⬡ ⬡ ⬡ ⬡ ⬡ ⬡ ⬡
                //    ⬡ ⬡ ⬡ ⬡ ⬡ ⬡
                //     ⬡ ⬡ ⬡ ⬡ ⬡
                Arguments.of(5, new int[]{-3, 0}, new int[]{-2, -1}, new int[]{-1, -2}),

                // Description picture 1, arithmetic progression in the bottom-left
                //     ⬡ ⬡ ⬡ ⬡ ⬡
                //    ⬡ ⬡ ⬡ ⬡ ⬡ ⬡
                //   ⬡ ⬡ ⬡ ⬡ ⬡ ⬡ ⬡
                //  ⬡ ⬡ ⬡ ⬡ ⬡ ⬡ ⬡ ⬡
                // ⬡ ⬡ ⬡ ⬡ 3 ⬡ ⬡ ⬡ ⬡
                //  ⬡ ⬡ 2 ⬡ ⬡ ⬡ ⬡ ⬡
                //   1 ⬡ ⬡ ⬡ ⬡ ⬡ ⬡
                //    ⬡ ⬡ ⬡ ⬡ ⬡ ⬡
                //     ⬡ ⬡ ⬡ ⬡ ⬡
                Arguments.of(5, new int[]{-4, 2}, new int[]{-2, 1}, new int[]{0, 0}),

                // Description picture 1, arithmetic progression on the right
                //     ⬡ ⬡ ⬡ ⬡ ⬡
                //    ⬡ ⬡ ⬡ ⬡ 1 ⬡
                //   ⬡ ⬡ ⬡ ⬡ ⬡ ⬡ ⬡
                //  ⬡ ⬡ ⬡ ⬡ ⬡ ⬡ ⬡ ⬡
                // ⬡ ⬡ ⬡ ⬡ ⬡ ⬡ 2 ⬡ ⬡
                //  ⬡ ⬡ ⬡ ⬡ ⬡ ⬡ ⬡ ⬡
                //   ⬡ ⬡ ⬡ ⬡ ⬡ ⬡ ⬡
                //    ⬡ ⬡ ⬡ ⬡ ⬡ 3
                //     ⬡ ⬡ ⬡ ⬡ ⬡
                Arguments.of(5, new int[]{3, -3}, new int[]{2, 0}, new int[]{1, 3}),

                // Test attempting to mark invalid cell with left/right neighbors
                //   ⬡ ⬡ ⬡
                //  ⬡ ⬡ ⬡ ⬡
                // ⬡ 1 3 2 ⬡
                //  ⬡ ⬡ ⬡ ⬡
                //   ⬡ ⬡ ⬡
                Arguments.of(3, new int[]{-1, 0}, new int[]{1, 0}, new int[]{0, 0}),

                // Test attempting to mark invalid cell with wide left/right neighbors
                //   ⬡ ⬡ ⬡
                //  ⬡ ⬡ ⬡ ⬡
                // 1 ⬡ 3 ⬡ 2
                //  ⬡ ⬡ ⬡ ⬡
                //   ⬡ ⬡ ⬡
                Arguments.of(3, new int[]{-2, 0}, new int[]{2, 0}, new int[]{0, 0}),

                // Test attempting to mark invalid cell with backslash-like neighbors
                //   ⬡ ⬡ ⬡
                //  ⬡ 1 ⬡ ⬡
                // ⬡ ⬡ 3 ⬡ ⬡
                //  ⬡ ⬡ 2 ⬡
                //   ⬡ ⬡ ⬡
                Arguments.of(3, new int[]{0, -1}, new int[]{0, 1}, new int[]{0, 0}),

                // Test attempting to mark invalid cell with wide backslash-like neighbors
                //   1 ⬡ ⬡
                //  ⬡ ⬡ ⬡ ⬡
                // ⬡ ⬡ 3 ⬡ ⬡
                //  ⬡ ⬡ ⬡ ⬡
                //   ⬡ ⬡ 2
                Arguments.of(3, new int[]{0, -2}, new int[]{0, 2}, new int[]{0, 0}),

                // Test attempting to mark invalid cell with top/bottom neighbors
                //   ⬡ 1 ⬡
                //  ⬡ ⬡ ⬡ ⬡
                // ⬡ ⬡ 3 ⬡ ⬡
                //  ⬡ ⬡ ⬡ ⬡
                //   ⬡ 2 ⬡
                Arguments.of(3, new int[]{1, -2}, new int[]{-1, 2}, new int[]{0, 0}),

                // Test attempting to mark invalid cell with forward slash-like neighbors
                //   ⬡ ⬡ ⬡
                //  ⬡ ⬡ 2 ⬡
                // ⬡ ⬡ 3 ⬡ ⬡
                //  ⬡ 1 ⬡ ⬡
                //   ⬡ ⬡ ⬡
                Arguments.of(3, new int[]{-1, 1}, new int[]{1, -1}, new int[]{0, 0}),

                // Test attempting to mark invalid cell with wide forward slash-like neighbors
                //   ⬡ ⬡ 2
                //  ⬡ ⬡ ⬡ ⬡
                // ⬡ ⬡ 3 ⬡ ⬡
                //  ⬡ ⬡ ⬡ ⬡
                //   1 ⬡ ⬡
                Arguments.of(3, new int[]{-2, 2}, new int[]{2, -2}, new int[]{0, 0})
        ).flatMap(arguments -> {
            int n = (int) arguments.get()[0];
            int[] cell1 = (int[]) arguments.get()[1];
            int[] cell2 = (int[]) arguments.get()[2];
            int[] cell3 = (int[]) arguments.get()[3];

            // Ensure behavior is the same when cells are marked in different order
            return Stream.of(
                    Arguments.of(n, cell1, cell2, cell3),
                    Arguments.of(n, cell1, cell3, cell2),
                    Arguments.of(n, cell2, cell1, cell3),
                    Arguments.of(n, cell2, cell3, cell1),
                    Arguments.of(n, cell3, cell1, cell2),
                    Arguments.of(n, cell3, cell2, cell1)
            );
        });
    }

    @Test
    void isMarkedReturnsWhetherCellHasBeenSuccessfullyMarked() {
        Grid grid = new Grid(3);

        grid.markCell(0, -2);
        grid.markCell(2, -2);
        grid.markCell(-2, 2);
        grid.markCell(0, 2);

        assertFalse(grid.isBlocked(-2, 0));
        assertTrue(grid.isBlocked(-1, 0));
        assertTrue(grid.isBlocked(0, 0));
        assertTrue(grid.isBlocked(1, 0));
        assertFalse(grid.isBlocked(2, 0));
    }

    void isBlockedReturnsWhetherCellCanBeMarked() {
        Grid grid = new Grid(3);

        grid.markCell(0, -2);
        grid.markCell(2, -2);
        grid.markCell(-2, 2);
        grid.markCell(0, 2);

        assertTrue(grid.isMarked(0, -2));
        assertTrue(grid.isMarked(2, -2));
        assertTrue(grid.isMarked(-2, 2));
        assertTrue(grid.isMarked(0, 2));
        assertFalse(grid.isMarked(0, 0));
    }

    @Test
    void getScoreReturnsNumberOfSuccessfullyMarkedCells() {
        Grid grid = new Grid(3);

        grid.markCell(0, -2);
        grid.markCell(2, -2);
        grid.markCell(-2, 2);
        grid.markCell(0, 2);
        grid.markCell(0, 0);

        assertEquals(4, grid.getScore());
    }

    @Test
    void getSubmissionReturnsTheGridInSubmissionFormat() {
        Grid grid = new Grid(3);

        grid.markCell(0, -2);
        grid.markCell(2, -2);
        grid.markCell(-2, 2);
        grid.markCell(0, 2);
        grid.markCell(0, 0);

        assertEquals("{0, 2}, {}, {}, {}, {0, 2}", grid.getSubmission());
    }

    @Test
    void toStringReturnsReadableGrid() {
        Grid grid = new Grid(3);

        grid.markCell(0, -2);
        grid.markCell(2, -2);
        grid.markCell(-2, 2);
        grid.markCell(0, 2);
        grid.markCell(0, 0);

        List<String> lines = new ArrayList<>();
        lines.add("  ⬢ ⬡ ⬢");
        lines.add(" ⬡ ⬡ ⬡ ⬡");
        lines.add("⬡ ⬡ ⬡ ⬡ ⬡");
        lines.add(" ⬡ ⬡ ⬡ ⬡");
        lines.add("  ⬢ ⬡ ⬢");

        assertEquals(String.join("\n", lines), grid.toString());
    }
}
