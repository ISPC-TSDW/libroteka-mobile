package com.example.libroteka;

import java.util.List;

public class Compra {
    private String estado;
    private List<Libro> libros;

    public Compra(String estado, List<Libro> libros) {
        this.estado = estado;
        this.libros = libros;
    }

    public String getEstado() { return estado; }
    public List<Libro> getLibros() { return libros; }
}