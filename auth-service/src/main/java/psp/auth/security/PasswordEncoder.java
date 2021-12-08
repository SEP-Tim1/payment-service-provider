package psp.auth.security;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PasswordEncoder {

    private static final int ITERATIONS = 1000;
    private static final int KEY_LENGTH = 512;
    private static final String SALT = "gbifrnewqmk";

    public static byte[] encode(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(), ITERATIONS, KEY_LENGTH);
        SecretKey key = skf.generateSecret(spec);
        byte[] encoded = key.getEncoded();
        return encoded;
    }

    public static boolean valid(String password, byte[] hashedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return encode(password).equals(hashedPassword);
    }
}
