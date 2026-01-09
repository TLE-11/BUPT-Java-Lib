import java.util.Scanner;

/**
 * @description 四子连
 * @author TLE-11
 * @create 2025/10/28
 **/
public class ConnectFour {
    // 棋盘尺寸（6行7列）
    private static final int ROWS = 6;
    private static final int COLS = 7;
    // 棋盘：0=空，1=红方(R)，2=黄方(Y)
    private int[][] pieces;
    // 当前玩家（1=红，2=黄）
    private int currentPlayer;
    // 游戏结束标记
    private boolean gameOver;

    public ConnectFour() {
        // 初始化空棋盘
        pieces = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                pieces[i][j] = 0;
            }
        }
        // 红方先出手
        currentPlayer = 1;
        gameOver = false;
    }

    // 绘制棋盘
    public void drawBoard() {
        for (int[] piece : pieces) {
            System.out.print("|");
            for (int i : piece) {
                char c = switch (i) {
                    case 1 -> 'R';
                    case 2 -> 'Y';
                    default -> ' ';
                };
                System.out.print(c);
                System.out.print('|');
            }
            System.out.println();
        }
        // 绘制底部分隔线和列索引
        StringBuffer bottomLine = new StringBuffer("-");
        StringBuffer bottomIndicator = new StringBuffer(" ");
        for (int i = 0; i < COLS; i++) {
            bottomLine.append("--");
            bottomIndicator.append(i).append(" ");
        }
        System.out.println(bottomLine);
        System.out.println(bottomIndicator);
    }

    // 落子：指定列，棋子落到该列最下方空位
    public boolean dropDisk(int col) {
        // 列号合法性检查
        if (col < 0 || col >= COLS) {
            System.out.println("列号无效！请输入0-6之间的数字");
            return false;
        }
        // 找到该列最下方的空位
        for (int i = ROWS - 1; i >= 0; i--) {
            if (pieces[i][col] == 0) {
                pieces[i][col] = currentPlayer;
                return true;
            }
        }
        // 列已满
        System.out.println("该列已落满棋子！请选择其他列");
        return false;
    }

    private boolean checkWin(int row, int col) {
        char player = (char) pieces[row][col];

        // 检查水平方向
        int count = 0;
        for (int c = 0; c < COLS; c++) {
            count = (pieces[row][c] == player) ? count + 1 : 0;
            if (count >= 4) return true;
        }

        // 检查垂直方向
        count = 0;
        for (int r = 0; r < ROWS; r++) {
            count = (pieces[r][col] == player) ? count + 1 : 0;
            if (count >= 4) return true;
        }

        // 检查左上到右下的对角线
        count = 0;
        int startRow = row - Math.min(row, col);
        int startCol = col - Math.min(row, col);
        for (int r = startRow, c = startCol; r < ROWS && c < COLS; r++, c++) {
            count = (pieces[r][c] == player) ? count + 1 : 0;
            if (count >= 4) return true;
        }

        // 检查右上到左下的对角线
        count = 0;
        startRow = row + Math.min(ROWS - 1 - row, col);
        startCol = col - Math.min(ROWS - 1 - row, col);
        for (int r = startRow, c = startCol; r >= 0 && c < COLS; r--, c++) {
            count = (pieces[r][c] == player) ? count + 1 : 0;
            if (count >= 4) return true;
        }

        return false;
    }

//    // 判断当前落子后是否获胜
//    public boolean checkWin(int row, int col) {
//        int player = pieces[row][col];
//        // 方向数组：右、下、右下、左下（覆盖所有连子可能）
//        int[][] dir = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
//
//        for (int[] d : dir) {
//            // 当前位置已算1个
//            int count = 1;
//            // 正向遍历
//            int r = row + d[0];
//            int c = col + d[1];
//            while (r >= 0 && r < ROWS && c >= 0 && c < COLS && pieces[r][c] == player) {
//                count ++;
//                r += d[0];
//                c += d[1];
//            }
//            // 反向遍历
//            r = row - d[0];
//            c = col - d[1];
//            while (r >= 0 && r < ROWS && c >= 0 && c < COLS && pieces[r][c] == player) {
//                count ++;
//                r -= d[0];
//                c -= d[1];
//            }
//            System.out.println(count);
//            // 连续4个则获胜
//            if (count >= 4) {
//                return true;
//            }
//        }
//        return false;
//    }

    // 判断棋盘是否已满（平局）
    public boolean isBoardFull() {
        for (int j = 0; j < COLS; j++) {
            //  第一行有空位则棋盘未满
            if (pieces[0][j] == 0) {
                return false;
            }
        }
        return true;
    }

    // 切换玩家
    public void switchPlayer() {
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
    }

    // 游戏主逻辑
    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== 四子连游戏 ===");
        drawBoard();

        while (!gameOver) {
            // 提示当前玩家落子
            if (currentPlayer == 1) {
                System.out.print("drop a red disk at column (0-6): ");
            } else {
                System.out.print("drop a yellow disk at column (0-6): ");
            }
            String player = (currentPlayer == 1) ? "红方(R)" : "黄方(Y)";
            int col = scanner.nextInt();

            // 落子成功后检查游戏状态
            if (dropDisk(col)) {
                drawBoard();

                // 找到当前落子的行（用于胜负判断）
                int row = -1;
                for (int i = ROWS - 1; i >= 0; i--) {
                    if (pieces[i][col] == currentPlayer) {
                        row = i;
                        break;
                    }
                }

                // 检查胜负/平局
                if (checkWin(row, col)) {
                    if ("红方(R)".equals(player)) {
                        System.out.println("The Red player won, congratulations!");
                    } else {
                        System.out.println("The Yellow player won, congratulations!");
                    }
                    gameOver = true;
                } else if (isBoardFull()) {
                    System.out.println("棋盘已满，游戏平局！");
                    gameOver = true;
                } else {
                    switchPlayer();
                }
            }
        }
        scanner.close();
    }

    public static void main(String[] args) {
        ConnectFour game = new ConnectFour();
        game.startGame();
    }
}