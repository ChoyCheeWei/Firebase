    package com.example.ccw.firebase;

    import android.content.Intent;
    import android.support.annotation.NonNull;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.view.View;
    import android.widget.EditText;
    import android.widget.Toast;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;

    public class MainActivity extends AppCompatActivity {
        private FirebaseAuth mAuth;
        private FirebaseAuth.AuthStateListener mAuthListener;
        private EditText edusername, edpassword;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            edusername = (EditText) findViewById(R.id.email);
            edpassword = (EditText) findViewById(R.id.password);
            mAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                    } else {
                        // User is signed out
                    }
                    // ...
                }
            };
        }

        public void loginUser(View v){
            String email = edusername.getText().toString();
            String password = edpassword.getText().toString();

            if (TextUtils.isEmpty(email)){
                edusername.setError("Email is required");
            }

            if (TextUtils.isEmpty(password)){
                edpassword.setError("password is required");
            }
            if ((!TextUtils.isEmpty(email)) && (!TextUtils.isEmpty(password))) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, R.string.auth_failed,
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, R.string.action_auth_success,
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, StudentActivity.class);
                                    startActivity(intent);
                                }

                            }
                        });
            }
        }

        public void createUser(View v){
            String email = edusername.getText().toString();
            String password = edpassword.getText().toString();

            if (TextUtils.isEmpty(email)){
                edusername.setError("Email is required");
            }

            if (TextUtils.isEmpty(password)){
                edpassword.setError("Password is required");
            }
            if ((!TextUtils.isEmpty(email)) && (!TextUtils.isEmpty(password))){
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, R.string.auth_failed,
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, R.string.action_success, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }

        @Override
        public void onStop() {
            super.onStop();
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
        }


    }
