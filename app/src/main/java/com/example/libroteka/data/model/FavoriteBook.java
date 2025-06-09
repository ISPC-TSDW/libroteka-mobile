package com.example.libroteka.data.model;

import com.example.libroteka.data.BookResponse;
import com.example.libroteka.data.FavoriteRequest;

public class FavoriteBook {
    public FavoriteRequest favorite;
    public BookResponse book;

    public FavoriteBook(FavoriteRequest favorite, BookResponse book) {
        this.favorite = favorite;
        this.book = book;
    }
}