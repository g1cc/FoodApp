package com.example.foodapp.Domain;

public class CouponsDomain {

    private String title, coupon;
    private int oldPrice, newPrice;

    public CouponsDomain(String title, String coupon, int oldPrice, int newPrice) {
        this.title = title;
        this.coupon = coupon;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public int getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(int oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(int newPrice) {
        this.newPrice = newPrice;
    }
}
