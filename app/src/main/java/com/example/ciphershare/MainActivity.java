package com.example.ciphershare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
//import java.util.Base64;
import android.util.Base64;

public class MainActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextConfirmPassword,editTextName;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        // Set onClickListener for the registration button
        buttonRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim(); // Get user's name
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        // Check if the email is valid
        if (!isValidEmail(email)) {
            showToast("Invalid email address");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            showToast("Passwords do not match");
            return;
        }

        // Check password strength
        if (!isPasswordStrong(password)) {
            showToast("Password is not strong enough");
            return;
        }

        // Register user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                //saveUserDataToDatabase(user, name); // Pass name to save to database method

                                // Save user's email to the database
                                saveUserEmailToDatabase(user,name);
                                // Send verification email
                                sendVerificationEmail();
                                // Generate and save keys
                                generateKeyPairForUser();
                                showToast("Registration successful");

                                Intent intent = new Intent(MainActivity.this, login_activity.class);
                                startActivity(intent);                            }
                        } else {
                            // Registration failed
                            showToast("Registration failed: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private boolean isPasswordStrong(String password) {
        // Minimum length requirement
        if (password.length() < 8) {
            return false;
        }

        // Additional criteria (e.g., presence of uppercase, lowercase, numbers, special characters)
        // Implement your own logic here based on your requirements

        return true;
    }

    private boolean isValidEmail(String email) {
        // Regular expression for validating email addresses
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";

        // Check if the email matches the pattern
        return email.matches(emailPattern);
    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            showToast("Verification email sent");
                        } else {
                            showToast("Failed to send verification email");
                        }
                    });
        }
    }
    private void saveUserDataToDatabase(FirebaseUser user, String name) {
        String userId = user.getUid();
        String email = user.getEmail();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userRef.child("name").setValue(name); // Save user's name
        userRef.child("email").setValue(email);
    }

    private void saveUserEmailToDatabase(FirebaseUser user,String name) {
        String userId = user.getUid();
        String email = user.getEmail();


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userRef.child("email").setValue(email);
        userRef.child("name").setValue(name);
    }

    private void generateKeyPairForUser() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // Key size (adjust as needed)
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // Get public and private keys
            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();

            // Encode keys to Base64
            String publicKey = Base64.encodeToString(publicKeyBytes, Base64.DEFAULT);
            String privateKey = Base64.encodeToString(privateKeyBytes, Base64.DEFAULT);

            // Save keys in Firebase Realtime Database
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                userRef.child("publicKey").setValue(publicKey);
                userRef.child("privateKey").setValue(privateKey);
            }

            //showToast("Keys generated and saved successfully");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            showToast("Failed to generate key pair");
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
