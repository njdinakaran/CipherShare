package com.example.ciphershare;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

public class DESEncryption {

    public static String encryptDES(String message, String key) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        byte[] myMessage = message.getBytes(); // String to byte array as DES works on bytes

        // Using your own key
        byte[] mybyte = hexStringToByteArray(key);

        // Generating DES key from password
        DESKeySpec myMaterial = new DESKeySpec(mybyte);
        SecretKeyFactory MyKeyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey myDesKey = MyKeyFactory.generateSecret(myMaterial);

        // Initializing crypto algorithm
        Cipher myCipher = Cipher.getInstance("DES");

        // Setting encryption mode
        myCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
        byte[] myEncryptedBytes = myCipher.doFinal(myMessage);

        // Convert encrypted bytes to hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte b : myEncryptedBytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }
    public static String decryptDES(String encryptedMessage, String key) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedBytes = hexStringToByteArray(encryptedMessage);

        // Using your own key
        byte[] mybyte = hexStringToByteArray(key);

        // Generating DES key from password
        DESKeySpec myMaterial = new DESKeySpec(mybyte);
        SecretKeyFactory MyKeyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey myDesKey = MyKeyFactory.generateSecret(myMaterial);

        // Initializing crypto algorithm
        Cipher myCipher = Cipher.getInstance("DES");

        // Setting decryption mode
        myCipher.init(Cipher.DECRYPT_MODE, myDesKey);
        byte[] myDecryptedBytes = myCipher.doFinal(encryptedBytes);

        // Convert decrypted bytes to String
        return new String(myDecryptedBytes);
    }

    // Helper method to convert hexadecimal string to byte array
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
