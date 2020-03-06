package com.example.blank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.imperiumlabs.geofirestore.GeoFirestore;

import java.util.List;

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
    private MutableLiveData<String> mMessage;

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
        mMessage = new MutableLiveData<>();
        /* ------------------------------------------------------ */

        updateTextBox();

        mSignInBtn.setOnClickListener((View view) -> {
            mAuth.signInWithEmailAndPassword("b.pranav.k@gmail.com", "password")
                    .addOnSuccessListener((AuthResult authResult) -> {
                        mUser = mAuth.getCurrentUser();
                        updateTextBox();
                        mMessage.setValue("Sign In Successful");

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
            mMessage.setValue("Signed out");
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

            CollectionReference ref = mDatabase.collection("geofire");
            GeoFirestore geoFirestore = new GeoFirestore(ref);

//            geoFirestore.setLocation("", new GeoPoint(37.7889, -122.4056973), (exception) -> {
//                if (exception == null) {
//                    Log.d(TAG, "Location saved on server successfully!");
//                } else {
//                    Log.d(TAG, "lmao exception An error has occurred: " + exception);
//                }
//            });

//            geoFirestore.setLocation("here", new GeoPoint(53.558944, -113.469944), (exception) -> {});
////            geoFirestore.setLocation("station", new GeoPoint(53.559666, -113.469125), (exception) -> {});
////            geoFirestore.setLocation("save-on-foods", new GeoPoint(53.562392, -113.466213), (exception) -> {});
////            geoFirestore.setLocation("uni", new GeoPoint(53.528478, -113.527200), (exception) -> {});
////            geoFirestore.setLocation("madeeha", new GeoPoint(53.416017, -113.435362), (exception) -> {});
////            geoFirestore.setLocation("calgary", new GeoPoint(51.166625, -113.952498), (exception) -> {});

//            geoFirestore.getAtLocation( (docs, ex) => {
//                for (QueryDocumentSnapshot doc: docs) {
//                    Log.d(TAG, "Got: " doc.);
//                }
//            });

            // Link: https://github.com/imperiumlabs/GeoFirestore-Android
            // another one: https://stackoverflow.com/questions/58999535/geofirestore-getatlocation-not-returning-the-full-list-of-documents
            
            // Return everything within radius starting at geolocation
            geoFirestore.getAtLocation(new GeoPoint(53.558944, -113.469944), 10000.0, new GeoFirestore.SingleGeoQueryDataEventCallback() {
                @Override
                public void onComplete(@Nullable List<? extends DocumentSnapshot> list, @Nullable Exception e) {
                    Log.i(TAG, "GeoFirestore: onComplete");
                    if (e != null) {
                        Log.i(TAG, "GeoFirestore: onComplete: error");
                        return;
                    } else {
                        Log.i(TAG, "GeoFirestore: onComplete: list.size() is "+ list.size());
                        for (DocumentSnapshot documentSnapshot : list) {
                            Log.i(TAG, "GeoFirestore: onComplete: documentSnapshot is "+ documentSnapshot.getId());
                        }
                    }
                }
            });

        });

        mMessage.observe(this, (
                @Nullable
                        String string) ->

        {
            mTextBox.setText(string);
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
