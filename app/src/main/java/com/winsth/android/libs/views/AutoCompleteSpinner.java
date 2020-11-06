package com.winsth.android.libs.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.winsth.android.libs.R;
import com.winsth.android.libs.adapters.ArrayAdapter;

/**
 * Created by aaron.zhao on 2017/5/12.
 */

public class AutoCompleteSpinner extends LinearLayout implements OnClickListener {
    public interface OnItemSelectListener {
        void onItemSelect(String text, int position);
    }

    /*  */
    private static final int mStrokeWidth = 2;     // 2dp 边框宽度
    private static final int mRoundRadius = 5;     // 5dp 圆角半径
    private static final int mStrokeColor = Color.parseColor("#FF000000");  //边框颜色
    private static final int mFillColor = Color.parseColor("#FFFFFFFF");    //内部填充颜色
    private static final int mTitleColor = Color.parseColor("#FF000000");  //边框颜色

    private TextView tvTitle;
    private AutoCompleteTextView actvBody;
    private ImageView ivDropdown;

    public AutoCompleteSpinner(Context context) {
        super(context);
    }

    @SuppressLint("WrongConstant")
    public AutoCompleteSpinner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.auto_complete_spinner, this);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        actvBody = (AutoCompleteTextView) findViewById(R.id.actv_body);
        ivDropdown = (ImageView) findViewById(R.id.iv_dropdown);

        actvBody.setThreshold(1);           // 从第一个字符就开始自动匹配
        actvBody.setOnClickListener(this);  // 点击显示下拉列表
        ivDropdown.setOnClickListener(this);// 点击显示下拉列表

        /* 设置控件背景 */
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.AutoCompleteSpinner);
        int strokeWidth = typedArray.getInteger(R.styleable.AutoCompleteSpinner_stroke_width, mStrokeWidth);
        int strokeColor = typedArray.getColor(R.styleable.AutoCompleteSpinner_stroke_color, mStrokeColor);
        int fillColor = typedArray.getColor(R.styleable.AutoCompleteSpinner_fill_color, mFillColor);
        int titleColor = typedArray.getColor(R.styleable.AutoCompleteSpinner_title_color, mTitleColor);
        typedArray.recycle();

        GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
        gradientDrawable.setColor(fillColor);
        gradientDrawable.setCornerRadius(mRoundRadius);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setGradientType(GradientDrawable.RECTANGLE);
        setBackgroundDrawable(gradientDrawable);

        /* 设置倒三角颜色 */
        LayerDrawable layerDrawable = (LayerDrawable) ivDropdown.getBackground();
        RotateDrawable rotateDrawable = (RotateDrawable) layerDrawable.findDrawableByLayerId(R.id.shape_id);
        GradientDrawable drawable = (GradientDrawable) rotateDrawable.getDrawable();
        drawable.setColor(strokeColor);

        /* 设置Title的颜色 */
        tvTitle.setTextColor(titleColor);
    }

    public AutoCompleteSpinner(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AutoCompleteSpinner(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        super(context, attributeSet, defStyleAttr, defStyleRes);
    }

    @Override
    public void onClick(View v) {
        actvBody.showDropDown();
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    /**
     * 设置默认选中的内同
     *
     * @param text 选中的文本
     */
    public void setText(String text) {
        actvBody.setText(text);
    }

    public String getText() {
        return actvBody.getText().toString().trim();
    }

    /**
     * 清除AutoCompleteTextView已经设置的文本
     */
    public void clearText() {
        actvBody.setText("");
    }

    /**
     * 设置提示文本
     *
     * @param hint 提示文本
     */
    public void setHint(String hint) {
        actvBody.setHint(hint);
    }

    /**
     * 设置数据源
     *
     * @param arrayAdapter 数据源
     */
    public void setAdapter(ArrayAdapter arrayAdapter) {
//        if(arrayAdapter.getCount()<4){
//            actvBody.setDropDownHeight(arrayAdapter.getCount()*50);
//        }
        actvBody.setAdapter(arrayAdapter);
    }

    /**
     * 设定选择事件
     *
     * @param onItemSelectListener 选择事件
     */
    public void setOnItemSelectListener(final OnItemSelectListener onItemSelectListener) {
        actvBody.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                onItemSelectListener.onItemSelect(text, position);
            }
        });
    }
}
