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
import com.example.libroteka.data.model.FavoriteBook;

import java.util.List;

class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {
    private List<FavoriteBook> favoriteBooks;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int favoriteId, int position);
    }

    public FavoritesAdapter(List<FavoriteBook> favoriteBooks, OnDeleteClickListener listener) {
        this.favoriteBooks = favoriteBooks;
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorito, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteBook favoriteBook = favoriteBooks.get(position);
        BookResponse book = favoriteBook.book;

        Context context = holder.itemView.getContext();

        Glide.with(context)
                .load(book.getImage())
                .into(holder.bookImageView);

        holder.deleteButton.setOnClickListener(v -> {
            deleteClickListener.onDeleteClick(favoriteBook.favorite.getId(), position);
        });
    }

    @Override
    public int getItemCount() {
        return favoriteBooks.size();
    }

    public void removeAt(int position) {
        favoriteBooks.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favoriteBooks.size());
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
}