package com.example.foodapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Domain.FoodDomain;
import com.example.foodapp.R;
import com.example.foodapp.about_food;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder>
{
    ArrayList<FoodDomain> foodDomains;
    public PopularAdapter(ArrayList<FoodDomain> foodDomains) { this.foodDomains = foodDomains; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_popular, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(foodDomains.get(position).getTitle());
//        holder.description.setText(foodDomains.get(position).getIngredients());
        holder.price.setText(String.valueOf(foodDomains.get(position).getPrice()) + " ₽");

        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(foodDomains.get(position).getImage(), "drawable", holder.itemView.getContext().getPackageName());
        holder.foodImage.setImageResource(drawableResourceId);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), about_food.class);
                intent.putExtra("object", foodDomains.get(position));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodDomains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, price;
        ImageView foodImage;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.vhpTitle);
            price = itemView.findViewById(R.id.vhpPrice);
            foodImage = itemView.findViewById(R.id.vhpImage);
            constraintLayout = itemView.findViewById(R.id.vhpConstLay);
        }
    }
}
