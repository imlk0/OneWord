package top.imlk.oneword.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

import top.imlk.oneword.R;

/**
 * Created by imlk on 2018/5/20.
 */
public class BottomNavigatorItem extends LinearLayout {
    public ImageView image;
    public TextView text;

    public int index;


    public BottomNavigatorItem(Context context) {
        super(context);
    }

    public BottomNavigatorItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomNavigatorItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomNavigatorItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        image = findViewById(R.id.item_selector_icon);
        text = findViewById(R.id.item_selector_text);
    }

    public BottomNavigatorItem setImage(int id) {
        this.image.setImageResource(id);
        return this;
    }

    public BottomNavigatorItem setBgByColor(int color) {
        this.setBackgroundColor(color);
        return this;
    }

    public BottomNavigatorItem setText(String str) {
        this.text.setText(str);
        return this;
    }

    public BottomNavigatorItem setIndex(int index) {
        this.index = index;
        return this;
    }




}
