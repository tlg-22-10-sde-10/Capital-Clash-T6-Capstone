package com.game.client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Scanner;

public class GameClient {

    ScreenHandler screenHandler = new ScreenHandler();

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) throws IOException, InterruptedException {

        new GameClient();
        Scanner scanMe = new Scanner(System.in);
        boolean startGame = true;

        while (startGame) {
            Game game = new Game();
            game.gameOn();

            System.out.println("\nWould you like to" + ANSI_RED + " EXIT " + ANSI_RESET + "the game?");
            System.out.println("1) YES \n2) NO\n");

            String sc = scanMe.nextLine();
            while (!sc.equals("1") && !sc.equals("2")) {
                System.out.println("Please select either 1 or 2.");
                sc = scanMe.nextLine();
            }
            if (sc.equals("1")) {
                startGame = false;
            }
        }
    }

    public class ScreenHandler implements ActionListener {

        public void actionPerformed(ActionEvent event) {

        }
    }
}





