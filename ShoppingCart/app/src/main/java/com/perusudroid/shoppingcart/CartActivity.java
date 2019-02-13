package com.perusudroid.shoppingcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.perusudroid.shoppingcart.model.Data;

import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements IListener {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView tvCost;
    private ConstraintLayout bottomLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        bindViews();
        doSetLocalData();
    }

    private void bindViews() {
        recyclerView = findViewById(R.id.recyclerView);
        tvCost = findViewById(R.id.tvCost);
        bottomLay = findViewById(R.id.bottomLay);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void doSetLocalData() {
        if (CartHashMap.getInstance().isNonEmpty(this)) {

            List<Data> mList = CartHashMap.getInstance().getSelectedProducts(this);

            Log.d("CartActivity", "doSetLocalData: listToParse " + new Gson().toJson(mList));

            cartAdapter = new CartAdapter(mList, this);
            recyclerView.setAdapter(cartAdapter);

        } else {
            Toast.makeText(this, "Cart Empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CartHashMap.getInstance().getProductsSize(this) > 0) {
            onUpdated(CartHashMap.getInstance().getSelectedProductCost(this));
        }

    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("refresh", true);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onUpdated(Double cost) {
        if (cost > 0) {
            tvCost.setText(String.format(Locale.getDefault(), "%s%s", getString(R.string.Rs), cost));
            bottomLay.setVisibility(View.VISIBLE);
        } else {
            bottomLay.setVisibility(View.GONE);
        }
    }

    @Override
    public void onImageClicked(Data tag) {

    }
}
