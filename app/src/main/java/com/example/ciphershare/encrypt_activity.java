package com.example.ciphershare;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class encrypt_activity extends AppCompatActivity {

    private Spinner algorithmsSpinner;
    private Button generateCipherButton;
    private TextView plainTextInput;
    private TextView keyInputLabel;
    private char[][] keyMatrix;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);

        // Initialize views
        algorithmsSpinner = findViewById(R.id.algorithmsSpinner);
        generateCipherButton = findViewById(R.id.generateCipherButton);
        plainTextInput = findViewById(R.id.plainTextInput);
        keyInputLabel = findViewById(R.id.keyInputLabel);


        // Set up the dropdown menu
        List<String> algorithmsList = new ArrayList<>();
        algorithmsList.add("Play Fair");
        algorithmsList.add("AES");
        algorithmsList.add("DES");
        algorithmsList.add("Caesar Cipher");
        algorithmsList.add("Rail fence");
        algorithmsList.add("Vigenère Cipher");
        algorithmsList.add("RSA");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, algorithmsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        algorithmsSpinner.setAdapter(adapter);

        // Set up listener for item selection in the spinner
        algorithmsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Check the selected algorithm
                String selectedAlgorithm = algorithmsList.get(position);
                if (selectedAlgorithm.equals("Play Fair")) {
                    keyInputLabel.setVisibility(View.VISIBLE);
                }else if (selectedAlgorithm.equals("AES")) {
                    keyInputLabel.setVisibility(View.VISIBLE);
                } else if (selectedAlgorithm.equals("DES")) {
                    keyInputLabel.setVisibility(View.VISIBLE);
                }else if (selectedAlgorithm.equals("Rail fence")) {
                    keyInputLabel.setVisibility(View.VISIBLE);
                    keyInputLabel.setHint("Enter Intergers");
                }else if (selectedAlgorithm.equals("Vigenère Cipher")) {
                    keyInputLabel.setVisibility(View.VISIBLE);
                }else if (selectedAlgorithm.equals("RSA")) {
                    keyInputLabel.setHint("Enter Receivers Public Key");
                    keyInputLabel.setVisibility(View.VISIBLE);
                }
                else {
                    // Otherwise, hide the key input field
                    keyInputLabel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing if nothing is selected
            }
        });

        // Set up click listener for the button
        generateCipherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plainText = plainTextInput.getText().toString().trim();

                // Check if the input is null or empty
                if (plainText.isEmpty()) {
                    // If input is empty, show a toast message
                    Toast.makeText(encrypt_activity.this, "Please enter plain text", Toast.LENGTH_SHORT).show();
                    return; // Exit the method
                }
                // Get the selected algorithm from the spinner
                String selectedAlgorithm = algorithmsSpinner.getSelectedItem().toString();

                String cipherText;
                if (selectedAlgorithm.equals("Play Fair")) {
                    // Get the key from the user
                    String key = keyInputLabel.getText().toString().toUpperCase().replaceAll("[^A-Z]", "");
                    // Generate the Playfair key matrix
                    generatePlayfairMatrix(key);
                    // Perform Playfair encryption
                    cipherText = playfairEncrypt(plainText);
                }
                else if (selectedAlgorithm.equals("AES")) {
                    // Perform AES encryption
                    String key = keyInputLabel.getText().toString();
                    // Check if key length is valid
//                    if (key.length() != 16 && key.length() != 24 && key.length() != 32) {
//                        Toast.makeText(encrypt_activity.this, "Invalid key length. Key length should be 16, 24, or 32 characters.", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    try {
                        cipherText = EncryptionUtil.encryptAES(plainText, key);
                        // Print the encrypted output in the log
                        Log.d(TAG, "AES ENCRYPTED OUTPUT: " + cipherText);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Log.d(TAG, "AES ENCRYPTED OUTPUT: "+cipherText);
                }
                else if (selectedAlgorithm.equals("DES")) {
                    // DES encryption
                    String key = keyInputLabel.getText().toString();
                    try {
                        cipherText = DESEncryption.encryptDES(plainText, key);
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    } catch (InvalidKeyException e) {
                        throw new RuntimeException(e);
                    } catch (InvalidKeySpecException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchPaddingException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalBlockSizeException e) {
                        throw new RuntimeException(e);
                    } catch (BadPaddingException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d(TAG, "DES ENCRYPTED OUTPUT: " + cipherText);
                }else if (selectedAlgorithm.equals("Caesar Cipher")) {
                    // Caesar Cipher encryption
                    int shift = 3; // Example: Shift by 3 positions
                    cipherText = encryptCaesar(plainText, shift);
                    Log.d(TAG, "Caesar Cipher ENCRYPTED OUTPUT: " + cipherText);
                }else if (selectedAlgorithm.equals("Rail fence")) {
                    // Get the key from the user
                    String keyStr = keyInputLabel.getText().toString();
                    int key = Integer.parseInt(keyStr);
                    String[] arrangement = arrangeText(plainText, key);
                    String encryptedText = encryptRailFence(plainText, key);

                    // Perform Rail Fence encryption
                    cipherText = encryptRailFence(plainText, key);
                    Log.d(TAG, "Rail Fence ENCRYPTED OUTPUT: " + cipherText);
                }else if (selectedAlgorithm.equals("Vigenère Cipher")) {
                    String key = keyInputLabel.getText().toString().toUpperCase().replaceAll("[^A-Z]", "");
                    String pt = plainTextInput.getText().toString().toUpperCase().replaceAll("[^A-Z]", "");
                    // Perform Vigenère Cipher encryption
                    cipherText = vigencrypt(pt, key);
                    Log.d(TAG, "Vigenère Cipher ENCRYPTED OUTPUT: " + cipherText);
                }else if (selectedAlgorithm.equals("RSA")) {
                    String pt = plainTextInput.getText().toString().toUpperCase().replaceAll("[^A-Z]", "");
                    String key = keyInputLabel.getText().toString().toUpperCase().replaceAll("[^A-Z]", "");

                    try {
                        cipherText = RSAEncryption.encryptRSA("","");

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Log.d(TAG, "Vigenère Cipher ENCRYPTED OUTPUT: " + cipherText);
                }
                else {
                    // Handle other encryption algorithms (AES, DES) if needed
                    cipherText = ""; // Placeholder for other algorithms
                }
                // Create an Intent to navigate to the next activity
                Intent intent = new Intent(encrypt_activity.this, display_cipher.class);
                // Pass the cipher text as an extra
                intent.putExtra("cipherText", cipherText);
                // Start the activity
                startActivity(intent);

            }
        });
    }


    private String vigencrypt(String plainText, String key) {
        StringBuilder encryptedText = new StringBuilder();
        int keyLength = key.length();

        for (int i = 0; i < plainText.length(); i++) {
            char plainChar = plainText.charAt(i);
            char keyChar = key.charAt(i % keyLength);

            char encryptedChar = (char) (((plainChar + keyChar - 2 * 'A') % 26) + 'A');
            encryptedText.append(encryptedChar);
        }

        return encryptedText.toString();
    }
    private static String encryptRailFence(String plainText, int depth) {
        StringBuilder encryptedText = new StringBuilder();

        // Remove spaces from the plaintext
        plainText = plainText.replaceAll("\\s", "");

        // Initialize variables for current row and direction
        int row = 0;
        boolean goingDown = false;

        // Create an array to store characters for each row
        StringBuilder[] rails = new StringBuilder[depth];
        for (int i = 0; i < depth; i++) {
            rails[i] = new StringBuilder();
        }

        // Fill the rail fence pattern with characters from the plaintext
        for (char c : plainText.toCharArray()) {
            // Append character to the current rail
            rails[row].append(c);

            // Change direction if we reach the top or bottom rail
            if (row == 0 || row == depth - 1) {
                goingDown = !goingDown;
            }

            // Move to the next row
            row += goingDown ? 1 : -1;
        }

        // Combine the characters from each rail to form the encrypted text
        for (StringBuilder rail : rails) {
            encryptedText.append(rail);
        }

        return encryptedText.toString();
    }

    private static String[] arrangeText(String plainText, int depth) {
        String[] arrangement = new String[depth];
        int row = 0;
        boolean goingDown = false;

        // Initialize the arrangement
        for (int i = 0; i < depth; i++) {
            arrangement[i] = "";
        }

        // Fill the arrangement with characters from the plaintext
        for (char c : plainText.toCharArray()) {
            arrangement[row] += c;

            // Change direction if we reach the top or bottom row
            if (row == 0 || row == depth - 1) {
                goingDown = !goingDown;
            }

            // Move to the next row
            row += goingDown ? 1 : -1;
        }

        return arrangement;
    }
    private String encryptCaesar(String plainText, int shift) {
        StringBuilder encryptedText = new StringBuilder();
        for (int i = 0; i < plainText.length(); i++) {
            char currentChar = plainText.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                char encryptedChar = (char) ((currentChar + shift - 'A') % 26 + 'A');
                encryptedText.append(encryptedChar);
            } else if (Character.isLowerCase(currentChar)) {
                char encryptedChar = (char) ((currentChar + shift - 'a') % 26 + 'a');
                encryptedText.append(encryptedChar);
            } else {
                // If the character is not an alphabet, leave it unchanged
                encryptedText.append(currentChar);
            }
        }
        return encryptedText.toString();
    }
    private String encryptDES(String plainText, String key) {
        try {
            // Create DES key specification from the provided key
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            // Initialize DES cipher in encryption mode
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Encrypt the plaintext
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

            // Encode the encrypted bytes to Base64 string
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    //AES START


    //AES END

    // Convert byte array to hexadecimal string
    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


    private void generatePlayfairMatrix(String key) {
        // Remove duplicate characters from the key
        Set<Character> uniqueChars = new HashSet<>();
        StringBuilder cleanedKey = new StringBuilder();
        for (char ch : key.toCharArray()) {
            if (!uniqueChars.contains(ch)) {
                cleanedKey.append(ch);
                uniqueChars.add(ch);
            }
        }

        // Initialize the Playfair key matrix
        keyMatrix = new char[5][5];

        // Fill the key matrix with the key
        int index = 0;
        int alphabetIndex = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (index < cleanedKey.length()) {
                    keyMatrix[i][j] = cleanedKey.charAt(index++);
                } else {
                    // Add remaining alphabet letters in order, skipping those already in the key
                    char currentChar = (char) ('A' + alphabetIndex);
                    while (uniqueChars.contains(currentChar)) {
                        currentChar = (char) ('A' + ++alphabetIndex);
                    }
                    keyMatrix[i][j] = currentChar;
                    alphabetIndex++;
                }
            }
        }

        // Print the matrix in log
        for (int i = 0; i < 5; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < 5; j++) {
                row.append(keyMatrix[i][j]).append(" ");
            }
            Log.d("PlayfairMatrix", row.toString());
        }
    }
    private String playfairEncrypt(String plainText) {
        // Convert the plaintext to uppercase and remove any non-alphabetic characters
        plainText = plainText.toUpperCase().replaceAll("[^A-Z]", "");

        // Initialize the StringBuilder to store the cipher text
        StringBuilder cipherText = new StringBuilder();

        // Perform Playfair encryption
        for (int i = 0; i < plainText.length(); i += 2) {
            char firstChar = plainText.charAt(i);
            char secondChar = (i + 1 < plainText.length()) ? plainText.charAt(i + 1) : 'X'; // If the text length is odd, append 'X'

            // Find the positions of the two characters in the key matrix
            int[] firstPos = findPosition(keyMatrix, firstChar);
            int[] secondPos = findPosition(keyMatrix, secondChar);

            // Handle same row
            if (firstPos[0] == secondPos[0]) {
                cipherText.append(keyMatrix[firstPos[0]][(firstPos[1] + 1) % 5]);
                cipherText.append(keyMatrix[secondPos[0]][(secondPos[1] + 1) % 5]);
            }
            // Handle same column
            else if (firstPos[1] == secondPos[1]) {
                cipherText.append(keyMatrix[(firstPos[0] + 1) % 5][firstPos[1]]);
                cipherText.append(keyMatrix[(secondPos[0] + 1) % 5][secondPos[1]]);
            }
            // Handle different rows and columns
            else {
                cipherText.append(keyMatrix[firstPos[0]][secondPos[1]]);
                cipherText.append(keyMatrix[secondPos[0]][firstPos[1]]);
            }
        }

        return cipherText.toString();
    }

    private int[] findPosition(char[][] keyMatrix, char ch) {
        int[] pos = new int[2];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (keyMatrix[i][j] == ch) {
                    pos[0] = i;
                    pos[1] = j;
                    return pos;
                }
            }
        }
        return pos;
    }
}
