package com.introlab_systems.utils;

import java.util.Random;

public final class Utils {

    public static int generateAmount(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static int generateAmountExcept(int to, int except) {
        int x = 0;
        final Random random = new Random();
        for (int i = 0; i < to; i++) {
            x = random.nextInt(to) + 1;
            while (x == except) {
                x = random.nextInt(to) + 1;
            }
            return x;
        }
        return x;
    }
}
