package com.example.ciphershare;

import static android.content.ContentValues.TAG;

import android.util.Log;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import java.util.Base64;
public class RSAEncryption {


    public static void main(String[] args) {
        try {
            String publicKeysString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0KRZHQHWBTvuuzgti7/aHd9twbT6p+d0G/BBN9S1GY3LDbJXnRFoqu9M2LCT0cpQhkv0+4wTcYn7lY6hIuHfQsVCs2SYl1K7o0M7aidZlhpySOxXSJ+SXYLeXbxfRtWwIDDtML2IWDXFEO8WAcyw7wMimsR3elz1GMH0qEsz7KK05SfWxN3N1AMaLJlBaZfDitWOEgY3UVrmSbwOOzWKBdP4WPTJ0ogiqHSy3Ki7jdwLOHlSoE1vO3Vis3RGNVubiK3tWiU9sr7ZWP6z/46kvUTnw2g7V5LRjT8Yv0tewpG+xuLF1IrYy1SkkijIj8nn/ylkHMhtv2tBik4tvipUlwIDAQAB";

            String plaintext = "Hello, World!";
            System.out.println("Plaintext: " + plaintext);

            String ciphertext = encryptRSA(plaintext, publicKeysString);
            System.out.println("Ciphertext: " + ciphertext);

            String privateKeystring = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDQpFkdAdYFO+67OC2Lv9od323BtPqn53Qb8EE31LUZjcsNsledEWiq70zYsJPRylCGS/T7jBNxifuVjqEi4d9CxUKzZJiXUrujQztqJ1mWGnJI7FdIn5Jdgt5dvF9G1bAgMO0wvYhYNcUQ7xYBzLDvAyKaxHd6XPUYwfSoSzPsorTlJ9bE3c3UAxosmUFpl8OK1Y4SBjdRWuZJvA47NYoF0/hY9MnSiCKodLLcqLuN3As4eVKgTW87dWKzdEY1W5uIre1aJT2yvtlY/rP/jqS9ROfDaDtXktGNPxi/S17Ckb7G4sXUitjLVKSSKMiPyef/KWQcyG2/a0GKTi2+KlSXAgMBAAECggEAUcbI4tSwZRbLiHgJaggFXsYPB+h+gkb2vGibt11Yqi5oUvL+4iJLqwvmS9xR491vWGP3Ho0Vpd/WSxU/VSPA1TPp49Re9pjH0MLuS7omo+2zs59myqE4xgwgSg8mLOZehQ+IOB1hjmls1ylOr9/hzZyl+JFe3wgGTPDgiwIG1Q1vp/iL98b76zREviKmxi+oB5o5ONIFp1cXLNX3f0TtIRNy0UFkXAZzgRjmDi6SKpPHehv59lRz9AJa0UQqP1KLrMtfJ0ahE8u3oAzfhUFF4KnuUu16NOtGiZORhajk9InyvuKqHctxIhL36mSxaBedE/a2r3Y0cPXwpeSmDeoBiQKBgQDuC/slT3O0Zdv272hTD3KFNHs8eaHYJNEfYY+8MoR8LANb3qePMynEDWEGFLqHGCRvuJZ6z/x+xEKgXZtUdYdwsa+0Es758xMY+33sVV0uJWrIETJMx8WJsowA+KgO2sn9nVQQ6f/DiA173OKRykab5g8m7vzSe+KJxFD1K7RcAwKBgQDgYKRlohK6s0k7eP50GWa1eEjzE3irBchPLz19IAm9HX03OYy1BbuOvJlRuDpDUV8MP1xhGBkbMh61gLO0rAnBWE2awDz/OjJR5Jvh0xsylb26/WMd8lyy30DHxoi8ffX8DcmoBoCwaQ79cQe1V/Ht+N/FbJK1+Ky1n1Pbab+i3QKBgEAtoL9VZWd8kspHj4PdRU3ecV9Dl7vTaUIhpoPhxtBHeXxaUiiMbbU6ui7Ohd+IRcKNLJzd2EBBH4lm00r5CNP8Q9ezvnOq8jopC7Ec7qtTUS+xWzNSJNLT/WUBpSWkHRMwSIrq25bW5nO/gKI3u8OqSemAQ09oMRJdiEohR1YLAoGBAJvCmv0uuJ/AzAqsIMAJziIVwVZ04uSeXAKUQbQ15GzTJtSKb+JOe3hH9osjP9bxivDen0uLxgn6DhVjpdy7PP5qRV0zLGTlSn9NGuV2fj/scUx4kDD+yVkm4DqO2Oc47eUu0RmCgh10oQJRT8+UoI7ZoMXVbDdEzOg8FaOQUodFAoGBALqCEePahMf+xmaPg6veuiEB1joBqRAApNCOvg7nt9x6Gu8Fndg603DNbnbVMEZFe+WH0b2oCUELwMbdqmO87Fwa5NnDn94jKgMF9/oNMftXfQW78PLbwW0173gLbg32aNz97JSm/ShScvX5eCNLsAcjUSpAfpA2gMzKzNHht6HI";

            String myplain = decryptRSA(ciphertext, privateKeystring);
            System.out.println("Ciphertext: to plaintext" + myplain);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encryptRSA(String plainText, String publicKeyString) throws Exception {
        try {
            String publicKeysString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0KRZHQHWBTvuuzgti7/aHd9twbT6p+d0G/BBN9S1GY3LDbJXnRFoqu9M2LCT0cpQhkv0+4wTcYn7lY6hIuHfQsVCs2SYl1K7o0M7aidZlhpySOxXSJ+SXYLeXbxfRtWwIDDtML2IWDXFEO8WAcyw7wMimsR3elz1GMH0qEsz7KK05SfWxN3N1AMaLJlBaZfDitWOEgY3UVrmSbwOOzWKBdP4WPTJ0ogiqHSy3Ki7jdwLOHlSoE1vO3Vis3RGNVubiK3tWiU9sr7ZWP6z/46kvUTnw2g7V5LRjT8Yv0tewpG+xuLF1IrYy1SkkijIj8nn/ylkHMhtv2tBik4tvipUlwIDAQAB";
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeysString);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            String plaintext = "Hello world";

            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
            String op = new String(encryptedBytes);
            Log.d(TAG, "encryptedBytes: ===="+ op);
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error encrypting plaintext with RSA public key");
        }
    }

    public static String decryptRSA(String cipherText, String privateKeyString) throws Exception {
        try {
            String chtext = cipherText;
            String privatekeyss = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDQpFkdAdYFO+67OC2Lv9od323BtPqn53Qb8EE31LUZjcsNsledEWiq70zYsJPRylCGS/T7jBNxifuVjqEi4d9CxUKzZJiXUrujQztqJ1mWGnJI7FdIn5Jdgt5dvF9G1bAgMO0wvYhYNcUQ7xYBzLDvAyKaxHd6XPUYwfSoSzPsorTlJ9bE3c3UAxosmUFpl8OK1Y4SBjdRWuZJvA47NYoF0/hY9MnSiCKodLLcqLuN3As4eVKgTW87dWKzdEY1W5uIre1aJT2yvtlY/rP/jqS9ROfDaDtXktGNPxi/S17Ckb7G4sXUitjLVKSSKMiPyef/KWQcyG2/a0GKTi2+KlSXAgMBAAECggEAUcbI4tSwZRbLiHgJaggFXsYPB+h+gkb2vGibt11Yqi5oUvL+4iJLqwvmS9xR491vWGP3Ho0Vpd/WSxU/VSPA1TPp49Re9pjH0MLuS7omo+2zs59myqE4xgwgSg8mLOZehQ+IOB1hjmls1ylOr9/hzZyl+JFe3wgGTPDgiwIG1Q1vp/iL98b76zREviKmxi+oB5o5ONIFp1cXLNX3f0TtIRNy0UFkXAZzgRjmDi6SKpPHehv59lRz9AJa0UQqP1KLrMtfJ0ahE8u3oAzfhUFF4KnuUu16NOtGiZORhajk9InyvuKqHctxIhL36mSxaBedE/a2r3Y0cPXwpeSmDeoBiQKBgQDuC/slT3O0Zdv272hTD3KFNHs8eaHYJNEfYY+8MoR8LANb3qePMynEDWEGFLqHGCRvuJZ6z/x+xEKgXZtUdYdwsa+0Es758xMY+33sVV0uJWrIETJMx8WJsowA+KgO2sn9nVQQ6f/DiA173OKRykab5g8m7vzSe+KJxFD1K7RcAwKBgQDgYKRlohK6s0k7eP50GWa1eEjzE3irBchPLz19IAm9HX03OYy1BbuOvJlRuDpDUV8MP1xhGBkbMh61gLO0rAnBWE2awDz/OjJR5Jvh0xsylb26/WMd8lyy30DHxoi8ffX8DcmoBoCwaQ79cQe1V/Ht+N/FbJK1+Ky1n1Pbab+i3QKBgEAtoL9VZWd8kspHj4PdRU3ecV9Dl7vTaUIhpoPhxtBHeXxaUiiMbbU6ui7Ohd+IRcKNLJzd2EBBH4lm00r5CNP8Q9ezvnOq8jopC7Ec7qtTUS+xWzNSJNLT/WUBpSWkHRMwSIrq25bW5nO/gKI3u8OqSemAQ09oMRJdiEohR1YLAoGBAJvCmv0uuJ/AzAqsIMAJziIVwVZ04uSeXAKUQbQ15GzTJtSKb+JOe3hH9osjP9bxivDen0uLxgn6DhVjpdy7PP5qRV0zLGTlSn9NGuV2fj/scUx4kDD+yVkm4DqO2Oc47eUu0RmCgh10oQJRT8+UoI7ZoMXVbDdEzOg8FaOQUodFAoGBALqCEePahMf+xmaPg6veuiEB1joBqRAApNCOvg7nt9x6Gu8Fndg603DNbnbVMEZFe+WH0b2oCUELwMbdqmO87Fwa5NnDn94jKgMF9/oNMftXfQW78PLbwW0173gLbg32aNz97JSm/ShScvX5eCNLsAcjUSpAfpA2gMzKzNHht6HI";
            byte[] privateKeyBytes = Base64.getDecoder().decode(privatekeyss);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(chtext));
            String op = new String(decryptedBytes);
            Log.d(TAG, "decryptRSA: ===="+ op);
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error decrypting ciphertext with RSA private key");
        }
    }
}





