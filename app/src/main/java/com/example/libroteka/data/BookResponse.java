package com.example.libroteka.data;

import android.content.Context;
import com.google.gson.annotations.SerializedName;

public class BookResponse {
    private Integer id_Book;

    @SerializedName("id_Author")
    private Integer id_Author;

    private Integer id_Genre;
    private Integer id_Editorial;
    private String title;
    private String description;
    private Float price;
    private Integer stock;
    private Float avg_rating;
    private String image;
    private String ISBN;
    private Integer year;

    public BookResponse(Integer id_Book, String title, Integer id_Author, Integer id_Genre,
                        Integer id_Editorial, String description, Float price, Integer stock,
                        Float avg_rating, String image, String ISBN, Integer year) {
        this.id_Book = id_Book;
        this.title = title;
        this.id_Author = id_Author;
        this.id_Genre = id_Genre;
        this.id_Editorial = id_Editorial;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.avg_rating = avg_rating;
        this.image = image;
        this.ISBN = ISBN;
        this.year = year;
    }

    // Getters
    public Integer getId_Book() {
        return id_Book;
    }

    public Integer getId_Author() {
        return id_Author;
    }

    public Integer getId_Genre() {
        return id_Genre;
    }

    public Integer getId_Editorial() {
        return id_Editorial;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Float getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public Float getAvg_rating() {
        return avg_rating;
    }

    public String getImage() {
        return image;
    }

    public String getISBN() {
        return ISBN;
    }

    public Integer getYear() {
        return year;
    }
}
