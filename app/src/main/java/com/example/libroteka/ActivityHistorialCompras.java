package com.example.libroteka;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.libroteka.data.ApiManager;
import com.example.libroteka.data.GetUserResponse;
import com.example.libroteka.data.MyApp;
import com.example.libroteka.retrofit.ApiInterface;
import com.example.libroteka.retrofit.RetrofitClient;
import java.util.ArrayList;
import java.util.List;

public class ActivityHistorialCompras extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private ApiInterface apiService;
    private SessionManager sessionManager;
    private android.widget.ProgressBar progressBar;
    private String userEmail; // Store user email as a class variable
    private static final String TAG = "ActivityHistorialCompras";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_compras);

        recyclerView = findViewById(R.id.recyclerOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);

        // Setup back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Setup refresh button
        findViewById(R.id.btnRefresh).setOnClickListener(v -> refreshOrders());

        // Initialize SessionManager
        sessionManager = new SessionManager(this);

        // Get the API service using RetrofitClient
        apiService = RetrofitClient.getRetrofitInstance(sessionManager).create(ApiInterface.class);

        // Get user information and then load orders
        MyApp app = (MyApp) getApplicationContext();
        userEmail = app.getUserEmail();

        if (userEmail != null && !userEmail.isEmpty()) {
            getUserInfoAndLoadOrders(userEmail);
        } else {
            Toast.makeText(this, "No se pudo obtener el correo del usuario", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to refresh orders
    private void refreshOrders() {
        if (userEmail != null && !userEmail.isEmpty()) {
            getUserInfoAndLoadOrders(userEmail);
        } else {
            Toast.makeText(this, "No se pudo obtener el correo del usuario", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserInfoAndLoadOrders(String email) {
        progressBar.setVisibility(android.view.View.VISIBLE);
        ApiManager apiManager = new ApiManager(sessionManager);
        apiManager.getUserByEmail(email, new ApiManager.ApiCallback<GetUserResponse>() {
            @Override
            public void onSuccess(GetUserResponse response) {
                String userId = response.getId();
                if (userId != null) {
                    try {
                        int userIdInt = Integer.parseInt(userId);
                        cargarHistorial(userIdInt);
                    } catch (NumberFormatException e) {
                        Toast.makeText(ActivityHistorialCompras.this,
                                "Error al convertir ID del usuario", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error parsing user ID: " + e.getMessage());
                    }
                } else {
                    Toast.makeText(ActivityHistorialCompras.this,
                            "No se pudo obtener el ID del usuario", Toast.LENGTH_SHORT).show();
                }
            }            @Override
            public void onFailure(String errorMessage) {
                progressBar.setVisibility(android.view.View.GONE);
                Toast.makeText(ActivityHistorialCompras.this,
                        "Error al obtener información del usuario: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error getting user info: " + errorMessage);
            }
        });
    }
    private void cargarHistorial(int userId) {
        Log.d(TAG, "Cargando historial para usuario ID: " + userId);
        retrofit2.Call<List<Order>> call = apiService.getOrdersByUser(userId);
        call.enqueue(new retrofit2.Callback<List<Order>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Order>> call, retrofit2.Response<List<Order>> response) {
                progressBar.setVisibility(android.view.View.GONE);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Order> ordenes = response.body();
                        if (ordenes.isEmpty()) {
                            Toast.makeText(ActivityHistorialCompras.this,
                                    "No hay pedidos para mostrar", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "Órdenes recibidas: " + ordenes.size());
                            adapter = new OrderAdapter(ordenes);
                            recyclerView.setAdapter(adapter);
                            // Force a layout pass
                            recyclerView.post(() -> adapter.notifyDataSetChanged());
                        }
                    } else {
                        Toast.makeText(ActivityHistorialCompras.this,
                                "Error en respuesta del servidor: " + response.code(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Server response error: " + response.code());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error processing response: " + e.getMessage());
                    Toast.makeText(ActivityHistorialCompras.this,
                            "Error procesando datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Order>> call, Throwable t) {
                progressBar.setVisibility(android.view.View.GONE);
                Toast.makeText(ActivityHistorialCompras.this,
                        "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Network error: " + t.getMessage());
            }
        });
    }
}
