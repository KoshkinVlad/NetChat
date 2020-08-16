package ru.geekbrains.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

public class ArraysHandler {

    private static final int NUMBER = 4;

    public int[] afterFour(int[] arr) {
        boolean contains = IntStream.of(arr).anyMatch(x -> x == NUMBER);
        if (!contains) {
            throw new RuntimeException("Array does not contain " + NUMBER);
        }
        int pos = arr.length - 1;
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == NUMBER) {
                pos = i;
                break;
            }
        }
        int[] result = new int[arr.length - (pos + 1)];
        for (int i = pos + 1, k = 0; i < arr.length; i++, k++) {
            result[k] = arr[i];
        }
        return result;
    }

    public boolean hasFourAndOne(int[] arr) {
        boolean hasOne = false;
        boolean hasFour = false;
        boolean hasOther = false;
        for (int i = 0; i < arr.length; i++) {
            switch (arr[i]) {
                case 1:
                    hasOne = true;
                    break;
                case 4:
                    hasFour = true;
                    break;
                default:
                    hasOther = true;
                    break;
            }
        }
        return hasFour && hasOne && !hasOther;
    }
}
