package com.example.libroteka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.libroteka.data.ApiManager;
import com.example.libroteka.data.LoginRequest;
import com.example.libroteka.data.TokenRequest;
import com.example.libroteka.data.TokenResponse;
import com.example.libroteka.data.UserResponse;

public class Main_login2 extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button button;
    private ApiManager apiManager;
    private SessionManager sessionManager;
    private ImageButton togglePasswordButton;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login2);

        sessionManager = new SessionManager(getApplicationContext());
        apiManager = new ApiManager(sessionManager);
        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_pass);
        togglePasswordButton = findViewById(R.id.btn_toggle_password);
        button = findViewById(R.id.button); // Asegúrate de que este ID sea correcto

        button.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String token = getToken(email, password);

        });
        togglePasswordButton.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Si la contraseña está visible, la ocultamos
                passwordEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePasswordButton.setImageResource(R.drawable.ic_visibility_off); // Cambia el ícono
            } else {
                // Si la contraseña está oculta, la mostramos
                passwordEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                togglePasswordButton.setImageResource(R.drawable.ic_visibility); // Cambia el ícono
            }
            isPasswordVisible = !isPasswordVisible; // Alterna el estado de la visibilidad
            passwordEditText.setSelection(passwordEditText.getText().length()); // Mantén el cursor al final del texto
        });
    }

    private void loginUser(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);

        apiManager.loginUser(loginRequest, new ApiManager.ApiCallback<UserResponse>() {
            @Override
            public void onSuccess(UserResponse response) {
                Toast.makeText(Main_login2.this, "Login exitoso!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Main_login2.this, Home.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                if (errorMessage.equals("User account is deactivated")) {
                    Toast.makeText(Main_login2.this, "Cuenta desactivada. Por favor, contacta al soporte.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Main_login2.this, "Login fallido", Toast.LENGTH_SHORT).show();
                }
            }
        } );
    }

    private String getToken(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);
        String token = null;
        apiManager.getToken(tokenRequest, new ApiManager.ApiCallback<TokenResponse>() {
            @Override
            public void onSuccess(TokenResponse response) {
                sessionManager.createLoginSession(response.getAccess(), response.getRefreshToken(), email);
                if (validateInputs(email, password)) {

                    loginUser(email, password);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(Main_login2.this, "Error en token: " + errorMessage, Toast.LENGTH_SHORT).show();
            }

        });
        return token;

    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            Toast.makeText(this, "El correo electrónico no puede estar vacío", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Por favor, ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 12) {
            Toast.makeText(this, "La contraseña debe tener al menos 12 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Añadir validaciones adicionales de contraseña aquí si es necesario.
        return true;
    }
}
