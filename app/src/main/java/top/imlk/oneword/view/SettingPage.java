package top.imlk.oneword.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import top.imlk.oneword.R;
import top.imlk.oneword.client.OneWordAutoRefreshService;
import top.imlk.oneword.util.SharedPreferencesUtil;
import top.imlk.oneword.util.StyleHelper;

/**
 * Created by imlk on 2018/5/30.
 */
public class SettingPage extends LinearLayout implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Context context;

    public SwitchCompat swOpenAutoRefresh;
    public LinearLayout llOpenAutoRefresh;
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

        swOpenAutoRefresh = findViewById(R.id.sw_open_auto_refresh);
        swOpenAutoRefresh.setOnCheckedChangeListener(this);

        llOpenAutoRefresh = findViewById(R.id.ll_open_auto_refresh);
        llOpenAutoRefresh.setOnClickListener(this);

        llSetRefreshMode = findViewById(R.id.ll_set_refresh_mode);
        llSetRefreshMode.setOnClickListener(this);

        swOpenAutoRefresh.setChecked(SharedPreferencesUtil.isRefreshOpened(context));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_set_refresh_mode:


                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(context, R.layout.item_dialog_choose_refresh_mode, R.id.tv_item_name, context.getResources().getStringArray(R.array.auto_update_time_item));
                new LovelyChoiceDialog(context)
                        .setTopColor(StyleHelper.getColorByAttributeId(context, R.attr.colorPrimary))
                        .setTitle("选择刷新频率（当前频率：" + context.getResources().getStringArray(R.array.auto_update_time_item)[SharedPreferencesUtil.getRefreshMode(context).ordinal()])
                        .setIcon(R.drawable.ic_av_timer_white_48dp)
                        .setMessage("从下面的选项中选一个刷新频率，若您启用了自动刷新选项，那么在应用退出后，锁屏一言将会在的时间里自动刷新")
                        .setItems(arrayAdapter, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                            @Override
                            public void onItemSelected(int position, String item) {
                                Log.e("SettingPage", "position -> " + position);
                                SharedPreferencesUtil.setRefreshMode(context, OneWordAutoRefreshService.Mode.values()[position]);
                            }

                        })
                        .setCancelable(true)
                        .show();
                break;

            case R.id.ll_open_auto_refresh:
                swOpenAutoRefresh.setChecked(!swOpenAutoRefresh.isChecked());
                break;

        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_open_auto_refresh:
                SharedPreferencesUtil.setAutoRefreshOpened(context, isChecked);
                break;
        }
    }
}
