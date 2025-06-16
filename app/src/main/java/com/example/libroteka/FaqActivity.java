package com.example.libroteka;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FaqActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        // Inicializa el RecyclerView
        RecyclerView faqRecyclerView = findViewById(R.id.faqRecyclerView);
        faqRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Preguntas frecuentes
        List<FaqItem> faqList = new ArrayList<>();
        faqList.add(new FaqItem("¿Cómo puedo buscar un libro?", "Puedes buscar un libro desde la barra de búsqueda o filtrando por categorías..."));
        faqList.add(new FaqItem("¿Cómo puedo agregar un libro a mi lista de favoritos?", "Para agregar un libro a tu lista de favoritos, simplemente haz clic en el ícono de corazón que aparece junto al título del libro. Este se añadirá automáticamente a tu lista de favoritos dentro de tu perfil."));
        faqList.add(new FaqItem("¿Puedo leer libros sin conexión a internet?", "Sí, puedes descargar libros para leerlos sin conexión. Simplemente selecciona la opción de descarga en el libro que quieras leer y estará disponible en tu dispositivo para acceso sin conexión."));
        faqList.add(new FaqItem("¿Cómo puedo compartir un libro con mis amigos?", "Para compartir un libro, selecciona el ícono de compartir dentro de la página del libro. Podrás enviarlo a tus amigos a través de redes sociales, correo electrónico o cualquier otra aplicación de mensajería compatible."));
        faqList.add(new FaqItem("¿Cómo puedo dejar una reseña de un libro que he leído?", "Después de haber leído un libro, ve a la sección de comentarios y selecciona la opción 'Dejar una reseña'. Allí podrás calificar el libro y escribir tus pensamientos sobre él."));
        faqList.add(new FaqItem("¿Qué hago si no encuentro un libro en la búsqueda?", "Si no puedes encontrar un libro, asegúrate de que estás utilizando las palabras clave correctas. También puedes probar filtrando por categorías o autor. Si aún no lo encuentras, es posible que el libro no esté disponible en nuestra biblioteca."));

        // Configura el adaptador
        FaqAdapter faqAdapter = new FaqAdapter(faqList);
        faqRecyclerView.setAdapter(faqAdapter);
    }

    // Método para navegar a Home
    private void goToHome() {
        Intent intent = new Intent(FaqActivity.this, Home.class);
        startActivity(intent);
    }

    // Método para navegar a la pantalla de categorías
    private void goToCategories() {
        Intent intent = new Intent(FaqActivity.this, Catalogo.class);
        startActivity(intent);
    }

    // Método para navegar a la pantalla de favoritos (agregar cuando esté lista)
    private void goToFavorites() {
        Intent intent = new Intent(FaqActivity.this, FaqActivity.class);
        startActivity(intent);
        // Agregar cuando esté lista la pantalla de favoritos
    }
}
