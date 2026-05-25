package com.example.rpgpartymanager.utils;

import java.util.Random;

public class DiceManager {

    private static final Random random = new Random();

    public static int rollD20() {
        return random.nextInt(20) + 1;
    }
}