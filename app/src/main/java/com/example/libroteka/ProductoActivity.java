package com.example.libroteka;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.libroteka.data.ApiManager;
import com.example.libroteka.data.MyApp;

public class ProductoActivity extends AppCompatActivity {

    private TextView txtTitle;
    private TextView txtAuthor;
    private TextView txtDescription;
    private ApiManager apiManager;
    private boolean isFavorite = false; // Track favorite status
    private String userId; // Set this with the current user ID
    private Integer bookId; // Store the book ID from Intent
    private ImageView imgBook; // Assuming you have an ImageView to display the book image
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        MyApp app = (MyApp) getApplicationContext();
        String email = app.getUserEmail();

        txtTitle = findViewById(R.id.txtTitle);
        txtAuthor = findViewById(R.id.txtAuthor);
        txtDescription = findViewById(R.id.txtDescription);
        ImageButton btnFavorite = findViewById(R.id.btnFavorite); // Ensure there's an ImageButton in your layout
        imgBook = findViewById(R.id.srcImg); // Ensure you have an ImageView with this ID in your layout

        // Retrieve data from the Intent
        Intent intent = getIntent();
        if (intent != null) {
            // Retrieve the data passed from the previous activity
            String title = intent.getStringExtra("title");
            String author = intent.getStringExtra("author");
            String description = intent.getStringExtra("description");
            Float price = intent.getFloatExtra("price", 0.0f);
            Float avgRating = intent.getFloatExtra("avg_rating", 0.0f);
            bookId = intent.getIntExtra("book_id", -1);// Store the book ID from the intent
            userId = email;// You can dynamically set this to the actual user if necessary

            // Get the image resource ID
            int imageResId = intent.getIntExtra("image_res_id", -1);

            // Set the book image if available
            if (imageResId != -1) {
                imgBook.setImageResource(imageResId);
            } else {
                imgBook.setImageResource(R.drawable.ic_book_dracula); // Set a default image if no image is found
            }

            // Set the book details to the corresponding TextViews
            txtTitle.setText(title != null ? title : "Unknown Title");
            txtAuthor.setText(author != null ? author : "Unknown Author");
            txtDescription.setText(description != null ? description : "No description available");

            // You can display the price and rating somewhere in the UI if needed
            // Example: txtPrice.setText("$" + price.toString());
            // Example: txtRating.setText(avgRating.toString());
        }

        // Initialize the ApiManager
        sessionManager = new SessionManager(getApplicationContext());
        apiManager = new ApiManager(sessionManager);
        getFavoriteStatus(); // Check if the book is already in favorites

        // Set up favorite button click listener
        btnFavorite.setOnClickListener(v -> toggleFavorite()); // Toggle favorite status when clicked
    }

    private void getFavoriteStatus() {
        apiManager.getFavoriteStatus(userId, bookId, new ApiManager.ApiCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean response) {
                isFavorite = response; // Update favorite status
                updateFavoriteIcon(); // Update the icon based on status
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle the error appropriately (e.g., show a toast)
            }
        });
    }


    private void toggleFavorite() {

            apiManager.toggleFavorite(userId, bookId, new ApiManager.ApiCallback<Void>() {
                @Override
                public void onSuccess(Void response) {
                    isFavorite = true; // Update favorite status
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
        // Change the icon color based on the favorite status
        ImageButton btnFavorite = findViewById(R.id.btnFavorite); // Ensure you have a button in your layout
        if (isFavorite) {
            btnFavorite.setBackgroundResource(R.drawable.ic_heart); // Change to filled icon
        } else {
            btnFavorite.setBackgroundResource(R.drawable.ic_heart_outline); // Change to outline icon
        }
    }
}