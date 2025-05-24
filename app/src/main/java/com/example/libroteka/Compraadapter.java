package com.example.libroteka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class Compraadapter extends RecyclerView.Adapter<Compraadapter.CompraViewHolder> {

    private List<Compra> compras;
    private Context context;

    public Compraadapter(Context context, List<Compra> compras) {
        this.context = context;
        this.compras = compras;
    }

    public static class CompraViewHolder extends RecyclerView.ViewHolder {
        TextView estado, totalLibros;
        LinearLayout layoutLibros;

        public CompraViewHolder(View view) {
            super(view);
            estado = view.findViewById(R.id.tvEstadoCompra);
            totalLibros = view.findViewById(R.id.tvTotalLibros);
            layoutLibros = view.findViewById(R.id.layoutLibros);
        }
    }

    @Override
    public CompraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_compra, parent, false);
        return new CompraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompraViewHolder holder, int position) {
        Compra compra = compras.get(position);
        holder.estado.setText("Estado: " + compra.getEstado());
        holder.totalLibros.setText("Libros comprados: " + compra.getLibros().size());
        holder.layoutLibros.removeAllViews();

        for (Libro libro : compra.getLibros()) {
            TextView libroText = new TextView(context);
            libroText.setText("- " + libro.getTitulo() + " (" + libro.getDetalle() + "): $" + libro.getPrecio());
            holder.layoutLibros.addView(libroText);
        }
    }

    @Override
    public int getItemCount() {
        return compras.size();
    }
}



