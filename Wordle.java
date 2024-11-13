import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Wordle {
    public static final String[][] allWordsLists = {wordsList.A, wordsList.B, wordsList.C, wordsList.D, wordsList.E, wordsList.F, wordsList.G, wordsList.H, wordsList.I, wordsList.J, wordsList.K, wordsList.L, wordsList.M, wordsList.N, wordsList.O, wordsList.P, wordsList.Q, wordsList.R, wordsList.S, wordsList.T, wordsList.U, wordsList.V, wordsList.W, wordsList.Y, wordsList.Z};

    public static void main(String[] args) throws FileNotFoundException {
        randomWordSelection();
    }

    public static void randomWordSelection() throws FileNotFoundException {
        Random random = new Random();
        int listPicker = random.nextInt(24);
        int wordPicker = random.nextInt(allWordsLists[listPicker].length);
        playGame(allWordsLists[listPicker][wordPicker].toUpperCase());
    }

    public static void playGame(String word) throws FileNotFoundException {
        String[] guesses = new String[12];
        Scanner scanner = new Scanner(System.in);
        System.out.print("Welcome to Wordle! Input your guess: ");
        int attempts = 0;
        boolean gameLoop = true;
        Scanner txtscan = new Scanner(new File("./validWords.txt"));
        StringBuilder validWords = new StringBuilder();
        while (txtscan.hasNextLine()) {
            validWords.append(txtscan.nextLine()).append(",");
        }
        while (gameLoop) {
            String guessedWord = scanner.nextLine().toUpperCase();
            if (guessedWord.length() != 5) {
                System.out.print("\nPlease input a word that is 5 letters long: ");
            } else if (!validWords.toString().contains(guessedWord)) {
                System.out.print("\nPlease input a valid English word: ");
            } else {
                String wordFeedback = giveFeedback(guessedWord, word);
                guesses[attempts * 2] = guessedWord;
                guesses[attempts * 2 + 1] = wordFeedback;
                displayGuesses(guesses);
                attempts++;
                if (guessedWord.equals(word)) {
                    gameLoop = false;
                    System.out.println("You win! Your attempt count was " + attempts + ".");
                } else if (attempts == 6) {
                    gameLoop = false;
                    System.out.println("You lost! The word was " + word + ".");
                }
            }
        }
        playAgainPrompt();
    }

    public static String giveFeedback(String guessedWord, String word) {
        StringBuilder feedback = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (guessedWord.charAt(i) == word.charAt(i)) {
                feedback.append("*");
            } else if (word.contains(String.valueOf(guessedWord.charAt(i)))) {
                feedback.append("+");
            } else {
                feedback.append("-");
            }
        }
        return feedback.toString();
    }

    public static void displayGuesses(String[] guesses) {
        System.out.println();
        for (int i = 0; i < 6; i++) {
            if (guesses[i * 2] == null) {
                break;
            }
            System.out.println(guesses[i * 2] + " " + guesses[i * 2 + 1]);
        }
    }

    public static void playAgainPrompt() throws FileNotFoundException {
        if (getYesOrNo("\nPlay Again? ")) {
            randomWordSelection();
        } else {
            System.out.println("\n\n\n--------- Thanks for playing! ---------");
        }
    }

    public static boolean getYesOrNo(String prompt) {
        Scanner scanner = new Scanner(System.in);
        String response;
        boolean loop = true;
        while (loop) {
            System.out.print(prompt);
            response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                return true;
            } else if (response.equals("no") || response.equals("n")) {
                return false;
            } else {
                System.out.println("Please respond with yes/y or no/n.");
            }
        }
        return false;
    }
}
