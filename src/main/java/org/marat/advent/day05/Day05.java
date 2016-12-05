package org.marat.advent.day05;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Day05 {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        final String doorId = "uqwqemis";

        MessageDigest md5 = MessageDigest.getInstance("MD5");

        char[] pw = new char[8];
        int pwi = 0;

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String hash = DatatypeConverter.printHexBinary(md5.digest((doorId + i).getBytes()));

            if (hash.startsWith("00000")) {
                pw[pwi++] = hash.charAt(5);
            }

            if (pwi >= pw.length) {
                break;
            }
        }

        System.out.println(new String(pw));
    }

}
