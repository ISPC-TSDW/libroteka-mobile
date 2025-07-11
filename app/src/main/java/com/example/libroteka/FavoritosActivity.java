package com.example.libroteka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libroteka.data.ApiManager;
import com.example.libroteka.data.BookResponse;
import com.example.libroteka.data.FavoriteRequest;
import com.example.libroteka.data.MyApp;
import com.example.libroteka.data.model.FavoriteBook;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class FavoritosActivity extends AppCompatActivity implements FavoritesAdapter.OnDeleteClickListener {

    private RecyclerView recyclerViewFavorites;
    private FavoritesAdapter adapter;
    private ApiManager apiManager;
    private String userId;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        recyclerViewFavorites = findViewById(R.id.recyclerViewFavorites);
        recyclerViewFavorites.setLayoutManager(new GridLayoutManager(this, 2));

        sessionManager = new SessionManager(getApplicationContext());
        apiManager = new ApiManager(sessionManager);
        MyApp app = (MyApp) getApplicationContext();
        userId = app.getUserId();

        fetchFavorites(userId);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_favorites) {
                return true;
            } else if (id == R.id.navigation_categories) {
                goToCategories();
                return true;
            } else if (id == R.id.navigation_home) {
                goToHome();
                return true;
            }
            return false;
        });
    }

    private void fetchFavorites(String userId) {
        final boolean[] favoritesFetched = {false};
        final boolean[] booksFetched = {false};
        final List<FavoriteRequest>[] favoritesResult = new List[1];
        final List<BookResponse>[] booksResult = new List[1];

        apiManager.getFavorites(userId, new ApiManager.ApiCallback<List<FavoriteRequest>>() {
            @Override
            public void onSuccess(List<FavoriteRequest> favorites) {
                favoritesFetched[0] = true;
                favoritesResult[0] = favorites;
                maybeProcessData();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(FavoritosActivity.this, "Error loading favorites: " + errorMessage, Toast.LENGTH_SHORT).show();
            }

            private void maybeProcessData() {
                if (favoritesFetched[0] && booksFetched[0]) {
                    displayFavorites(favoritesResult[0], booksResult[0]);
                }
            }
        });

        apiManager.getBooks(new ApiManager.ApiCallback<List<BookResponse>>() {
            @Override
            public void onSuccess(List<BookResponse> books) {
                booksFetched[0] = true;
                booksResult[0] = books;
                maybeProcessData();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(FavoritosActivity.this, "Error loading books: " + errorMessage, Toast.LENGTH_SHORT).show();
            }

            private void maybeProcessData() {
                if (favoritesFetched[0] && booksFetched[0]) {
                    displayFavorites(favoritesResult[0], booksResult[0]);
                }
            }
        });
    }

    private void displayFavorites(List<FavoriteRequest> favorites, List<BookResponse> books) {
        List<FavoriteBook> combinedList = new ArrayList<>();

        for (FavoriteRequest fav : favorites) {
            for (BookResponse book : books) {
                if (book.getId() == fav.getIdBook()) {
                    combinedList.add(new FavoriteBook(fav, book));
                    break;
                }
            }
        }

        adapter = new FavoritesAdapter(combinedList, FavoritosActivity.this);
        recyclerViewFavorites.setAdapter(adapter);

        if (combinedList.isEmpty()) {
            Toast.makeText(FavoritosActivity.this, "No favorites available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteClick(int favoriteId, int position) {
        apiManager.removeFavorite(favoriteId, new ApiManager.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                adapter.removeAt(position);
                Toast.makeText(FavoritosActivity.this, "Favorito eliminado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(FavoritosActivity.this, "Error eliminando favorito", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToCategories() {
        Intent intent = new Intent(FavoritosActivity.this, Catalogo.class);
        startActivity(intent);
    }

    private void goToHome() {
        Intent intent = new Intent(FavoritosActivity.this, Home.class);
        startActivity(intent);
    }
}