package top.imlk.oneword.application.client.activity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import top.imlk.oneword.bean.ApiBean;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.R;
import top.imlk.oneword.net.WordRequestObserver;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;
import top.imlk.oneword.net.OneWordApi;
import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.OneWordFileStation;
import top.imlk.oneword.util.SharedPreferencesUtil;
import top.imlk.oneword.util.AppStyleHelper;
import top.imlk.oneword.application.view.MainOneWordView;
import top.imlk.oneword.application.view.OneWordShowPanel;
import top.imlk.oneword.application.view.PastedNestedScrollView;


public class MainActivity extends BaseActivity implements WordRequestObserver, OnRefreshListener, PastedNestedScrollView.OnPasteListener {


    private PastedNestedScrollView pastedNestedScrollView;

    private RefreshLayout refreshLayout;

    private MainOneWordView mainOneWordView;

    private OneWordShowPanel oneWordShowPanel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fixServiceWhenStartUp();

//        setTheme(R.style.GreenTheme);

        setContentView(R.layout.activity_main);

        this.pastedNestedScrollView = findViewById(R.id.pasted_scroll_view);
        this.pastedNestedScrollView.setOnPasteListener(this);

        this.mainOneWordView = findViewById(R.id.ll_main_oneword);
        this.mainOneWordView.init(this);

        this.oneWordShowPanel = findViewById(R.id.one_word_show_panel);
        this.oneWordShowPanel.init(this);

        this.refreshLayout = findViewById(R.id.refreshLayout);
        this.refreshLayout.setOnRefreshListener(this);
        this.refreshLayout.setEnableLoadMore(false);

        refreshLayout.setPrimaryColors(
                AppStyleHelper.getColorByAttributeId(this, R.attr.color_primary_light),
                AppStyleHelper.getColorByAttributeId(this, R.attr.color_primary_dark));


        SharedPreferencesUtil.onMainActivityCreate(this);


        lpResolved = false;
        isOnConfigurationChanged = false;

        resolveIntent(getIntent());

    }

    private void resolveIntent(Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        switch (intent.getAction()) {
            case Intent.ACTION_MAIN:
                WordBean wordBean = intent.getParcelableExtra(BroadcastSender.THE_CLICKED_WORDBEAN);
                if (wordBean != null) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(KEYGUARD_SERVICE);
                        keyguardManager.requestDismissKeyguard(this, null);
                    } else {
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                    }

                    oneWordShowPanel.updateCurWordBeanOnUI(wordBean);
                }
                break;
        }
    }


    public void gotoPage(int index) {

        if (this.mainOneWordView.viewPager.getCurrentItem() == index && this.pastedNestedScrollView.canScroll()) {
            this.pastedNestedScrollView.scrollToTop();
        } else {
            this.pastedNestedScrollView.scrollToBottom();
            this.mainOneWordView.gotoPage(index);
        }

    }


    public void updateAndSetCurWordBean(WordBean wordBean) {
        if (wordBean != null) {
            updateButDoNOTSetCurWordBean(wordBean);
            OneWordFileStation.saveOneWordToJSON(wordBean);
            BroadcastSender.sendUseNewOneWordBroadcast(this, wordBean);
            Toast.makeText(this, "设置锁屏一言中...", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateButDoNOTSetCurWordBean(WordBean wordBean) {
        if (wordBean != null) {

            oneWordShowPanel.updateCurWordBeanOnUI(wordBean);
            pastedNestedScrollView.scrollToTop();
            OneWordSQLiteOpenHelper.getInstance().insertToHistory(wordBean);
        }
    }


    public void checkIfCurBeanFavorStateChanged(WordBean wordBean) {
        oneWordShowPanel.checkIfCurBeanFavorStateChanged(wordBean);
    }

    public void checkIfCurBeanRemoved(WordBean wordBean) {
        oneWordShowPanel.checkIfCurBeanRemoved(wordBean);
    }

    //implement reference WordRequestObserver

    @Override
    public void onStart(ApiBean apiBean) {
        Toast.makeText(this, String.format("正在从\n%s\n%s\n拉取一言数据", apiBean.name, apiBean.url), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAcquire(ApiBean apiBean, WordBean wordBean) {

        int id = OneWordSQLiteOpenHelper.getInstance().queryIdOfOneWordInAllOneWord(wordBean);
        if (id <= 0) {
            id = OneWordSQLiteOpenHelper.getInstance().insertOneWordWithoutCheck(wordBean);
        }

        wordBean.id = id;

        // 获取到一言后不再自动设置
        updateButDoNOTSetCurWordBean(wordBean);

        refreshLayout.finishRefresh(300, true);

    }

    @Override
    public void onError(ApiBean apiBean, Throwable e) {
//        Toast.makeText(MainActivity.this, "发生异常，获取失败", Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this, "请求：" + apiBean.name + " 时出错\n" + e.getMessage() + (e.getCause() == null ? "" : "\ncause:\n" + e.getCause()), Toast.LENGTH_LONG).show();

        refreshLayout.finishRefresh(0, false);
    }


    @Override
    protected void onDestroy() {
        OneWordSQLiteOpenHelper.closeDataBase();

        if (!isOnConfigurationChanged) {
            if (SharedPreferencesUtil.isAutoRefreshOpened(this)) {
                BroadcastSender.startAutoRefresh(this);
            }

            if (SharedPreferencesUtil.isShowNotificationOneWordOpened(this)) {
                BroadcastSender.startShowNitificationOneword(this);
            }

        }
        super.onDestroy();
//        System.exit(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!this.pastedNestedScrollView.isOnTop()) {
                this.pastedNestedScrollView.scrollToTop();
            } else {
                finish();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    public void fixServiceWhenStartUp() {

        if (SharedPreferencesUtil.isAutoRefreshOpened(this)) {
            BroadcastSender.pauseAutoRefresh(this);
        }
        if (SharedPreferencesUtil.isShowNotificationOneWordOpened(this)) {
            BroadcastSender.startShowNitificationOneword(this);
        }

//        Intent intent = new Intent(this, OneWordAutoRefreshService.class);
//        this.stopService(intent);

    }


    public void performRefresh() {
        this.refreshLayout.autoRefresh(0, 200, 1);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

        startAnOneWordRequest();

    }

    public void startAnOneWordRequest() {
        try {
            OneWordApi.requestOneWord(this);
            this.pastedNestedScrollView.scrollToTop();
        } catch (Exception e) {
            Toast.makeText(this, "发生错误:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            refreshLayout.finishRefresh(500, false);
        }
    }

    @Override
    public void onPastedToTop() {
        mainOneWordView.bottomMagicIndicator.onPageSelected(-1);
    }

    @Override
    public void onPastedToBottom() {

    }


    public void upDateLP() {
        if (this.mainOneWordView != null) {
            mainOneWordView.upDateLP(getAreaView());
        }
    }

    public Rect getAreaView() {
        Rect outRect = new Rect();
        getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
        return outRect;
    }


    boolean lpResolved = false;

    @Override

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && !lpResolved) {
            lpResolved = true;
            upDateLP();
        }

    }

    public WordBean getCurWordBeanCopy() {
        return this.oneWordShowPanel.getCurBeanCopy();
    }


    private boolean isOnConfigurationChanged = false;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isOnConfigurationChanged = true;
    }


}
