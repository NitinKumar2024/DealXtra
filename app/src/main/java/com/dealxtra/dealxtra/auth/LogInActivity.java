package com.dealxtra.dealxtra.auth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dealxtra.dealxtra.ProgressBarManager;
import com.dealxtra.dealxtra.R;

import com.dealxtra.dealxtra.api_service.SecureStorage;
import com.dealxtra.dealxtra.api_service.VolleyCallback;
import com.dealxtra.dealxtra.api_service.VolleyService;
import com.dealxtra.dealxtra.customer.CustomerMainBottomActivity;
import com.dealxtra.dealxtra.useful_classes.ConstantValue;
import com.dealxtra.dealxtra.useful_classes.ShopDataStorage;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {
    private static final String TAG = "LogInActivity";
    private ProgressBarManager progressBarManager;
    private static final int RC_SIGN_IN = 9001;

    private TextInputEditText nameEditText, emailEditText, passwordEditText;
    private MaterialButton logInButton, googleSignUpButton, signInButton, signUpButton;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        emailEditText = findViewById(R.id.etEmail);
        passwordEditText = findViewById(R.id.etPassword);
        signInButton = findViewById(R.id.btnLogin);
        signUpButton = findViewById(R.id.btnSignUp);
        googleSignUpButton = findViewById(R.id.btnGoogleSignIn);

        signInButton.setOnClickListener(v -> signInWithEmailPassword());
        signUpButton.setOnClickListener(v -> startActivity(new Intent(LogInActivity.this, SignUpActivity.class)));

        googleSignUpButton.setOnClickListener(v -> signUpWithGoogle());



    }
    private void signInWithEmailPassword() {

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        //  startActivity(new Intent(SignUpActivity.this, ShopDetails.class));

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBarManager = ProgressBarManager.getInstance(this);
        progressBarManager.show("Login...");

        // Dynamic API endpoint and data
        String apiUrl = ConstantValue.baseUrl +"?action=login";  // Dynamic API endpoint for signup

        // Call the API
        // Prepare parameters for the POST request
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "login");
        postParams.put("email", email);
        postParams.put("password", password);

        // Call the POST method
        VolleyService.getInstance(this).postData(apiUrl, postParams, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                // Handle the successful response
                progressBarManager.hide();
                if (result.getBoolean("success")) {
                    String name = result.getString("name");
                    String email = postParams.get("email");
                    String token = result.getString("token");

                    // Save user data securely
                    SecureStorage secureStorage = new SecureStorage(LogInActivity.this);
                    secureStorage.saveUserData(name, email, token);


                        // Proceed to the ShopDetails activity if shop data exists
                        Intent intent = new Intent(LogInActivity.this, CustomerMainBottomActivity.class);
                        startActivity(intent);
                        finish();


                }
                Toast.makeText(LogInActivity.this, result.getString("message"), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                // Handle errors
                progressBarManager.hide();
                Toast.makeText(LogInActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });




    }
    // Utility method to decode Base64 to Bitmap (similar to the one in ShopDataStorage)



    private void signUpWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleGoogleSignInResult(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Google sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleGoogleSignInResult(GoogleSignInAccount account) {
        String idToken = account.getIdToken();
        String name = account.getDisplayName();
        String email = account.getEmail();
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("idToken", idToken);
            jsonBody.put("name", name);
            jsonBody.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}