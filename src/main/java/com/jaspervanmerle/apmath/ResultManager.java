package com.jaspervanmerle.apmath;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResultManager {
    private final DecimalFormat integerFormat;

    private final Path readmeFile;
    private final Path resultsDirectory;

    private final Map<Integer, Integer> bestAllTimeScores = new HashMap<>();
    private final Map<Integer, Integer> bestSessionScores = new HashMap<>();

    public ResultManager() {
        integerFormat = new DecimalFormat();
        integerFormat.setMinimumFractionDigits(0);
        integerFormat.setMaximumFractionDigits(0);
        integerFormat.setGroupingUsed(true);
        integerFormat.setGroupingSize(3);

        Path projectRoot = getProjectRoot();
        readmeFile = projectRoot.resolve("README.md");
        resultsDirectory = projectRoot.resolve("results");

        if (!resultsDirectory.toFile().isDirectory()) {
            return;
        }

        for (File file : Objects.requireNonNull(resultsDirectory.toFile().listFiles())) {
            String name = file.getName();
            int n = Integer.parseInt(name.split("\\.")[0]);

            try {
                List<String> lines = Files.readAllLines(file.toPath());
                String scoreLine = lines.get(lines.size() - 1);
                bestAllTimeScores.put(n, Integer.parseInt(scoreLine.split(": ")[1].replaceAll(",", "")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized void submitGrid(Grid grid) {
        int n = grid.getN();
        int score = grid.getScore();

        boolean isBestAllTime = !bestAllTimeScores.containsKey(n) || score > bestAllTimeScores.get(n);
        if (isBestAllTime) {
            bestAllTimeScores.put(n, score);
        }

        boolean isBestSession = !bestSessionScores.containsKey(n) || score > bestSessionScores.get(n);
        if (isBestSession) {
            bestSessionScores.put(n, score);
        }

        String scoreStr = integerFormat.format(score);

        if (isBestAllTime) {
            System.out.println("New best all-time score for n = " + n + ": " + scoreStr);
        } else if (isBestSession) {
            String bestAllTimeScoreStr = integerFormat.format(bestAllTimeScores.get(n));
            System.out.println("New best session score for n = " + n + ": " + scoreStr + " (best all-time score: " + bestAllTimeScoreStr + ")");
            return;
        } else {
            return;
        }

        try {
            updateReadme(n);
            updateResultsFile(grid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateReadme(int n) throws IOException {
        int newScore = bestAllTimeScores.get(n);
        String resultsFileName = String.format("%03d.txt", n);

        List<String> readmeLines = new ArrayList<>();

        for (String line : Files.readAllLines(readmeFile)) {
            if (!line.startsWith("| " + n + " |")) {
                readmeLines.add(line);
                continue;
            }

            List<String> columns = new ArrayList<>();
            columns.add(Integer.toString(n));
            columns.add("[" + integerFormat.format(newScore) + "](./results/" + resultsFileName + ")");

            readmeLines.add("| " + String.join(" | ", columns) + " |");
        }

        Files.writeString(readmeFile, String.join("\n", readmeLines).trim() + "\n");
    }

    private void updateResultsFile(Grid grid) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("Submission:");
        lines.add(grid.getSubmission());
        lines.add("");
        lines.add("Grid:");
        lines.add(grid.toString());
        lines.add("");
        lines.add("Score: " + integerFormat.format(grid.getScore()));

        Path resultsFile = resultsDirectory.resolve(String.format("%03d.txt", grid.getN()));

        Files.createDirectories(resultsFile.getParent());
        Files.writeString(resultsFile, String.join("\n", lines) + "\n");
    }

    private Path getProjectRoot() {
        Path current = Paths.get("").toAbsolutePath();
        while (current != null) {
            if (current.resolve("README.md").toFile().isFile()) {
                return current;
            }

            current = current.getParent();
        }

        throw new RuntimeException("Cannot find project root");
    }
}
