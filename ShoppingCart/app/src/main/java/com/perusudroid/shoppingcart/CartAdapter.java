package com.perusudroid.shoppingcart;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.perusudroid.shoppingcart.model.Data;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Data> productList;
    private IListener iListener;
    private CartHashMap cartHashMap;

    public void refresh(List<Data> data) {
        this.productList = data;
        notifyDataSetChanged();
    }

    public CartAdapter(List<Data> data, IListener iListener) {
        this.iListener = iListener;
        this.productList = data;
        cartHashMap = CartHashMap.getInstance();
        cartHashMap.setListener(iListener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inflater_cart_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindData(productList.get(i));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivPic;
        private TextView tvCost, tvName;
        private NumberSwitch numberSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bindViews(itemView);
            setAssets();
        }

        private void bindViews(View itemView) {
            ivPic = itemView.findViewById(R.id.ivPic);
            tvCost = itemView.findViewById(R.id.tvCost);
            tvName = itemView.findViewById(R.id.tvName);
            numberSwitch = itemView.findViewById(R.id.numberSwitch);
        }

        private void setAssets() {

            numberSwitch.setValueChangedListener(
                    new NumberSwitch.ValueChangedListener() {
                        @Override
                        public void onValueChanged(int value, NumberSwitch.ValueType valueType) {
                            Data mData = (Data) itemView.getTag();

                            Log.d("CartAdapter", "onValueChanged: value "+ value);

                            if (value > 0) {

                                Log.d("CartAdapter", "onValueChanged: cartValue "+ new Gson().toJson(mData));

                                mData.set_selected(true);
                                mData.setProduct_selected_qty(value);
                                mData.setProduct_selected_mrp_price(mData.getProduct_mrp_price() * value);

                                CartHashMap.getInstance().addrUpdateProductInfo(mData, itemView.getContext());
                            }else{
                                iListener.showAlert(mData, getAdapterPosition());
                            }
                        }
                    }
            );

        }

        public void bindData(Data data) {

            itemView.setTag(data);

            tvName.setText(String.valueOf(data.getName()));
            tvCost.setText(String.valueOf(data.getProduct_selected_mrp_price()));

            numberSwitch.setCount(data.getProduct_selected_qty());

            Glide.
                    with(itemView.getContext()).
                    load(data.getImage()).
                    into(ivPic);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvAdd:
                    Data mData = (Data) itemView.getTag();
                    mData.set_selected(true);
                    mData.setProduct_selected_qty(1);
                    mData.setProduct_selected_mrp_price(mData.getProduct_mrp_price() * 1);
                    CartHashMap.getInstance().addrUpdateProductInfo(mData, itemView.getContext());

                    break;
            }
        }
    }
}
