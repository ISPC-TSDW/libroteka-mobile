package com.example.libroteka;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Catalogo extends AppCompatActivity {

    private RecyclerView recyclerViewLibros;
    private LibrosAdapter librosAdapter;
    private List<Libro> listaLibrosOriginales; // Para almacenar la lista original de libros
    private List<Libro> listaLibrosFiltrados;  // Para almacenar la lista filtrada de libros
    private EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);


        // Icono del perfil y listener para navegar a ProfileActivity
        ImageView profileImageView = findViewById(R.id.icon_profile);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile(); // Navegamos a ProfileActivity
            }
        });

        // Inicializamos el RecyclerView para los libros
        recyclerViewLibros = findViewById(R.id.recycler_view_libros);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // 2 columnas para libros
        recyclerViewLibros.setLayoutManager(gridLayoutManager);

        // Inicializamos el campo de búsqueda
        searchInput = findViewById(R.id.search_input);

        // Cargamos los libros originales (sin filtrar)
        cargarLibrosOriginales();

        // Cargamos los libros por defecto
        cargarLibrosFiltrados(null);

        // Configuramos la navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_categories) {
                return true;
            } else if (id == R.id.navigation_home) {
                goToHome();
                return true;
            } else if (id == R.id.navigation_favorites) {
                goToFavourites();
                return true;
            }
            return false;
        });

        // Icono de filtro
        findViewById(R.id.icon_filter).setOnClickListener(v -> mostrarFiltroCategorias());

        // Icono de volver atrás
        findViewById(R.id.icon_back).setOnClickListener(v -> onBackPressed());

        // Implementación de la funcionalidad de búsqueda
        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            String query = searchInput.getText().toString();
            buscarLibros(query);  // Filtra los libros según el texto ingresado
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reinicia los libros cargados sin filtrar cuando vuelves a la actividad
        cargarLibrosFiltrados(null);
    }

    // Método para cargar libros sin filtrar (originales)
    private void cargarLibrosOriginales() {
        listaLibrosOriginales = new ArrayList<>();
        listaLibrosOriginales.add(new Libro("Romeo y Julieta", "Drama", R.drawable.ic_book_romeoyjulieta));
        listaLibrosOriginales.add(new Libro("Hamlet", "Drama", R.drawable.ic_book_hamlet));
        listaLibrosOriginales.add(new Libro("Cumbres borrascosas", "Drama", R.drawable.ic_book_cumbresborrascosas));
        listaLibrosOriginales.add(new Libro("El silencio de lo perdido", "Suspenso", R.drawable.ic_book_elsilenciodeloperdido));
        listaLibrosOriginales.add(new Libro("El resplandor", "Suspenso", R.drawable.ic_book_elresplandor));
        listaLibrosOriginales.add(new Libro("La hora azul", "Suspenso", R.drawable.ic_book_lahoraazul));
        listaLibrosOriginales.add(new Libro("Harry Potter y la Piedra Filosofal", "Fantasia", R.drawable.ic_book_harrypotterylapiedrafilosofal));
        listaLibrosOriginales.add(new Libro("El hobbit", "Fantasia", R.drawable.ic_book_elhobbit));
        listaLibrosOriginales.add(new Libro("Juego de tronos", "Fantasia", R.drawable.ic_book_juegodetronos));
        listaLibrosOriginales.add(new Libro("El principito", "Juvenil", R.drawable.ic_book_elprincipito));
        listaLibrosOriginales.add(new Libro("Los juegos del hambre", "Juvenil", R.drawable.ic_book_losjuegosdelhambre));
        listaLibrosOriginales.add(new Libro("Divergente", "Juvenil", R.drawable.ic_book_divergente));
        listaLibrosOriginales.add(new Libro("1984", "Ciencia ficción", R.drawable.ic_book_1984));
        listaLibrosOriginales.add(new Libro("Fahrenheit 451", "Ciencia ficción", R.drawable.ic_book_fahrenheit451));
        listaLibrosOriginales.add(new Libro("Dune", "Ciencia ficción", R.drawable.ic_book_dune));
        listaLibrosOriginales.add(new Libro("Orgullo y prejuicio", "Romance", R.drawable.ic_book_orgulloyprejuicio));
        listaLibrosOriginales.add(new Libro("Bajo la misma estrella", "Romance", R.drawable.ic_book_bajolamismaestrella));
        listaLibrosOriginales.add(new Libro("After", "Romance", R.drawable.ic_book_after));
        listaLibrosOriginales.add(new Libro("IT", "Terror", R.drawable.id_6));
        listaLibrosOriginales.add(new Libro("Drácula", "Terror", R.drawable.ic_book_dracula));
        listaLibrosOriginales.add(new Libro("La llamada de Cthulhu", "Terror", R.drawable.ic_book_lallamadadecthulhu));
    }

    // Método para cargar libros filtrados por categoría
    private void cargarLibrosFiltrados(String categoria) {
        listaLibrosFiltrados = new ArrayList<>(listaLibrosOriginales); // Copiamos la lista original
        if (categoria != null) {
            List<Libro> librosFiltrados = new ArrayList<>();
            for (Libro libro : listaLibrosOriginales) {
                if (libro.getCategoria().equals(categoria)) {
                    librosFiltrados.add(libro);
                }
            }
            listaLibrosFiltrados = librosFiltrados; // Reemplazamos por la lista filtrada
        }

        // Actualizamos el adaptador de libros
        librosAdapter = new LibrosAdapter(listaLibrosFiltrados);
        recyclerViewLibros.setAdapter(librosAdapter);
    }

    // Método para buscar libros por título
    private void buscarLibros(String query) {
        List<Libro> librosFiltradosPorBusqueda = new ArrayList<>();
        for (Libro libro : listaLibrosOriginales) {
            if (libro.getTitulo().toLowerCase().contains(query.toLowerCase())) {
                librosFiltradosPorBusqueda.add(libro);
            }
        }

        // Actualizamos el RecyclerView con los resultados de la búsqueda
        librosAdapter = new LibrosAdapter(librosFiltradosPorBusqueda);
        recyclerViewLibros.setAdapter(librosAdapter);
    }

    // Método para mostrar el diálogo de filtro de categorías
    private void mostrarFiltroCategorias() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        builder.setTitle("Seleccionar Categoría");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filtro_categorias,  null);
        builder.setView(dialogView);

        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroupCategorias);

        builder.setPositiveButton("Filtrar", (dialog, which) -> {
            int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = dialogView.findViewById(selectedRadioButtonId);
            String categoriaSeleccionada = selectedRadioButton.getText().toString();

            // Filtramos la lista según la categoría seleccionada
            cargarLibrosFiltrados(categoriaSeleccionada);
        });

        builder.setNegativeButton("Cancelar", null);

        builder.create().show();
    }

    // Método para navegar a ProfileActivity
    private void goToProfile() {
        Intent intent = new Intent(Catalogo.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void goToFavourites() {
        Intent intent = new Intent(Catalogo.this, FavoritosActivity.class); // Asegúrate de tener Catalogo.java creado
        startActivity(intent);
    }

    // Método para navegar a la pantalla de favoritos (agregar cuando esté lista)
    private void goToHome() {
        Intent intent = new Intent(Catalogo.this, Home.class); // Asegúrate que tienes ProfileActivity creada
        startActivity(intent);
    }
}











