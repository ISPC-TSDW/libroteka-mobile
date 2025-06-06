package com.example.libroteka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.libroteka.data.BookResponse;

import java.util.List;

class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private List<BookResponse> favoriteBooks;
    private OnDeleteClickListener onDeleteClickListener;

    public FavoritesAdapter(List<BookResponse> favoriteBooks, OnDeleteClickListener onDeleteClickListener) {
        this.favoriteBooks = favoriteBooks;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorito, parent, false);
        return new FavoriteViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        BookResponse book = favoriteBooks.get(position);

        Context context = holder.itemView.getContext();

        // Load image using Glide from URL
        Glide.with(context)
                .load(book.getImage())
                .into(holder.bookImageView);

        holder.deleteButton.setOnClickListener(v ->
                onDeleteClickListener.onDeleteClick(book.getId_Book(), position)
        );
    }

    @Override
    public int getItemCount() {
        return favoriteBooks.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImageView;
        ImageButton deleteButton;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImageView = itemView.findViewById(R.id.bookImageView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int bookId, int position);
    }
}