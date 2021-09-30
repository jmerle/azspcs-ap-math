package com.jaspervanmerle.apmath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Runner {
    private final ResultManager resultManager = new ResultManager();

    private void run(int n) {
        System.out.println("Running for n = " + n);

        Grid grid = new Grid(n);
        int span = grid.getSpan();

        List<int[]> moves = new ArrayList<>(3 * span * (span + 1) + 1);
        for (int y = -span; y <= span; y++) {
            for (int x = -span; x <= span; x++) {
                if (grid.isOnGrid(x, y)) {
                    moves.add(new int[]{x, y});
                }
            }
        }

        long startTime = System.nanoTime();
        while (System.nanoTime() - startTime <= 60 * 1e9) {
            Grid newGrid = new Grid(n);
            for (int[] move : moves) {
                newGrid.markCell(move[0], move[1]);
            }

            resultManager.submitGrid(newGrid);
            Collections.shuffle(moves);
        }
    }

    public static void main(String[] args) {
        Runner runner = new Runner();

        if (args.length == 1) {
            runner.run(Integer.parseInt(args[0]));
        } else {
            for (int n : new int[]{
                    2, 6, 11, 18, 27,
                    38, 50, 65, 81, 98,
                    118, 139, 162, 187, 214,
                    242, 273, 305, 338, 374,
                    411, 450, 491, 534, 578
            }) {
                runner.run(n);
            }
        }
    }
}
