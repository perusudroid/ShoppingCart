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
import com.perusudroid.shoppingcart.model.Data;

import java.util.List;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {

    private List<Data> productList;
    private IListener iListener;
    private CartHashMap cartHashMap;

    public void refresh(List<Data> data, IListener iListener) {
        this.productList = data;
        this.iListener = iListener;
        if (cartHashMap != null) {
            cartHashMap.setListener(iListener);
        }
        notifyDataSetChanged();
    }

    public ShoppingAdapter(List<Data> data, IListener iListener) {
        this.iListener = iListener;
        this.productList = data;
        cartHashMap = CartHashMap.getInstance();
        cartHashMap.setListener(iListener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inflater_product_list, viewGroup, false));
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

        private ImageView ivPic, ivLayer;
        private TextView tvCost, tvName, tvAdd;
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
            tvAdd = itemView.findViewById(R.id.tvAdd);
            ivLayer = itemView.findViewById(R.id.ivLayer);
            numberSwitch = itemView.findViewById(R.id.numberSwitch);
        }

        private void setAssets() {
            ivPic.setOnClickListener(this);
            tvAdd.setOnClickListener(this);


            numberSwitch.setValueChangedListener(
                    new NumberSwitch.ValueChangedListener() {
                        @Override
                        public void onValueChanged(int value, NumberSwitch.ValueType valueType) {
                            Data mData = (Data) itemView.getTag();

                            if(value == 0){
                                iListener.showAlert(mData, getAdapterPosition());
                                numberSwitch.setCount(1);
                                return;
                            }

                            Log.d("ShoppingAdapter", "onValueChanged: value "+ value);

                            mData.set_selected(true);
                            mData.setProduct_mrp_price(mData.getProduct_mrp_price());
                            mData.setProduct_selected_qty(value);
                            mData.setProduct_selected_mrp_price(mData.getProduct_mrp_price() * value);

                            CartHashMap.getInstance().addrUpdateProductInfo(mData, itemView.getContext());
                        }
                    }
            );

        }

        public void bindData(Data data) {

            itemView.setTag(data);
            ivLayer.setVisibility(data.isSelected() ? View.VISIBLE : View.GONE);
            if (data.isSelected()) {
                numberSwitch.setCount(data.getProduct_selected_qty());
            }
            tvAdd.setVisibility(data.isSelected() ? View.GONE : View.VISIBLE);
            tvName.setText(String.valueOf(data.getName()));
            tvCost.setText(String.valueOf(data.getProduct_mrp_price()));


            Glide.
                    with(itemView.getContext()).
                    load(data.getImage()).
                    into(ivPic);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.ivPic:
                    if(iListener != null){
                        iListener.onImageClicked((Data)itemView.getTag());
                    }
                    break;

                case R.id.tvAdd:
                    numberSwitch.setCount(1);
                    Data mData = (Data) itemView.getTag();
                    mData.set_selected(true);
                    mData.setProduct_selected_qty(1);
                    mData.setProduct_mrp_price(mData.getProduct_mrp_price());
                    mData.setProduct_selected_mrp_price(mData.getProduct_mrp_price() * 1);
                    notifyItemChanged(getAdapterPosition());
                    CartHashMap.getInstance().addrUpdateProductInfo(mData, itemView.getContext());
                    tvAdd.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
