package com.example.libroteka;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class HistorialComprasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvTotalPedidos;
    private List<Compra> compras;
    private Button volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_compras);

        recyclerView = findViewById(R.id.rvHistorial);
        tvTotalPedidos = findViewById(R.id.tvTotalPedidos);
        volver = findViewById(R.id.volver); // <- Aquí sí es válido

        compras = getComprasEjemplo();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Compraadapter(this, compras));

        tvTotalPedidos.setText("Total de pedidos: " + compras.size());

        // Listener dentro de onCreate
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistorialComprasActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private List<Compra> getComprasEjemplo() {
        return Arrays.asList(
                new Compra("PAID", Arrays.asList(new Libro("El Principito", "drama", 1),
                        new Libro("1984", "Suspenso", 2))),
                new Compra("SENT", Arrays.asList(new Libro("A la caza del Conde Dracula", "Ficcion", 3))),
                new Compra("PAID", Arrays.asList(new Libro("Invasion", "drama", 4),
                        new Libro("1984", "Suspenso", 5))),
                new Compra("SENT", Arrays.asList(new Libro("Cien años de soledad", "Accion", 6)))
        );
    }
}





