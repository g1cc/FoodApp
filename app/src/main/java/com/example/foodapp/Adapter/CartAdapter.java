package com.example.foodapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Domain.CartDomain;
import com.example.foodapp.Helper.CartManager;
import com.example.foodapp.R;
import com.example.foodapp.iface.ChangeNumberItemsListener;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>
{
    ArrayList<CartDomain> cartDomains;
    private ChangeNumberItemsListener changeNumberItemsListener;
    CartManager cartManager;

    public CartAdapter(ArrayList<CartDomain> cartDomains, ChangeNumberItemsListener changeNumberItemsListener, Context context)
    {
        this.cartDomains = cartDomains;
        this.cartManager = new CartManager(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(cartDomains.get(position).getTitle());
        holder.description.setText(cartDomains.get(position).getIngredients());
        holder.price.setText(String.valueOf(cartDomains.get(position).getPrice()));
        holder.qtyText.setText(String.valueOf(cartDomains.get(position).getFoodQTY()));

        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(cartDomains.get(position).getImage(), "drawable", holder.itemView.getContext().getPackageName());

        holder.pizzaImage.setImageResource(drawableResourceId);

        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartManager.plusNumberFood(cartDomains, position, new ChangeNumberItemsListener() {
                    @Override
                    public void changed() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.changed();
                    }
                });
            }
        });

        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                cartManager.minusNumberFood(cartDomains, position, new ChangeNumberItemsListener() {
                    @Override
                    public void changed() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.changed();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartDomains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, price, description, plusButton, minusButton, qtyText;
        ImageView pizzaImage;
        ConstraintLayout constraintLayout;
        int qty = 1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.vhcTitle);
            price = itemView.findViewById(R.id.vhcPrice);
            description = itemView.findViewById(R.id.vhcDescription);
            pizzaImage = itemView.findViewById(R.id.vhcImage);
            constraintLayout = itemView.findViewById(R.id.cartConstLay);
            plusButton = itemView.findViewById(R.id.plusButton);
            minusButton = itemView.findViewById(R.id.minusButton);
            qtyText = itemView.findViewById(R.id.qtyText);
        }
    }


}
