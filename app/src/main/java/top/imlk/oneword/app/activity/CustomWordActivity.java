package top.imlk.oneword.app.activity;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import top.imlk.oneword.R;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.systemui.view.OneWordView;
import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.OneWordFileStation;
import top.imlk.oneword.util.SharedPreferencesUtil;

public class CustomWordActivity extends BaseOnewordEditActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_word);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llOnewordviewContainer = findViewById(R.id.ll_onewordview_container);
        oneWordView = new OneWordView(this);
        llOnewordviewContainer.addView(oneWordView);

        wordBean = new WordBean();

        initEditText();

        initFAB();

        notifyWordViewConfigChanged();
    }

    FloatingActionButton fabSaveWordBean;


    protected void initFAB() {
        fabSaveWordBean = findViewById(R.id.fab_save_oneword);

        fabSaveWordBean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (wordBean == null || (TextUtils.isEmpty(wordBean.content) && TextUtils.isEmpty(wordBean.reference))) {

                    Toast.makeText(CustomWordActivity.this, "啥都没有可不行欸", Toast.LENGTH_SHORT).show();

                    return;
                }

                SharedPreferencesUtil.setAutoRefreshOpened(CustomWordActivity.this, false);
                BroadcastSender.stopAutoRefresh(CustomWordActivity.this);

                BroadcastSender.sendUseNewOneWordBroadcast(CustomWordActivity.this, wordBean);
                OneWordFileStation.saveOneWordToJSON(wordBean);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        alertDoesNotSave();
    }
}
