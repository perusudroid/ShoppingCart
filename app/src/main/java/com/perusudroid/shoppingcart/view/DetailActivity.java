package com.perusudroid.shoppingcart.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.perusudroid.numberswitcher.NumberSwitch;
import com.perusudroid.shoppingcart.helper.CartHashMap;
import com.perusudroid.shoppingcart.R;
import com.perusudroid.shoppingcart.adapter.IListener;
import com.perusudroid.shoppingcart.model.Data;

public class DetailActivity extends AppCompatActivity implements IListener {

    private NumberSwitch numberSwitch;
    private ImageView ivPic;
    private TextView tvName, tvDesc, tvCost;
    private Data data;
    private CartHashMap cartHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        bindViews();
        getInputs(getIntent().getExtras());
        setAssets();
    }

    private void getInputs(Bundle extras) {
        if (extras != null) {
            data = (Data) extras.getSerializable("data");
        }
    }

    private void bindViews() {
        numberSwitch = findViewById(R.id.numberSwitch);
        ivPic = findViewById(R.id.ivPic);
        tvName = findViewById(R.id.tvName);
        tvDesc = findViewById(R.id.tvDesc);
        tvCost = findViewById(R.id.tvCost);
    }

    private void setAssets() {

        getSupportActionBar().setTitle("Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (data != null) {

            cartHashMap = new CartHashMap();
            cartHashMap.setListener(this);

            tvName.setText(data.getName());
            tvDesc.setText(data.getProduct_description());
            numberSwitch.setCount(data.getProduct_selected_qty());
            Glide.
                    with(this).
                    load(data.getImage()).
                    into(ivPic);
        }

        numberSwitch.setValueChangedListener(
                new NumberSwitch.ValueChangedListener() {
                    @Override
                    public void onValueChanged(int value, NumberSwitch.ValueType valueType) {
                        data.setProduct_selected_qty(value);
                        data.setProduct_selected_mrp_price(data.getProduct_mrp_price() * value);
                        CartHashMap.getInstance().addrUpdateProductInfo(data, DetailActivity.this);
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdated(Double cost) {
        tvCost.setText(String.valueOf(cost));
    }

    @Override
    public void onImageClicked(Data tag) {

    }

    @Override
    public void showAlert(Data mData, int adapterPosition) {

    }

    @Override
    public void onRemoved(Double selectedProductCost, int size, int productsSize) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("refresh", true);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
