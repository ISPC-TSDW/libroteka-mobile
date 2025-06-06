package com.example.libroteka;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libroteka.data.ApiManager;
import com.example.libroteka.data.Author;
import com.example.libroteka.data.BookResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Catalogo extends AppCompatActivity {

    private RecyclerView recyclerViewLibros;
    private EditText searchInput;
    private ApiManager apiManager;
    private SessionManager sessionManager;
    private Map<Integer, String> authorMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        // Inicialización
        sessionManager = new SessionManager(getApplicationContext());
        apiManager = new ApiManager(sessionManager);

        recyclerViewLibros = findViewById(R.id.recycler_view_libros);
        recyclerViewLibros.setLayoutManager(new GridLayoutManager(this, 2));

        searchInput = findViewById(R.id.search_input);

        // Listeners
        findViewById(R.id.icon_profile).setOnClickListener(v -> goToProfile());
        findViewById(R.id.icon_back).setOnClickListener(v -> onBackPressed());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                goToHome();
                return true;
            } else if (item.getItemId() == R.id.navigation_favorites) {
                goToFavourites();
                return true;
            }
            return false;
        });

        // Primero traemos autores, después libros
        getAuthors();
    }

    private void getAuthors() {
        apiManager.getAuthors(new ApiManager.ApiCallback<List<Author>>() {
            @Override
            public void onSuccess(List<Author> authors) {
                for (Author author : authors) {
                    authorMap.put(author.getId_Author(), author.getName());
                }
                getBooks(); // Cuando se cargan los autores, traemos los libros
            }

            @Override
            public void onFailure(String errorMessage) {
            }
        });
    }

    private void getBooks() {
        apiManager.getBooks(new ApiManager.ApiCallback<List<BookResponse>>() {
            @Override
            public void onSuccess(List<BookResponse> books) {
                runOnUiThread(() -> {
                    BookAdapter adapter = new BookAdapter(Catalogo.this, books, authorMap);
                    recyclerViewLibros.setAdapter(adapter);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
            }
        });
    }

    private void goToProfile() {
        startActivity(new Intent(Catalogo.this, ProfileActivity.class));
    }

    private void goToFavourites() {
        startActivity(new Intent(Catalogo.this, FavoritosActivity.class));
    }

    private void goToHome() {
        startActivity(new Intent(Catalogo.this, Home.class));
    }
}












