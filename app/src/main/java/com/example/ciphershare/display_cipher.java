/*
package com.example.ciphershare;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class display_cipher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cipher);

        // Find views
        TextView cipherTextView = findViewById(R.id.cipherTextView);
        Button copyToClipboardButton = findViewById(R.id.copyToClipboardButton);
        Button tryAgainButton = findViewById(R.id.tryAgainButton);

        // Get the received cipher text from the intent
        String cipherText = getIntent().getStringExtra("cipherText");

        // Set the received cipher text to the TextView
        cipherTextView.setText(cipherText);

        // Set click listener for the Copy to Clipboard button
        copyToClipboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the cipher text from the TextView
                String cipherText = cipherTextView.getText().toString();

                // Copy the cipher text to the clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("", cipherText);
                clipboardManager.setPrimaryClip(clipData);

                // Show a toast message indicating the text has been copied
                Toast.makeText(display_cipher.this, "Cipher text copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for the Try Again button
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the encrypt activity
                Intent intent = new Intent(display_cipher.this, encrypt_activity.class);
                startActivity(intent);
                finish(); // Finish the current activity to prevent going back to it when pressing the back button
            }
        });
    }
}*/
package com.example.ciphershare;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class display_cipher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cipher);

        // Find views
        TextView textDisplay = findViewById(R.id.cipherTextView);
        Button copyToClipboardButton = findViewById(R.id.copyToClipboardButton);
        Button tryAgainButton = findViewById(R.id.tryAgainButton);

        // Check for both cipher text and decrypted text in the intent
        String cipherText = getIntent().getStringExtra("cipherText");
        String decryptedText = getIntent().getStringExtra("decryptedText");

        String displayText;
        String toastMessage;

        // Determine which text to display and set the message accordingly
        if (decryptedText != null) {
            displayText = decryptedText;
            toastMessage = "Decrypted text copied to clipboard";
        } else {
            displayText = cipherText;
            toastMessage = "Cipher text copied to clipboard";
        }

        // Set the appropriate text to the TextView
        textDisplay.setText(displayText);

        // Set click listener for the Copy to Clipboard button
        copyToClipboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Copy the text to the clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("", displayText);
                clipboardManager.setPrimaryClip(clipData);

                // Show a toast message indicating the text has been copied
                Toast.makeText(display_cipher.this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for the Try Again button
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous activity based on the context
                if (decryptedText != null) {
                    // If displaying decrypted text, go back to decrypt_activity
                    Intent intent = new Intent(display_cipher.this, decrypt_activity.class);
                    startActivity(intent);
                } else {
                    // If displaying cipher text, go back to encrypt_activity
                    Intent intent = new Intent(display_cipher.this, encrypt_activity.class);
                    startActivity(intent);
                }
                finish(); // Finish the current activity to prevent going back to it when pressing the back button
            }
        });
    }
}


