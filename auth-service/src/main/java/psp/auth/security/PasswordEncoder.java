package psp.auth.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PasswordEncoder {

    private static final String HASH_FUNC_NAME = "SHA-512";

    public static String encode(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        MessageDigest md = MessageDigest.getInstance(HASH_FUNC_NAME);
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    public static boolean equals(String password, String hashedPassword) {
        try {
            return encode(password).equals(hashedPassword);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return false;
        }
    }
}
