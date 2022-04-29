package by.slizh.lab_3.activity;

import static by.slizh.lab_3.activity.MainActivity.CURR_USER_DB_INFO;
import static by.slizh.lab_3.activity.MainActivity.DB_HELPER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import by.slizh.lab_3.R;
import by.slizh.lab_3.db.DatabaseHelper;
import by.slizh.lab_3.entity.User;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextUserName, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    private Button registerButton;


    private void getRidOfTopBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getRidOfTopBar();

        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(view -> {
            registerUser();
        });

        editTextUserName = findViewById(R.id.RegistrationUserName);
        editTextEmail = findViewById(R.id.RegistrationEmail);
        editTextPassword = findViewById(R.id.RegistrationPassword);
        progressBar = findViewById(R.id.progressBarRegistration);

    }

    private void registerUser() {
        String name = editTextUserName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (name.isEmpty()) {
            editTextUserName.setError("Name is required!");
            editTextUserName.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password has to be at least 6 characters long!");
            editTextPassword.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please, provide valid email!");
            editTextEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        System.out.println("BEFORE CREATE USER");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(success -> {
                    System.out.println(1);
                    Toast.makeText(RegistrationActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);

                    insertNewUserIntoDatabase(success.getUser().getUid(), name);

                    Intent toMainActivity = new Intent(this, WelcomeActivity.class);
                    startActivity(toMainActivity);

//                    FirebaseDatabase.getInstance().getReference("Users")
//                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .setValue(user)
//                            .addOnSuccessListener(success2 -> {
//                                Toast.makeText(RegistrationActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
//                                progressBar.setVisibility(View.GONE);
//                            })
//                            .addOnFailureListener(failure2 -> {
//                                String message = failure2.getLocalizedMessage();
//                                Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_LONG).show();
//                                progressBar.setVisibility(View.GONE);
//                            });
                })
                .addOnFailureListener(failure -> {
                    String message = failure.getLocalizedMessage();
                    Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                });
        System.out.println("Out Register");
    }

    private void insertNewUserIntoDatabase(String userId, String name) {
        DB_HELPER = new DatabaseHelper(this);
        SQLiteDatabase sQlitedatabase = DB_HELPER.getWritableDatabase();
        Log.e("123", "Вставили " + userId);
        {//TABLE_USERS
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.KEY_id, userId);
            contentValues.put(DatabaseHelper.KEY_name, name);
            contentValues.put(DatabaseHelper.KEY_weight, -1);
            contentValues.put(DatabaseHelper.KEY_height, -1);
            contentValues.put(DatabaseHelper.KEY_sysPressure, -1);
            contentValues.put(DatabaseHelper.KEY_diaPressure, -1);
            contentValues.put(DatabaseHelper.KEY_age, -1);
            contentValues.put(DatabaseHelper.KEY_zodiac, 0);
            contentValues.put(DatabaseHelper.KEY_realImageCount, 0);

            sQlitedatabase.insert(DatabaseHelper.TABLE_USERS, null, contentValues);
        }

        {//TABLE_MOODS
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.KEY_id, userId);
            contentValues.put(DatabaseHelper.KEY_calm, 0);
            contentValues.put(DatabaseHelper.KEY_relax, 0);
            contentValues.put(DatabaseHelper.KEY_focus, 0);
            contentValues.put(DatabaseHelper.KEY_excited, 0);
            contentValues.put(DatabaseHelper.KEY_authentic, 0);
            contentValues.put(DatabaseHelper.KEY_fake, 0);

            sQlitedatabase.insert(DatabaseHelper.TABLE_MOODS, null, contentValues);
        }
        DB_HELPER.close();
        //saveAttachableImagesToDatabase();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}