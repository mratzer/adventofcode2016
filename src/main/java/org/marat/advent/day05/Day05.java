package org.marat.advent.day05;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Day05 {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        final String doorId = "uqwqemis";

        MessageDigest md5 = MessageDigest.getInstance("MD5");

        char[] pw = new char[8];
        int pwc = 0;

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String hash = DatatypeConverter.printHexBinary(md5.digest((doorId + i).getBytes()));

            if (hash.startsWith("00000")) {
                int index = hash.charAt(5) - '0';

                if (index < pw.length && pw[index] == 0) {
                    pw[index] = hash.charAt(6);
                    pwc++;
                }
            }

            if (pwc >= pw.length) {
                break;
            }
        }

        System.out.println(new String(pw));
    }

}
