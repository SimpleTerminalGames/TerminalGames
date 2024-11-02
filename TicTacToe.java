import java.util.Scanner;

public class TicTacToe {
    public static String turn = "X";
    public static int[] board = {0, 0, 0, 0, 0, 0, 0, 0, 0}; // 0 = NONE, 1 = X, 2 = O

    public static void main(String[] args) {
        playGame();
    }

    public static void playGame() {
        board = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
        Scanner scanner = new Scanner(System.in);
        boolean loop = true;
        int input;
        printBoard();
        while (loop) {
            System.out.println("\nPlayer " + turn + ", it is your move!");
            if (scanner.hasNextInt()) {
                input = scanner.nextInt() - 1;
                if (input >= 0 && input <= 8) {
                    if (board[input] == 0) {
                        System.out.println();
                        if (turn.equals("X")) {
                            board[input] = 1;
                            turn = "O";
                        } else if (turn.equals("O")) {
                            board[input] = 2;
                            turn = "X";
                        }
                        printBoard();
                        if (checkBoard() == 'X') {
                            System.out.println("Tic-Tac-Toe! Player X wins!");
                            loop = false;
                        } else if (checkBoard() == 'O') {
                            System.out.println("Tic-Tac-Toe! Player O wins!");
                            loop = false;
                        } else if (checkBoard() == 'T') {
                            System.out.println("No Tic-Tac-Toe! Tie Game!");
                            loop = false;
                        }
                    } else {
                        System.out.println("That move has already been taken! Input another spot.");
                    }
                } else {
                    System.out.println("Invalid move! Input a number between 1-9.");
                }
            } else {
                System.out.println("Invalid move! Input a number between 1-9.");
                scanner.next();
            }
        }
        playAgain();
    }

    public static void printBoard() {
        for (int i = 0; i < board.length; i++) {
            String display;
            if (board[i] == 1) {
                display = "X";
            } else if (board[i] == 2) {
                display = "O";
            } else {
                display = String.valueOf(i + 1);
            }
            System.out.print(" " + display + " ");
            if ((i + 1) % 3 == 0 && i < 8) {
                System.out.println();
                System.out.println("---+---+---");
            } else if ((i + 1) % 3 != 0) {
                System.out.print("|");
            }
        }
        System.out.println();
    }

    public static char checkBoard() {
        int[][] winningPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // HORIZONTAL CASES
            {0, 3, 6}, {1, 4, 7}, {2, 5, 9}, // VERTICAL CASES
            {0, 4, 8}, {2, 4, 6} // DIAGONAL CASES
        };
        for (int[] positions : winningPositions) {
            if (board[positions[0]] == 1 && board[positions[1]] == 1 && board[positions[2]] == 1) {
                return 'X'; // X WINS!
            } else if (board[positions[0]] == 2 && board[positions[1]] == 2 && board[positions[2]] == 2) {
                return 'O'; // O WINS!
            }
        }
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) {
                return 'C'; // CONTINUE GAME
            }
        }
        return 'T'; // TIE GAME!
    }

    public static void playAgain() {
        Scanner scanner = new Scanner(System.in);
        String response;
        boolean loop = true;
        System.out.print("Play again? ");
        while (loop) {
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes")) {
                loop = false;
                playGame();
            } else if (response.equals("no")) {
                loop = false;
                System.out.println("Thanks for playing Tic-Tac-Toe!");
            } else {
                System.out.println("Please respond with 'yes' or 'no'.");
            }
        }
    }
}
