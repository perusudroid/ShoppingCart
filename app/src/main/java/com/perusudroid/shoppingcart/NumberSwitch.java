package com.perusudroid.shoppingcart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NumberSwitch extends LinearLayout implements View.OnClickListener {

    private ImageView ivAdd;
    private ImageView ivMinus;
    private TextView tvCount;
    private String zeroTxt;
    private int initValue = 0;
    private ValueChangedListener valueChangedListener;
    private Drawable plusPic, minusPic;

    public NumberSwitch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        bindViews();
        setAssets();
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberSwitch, 0, 0);
        this.plusPic = typedArray.getDrawable(R.styleable.NumberSwitch_plusPic);
        this.minusPic = typedArray.getDrawable(R.styleable.NumberSwitch_minusPic);
        this.zeroTxt = typedArray.getString(R.styleable.NumberSwitch_zerotext);
        this.initValue = typedArray.getInt(R.styleable.NumberSwitch_startvalue, -1);
        typedArray.recycle();
    }

    private void bindViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_number_switch, this, true);
        ivAdd = view.findViewById(R.id.ivAdd);
        ivMinus = view.findViewById(R.id.ivMinus);
        tvCount = view.findViewById(R.id.tvCount);
    }

    private void setAssets() {
        if (initValue != -1) {
            tvCount.setText(String.valueOf(initValue));
        }
        ivAdd.setImageDrawable(plusPic);
        ivMinus.setImageDrawable(minusPic);
        ivAdd.setOnClickListener(this);
        ivMinus.setOnClickListener(this);
    }

    public void setCount(int count){
        tvCount.setText(String.valueOf(count));
    }

    public void setValueChangedListener(ValueChangedListener valueChangedListener){
        this.valueChangedListener = valueChangedListener;
    }

    public int getCount() {
        return Integer.parseInt(tvCount.getText().toString().trim());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivAdd:
                int v = Integer.parseInt(tvCount.getText().toString()) + 1;
                tvCount.setText(String.valueOf(v));
                if(valueChangedListener != null){
                    valueChangedListener.onValueChanged(v, ValueType.ADD);
                }
                break;
            case R.id.ivMinus:
                int v1 = Integer.parseInt(tvCount.getText().toString());
                Log.d("NumberSwitch", "onClick: v1 "+ v1);
                if (v1 > initValue) {
                    v1 = Integer.parseInt(tvCount.getText().toString()) - 1;
                    tvCount.setText(String.valueOf(v1));
                    if(valueChangedListener != null){
                        valueChangedListener.onValueChanged(v1, ValueType.MINUS);
                    }
                } else {
                    Toast.makeText(getContext(), zeroTxt, Toast.LENGTH_LONG).show();
                    return;
                }
                break;
        }
    }

    public enum ValueType{
        ADD,
        MINUS
    }

    public interface ValueChangedListener{
        void onValueChanged(int value, ValueType valueType);
    }
}
