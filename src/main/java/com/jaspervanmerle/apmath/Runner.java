package com.jaspervanmerle.apmath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Runner {
    private class Worker implements Runnable {
        private final int n;
        private final List<int[]> moves;
        private final ResultManager resultManager;

        public Worker(int n, ResultManager resultManager) {
            this.n = n;
            this.resultManager = resultManager;

            Grid grid = new Grid(n);
            int span = grid.getSpan();

            moves = new ArrayList<>();
            for (int y = -span; y <= span; y++) {
                for (int x = -span; x <= span; x++) {
                    if (grid.isOnGrid(x, y)) {
                        moves.add(new int[]{x, y});
                    }
                }
            }
        }

        @Override
        public void run() {
            while (true) {
                Collections.shuffle(moves, ThreadLocalRandom.current());

                Grid newGrid = new Grid(n);
                for (int[] move : moves) {
                    newGrid.markCell(move[0], move[1]);
                }

                resultManager.submitGrid(newGrid);
            }
        }
    }

    private void run(int n) {
        System.out.println("Running for n = " + n);

        ResultManager resultManager = new ResultManager();

        int cpuCount = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < cpuCount / 4 * 3; i++) {
            new Thread(new Worker(n, resultManager)).start();
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
