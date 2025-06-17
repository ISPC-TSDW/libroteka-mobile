package com.example.libroteka;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.text.InputType;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.example.libroteka.data.ApiManager;
import com.example.libroteka.data.RegisterRequest;
import com.example.libroteka.data.RegisterResponse;

public class RegisterActivity extends AppCompatActivity {

    private ApiManager apiManager;
    private SessionManager sessionManager;

    private EditText etContrasena, etConfirmarContrasena;
    private ImageButton btnTogglePassword, btnToggleConfirmPassword;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sessionManager = new SessionManager(getApplicationContext());
        apiManager = new ApiManager(sessionManager);

        // Definimos las referencias a los campos de entrada
        EditText etDni = findViewById(R.id.etDNI);
        EditText etApellido = findViewById(R.id.etApellido);
        EditText etNombre = findViewById(R.id.etNombre);
        EditText etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        TextView tvIniciarSesion = findViewById(R.id.tvIniciarSesion);
        btnTogglePassword = findViewById(R.id.btn_toggle_password);
        btnToggleConfirmPassword = findViewById(R.id.btn_toggle_confirm_password);

        btnTogglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                etContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnTogglePassword.setImageResource(R.drawable.ic_visibility_off);
            } else {
                etContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnTogglePassword.setImageResource(R.drawable.ic_visibility);
            }
            isPasswordVisible = !isPasswordVisible;
            etContrasena.setSelection(etContrasena.getText().length());
        });

        btnToggleConfirmPassword.setOnClickListener(v -> {
            if (isConfirmPasswordVisible) {
                etConfirmarContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnToggleConfirmPassword.setImageResource(R.drawable.ic_visibility_off);
            } else {
                etConfirmarContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnToggleConfirmPassword.setImageResource(R.drawable.ic_visibility);
            }
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
            etConfirmarContrasena.setSelection(etConfirmarContrasena.getText().length());
        });

        // Configuramos el botón de registro
        btnRegistrar.setOnClickListener(v -> {
            String usuario = etCorreo.getText().toString().trim();
            String nombre = etNombre.getText().toString().trim();
            String apellido = etApellido.getText().toString().trim();
            Integer dni = Integer.parseInt(etDni.getText().toString().trim());    // Convertir el String a Integer
            String correo = etCorreo.getText().toString().trim();
            String contrasena = etContrasena.getText().toString().trim();
            String confirmarContrasena = etConfirmarContrasena.getText().toString().trim();

            // Validamos los campos
            if (nombre.isEmpty() || apellido.isEmpty() || dni == null || correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar que el nombre no contenga caracteres especiales o números
            if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
                Toast.makeText(this, "El nombre no debe contener números o caracteres especiales", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar formato correo electrónico
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                Toast.makeText(this, "Por favor, ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar que el correo tenga un dominio como "gmail.com", "hotmail.com", etc.
            String[] validDomains = {"gmail.com", "hotmail.com", "yahoo.com", "outlook.com","libroteka.com"};
            String domain = correo.substring(correo.indexOf("@") + 1);
            boolean isDomainValid = false;
            for (String validDomain : validDomains) {
                if (domain.equalsIgnoreCase(validDomain)) {
                    isDomainValid = true;
                    break;
                }
            }

            if (!isDomainValid) {
                Toast.makeText(this, "Por favor, usa un dominio de correo electrónico válido (ej. gmail.com, hotmail.com)", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar la contraseña
            if (!isValidPassword(contrasena)){
                return;
            };

            if (!contrasena.equals(confirmarContrasena)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            // Aquí enviamos los datos al backend
            RegisterRequest registerRequest = new RegisterRequest(usuario, nombre, apellido, dni, contrasena, correo);

            apiManager.registerUser(registerRequest, new ApiManager.ApiCallback<RegisterResponse>() {
                @Override
                public void onSuccess(RegisterResponse response) {
                    Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    // Navigate to login activity after successful registration
                    Intent intent = new Intent(RegisterActivity.this, main_login.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Set up the login TextView click listener
        tvIniciarSesion.setOnClickListener(v -> {
            // Navigate to the login activity
            Intent intent = new Intent(RegisterActivity.this, main_login.class);
            startActivity(intent);
        });
    }

    private boolean isValidPassword(String contrasena) {
        Log.i("registro", "Validando contraseña");
        List<String> errors = new ArrayList<>();
        // Validar longitud
        if (contrasena.length() < 12) {
            errors.add("Debe ser mayor a 12 caracteres");
        }
        // Validar complejidad de la contraseña con el patrón regex
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{12,}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(contrasena);

        if (!matcher.matches()) {
            Log.i("registro", "Contraseña no válida");
            errors.add("Debe contener 1 minuscula, 1 mayuscula, numero y caracter");
        }

        // Verificar que no haya caracteres consecutivos iguales
        if (contrasena.matches(".*(.)\\1{2,}.*")) {
            Log.i("registro", "Contraseña no válida2");
            errors.add("No debe contener más de 2 caracteres consecutivos iguales");
        }

        // Verificar si hay secuencias consecutivas
        if (hasConsecutiveSequence(contrasena)) {
            Log.i("registro", "Contraseña no válida3");
           errors.add("No debe contener secuencias numéricas de más de 3 caracteres");
        }


        // Verificar patrones repetitivos
        if (hasRepetitivePattern(contrasena)) {
            errors.add("La contraseña no debe tener patrones repetitivos");
        }
        if (!errors.isEmpty()) {
            showErrorsDialog(errors);
            return false;
        }

        return true;
    }

    private boolean hasConsecutiveSequence(String contrasena) {
        int sequenceCount = 1;

        for (int i = 0; i < contrasena.length() - 1; i++) {
            // Verifica si ambos caracteres son dígitos
            if (Character.isDigit(contrasena.charAt(i)) && Character.isDigit(contrasena.charAt(i + 1))) {
                // Verifica si los dígitos son consecutivos
                if (contrasena.charAt(i + 1) == contrasena.charAt(i) + 1) {
                    sequenceCount++;
                    if (sequenceCount >= 3) {
                        return true;
                    }
                } else {
                    sequenceCount = 1;
                }
            } else {
                sequenceCount = 1;
            }
        }
        return false;
    }



    private boolean hasRepetitivePattern(String contrasena) {
        int length = contrasena.length();
        for (int i = 1; i <= length / 2; i++) {
            String substring = contrasena.substring(0, i);
            String repeated = substring.repeat(length / i);
            if (repeated.startsWith(contrasena)) {
                return true;
            }
        }
        return false;
    }

    // Método para mostrar errores en un AlertDialog
    private void showErrorsDialog(List<String> errors) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this,R.style.AlertDialog);
        builder.setTitle("Errores de registro");

        // Convertir la lista de errores en un solo String con saltos de línea
        StringBuilder errorMessage = new StringBuilder();
        for (String error : errors) {
            errorMessage.append("• ").append(error).append("\n");
        }

        builder.setMessage(errorMessage.toString());
        builder.setPositiveButton("Aceptar", null);
        builder.show();

    }
}
