package com.example.libroteka;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityHistorialCompras extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private ApiService apiService;
    private static final String BASE_URL = "http://127.0.0.1:8000/api/orders/";      //"https://tuservidor.com/api/"; // Ajusta tu URL base

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_compras);

        recyclerView = findViewById(R.id.recyclerOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        cargarHistorial(1); // <--- Reemplaza con el ID del usuario actual
    }

    private void cargarHistorial(int userId) {
        Call<List<Order>> call = apiService.getOrdersByUser(userId);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    List<Order> ordenes = response.body();
                    adapter = new OrderAdapter(ordenes);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ActivityHistorialCompras.this, "Error en respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(ActivityHistorialCompras.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
