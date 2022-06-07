package me.yangtao.spring.demo.common.utils;

import org.xerial.snappy.Snappy;

import java.io.IOException;

public class SnappyUtil {
    public static byte[] compress(String input) throws IOException {
        return Snappy.compress(input.getBytes("UTF-8"));
    }

    public static String uncompress(byte[] input) throws IOException {
        byte[] uncompressed = Snappy.uncompress(input);
        return new String(uncompressed, "UTF-8");
    }
}
