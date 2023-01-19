package com.sulzhenko.model.hashingPasswords;

import com.sulzhenko.model.Constants;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

/**
 * This class hashing passwords using login as salt
 */
public class Sha implements Constants {

    public String hashToHex(String hashMe, Optional<String> salt)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] bytes = hash(hashMe, salt);

        StringBuilder sp = new StringBuilder();

        for (byte element : bytes) {
            sp.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
        }
        return sp.toString().toUpperCase();
    }

    public String hashToBase64(String hashMe, Optional<String> salt)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return Base64.getEncoder()
                .encodeToString(hash(hashMe, salt))
                .toUpperCase();
    }

    private byte[] hash(String hashMe, Optional<String> salt)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");

        md.update(hashMe.getBytes(UTF8));
        salt.ifPresent(s -> {
            try {
                md.update(s.getBytes(UTF8));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return md.digest();
    }

//    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        System.out.println(new Sha().hashToHex("Password@1", Optional.of("Aeneas")));
//    }
}
//https://coderlessons.com/articles/java/vybor-kriptograficheskikh-algoritmov-java-chast-1-kheshirovanie
