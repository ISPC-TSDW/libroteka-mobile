package com.example.libroteka;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.libroteka.data.ApiManager;
import com.example.libroteka.data.MyApp;

public class ProductoActivity extends AppCompatActivity {

    private TextView txtTitle;
    private TextView txtAuthor;
    private TextView txtDescription;
    private ImageView imgBook;
    private ApiManager apiManager;
    private boolean isFavorite = false;
    private String userId;
    private Integer bookId;
    private SessionManager sessionManager;
    private Button webButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        // Referencias UI
        txtTitle = findViewById(R.id.txtTitle);
        txtAuthor = findViewById(R.id.txtAuthor);
        txtDescription = findViewById(R.id.txtDescription);
        imgBook = findViewById(R.id.srcImg);
        ImageButton btnFavorite = findViewById(R.id.btnFavorite);
        webButton = findViewById(R.id.webButton);
        // Obtener email de sesión
        MyApp app = (MyApp) getApplicationContext();
        userId = app.getUserEmail();

        // Obtener datos del intent
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String author = intent.getStringExtra("author");
            String description = intent.getStringExtra("description");
            Float price = intent.getFloatExtra("price", 0.0f);
            Float avgRating = intent.getFloatExtra("avg_rating", 0.0f);
            String imageUrl = intent.getStringExtra("image_url");
            bookId = intent.getIntExtra("book_id", -1);

            // Setear los datos en la vista
            txtTitle.setText(title != null ? title : "Título desconocido");
            txtAuthor.setText(author != null && !author.isEmpty() ? author : "Unknown Author");
            txtDescription.setText(description != null ? description : "Sin descripción");

            // Cargar imagen con Glide
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_book_dracula)
                        .error(R.drawable.ic_book_dracula)
                        .into(imgBook);
            } else {
                imgBook.setImageResource(R.drawable.ic_book_dracula);
            }
        }

        // Inicializar API y obtener estado de favorito
        sessionManager = new SessionManager(getApplicationContext());
        apiManager = new ApiManager(sessionManager);
        getFavoriteStatus();

        // Toggle favorito
        btnFavorite.setOnClickListener(v -> toggleFavorite());

        webButton.setOnClickListener(v -> {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                startActivity(browserIntent);
            } catch (Exception e) {
                Toast.makeText(ProductoActivity.this, "No se pudo abrir el enlace", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static final String URL = "https://libroteka-frontend-prod.onrender.com/";


    private void getFavoriteStatus() {
        apiManager.getFavoriteStatus(userId, bookId, new ApiManager.ApiCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean response) {
                isFavorite = response;
                updateFavoriteIcon();
            }

            @Override
            public void onFailure(String errorMessage) {
            }
        });
    }

    private void toggleFavorite() {
        apiManager.toggleFavorite(userId, bookId, new ApiManager.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void response) {
                isFavorite = true;
                updateFavoriteIcon();
                Toast.makeText(ProductoActivity.this, "Agregado a Favoritos", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ProductoActivity.this, "Ya tienes este libro en favoritos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFavoriteIcon() {
        ImageButton btnFavorite = findViewById(R.id.btnFavorite);
        if (isFavorite) {
            btnFavorite.setBackgroundResource(R.drawable.ic_heart);
        } else {
            btnFavorite.setBackgroundResource(R.drawable.ic_heart_outline);
        }
    }
}
