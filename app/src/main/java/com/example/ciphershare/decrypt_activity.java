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
import javax.crypto.spec.SecretKeySpec;

public class decrypt_activity extends AppCompatActivity {

    private Spinner algorithmsSpinner;
    private Button decryptCipherButton;
    private TextView cipherTextInput;
    private TextView keyInputLabel;
    private char[][] keyMatrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);

        // Initialize views
        algorithmsSpinner = findViewById(R.id.algorithmsSpinner);
        decryptCipherButton = findViewById(R.id.decryptCipherButton);
        cipherTextInput = findViewById(R.id.cipherTextInput);
        keyInputLabel = findViewById(R.id.keyInputLabel);

        // Set up the dropdown menu
        List<String> algorithmsList = new ArrayList<>();
        algorithmsList.add("Play Fair");
        algorithmsList.add("AES");
        algorithmsList.add("DES");
        algorithmsList.add("Caesar Cipher");
        algorithmsList.add("Rail Fence");
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
                if (selectedAlgorithm.equals("Play Fair") ||
                        selectedAlgorithm.equals("AES") ||
                        selectedAlgorithm.equals("DES") ||
                        selectedAlgorithm.equals("Rail Fence") ||
                        selectedAlgorithm.equals("Vigenère Cipher")) {
                    keyInputLabel.setVisibility(View.VISIBLE);
                } else {
                    keyInputLabel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing if nothing is selected
            }
        });

        // Set up click listener for the decrypt button
        decryptCipherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cipherText = cipherTextInput.getText().toString().trim();

                if (cipherText.isEmpty()) {
                    Toast.makeText(decrypt_activity.this, "Please enter cipher text", Toast.LENGTH_SHORT).show();
                    return;
                }

                String selectedAlgorithm = algorithmsSpinner.getSelectedItem().toString();
                String decryptedText;

                if (selectedAlgorithm.equals("Play Fair")) {
                    String key = keyInputLabel.getText().toString().toUpperCase().replaceAll("[^A-Z]", "");
                    generatePlayfairMatrix(key);
                    decryptedText = playfairDecrypt(cipherText);
                } else if (selectedAlgorithm.equals("AES")) {
                    String key = keyInputLabel.getText().toString();
                    decryptedText = EncryptionUtil.decryptAES(cipherText, key);
                    Log.d(TAG, "AES DECRYPTED OUTPUT: " + decryptedText);
                } else if (selectedAlgorithm.equals("DES")) {
                    String key = keyInputLabel.getText().toString();
                    try {
                        decryptedText = DESEncryption.decryptDES(cipherText, key);
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
                    Log.d(TAG, "DES DECRYPTED OUTPUT: " + decryptedText);
                } else if (selectedAlgorithm.equals("Caesar Cipher")) {
                    int shift = 3; // Example: shift by 3 positions for encryption, so decrypt in the opposite direction
                    decryptedText = decryptCaesar(cipherText, shift);
                    Log.d(TAG, "Caesar Cipher DECRYPTED OUTPUT: " + decryptedText);
                } else if (selectedAlgorithm.equals("Rail Fence")) {
                    int depth = Integer.parseInt(keyInputLabel.getText().toString());
                    decryptedText = decryptRailFence(cipherText, depth);
                    Log.d(TAG, "Rail Fence DECRYPTED OUTPUT: " + decryptedText);
                } else if (selectedAlgorithm.equals("Vigenère Cipher")) {
                    String key = keyInputLabel.getText().toString().toUpperCase().replaceAll("[^A-Z]", "");
                    String ct = cipherTextInput.getText().toString().toUpperCase().replaceAll("[^A-Z]", "");
                    decryptedText = vigenDecrypt(ct, key);
                    Log.d(TAG, "Vigenère Cipher DECRYPTED OUTPUT: " + decryptedText);
                } else if (selectedAlgorithm.equals("RSA")) {
                    String ct = cipherTextInput.getText().toString().toUpperCase().replaceAll("[^A-Z]", "");
                    try {
                        decryptedText = RSAEncryption.decryptRSA(ct,"");

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Log.d(TAG, "RSA DECRYPTED OUTPUT: " + decryptedText);
                } else {
                    decryptedText = ""; // Placeholder for other algorithms
                }

                // Display the decrypted text
                Toast.makeText(decrypt_activity.this, "Decrypted Text: " + decryptedText, Toast.LENGTH_SHORT).show();

                Intent intentt = new Intent(decrypt_activity.this, display_cipher.class);
                // Pass the cipher text as an extra
                intentt.putExtra("decryptedText", decryptedText);
                // Start the activity
                startActivity(intentt);
            }
        });
    }

    private String calldecrsa(String ct) {

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("users");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String[] Plaintext = new String[1];

// Check if a user is signed in
        if (user != null) {
            // Get the user ID
            String userId = user.getUid();

            // Access the public key from the database (assuming it's stored under "publicKey")
            DatabaseReference userRef = dbref.child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String privatekey = dataSnapshot.child("privateKey").getValue(String.class);

                        try {
                            Plaintext[0] = RSAEncryption.decryptRSA(ct,privatekey);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        // Log the public key (avoid directly printing it for security reasons)
                        Log.d("FirebaseUser", "Public Key (not shown for security)"); // Consider using a more secure logging method
                    } else {
                        Log.w("FirebaseUser", "Public key not found for user: " + userId);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("FirebaseUser", "Error retrieving user data: ", error.toException());
                }
            });
        } else {
            // No user is signed in
            Log.d("FirebaseUser", "No user signed in");
        }


        return Plaintext[0];
    }


    private String playfairDecrypt(String cipherText) {
        StringBuilder decryptedText = new StringBuilder();

        // Process cipher text in pairs of characters
        for (int i = 0; i < cipherText.length(); i += 2) {
            char firstChar = cipherText.charAt(i);
            char secondChar = (i + 1 < cipherText.length()) ? cipherText.charAt(i + 1) : 'X';

            int[] firstPos = findPosition(keyMatrix, firstChar);
            int[] secondPos = findPosition(keyMatrix, secondChar);

            if (firstPos[0] == secondPos[0]) {
                decryptedText.append(keyMatrix[firstPos[0]][(firstPos[1] - 1 + 5) % 5]);
                decryptedText.append(keyMatrix[secondPos[0]][(secondPos[1] - 1 + 5) % 5]);
            } else if (firstPos[1] == secondPos[1]) {
                decryptedText.append(keyMatrix[(firstPos[0] - 1 + 5) % 5][firstPos[1]]);
                decryptedText.append(keyMatrix[(secondPos[0] - 1 + 5) % 5][secondPos[1]]);
            } else {
                decryptedText.append(keyMatrix[firstPos[0]][secondPos[1]]);
                decryptedText.append(keyMatrix[secondPos[0]][firstPos[1]]);
            }
        }

        return decryptedText.toString();
    }

    private String decryptAES(String cipherText, String key) {
        try {
            byte[] decryptedBytes = decrypt(hexStringToByteArray(cipherText), key);
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static byte[] decrypt(byte[] byteArray, String key) throws Exception {
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return cipher.doFinal(byteArray);
    }


    public static String decryptDES(String cipherText, String key) {
        try {
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedBytes = Base64.decode(cipherText, Base64.DEFAULT);
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String decryptCaesar(String cipherText, int shift) {
        shift = 26 - shift; // To decrypt, shift in the opposite direction
        StringBuilder decryptedText = new StringBuilder();
        for (int i = 0; i < cipherText.length(); i++) {
            char currentChar = cipherText.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                char decryptedChar = (char) ((currentChar + shift - 'A') % 26 + 'A');
                decryptedText.append(decryptedChar);
            } else if (Character.isLowerCase(currentChar)) {
                char decryptedChar = (char) ((currentChar + shift - 'a') % 26 + 'a');
                decryptedText.append(decryptedChar);
            } else {
                decryptedText.append(currentChar);
            }
        }
        return decryptedText.toString();
    }

    private String decryptRailFence(String cipherText, int depth) {
        // Recreate the rail fence structure
        String[] rails = new String[depth];
        int[] railLengths = new int[depth];
        int row = 0;
        boolean goingDown = false;

        // Determine the length of each rail
        for (int i = 0; i < cipherText.length(); i++) {
            railLengths[row]++;
            if (row == 0 || row == depth - 1) {
                goingDown = !goingDown;
            }
            row += goingDown ? 1 : -1;
        }

        // Fill the rails with the corresponding characters from the cipher text
        int pos = 0;
        for (int i = 0; i < depth; i++) {
            rails[i] = cipherText.substring(pos, pos + railLengths[i]);
            pos += railLengths[i];
        }

        // Rebuild the plaintext from the rail fence pattern
        StringBuilder decryptedText = new StringBuilder();
        row = 0;
        goingDown = false;

        for (int i = 0; i < cipherText.length(); i++) {
            decryptedText.append(rails[row].charAt(0));
            rails[row] = rails[row].substring(1);

            if (row == 0 || row == depth - 1) {
                goingDown = !goingDown;
            }

            row += goingDown ? 1 : -1;
        }

        return decryptedText.toString();
    }

    private String vigenDecrypt(String cipherText, String key) {
        StringBuilder decryptedText = new StringBuilder();
        int keyLength = key.length();

        for (int i = 0; i < cipherText.length(); i++) {
            char cipherChar = cipherText.charAt(i);
            char keyChar = key.charAt(i % keyLength);

            char decryptedChar = (char) (((cipherChar - keyChar + 26) % 26) + 'A');
            decryptedText.append(decryptedChar);
        }

        return decryptedText.toString();
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

    public static byte[] hexStringToByteArray(String hexString) {
        // Ensure the length is even for valid byte representation
        if (hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("Invalid hexadecimal string length");
        }

        int length = hexString.length();
        byte[] byteArray = new byte[length / 2]; // Initialize byteArray

        // Convert hex pairs to byte array
        for (int i = 0; i < length; i += 2) {
            String hexPair = hexString.substring(i, i + 2);
            byteArray[i / 2] = (byte) Integer.parseInt(hexPair, 16); // Fill byteArray
        }

        return byteArray; // Return the byte array
    }


}
