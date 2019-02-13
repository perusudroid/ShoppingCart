package com.perusudroid.shoppingcart.model;

import java.io.Serializable;

/**
 * Awesome Pojo Generator
 */
public class Data implements Serializable {
    private String image;
    private Double product_mrp_price;
    private Double product_selected_mrp_price;
    private String name;
    private Integer product_id;
    private Integer product_selected_qty;
    private String product_description;
    private Boolean is_selected;

    public Boolean isSelected() {
        return is_selected;
    }

    public void set_selected(Boolean is_selected) {
        this.is_selected = is_selected;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getProduct_mrp_price() {
        return product_mrp_price;
    }

    public void setProduct_mrp_price(Double product_mrp_price) {
        this.product_mrp_price = product_mrp_price;
    }

    public Double getProduct_selected_mrp_price() {
        return product_selected_mrp_price;
    }

    public void setProduct_selected_mrp_price(Double product_selected_mrp_price) {
        this.product_selected_mrp_price = product_selected_mrp_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Integer getProduct_selected_qty() {
        return product_selected_qty;
    }

    public void setProduct_selected_qty(Integer product_selected_qty) {
        this.product_selected_qty = product_selected_qty;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }
}