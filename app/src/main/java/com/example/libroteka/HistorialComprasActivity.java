package com.example.libroteka;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_compras);

        recyclerView = findViewById(R.id.rvHistorial);
        tvTotalPedidos = findViewById(R.id.tvTotalPedidos);

        compras = getComprasEjemplo();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Compraadapter(this, compras));

        tvTotalPedidos.setText("Total de pedidos: " + compras.size());
    }

    private List<Compra> getComprasEjemplo() {
        return Arrays.asList(
                new Compra("PAID", Arrays.asList(new Libro("El Principito", "drama", 1),
                        new Libro("1984", "Suspenso", 2)
                )),
                new Compra("SENT", Arrays.asList(
                        new Libro("Cien años de soledad", "Accion", 3)
                ))
        );
    }
}

