package top.imlk.oneword.application.client.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import top.imlk.oneword.R;
import top.imlk.oneword.util.AppStyleHelper;

/**
 * Created by imlk on 2018/8/22.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(AppStyleHelper.getColorByAttributeId(this, R.attr.color_primary_dark));

    }
}
