package com.perusudroid.shoppingcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.perusudroid.shoppingcart.model.Data;
import com.perusudroid.shoppingcart.model.ProductResponse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements IListener {

    private RecyclerView recyclerView;
    private ShoppingAdapter shoppingAdapter;
    private TextView tvCost;
    private ConstraintLayout bottomLay;
    List<Data> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCost = findViewById(R.id.tvCost);
        bottomLay = findViewById(R.id.bottomLay);
        recyclerView = findViewById(R.id.recyclerView);
        // recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        bottomLay.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(MainActivity.this, CartActivity.class), 100);
                    }
                }
        );

        setAdapter();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null) {

            Bundle mBundle = data.getExtras();
            if (mBundle != null &&
                    mBundle.getBoolean("refresh", false)) {
                if (CartHashMap.getInstance().getProductsSize(this) > 0) {
                    onUpdated(CartHashMap.getInstance().getSelectedProductCost(this));
                    if (shoppingAdapter != null) {

                        List<Data> mData = getComparedList(CartHashMap.getInstance().getSelectedProducts(this));

                        Log.d("MainActivity", "onActivityResult: " + new Gson().toJson(mData));

                        shoppingAdapter.refresh(mData, this);
                    }
                }
            }


        }
    }

    private List<Data> getComparedList(List<Data> selectedProducts) {

        List<Data> filteredList = new ArrayList<>();

        if (selectedProducts != null && mList != null) {
            for (int i = 0; i < mList.size(); i++) {

                for (int y = 0; y < selectedProducts.size(); y++) {

                    if (selectedProducts.get(y).getProduct_id().equals(mList.get(i).getProduct_id())) {
                        filteredList.add(selectedProducts.get(y));
                    }else{
                        filteredList.add(mList.get(i));
                    }

                }

            }
        }

        return filteredList;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setAdapter() {

        ProductResponse productResponse = new Gson().fromJson(readFromFile(), ProductResponse.class);
        for (int i = 0; i < productResponse.getData().size(); i++) {
            productResponse.getData().get(i).set_selected(false);
        }

        mList = productResponse.getData();

        shoppingAdapter = new ShoppingAdapter(mList, this);
        recyclerView.setAdapter(shoppingAdapter);

    }


    private String readFromFile() {
        String ret = "";
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.sample_products);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }
            inputStream.close();
            ret = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            Log.e("readFromFile", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("readFromFile", "Can not read file: " + e.toString());
        }
        return ret;
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
}
