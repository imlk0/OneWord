package top.imlk.oneword.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import top.imlk.oneword.R;
import top.imlk.oneword.client.OneWordAutoUpdateService;
import top.imlk.oneword.util.SharedPreferencesUtil;

/**
 * Created by imlk on 2018/5/30.
 */
public class SettingPage extends LinearLayout implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Context context;

    public Switch swOpenAutoUpdating;
    public LinearLayout llSetRefreshMode;


    public SettingPage(Context context) {
        super(context);
        updateContext(context);
    }

    public SettingPage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        updateContext(context);
    }

    public SettingPage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        updateContext(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SettingPage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        updateContext(context);
    }


    public void updateContext(Context context) {
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        swOpenAutoUpdating = findViewById(R.id.sw_open_auto_updating);
        swOpenAutoUpdating.setOnCheckedChangeListener(this);
        llSetRefreshMode = findViewById(R.id.ll_set_refresh_mode);
        llSetRefreshMode.setOnClickListener(this);

        swOpenAutoUpdating.setChecked(SharedPreferencesUtil.isUpdatingOpened(context));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_set_refresh_mode:


                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(context, R.layout.choose_dialog_item, R.id.tv_item_name, context.getResources().getStringArray(R.array.auto_update_time_item));
                new LovelyChoiceDialog(context)
                        .setTopColorRes(R.color.colorPrimary)
                        .setTitle("选择刷新频率")
                        .setIcon(R.drawable.ic_av_timer_white_48dp)
                        .setMessage("从下面的选项中选一个刷新频率，若您启用了自动刷新选项，那么锁屏一言将会在规定的时间里自动刷新")
                        .setItems(arrayAdapter, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                            @Override
                            public void onItemSelected(int position, String item) {
                                Log.e("SettingPage", "position -> " + position);
                                SharedPreferencesUtil.setUpdatingMode(context, OneWordAutoUpdateService.Mode.values()[position]);
                            }

                        })
                        .setCancelable(true)
                        .show();
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_open_auto_updating:
                SharedPreferencesUtil.setUpdatingOpened(context, isChecked);
                break;
        }
    }
}
