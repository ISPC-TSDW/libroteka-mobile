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
        faqList.add(new FaqItem("¿Cómo puedo buscar un libro?", "Puedes buscar un libro desde la barra de búsqueda o filtrando por categorías."));
        faqList.add(new FaqItem("¿Cómo agrego un libro a favoritos?", "Haz clic en el ícono de corazón junto al libro para agregarlo a tu lista de favoritos."));
        faqList.add(new FaqItem("¿Cómo recupero mi contraseña?", "En la pantalla de inicio de sesión, selecciona '¿Olvidaste tu contraseña?' y sigue los pasos."));
        faqList.add(new FaqItem("¿Cómo contacto al soporte?", "Desde tu perfil, selecciona 'Contacto' y completa el formulario para comunicarte con nuestro equipo."));
        faqList.add(new FaqItem("¿Cómo compro un libro?", "Selecciona el libro que deseas y haz clic en 'Comprar'. Serás redirigido a la web para seguir los pasos para completar el pago."));
        faqList.add(new FaqItem("¿Qué métodos de pago aceptan?", "Aceptamos tarjetas de crédito, débito y Mercado Pago."));
        faqList.add(new FaqItem("¿Cómo veo mi historial de compras?", "En tu perfil, selecciona 'Historial de compras' para ver todas tus compras realizadas."));
        faqList.add(new FaqItem("¿Cómo edito mi perfil?", "En tu perfil, haz clic en 'Editar perfil' para modificar tus datos personales."));
        faqList.add(new FaqItem("¿Cómo elimino mi cuenta?", "Contáctanos a través del formulario de contacto para solicitar la eliminación de tu cuenta o bien en editar perfil hay una opción para eliminar tu cuenta."));
        faqList.add(new FaqItem("¿Por qué no encuentro un libro?", "Puede que el libro no esté disponible aún. Intenta buscar por título, autor o categoría."));
        faqList.add(new FaqItem("¿Qué hago si la app no funciona bien?", "Intenta cerrar y volver a abrir la app. Si el problema persiste, contacta a soporte."));

        // Configura el adaptador
        FaqAdapter faqAdapter = new FaqAdapter(faqList);
        faqRecyclerView.setAdapter(faqAdapter);
    }

    // Método para navegar a Home
    private void goToHome() {
        //Intent intent = new Intent(FaqActivity.this, Home.class);
        //startActivity(intent);
    }

    // Método para navegar a la pantalla de categorías
    private void goToCategories() {
        Intent intent = new Intent(FaqActivity.this, Catalogo.class);
        startActivity(intent);
    }

    // Método para navegar a la pantalla de favoritos (agregar cuando esté lista)
    private void goToFavorites() {
        // Agregar cuando esté lista la pantalla de favoritos
    }
}
