package com.example.libroteka;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Map;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import com.example.libroteka.data.ApiManager;
import com.example.libroteka.data.Author;
import com.example.libroteka.data.BookResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private RecyclerView rvDestacados;
    private SessionManager sessionManager;
    private Map<Integer, String> authorMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getAuthors();
        // Icono del perfil y listener para navegar a ProfileActivity
        ImageView profileImageView = findViewById(R.id.profileIcon);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile();
            }
        });

        // Lista de categorías
        List<Categoria> listaCategorias = new ArrayList<>();
        listaCategorias.add(new Categoria("Acción", R.drawable.accion));
        listaCategorias.add(new Categoria("Aventura", R.drawable.aventura));
        listaCategorias.add(new Categoria("Fantasía", R.drawable.fantasia));

        // Inicializamos RecyclerView y BottomNavigationView
        RecyclerView rvCategorias = findViewById(R.id.rvCategorias);
        rvDestacados = findViewById(R.id.rvDestacados);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Configuramos el RecyclerView horizontal para categorías con un listener de clic
        rvCategorias.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        CategoriasAdapter categoriasAdapter = new CategoriasAdapter(listaCategorias, new CategoriasAdapter.OnCategoriaClickListener() {
            @Override
            public void onCategoriaClick(Categoria categoria) {
                // Acción cuando se selecciona una categoría
                goToCategories();
            }
        });
        rvCategorias.setAdapter(categoriasAdapter);

        // Agregamos el SnapHelper para el efecto de "snap"
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvCategorias);

        // Configuramos el RecyclerView horizontal para libros destacados
        rvDestacados.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //rvDestacados.setAdapter(new LibrosAdapter(listaLibros));

        // Menú de navegación inferior
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                return true;
            } else if (id == R.id.navigation_categories) {
                goToCategories(); // Navegar a la pantalla de categorías
                return true;
            } else if (id == R.id.navigation_favorites) {
                goToFavorites(); // Navegar a la pantalla de favoritos
                return true;
            }
            return false;
        });
    }

    // Método para navegar a ProfileActivity
    private void goToProfile() {
        Intent intent = new Intent(Home.this, ProfileActivity.class); // Asegúrate que tienes ProfileActivity creada
        startActivity(intent);
    }

    private void getBooks() {
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        ApiManager apiManager = new ApiManager(sessionManager);
        apiManager.getBooks(new ApiManager.ApiCallback<List<BookResponse>>() {
            @Override
            public void onSuccess(List<BookResponse> response) {
                // Update the list of books and refresh the RecyclerView
                updateBooksList(response);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Log the error or show a message to the user
            }
        });
    }

    private void getAuthors() {
        ApiManager apiManager = new ApiManager(new SessionManager(getApplicationContext()));
        apiManager.getAuthors(new ApiManager.ApiCallback<List<Author>>() {
            @Override
            public void onSuccess(List<Author> authors) {
                for (Author author : authors) {
                    authorMap.put(author.getId_Author(), author.getName());
                }
                Log.d("Autores", "Se cargaron los nombres de los autores correctamente.");
                getBooks();
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("Autores", "Error al obtener autores: " + errorMessage);
            }
        });
    }

    private void updateBooksList(List<BookResponse> bookList) {
        if (bookList == null || bookList.isEmpty()) {
            Log.w("Home", "No se recibieron libros desde la API");
            return;
        }

        Log.d("Home", "Cantidad de libros recibidos: " + bookList.size());

        runOnUiThread(() -> {
            rvDestacados.setAdapter(new BookAdapter(this, bookList, authorMap));
        });
    }

    // Método para navegar a la pantalla de categorías
    private void goToCategories() {
        Intent intent = new Intent(Home.this, Catalogo.class); // Asegúrate de tener Catalogo.java creado
        startActivity(intent);
    }

    // Método para navegar a la pantalla de favoritos (agregar cuando esté lista)
    private void goToFavorites() {
        Intent intent = new Intent(Home.this, FavoritosActivity.class); // Asegúrate que tienes ProfileActivity creada
        startActivity(intent);
    }
}




