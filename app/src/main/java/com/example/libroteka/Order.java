package com.example.libroteka;

import com.example.libroteka.data.BookResponse;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Order {
    @SerializedName("id_Order")
    private Integer idOrder;
    private String date;
    private List<BookResponse> books;
    private String total;
    private Integer books_amount;
    private String address;
    private String city;
    private String telephone;
    private String dni;
    private String preference_id;
    @SerializedName("id_Order_Status")
    private Integer id_Order_Status;
    @SerializedName("id_User")
    private String id_User;


    public Integer getIdOrder() { return idOrder; }
    public String getDate() { return date; }
    public List<BookResponse> getBooks() { return books; }
    public String getTotal() { return total; }
    public Integer getBooks_amount() { return books_amount; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getTelephone() { return telephone; }
    public String getDni() { return dni; }
    public String getPreferenceId() { return preference_id; }
    public Integer getId_Order_Status() { return id_Order_Status; }
    public String getId_User() { return id_User; }
}
