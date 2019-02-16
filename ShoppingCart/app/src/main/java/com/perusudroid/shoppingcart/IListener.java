package com.perusudroid.shoppingcart;

import com.perusudroid.shoppingcart.model.Data;

public interface IListener {

    void onUpdated(Double cost);

    void onImageClicked(Data tag);

    void showAlert(Data mData, int adapterPosition);

    void onRemoved(Double selectedProductCost, int size, int productsSize);
}
