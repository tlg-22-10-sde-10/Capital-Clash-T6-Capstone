package com.game.random;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomNumberForNews {

    private static Set<Integer> shownNews = new HashSet<>();
    private static final int RANGE = 10;
    private static final Random RANDOM = new Random();

    public static int getRandomNumber() {
        int number = RANDOM.nextInt(RANGE) + 1;

        while (shownNews.contains(number)) {
            number = RANDOM.nextInt(RANGE) + 1;
        }
        shownNews.add(number);

        if(shownNews.size()>=9){
            shownNews.clear();
        }

        return number;
    }

    public static int getInsiderChance() {
        int number = RANDOM.nextInt(5);
        return number;
    }
}