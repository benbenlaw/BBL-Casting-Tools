package com.benbenlaw.castingtools.utils;

public class KeyCombo {

    public static final int MOD_SHIFT = 1;
    public static final int MOD_CONTROL = 2;
    public static final int MOD_ALT = 4;
    private static final int MOD_MASK = MOD_SHIFT | MOD_CONTROL | MOD_ALT;

    public static int encode(int key, int modifiers) {
        return (key & 0xFFFF) | ((modifiers & MOD_MASK) << 16);
    }

    public static int key(int combo) {
        return combo & 0xFFFF;
    }

    public static int modifiers(int combo) {
        return (combo >> 16) & MOD_MASK;
    }

    public static boolean isModifierKey(int key) {
        return (key >= 340 && key <= 347);
    }
}
