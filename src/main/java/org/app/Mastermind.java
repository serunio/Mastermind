package org.app;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Mastermind {

    private final Scanner in = new Scanner(System.in);
    private final Random random = new Random();
    private int length;
    private int range;
    private byte[] code;
    private byte[] guess;
    private int nOfTries = 0;
    private int nOfCorrectSpots, nOfIncorrectSpots;

    private Mastermind() {
    }

    public static void play() {
        new Mastermind().run();
    }

    private void run() {
        System.out.println("Witaj w grze Mastermind!");
        getSettings(0);
        getSettings(1);
        System.out.printf("Będziesz zgadywać %d cyfrowy kod składający się z liczb od 1 do %d.\n", length, range);
        generateCode();
        while (true) {
            readGuess();
            analiseGuess();
            if (gameIsFinished())
                return;
        }
    }

    private void generateCode() {
        code = new byte[length];
        for (int i = 0; i < length; i++) {
            code[i] = (byte) random.nextInt(49, 49+range);
        }
    }

    //mode = 0 - choose length
    //mode = 1 - choose range
    private void getSettings(int mode) {
        String[] messages;
        int[] acceptedRange;
        int value = 0;

        if(mode==0)
        {
            String[] m = {"Wybierz długość kodu który będziesz odgadywać (zakres 3-10): ",
                    "Podaj prawidłową wartość (3-10): ",
                    "Wybrałeś długość: "};
            int[] r = {3, 10};
            messages = m.clone();
            acceptedRange = r.clone();
        }
        else if(mode==1)
        {
            String[] m = {"Wybierz ilość cyfr z których może składać się kod (zakres 5-9): ",
                    "Podaj prawidłową wartość (5-9): ",
                    "Wybrałeś ilość: "};
            int[] r = {5, 9};
            messages = m.clone();
            acceptedRange = r.clone();
        }
        else{
            throw new IllegalArgumentException("Dozwolone tryby: 0, 1.");
        }

        boolean badInput = false;
        System.out.println(messages[0]);
        while (true) {
            try {
                value = in.nextInt();
            } catch (InputMismatchException e) {
                badInput = true;
            }
            in.nextLine();
            if (badInput || value < acceptedRange[0] || value > acceptedRange[1]) {
                System.out.println(messages[1]);
                badInput = false;
                continue;
            }
            System.out.println(messages[2] + value);
            break;
        }
        if(mode==0) length = value;
        if(mode==1) range = value;
    }

    private void printCode() {
        System.out.println(new String(code));
    }

    private void readGuess() {
        System.out.printf("Próba %d:\n", ++nOfTries);
        byte[] buffer;

        while (true) {
            buffer = in.nextLine().getBytes();
            try {
                if (validateGuess(buffer)) break;
            } catch(IllegalArgumentException e) {
                String message = e.getMessage().equals("length") ? "(zła długość)" :
                                 e.getMessage().equals("range") ? "(wartość spoza zakresu)" : "";
                System.out.printf("Nieprawidłowy format %s, spróbuj ponownie.\nPróba %d:\n", message, nOfTries);
            }
        }
        guess = buffer;
    }

    private boolean validateGuess(byte[] buffer) {
        if (buffer.length != length)
            throw new IllegalArgumentException("length");
        for (int i = 0; i < length; i++)
            if (buffer[i] < 49 || buffer[i] >= 49+range)
                throw new IllegalArgumentException("range");
        return true;
    }

    private void analiseGuess() {
        nOfCorrectSpots = 0;
        nOfIncorrectSpots = 0;
        byte[] codeCopy = code.clone();

        for(int i = 0; i < length; i++)
        {
            if(codeCopy[i] == guess[i]) {
                codeCopy[i] = guess[i] = 0;
                nOfCorrectSpots++;
            }
        }

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if(codeCopy[i] == guess[j] && codeCopy[i] > 0) {
                    codeCopy[i] = guess[j] = 0;
                    nOfIncorrectSpots++;
                }
            }
        }
        System.out.printf("Odgadnięte w dobrych miejscach: %d\nOdgadnięte w złych miejscach: %d\n", nOfCorrectSpots, nOfIncorrectSpots);
    }

    private boolean gameIsFinished() {
        if (nOfCorrectSpots == length) {
            System.out.println("Zgadłeś! Liczba prób: " + nOfTries);
            return true;
        } else
            return false;
    }
}
