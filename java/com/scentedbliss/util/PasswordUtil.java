package com.scentedbliss.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author 23049172 Sabin Devkota
 * 
 * This utility class provides methods for securely encrypting and decrypting passwords using
 * AES-GCM (Galois/Counter Mode) symmetric encryption. It derives an AES key from a username (as
 * a password) and a random salt using PBKDF2, and uses a random IV (Initialization Vector) for
 * each encryption operation to ensure security. The encrypted output includes the IV and salt for
 * decryption purposes.
 * 
 * Note: This implementation is designed for reversible encryption/decryption, not one-way hashing
 * (e.g., for password storage). For password storage, consider using a secure hashing algorithm
 * like bcrypt, Argon2, or PBKDF2 with a high iteration count.
 */
public class PasswordUtil {
    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding"; // AES encryption algorithm in GCM mode
    private static final int TAG_LENGTH_BIT = 128; // Authentication tag length for GCM (128 bits for max security)
    private static final int IV_LENGTH_BYTE = 12; // Recommended IV length for AES-GCM (12 bytes)
    private static final int SALT_LENGTH_BYTE = 16; // Salt length for key derivation (16 bytes)
    private static final Charset UTF_8 = StandardCharsets.UTF_8; // UTF-8 charset for string encoding/decoding

    /**
     * Generates a random nonce (e.g., IV or salt) of the specified length using SecureRandom.
     * 
     * @param numBytes The number of bytes for the nonce
     * @return A byte array containing the random nonce
     */
    public static byte[] getRandomNonce(int numBytes) {
        byte[] nonce = new byte[numBytes];
        new SecureRandom().nextBytes(nonce); // Fill the array with cryptographically secure random bytes
        return nonce;
    }

    /**
     * Generates a random AES key of the specified size using a secure key generator.
     * 
     * @param keysize The size of the AES key in bits (e.g., 128, 192, 256)
     * @return A SecretKey object for AES encryption
     * @throws NoSuchAlgorithmException if the AES algorithm is not available
     */
    public static SecretKey getAESKey(int keysize) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES"); // Initialize AES key generator
        keyGen.init(keysize, SecureRandom.getInstanceStrong()); // Set key size and use a strong SecureRandom
        return keyGen.generateKey(); // Generate and return the AES key
    }

    /**
     * Derives an AES 256-bit secret key from a password (username) and salt using PBKDF2 with HMAC-SHA256.
     * Uses 65,536 iterations for security.
     * 
     * @param password The password (username in this case) to derive the key from
     * @param salt A random salt to prevent rainbow table attacks
     * @return A SecretKey object for AES encryption, or null if an error occurs
     */
    public static SecretKey getAESKeyFromPassword(char[] password, byte[] salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"); // Use PBKDF2 with SHA-256
            // Configure key derivation: 65,536 iterations, 256-bit key length
            KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return secret; // Return the derived AES key
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PasswordUtil.class.getName()).log(Level.SEVERE, null, ex); // Log algorithm error
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(PasswordUtil.class.getName()).log(Level.SEVERE, null, ex); // Log key spec error
        }
        return null; // Return null if an error occurs
    }

    /**
     * Encrypts a password using AES-GCM with a key derived from the username and a random salt.
     * The output is a Base64-encoded string containing the IV, salt, and ciphertext.
     * 
     * @param username The username used to derive the encryption key
     * @param password The password to encrypt
     * @return A Base64-encoded string containing the encrypted password, IV, and salt, or null if an error occurs
     */
    public static String encrypt(String username, String password) {
        try {
            // Generate a random salt (16 bytes) for key derivation
            byte[] salt = getRandomNonce(SALT_LENGTH_BYTE);

            // Generate a random IV (12 bytes) for AES-GCM encryption
            byte[] iv = getRandomNonce(IV_LENGTH_BYTE);

            // Derive an AES key from the username (as password) and salt
            SecretKey aesKeyFromPassword = getAESKeyFromPassword(username.toCharArray(), salt);
            if (aesKeyFromPassword == null) {
                System.out.println("PasswordUtil: Failed to generate AES key for encryption");
                return null; // Return null if key generation fails
            }

            // Initialize cipher for encryption
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);

            // Initialize AES-GCM with the derived key and IV
            cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

            // Encrypt the password
            byte[] cipherText = cipher.doFinal(password.getBytes(UTF_8));

            // Combine IV, salt, and ciphertext into a single byte array
            byte[] cipherTextWithIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
                    .put(iv) // Prefix IV
                    .put(salt) // Prefix salt
                    .put(cipherText) // Append ciphertext
                    .array();

            // Encode the combined array as a Base64 string for transmission/storage
            return Base64.getEncoder().encodeToString(cipherTextWithIvSalt);
        } catch (Exception ex) {
            return null; // Return null if any error occurs (e.g., cipher initialization, encryption failure)
        }
    }

    /**
     * Decrypts a Base64-encoded encrypted password using AES-GCM with a key derived from the username.
     * The input string must contain the IV, salt, and ciphertext as generated by the encrypt method.
     * 
     * @param encryptedPassword The Base64-encoded string containing the IV, salt, and ciphertext
     * @param username The username used to derive the decryption key
     * @return The decrypted password as a string, or null if an error occurs
     */
    public static String decrypt(String encryptedPassword, String username) {
        try {
            // Decode the Base64-encoded string to retrieve IV, salt, and ciphertext
            byte[] decode = Base64.getDecoder().decode(encryptedPassword.getBytes(UTF_8));

            // Wrap the decoded bytes in a ByteBuffer to extract components
            ByteBuffer bb = ByteBuffer.wrap(decode);

            // Extract the IV (12 bytes)
            byte[] iv = new byte[IV_LENGTH_BYTE];
            bb.get(iv);

            // Extract the salt (16 bytes)
            byte[] salt = new byte[SALT_LENGTH_BYTE];
            bb.get(salt);

            // Extract the remaining ciphertext
            byte[] cipherText = new byte[bb.remaining()];
            bb.get(cipherText);

            // Derive the AES key using the same username and extracted salt
            SecretKey aesKeyFromPassword = PasswordUtil.getAESKeyFromPassword(username.toCharArray(), salt);
            if (aesKeyFromPassword == null) {
                System.out.println("PasswordUtil: Failed to generate AES key for decryption");
                return null; // Return null if key generation fails
            }

            // Initialize cipher for decryption
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);

            // Initialize AES-GCM with the derived key and extracted IV
            cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

            // Decrypt the ciphertext
            byte[] plainText = cipher.doFinal(cipherText);

            // Convert the decrypted bytes back to a string
            return new String(plainText, UTF_8);
        } catch (Exception ex) {
            return null; // Return null if any error occurs (e.g., cipher initialization, decryption failure)
        }
    }
}