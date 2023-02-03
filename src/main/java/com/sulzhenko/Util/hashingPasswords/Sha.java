package com.sulzhenko.Util.hashingPasswords;

import com.sulzhenko.Util.UtilException;
import com.sulzhenko.model.Constants;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

/**
 * Sha class for hashing passwords for safe keeping them in database
 *
 * @author Artem Sulzhenko
 * @version 1.0
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
            throws NoSuchAlgorithmException, UnsupportedEncodingException, UtilException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");

        md.update(hashMe.getBytes(UTF8));
        salt.ifPresent(s -> {
            try {
                md.update(s.getBytes(UTF8));
            } catch (Exception e) {
                throw new UtilException(UNKNOWN_ERROR);
            }
        });
        return md.digest();
    }
}