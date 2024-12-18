import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Wordle {
    public static final String[][] allWordsLists = {wordsList.A, wordsList.B, wordsList.C, wordsList.D, wordsList.E, wordsList.F, wordsList.G, wordsList.H, wordsList.I, wordsList.J, wordsList.K, wordsList.L, wordsList.M, wordsList.N, wordsList.O, wordsList.P, wordsList.Q, wordsList.R, wordsList.S, wordsList.T, wordsList.U, wordsList.V, wordsList.W, wordsList.Y, wordsList.Z};
    private static int[] guessDistribution = {0, 0, 0, 0, 0, 0};
    private static int played = 0;
    private static int wins = 0;
    private static int streak = 0;
    private static int maxStreak = 0;

    public static void main(String[] args) throws FileNotFoundException {
        loadPrompt();
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
                if (attempts == 6 || guessedWord.equals(word)) {
                    gameLoop = false;
                    stats(attempts, word, guessedWord);
                }
            }
        }
    }

    public static String giveFeedback(String guessedWord, String word) {
        StringBuilder feedback = new StringBuilder();
        int[] letterCount = new int[26];
        boolean[] correctPosition = new boolean[5];
        for (int i = 0; i < 5; i++) {
            char currentChar = word.charAt(i);
            letterCount[currentChar - 'A']++;
            if (guessedWord.charAt(i) == word.charAt(i)) {
                correctPosition[i] = true;
                letterCount[currentChar - 'A']--;
            }
        }
        for (int i = 0; i < 5; i++) {
            char guessedChar = guessedWord.charAt(i);
            if (correctPosition[i]) {
                feedback.append("*");
            } else if (letterCount[guessedChar - 'A'] > 0) {
                feedback.append("+");
                letterCount[guessedChar - 'A']--;
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
            savePrompt();
            System.out.println("\n--------- Thanks for playing! ---------");
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

    public static void stats(int guesses, String word, String guess) throws FileNotFoundException {
        if (guess.equals(word)) {
            System.out.println("\nYou win! Attempts: " + guesses);
            guessDistribution[guesses - 1]++;
            wins++;
            streak++;
            if (streak > maxStreak) {
                maxStreak = streak;
            }
        } else {
            System.out.println("\nYou lose! The word was " + word + ".");
            streak = 0;
        }
        played++;
        displayStats();
        playAgainPrompt();
    }

    public static void displayStats() {
        System.out.println("--------------- STATISTICS ---------------");
        System.out.println("Played: " + played + "     Win Percentage: " + ((wins * 100) / played) + "%");
        System.out.println("Current Streak: " + streak + "     Max Streak: " + maxStreak);
        System.out.println("----------- GUESS DISTRIBUTION -----------");
        for (int i = 0; i < 6; i++) {
            System.out.print((i + 1) + ": ");
            for (int j = 0; j < guessDistribution[i]; j++) {
                System.out.print("[]");
            }
            System.out.println(" " + guessDistribution[i]);
        }
        System.out.println("------------------------------------------");
    }

    public static void loadPrompt() throws FileNotFoundException {
        File file = new File("./saveData.txt");
        Scanner save = new Scanner(file);
        if (file.length() == 0) {
            PrintStream write = new PrintStream(file);
            for (int i = 0; i < 10; i++) {
                write.println(0);
            }
        } else if (getYesOrNo("Would you like to load your stats? ")) {
            for (int i = 0; i < 6; i++) {
                guessDistribution[i] = save.nextInt();
            }
            played = save.nextInt();
            wins = save.nextInt();
            streak = save.nextInt();
            maxStreak = save.nextInt();
            System.out.println("---- SAVE DATA SUCCESSFULLY LOADED ----");
        }
    }

    public static void savePrompt() throws FileNotFoundException {
        if (getYesOrNo("Would you like to save your statistics? (WARNING: Existing save data will be lost!) ")) {
            PrintStream save = new PrintStream(new File("./saveData.txt"));
            for (int i = 0; i < 6; i++) {
                save.println(guessDistribution[i]);
            }
            save.println(played);
            save.println(wins);
            save.println(streak);
            save.println(maxStreak);
            System.out.println("------- DATA SUCCESSFULLY SAVED -------");
        }
    }
}
