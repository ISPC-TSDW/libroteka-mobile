package com.example.libroteka;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.FaqViewHolder> {
    private List<FaqItem> faqList;
    private int expandedPosition = -1; // Solo una expandida a la vez

    public FaqAdapter(List<FaqItem> faqList) {
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public FaqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_faq, parent, false);
        return new FaqViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqViewHolder holder, int position) {
        FaqItem faq = faqList.get(position);
        holder.questionTextView.setText(faq.getQuestion());
        holder.answerTextView.setText(faq.getAnswer());

        final boolean isExpanded = position == expandedPosition;
        holder.answerTextView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            if (expandedPosition == position) {
                expandedPosition = -1;
            } else {
                expandedPosition = position;
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    static class FaqViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView, answerTextView;

        FaqViewHolder(View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            answerTextView = itemView.findViewById(R.id.answerTextView);
        }
    }
}