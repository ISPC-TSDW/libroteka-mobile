package com.example.libroteka;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;

    public OrderAdapter(List<Order> orders) {
        this.orderList = orders != null ? orders : new ArrayList<>();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(v);
    }    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        // Format date (assuming it comes in ISO format)
        String dateStr = "N/A";
        if (order.getDate() != null) {
            try {
                // Extract just the date part (before the T)
                String[] dateParts = order.getDate().split("T");
                if (dateParts.length > 0) {
                    dateStr = dateParts[0];
                }
            } catch (Exception e) {
                dateStr = order.getDate();
            }
        }
        holder.tvFecha.setText("Fecha: " + dateStr);

        Integer booksAmount = order.getBooks_amount();
        holder.tvLibros.setText("Cantidad de libros: " + (booksAmount != null ? booksAmount : 0));

        // Handle total as string from API
        String totalStr = order.getTotal();
        holder.tvTotal.setText("Total: $" + (totalStr != null ? totalStr : "0.00"));

        // Set order status text
        String status;
        Integer orderStatus = order.getId_Order_Status();
        if (orderStatus != null) {
            switch(orderStatus) {
                case 1:
                    status = "Pendiente";
                    break;
                case 2:
                    status = "Procesando";
                    break;
                case 3:
                    status = "Enviado";
                    break;
                case 4:
                    status = "Entregado";
                    break;
                case 5:
                    status = "Cancelado";
                    break;
                default:
                    status = "Desconocido";
            }
        } else {
            status = "Desconocido";
        }
        holder.tvStatus.setText("Estado: " + status);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvLibros, tvTotal, tvStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvLibros = itemView.findViewById(R.id.tvLibros);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
