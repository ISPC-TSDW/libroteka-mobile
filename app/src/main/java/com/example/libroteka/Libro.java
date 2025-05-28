package com.example.libroteka;

public class Libro {
    private final String titulo;
    private final int imagenResId;
    private final String categoria;
    private String detalle;
    private double precio;


    // Constructor que ahora incluye el parámetro de categoría
    public Libro(String titulo, String categoria, int imagenResId){//, String detalle, double precio) {
        this.titulo = titulo;
        this.imagenResId = imagenResId;
        this.categoria = categoria;
        //this.detalle = detalle;
        //this.precio = precio;
    }

    // Getters
    public String getTitulo() {
        return titulo;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDetalle() { return detalle; }

    public double getPrecio() { return precio; }

}



