package com.example.libroteka;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("orders")
    Call<List<Order>> getOrdersByUser(@Query("id_User_id") int id_User_id);  // Ajusta el endpoint según tu backend
}
