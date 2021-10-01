package com.jaspervanmerle.apmath;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Runner {
    private final ResultManager resultManager = new ResultManager();

    private void run(int n) {
        System.out.println("Running for n = " + n);

        Grid grid = new Grid(n);
        int span = grid.getSpan();

        while (true) {
            List<int[]> moves = new ArrayList<>();
            for (int y = -span; y <= span; y++) {
                for (int x = -span; x <= span; x++) {
                    if (grid.isOnGrid(x, y) && !grid.isMarked(x, y) && !grid.isBlocked(x, y)) {
                        moves.add(new int[]{x, y});
                    }
                }
            }

            if (moves.isEmpty()) {
                break;
            }

            Optional<Pair<int[], Integer>> bestPair = moves.parallelStream().map(move -> {
                Grid tmpGrid = new Grid(grid);

                if (!tmpGrid.markCell(move[0], move[1])) {
                    return null;
                }

                int remainingMoves = 0;
                for (int y = -span; y <= span; y++) {
                    for (int x = -span; x <= span; x++) {
                        if (tmpGrid.isOnGrid(x, y) && !tmpGrid.isMarked(x, y) && !tmpGrid.isBlocked(x, y)) {
                            remainingMoves++;
                        }
                    }
                }

                return new Pair<>(move, remainingMoves);
            }).max((a, b) -> {
                if (a == null && b == null) {
                    return 0;
                } else if (a == null) {
                    return -1;
                } else if (b == null) {
                    return 1;
                }

                int remainingMovesA = a.getSecond();
                int remainingMovesB = b.getSecond();
                if (remainingMovesA != remainingMovesB) {
                    return remainingMovesA - remainingMovesB;
                }

                int[] moveA = a.getFirst();
                int[] moveB = b.getFirst();
                return (Math.abs(moveA[0]) + Math.abs(moveA[1])) - (Math.abs(moveB[0]) + Math.abs(moveB[1]));
            });

            if (bestPair.isEmpty()) {
                break;
            }

            int[] bestMove = bestPair.get().getFirst();
            grid.markCell(bestMove[0], bestMove[1]);
        }

        resultManager.submitGrid(grid);
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
