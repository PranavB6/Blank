package com.example.blank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button mCreateAccountBtn;
    private Button mSignInBtn;
    private Button mSignOutBtn;
    private Button mActionBtn;
    private TextView mTextBox;


    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCreateAccountBtn = findViewById(R.id.create_account_btn);
        mSignInBtn = findViewById(R.id.sign_in_btn);
        mSignOutBtn = findViewById(R.id.sign_out_btn);
        mActionBtn = findViewById(R.id.action_btn);
        mTextBox = findViewById(R.id.text_box);

        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        /* ------------------------------------------------------ */

        updateTextBox();

        mSignInBtn.setOnClickListener((View view) -> {
            mAuth.signInWithEmailAndPassword("b.pranav.k@gmail.com", "password")
                    .addOnSuccessListener((AuthResult authResult) -> {
                        mUser = mAuth.getCurrentUser();
                        updateTextBox();
                        Toast.makeText(this, "Sign In Successful", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener((@NonNull Exception e) -> {
                        Log.d(TAG, "Sign In Failed", e);
                        Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                    });
        });

        mSignOutBtn.setOnClickListener((View view) -> {
            mAuth.signOut();
            mUser = mAuth.getCurrentUser();
            updateTextBox();
            Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
        });

        String email = "b.pranav.k@gmail.com";
        String password = "password";

        mCreateAccountBtn.setOnClickListener((View view) -> {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener((AuthResult authResult) -> {
                        mUser = mAuth.getCurrentUser();
                        updateTextBox();
                        Toast.makeText(this, "Create Account Successful", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener((@NonNull Exception e) -> {
                        Log.d(TAG, "Create Account Failed", e);
                        Toast.makeText(this, "Create Account Failed", Toast.LENGTH_SHORT).show();
                    });
        });

        /* ------------------------------------------------------ */

        // Set the "Display Name" of the user
        String displayName = "Pranav";

        mActionBtn.setOnClickListener((View view) -> {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build();

            mUser.updateProfile(profileUpdates)
                    .addOnSuccessListener((Void aVoid) -> {
                        mUser = mAuth.getCurrentUser();
                        updateTextBox();
                        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener((@NonNull Exception e) -> {
                        Log.d(TAG, "Profile updates failed", e);
                        Toast.makeText(this, "Profile updates failed", Toast.LENGTH_SHORT).show();
                    });
        });

    }

    private void updateTextBox() {
        if (mUser != null) {
            mTextBox.setText("Current User Email: " + mUser.getEmail() + "\nCurrent User Display Name: " + mUser.getDisplayName());
        } else {
            mTextBox.setText("No user");
        }

    }
}
