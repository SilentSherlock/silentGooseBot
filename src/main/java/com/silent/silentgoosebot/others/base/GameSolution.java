package com.silent.silentgoosebot.others.base;

import java.util.*;

/**
 * Date: 2024/4/12
 * Author: SilentSherlock
 * Description: describe the file
 */
public class GameSolution {

    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    private static final String TARGET_STATE = "123456780"; // Target state (solved state)

    public String solvePuzzle(int[][] board) {
        String initial = boardToString(board);
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();

        queue.offer(initial);
        visited.add(initial);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(TARGET_STATE)) {
                return reconstructPath(parent, current);
            }

            int zeroIndex = current.indexOf('0');
            int row = zeroIndex / 3;
            int col = zeroIndex % 3;

            for (int[] dir : DIRECTIONS) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                if (isValid(newRow, newCol)) {
                    String nextState = swap(current, zeroIndex, newRow * 3 + newCol);
                    if (!visited.contains(nextState)) {
                        queue.offer(nextState);
                        visited.add(nextState);
                        parent.put(nextState, current);
                    }
                }
            }
        }

        return "No solution found.";
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3;
    }

    private String swap(String s, int i, int j) {
        char[] chars = s.toCharArray();
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
        return new String(chars);
    }

    private String boardToString(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            for (int num : row) {
                sb.append(num);
            }
        }
        return sb.toString();
    }

    private String reconstructPath(Map<String, String> parent, String current) {
        StringBuilder path = new StringBuilder();
        while (parent.containsKey(current)) {
            path.insert(0, current + "\n");
            current = parent.get(current);
        }
        return path.toString();
    }

    public static void main(String[] args) {
        int[][] board = {{1, 4, 8}, {3, 6, 7}, {0, 2, 5}};
        GameSolution solver = new GameSolution();
        String solution = solver.solvePuzzle(board);
        System.out.println(solution);
    }
}
