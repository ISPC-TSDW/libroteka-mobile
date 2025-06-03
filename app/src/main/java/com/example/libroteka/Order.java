package com.example.libroteka;

public class Order {
    private String date;
    private String books;
    private Double total;
    private Integer books_amount;
    private Integer id_Order_Status_id;
    private Integer id_User_id;

    public String getDate() { return date; }
    public String getBooks() { return books; }
    public Double getTotal() { return total; }
    public Integer getBooks_amount() { return books_amount; }
    public Integer getId_Order_Status() { return id_Order_Status_id; }
    public Integer getId_User() { return id_User_id; }
}
