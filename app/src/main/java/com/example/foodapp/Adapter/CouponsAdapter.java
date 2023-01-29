package com.example.foodapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Domain.CouponsDomain;
import com.example.foodapp.R;

import java.util.ArrayList;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.ViewHolder>{
    ArrayList<CouponsDomain> couponsDomains;

    public CouponsAdapter(ArrayList<CouponsDomain> couponsDomains)
    {

        this.couponsDomains = couponsDomains;
    }

    @NonNull
    @Override
    public CouponsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_coupons, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.title.setText(couponsDomains.get(position).getTitle());
        holder.coupon.setText(couponsDomains.get(position).getCoupon());
        holder.newPrice.setText(String.valueOf(couponsDomains.get(position).getNewPrice()));
        holder.oldPrice.setText(String.valueOf(couponsDomains.get(position).getOldPrice()));
    }

    @Override
    public int getItemCount() {
        return couponsDomains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView title, coupon, newPrice, oldPrice;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cpTitle);
            coupon = itemView.findViewById(R.id.couponText);
            newPrice = itemView.findViewById(R.id.cpPriceNew);
            oldPrice = itemView.findViewById(R.id.cpPriceOld);
        }
    }
}
