public class Algorithm {
    private static final int SIZE = 5;
    // Directions for even and odd columns in a hexagonal grid
    private static final int[][][]DIRECTIONS = {
        {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1},  {1, 0}},  // Even column
        {{-1, 0}, {0, -1}, {0, 1}, {1, 0},{1,-1}, {1, 1}}   // Odd column
    };

    public static int hasSequence(int[][] board) {
        boolean[][] visited = new boolean[SIZE][SIZE];

        // Check for a sequence of 1 from first to last column
        for (int row = 0; row < SIZE; row++) {
            if (dfs(board, row, 0, 1, visited, true)) {
                return 1;
            }
        }

        // Reset visited array
        visited = new boolean[SIZE][SIZE];

        // Check for a sequence of 2 from first to last row
        for (int col = 0; col < SIZE; col++) {
            if (dfs(board, 0, col, 2, visited, false)) {
                return 2;
            }
        }

        // If no sequence is found, return 0
        return 0;
    }

    private static boolean dfs(int[][] board, int row, int col, int value, boolean[][] visited, boolean checkHorizontal) {
        // Check if we're out of bounds, already visited this cell, or the value doesn't match
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || visited[row][col] || board[row][col] != value) {
            return false;
        }

        // Check if we've reached the last column (for 1) or last row (for 2)
        if ((checkHorizontal && col == SIZE - 1) || (!checkHorizontal && row == SIZE - 1)) {
            return true;  // We've found a valid sequence
        }

        visited[row][col] = true;  // Mark this cell as visited

        // Determine which set of directions to use based on whether the column is even or odd
        int[][] currentDirections = DIRECTIONS[col % 2];

        // Check all possible directions from the current cell
        for (int[] dir : currentDirections) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            //System.out.println("Checking neighbor: (" + newRow + ", " + newCol + ")");
            if (dfs(board, newRow, newCol, value, visited, checkHorizontal)) {
                return true;
            }
        }

        visited[row][col] = false;  // Clean up if no sequence was found
        return false;
    }

    public static void main(String[] args) {
        int[][] board = {
            {1, 0, 1, 0, 1},
            {1, 1, 1, 1, 1},
            {1, 1, 0, 0, 1},
            {0, 2, 0, 1, 1},
            {2, 0, 0, 0, 2}
        };

        int result = hasSequence(board);
        System.out.println("Result: " + result);

        // Test case for sequence of 2
        int[][] board2 = {
            {1, 0, 2, 0, 1},
            {0, 1, 2, 2, 0},
            {2, 0, 2, 0, 2},
            {0, 2, 2, 1, 0},
            {2, 0, 2, 0, 0}
        };

        result = hasSequence(board2);
        System.out.println("Result for board2: " + result);
    }
}