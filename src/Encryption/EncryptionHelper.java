package Encryption;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
/**
 * Provides utility methods for encrypting and decrypting data using the AES
 * algorithm with CBC mode and PKCS5 padding. This class leverages the Bouncy
 * Castle security provider to perform cryptographic operations and includes
 * functionality for generating random initialization vectors (IVs) as well as
 * encrypting and decrypting text represented as byte arrays or character arrays.
 */

public class EncryptionHelper {

    private static final String BOUNCY_CASTLE_PROVIDER = "BC";    
    private Cipher cipher;

    // Key for AES (you should implement secure key management for production use)
    private byte[] keyBytes = new byte[] {
        0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
        0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
    };
    private SecretKey key = new SecretKeySpec(keyBytes, "AES");

    // Constructor to initialize the Bouncy Castle provider and Cipher instance
    public EncryptionHelper() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", BOUNCY_CASTLE_PROVIDER); // AES with CBC and padding
    }

    // Encrypt method: encrypts the plain text using AES/CBC/PKCS5Padding
    public byte[] encrypt(byte[] plainText, byte[] initializationVector) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(initializationVector));
        return cipher.doFinal(plainText);
    }

    // Decrypt method: decrypts the ciphertext back to the original text
    public byte[] decrypt(byte[] cipherText, byte[] initializationVector) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(initializationVector));
        return cipher.doFinal(cipherText);
    }

    // Method to generate a random Initialization Vector (IV)
    public byte[] generateIV() {
        byte[] iv = new byte[16]; // AES block size
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        return iv;
    }

    // Method to encrypt a char array
    public byte[] encryptCharArray(char[] charArray, byte[] initializationVector) throws Exception {
        // Convert char array to byte array using UTF-8 encoding
        byte[] byteArray = new String(charArray).getBytes(StandardCharsets.UTF_8);
        // Encrypt the byte array
        return encrypt(byteArray, initializationVector);
    }

    // Method to decrypt a byte array back to a char array
    public char[] decryptToCharArray(byte[] cipherText, byte[] initializationVector) throws Exception {
        // Decrypt the byte array
        byte[] decryptedBytes = decrypt(cipherText, initializationVector);
        // Convert decrypted bytes back to a char array
        return new String(decryptedBytes, StandardCharsets.UTF_8).toCharArray();
    }
}
