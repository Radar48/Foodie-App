package com.example.moblayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(FoodItem item);
    }

    private List<FoodItem> foodList;
    private final OnItemClickListener listener;

    public FoodAdapter(List<FoodItem> foodList, OnItemClickListener listener) {
        this.foodList = foodList;
        this.listener = listener;
    }

    public void setFilteredList(List<FoodItem> filteredList) {
        this.foodList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem item = foodList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(String.format("KSh %.0f", item.getPrice()));

        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(holder.ivFood);

        // Item click for details where quantity & order happen
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFood;
        TextView tvName, tvPrice;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFood = itemView.findViewById(R.id.ivFoodItem);
            tvName = itemView.findViewById(R.id.tvFoodName);
            tvPrice = itemView.findViewById(R.id.tvFoodPrice);
        }
    }
}
