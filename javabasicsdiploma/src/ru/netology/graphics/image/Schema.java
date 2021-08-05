package ru.netology.graphics.image;

public class Schema implements TextColorSchema {
    @Override
    public char convert(int color) {
        char[] chars = {'#', '$', '@', '%', '*', '+', '-'};
        int convert = (color * chars.length - 1) / 255;
        return chars[convert];
    }
}
