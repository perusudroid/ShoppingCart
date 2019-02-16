package com.perusudroid.shoppingcart;

import android.content.Context;
import android.util.Log;

import com.carteasy.v1.lib.Carteasy;
import com.perusudroid.shoppingcart.model.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartHashMap {


    private static CartHashMap cartHashMap;
    private Carteasy carteasy = new Carteasy();
    private static String TAG = CartHashMap.class.getSimpleName();
    private IListener iListener;

    public void setListener(IListener iListener) {
        this.iListener = iListener;
    }

    public static CartHashMap getInstance() {

        if (cartHashMap == null) {
            cartHashMap = new CartHashMap();
        }
        return cartHashMap;
    }

    public int getProductsSize(Context context) {
        if (carteasy.ViewAll(context) != null) {
            return carteasy.ViewAll(context).size();
        }
        return 0;
    }


    public List<Data> getSelectedProducts(Context context) {

        Map<Integer, Map> data = carteasy.ViewAll(context);

        List<Data> mList = new ArrayList<>();
        if (data != null && !data.isEmpty() && data.entrySet().size() > 0) {
            for (Map.Entry<Integer, Map> entry : data.entrySet()) {

                Map<String, String> innerdata = entry.getValue();
                Data mData = new Data();
                for (Map.Entry<String, String> innerEntry : innerdata.entrySet()) {
                    Log.d(TAG, "CREPPY: innerEntry KEY " + innerEntry.getKey() + " VALUE " + innerEntry.getValue());

                    String product_id = innerEntry.getKey();

                    Log.d(TAG, "doSetLocalData: product_id " + product_id);


                    switch (product_id) {
                        case "image":
                            mData.setImage(innerdata.get("image"));
                            break;
                        case "product_mrp_price":
                            mData.setProduct_mrp_price(getDoub(innerdata.get("product_mrp_price")));
                            break;
                        case "product_selected_mrp_price":
                            mData.setProduct_selected_mrp_price(getDoub(innerdata.get("product_selected_mrp_price")));
                            break;
                        case "name":
                            mData.setName(innerdata.get("name"));
                            break;
                        case "is_selected":
                            mData.set_selected(Boolean.parseBoolean(innerdata.get("is_selected")));
                            break;
                        case "product_id":
                            mData.setProduct_id(getInt(innerdata.get("product_id")));
                            break;
                        case "product_selected_qty":
                            mData.setProduct_selected_qty(getInt(innerdata.get("product_selected_qty")));
                            break;
                        case "product_description":
                            mData.setProduct_description(innerdata.get("product_description"));
                            break;
                    }
                }
                mList.add(mData);
            }
        }

        return mList;

    }

    private Integer getInt(String str) {
        return Integer.parseInt(str);
    }

    private Double getDoub(String str) {
        return Double.parseDouble(str);
    }

    public Double getSelectedProductCost(Context context) {

        Map<Integer, Map> data = carteasy.ViewAll(context);
        double costToShow = 0.0;
        if (data != null && !data.isEmpty() && data.entrySet().size() > 0) {
            for (Map.Entry<Integer, Map> entry : data.entrySet()) {
                Map<String, String> innerdata = entry.getValue();
                for (Map.Entry<String, String> innerEntry : innerdata.entrySet()) {
                    Log.d(TAG, "onCreate: innerEntry KEY " + innerEntry.getKey() + " VALUE " + innerEntry.getValue());
                    if (innerEntry.getKey().equals("product_selected_mrp_price") &&
                            innerEntry.getValue() != null) {
                        costToShow += Double.parseDouble(innerEntry.getValue());
                    }
                }
            }
        }
        return costToShow;
    }

    public Boolean isNonEmpty(Context context) {
        Map<Integer, Map> data = carteasy.ViewAll(context);
        return (data != null && !data.isEmpty() && data.entrySet().size() > 0);
    }


    public void addrUpdateProductInfo(Data data, Context context) {

        Log.d(TAG, "addrUpdateProductInfo: data is_selected "+ data.isSelected());

        Map<Integer, Map> hashData = carteasy.ViewAll(context);
        Boolean notExists = false;


        if (hashData != null && hashData.size() != 0) {

            for (Map.Entry<Integer, Map> entry : hashData.entrySet()) {
                //Retrieve the values of the Map by starting from index 0 - zero
                //Get the sub values of the Map
                Map<String, String> innerdata = entry.getValue();

                if (innerdata.containsKey("product_id")) {

                    String p_id = innerdata.get("product_id");

                    if (p_id.equals(String.valueOf(data.getProduct_id()))) {

                        Log.d(TAG, "addrUpdateProductInfo: crap exists updated");

                        carteasy.update(p_id, "image", data.getImage(), context);
                        carteasy.update(p_id, "is_selected", data.isSelected(), context);
                        carteasy.update(p_id, "product_selected_qty", data.getProduct_selected_qty(), context);
                        carteasy.update(p_id, "product_selected_mrp_price", data.getProduct_selected_mrp_price(), context);

                        if (iListener != null) {
                            iListener.onUpdated(getSelectedProductCost(context));
                        }

                    } else {
                        saveStuffs(data, context);
                        Log.d(TAG, "addrUpdateProductInfo: crap no exists saved");
                    }

                }

                for (Map.Entry<String, String> innerEntry : innerdata.entrySet()) {
                    Log.d(TAG, "addrUpdateProductInfo: printHash key " + innerEntry.getKey() + " value " + innerEntry.getValue());


                }
            }
        } else {
            data.setProduct_selected_qty(1);
            saveStuffs(data, context);
        }


    }

    private void saveStuffs(Data data, Context context) {

        Carteasy carteasy = new Carteasy();
        carteasy.add(data.getProduct_id().toString(), "product_id", data.getProduct_id());
        carteasy.add(data.getProduct_id().toString(), "product_mrp_price", data.getProduct_mrp_price());
        carteasy.add(data.getProduct_id().toString(), "product_selected_mrp_price", data.getProduct_selected_mrp_price());
        carteasy.add(data.getProduct_id().toString(), "name", data.getName());
        carteasy.add(data.getProduct_id().toString(), "image", data.getImage());
        carteasy.add(data.getProduct_id().toString(), "is_selected", data.isSelected());
        carteasy.add(data.getProduct_id().toString(), "product_id", data.getProduct_id());
        carteasy.add(data.getProduct_id().toString(), "product_selected_qty", data.getProduct_selected_qty());
        carteasy.add(data.getProduct_id().toString(), "product_description", data.getProduct_description());
        carteasy.commit(context);
        carteasy.persistData(context, true);

        if (iListener != null) {
            iListener.onUpdated(getSelectedProductCost(context));
        }

    }


    public void removeByProductId(Integer product_id, Context context, int adapterPosition) {
        Carteasy cs = new Carteasy();
        cs.RemoveId(String.valueOf(product_id), context);
        if(iListener != null){
            iListener.onRemoved(getSelectedProductCost(context), getProductsSize(context),adapterPosition);
        }
    }

    public void clearCart(Context context) {
        Carteasy cs = new Carteasy();
        cs.clearCart(context);
    }



}
