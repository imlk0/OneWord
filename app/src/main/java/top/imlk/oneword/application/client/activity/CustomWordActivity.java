package top.imlk.oneword.application.client.activity;

import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import top.imlk.oneword.R;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.systemui.view.OneWordView;
import top.imlk.oneword.util.AppStyleHelper;
import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.OneWordFileStation;

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

        initConfig();
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
                Toast.makeText(CustomWordActivity.this, "正在保存自定义一言，记得关闭自动刷新功能哦，不然自定义一言就被刷掉了", Toast.LENGTH_LONG).show();
                BroadcastSender.sendUseNewOneWordBroadcast(CustomWordActivity.this, wordBean);
                OneWordFileStation.saveOneWordJSON(wordBean);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
