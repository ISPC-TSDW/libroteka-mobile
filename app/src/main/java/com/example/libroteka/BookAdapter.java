package com.example.libroteka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.libroteka.data.BookResponse;

import java.util.List;
import java.util.Map;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private final List<BookResponse> listaLibros;
    private final Context context;
    private final Map<Integer, String> authorMap;

    public BookAdapter(Context context, List<BookResponse> listaLibros, Map<Integer, String> authorMap) {
        this.context = context;
        this.listaLibros = listaLibros;
        this.authorMap = authorMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_destacado, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookResponse book = listaLibros.get(position);

        holder.tvTitulo.setText(book.getTitle());

        Log.d("BookAdapter", "Cargando libro: " + book.getTitle());


        if (book.getPrice() != null) {
            holder.tvPrice.setText("$" + book.getPrice().toString());
        } else {
            holder.tvPrice.setText("N/A");

        }

        // Cargar imagen desde URL con Glide
        Glide.with(context)
                .load(book.getImage())
                .placeholder(R.drawable.ic_book_dracula) // imagen de carga
                .error(R.drawable.ic_book_dracula)       // imagen de error
                .into(holder.ivImagen);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductoActivity.class);
            intent.putExtra("book_id", book.getId_Book());
            intent.putExtra("title", book.getTitle());
            intent.putExtra("description", book.getDescription());
            intent.putExtra("author", authorMap.get(book.getId_Author()));
            intent.putExtra("price", book.getPrice());
            intent.putExtra("avg_rating", book.getAvg_rating());
            intent.putExtra("image_url", book.getImage()); // pasamos la URL
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaLibros.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitulo;
        public ImageView ivImagen;
        public TextView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloLibro);
            ivImagen = itemView.findViewById(R.id.imgLibro);
            tvPrice = itemView.findViewById(R.id.tvPrecioLibro);
        }
    }
}
