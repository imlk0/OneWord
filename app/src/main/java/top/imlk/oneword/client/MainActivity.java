package top.imlk.oneword.client;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.UserHandle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import top.imlk.oneword.R;
import top.imlk.oneword.Hitokoto.HitokotoBean;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;
import top.imlk.oneword.util.SharedPreferencesUtil;
import top.imlk.oneword.view.MainOneWordView;
import top.imlk.oneword.view.OneWordShowPanel;
import top.imlk.oneword.view.PastedNestedScrollView;

import static top.imlk.oneword.common.StaticValue.CMD_SERVICES_START_AUTO_UPDATE;
import static top.imlk.oneword.common.StaticValue.CMD_SERVICES_STOP_SERVICE;

public class MainActivity extends AppCompatActivity implements Observer<HitokotoBean> {

//    private ILockSettings mLockSettingsService;


//    private Button btnSetMsg;
//    private Button btnRequestMsg;

//    private EditText etInputMsg;


    public PastedNestedScrollView pastedNestedScrollView;

    public MainOneWordView mainOneWordView;

    public OneWordShowPanel oneWordShowPanel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.pastedNestedScrollView = findViewById(R.id.root_pasted_scroll_view);

        this.mainOneWordView = findViewById(R.id.ll_main_oneword);
        this.mainOneWordView.updateContext(this);

        this.oneWordShowPanel = findViewById(R.id.one_word_show_panel);
        this.oneWordShowPanel.updateContext(this);
        this.oneWordShowPanel.initView();

//        killRunningServices();

        startAutoUpdateService();
    }


    public Rect getAreaView() {
        Rect outRect = new Rect();
        getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
        return outRect;
    }

    public void gotoPage(int index) {

        if (this.mainOneWordView.viewPager.getCurrentItem() == index && this.pastedNestedScrollView.canScroll) {
            this.pastedNestedScrollView.scrollToTop();
        } else {
            this.pastedNestedScrollView.scrollToBottom();
            this.mainOneWordView.gotoPage(index);
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        upDateLP();

    }


    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(HitokotoBean hitokotoBean) {


        this.oneWordShowPanel.updateStateByBean(hitokotoBean);

        OneWordSQLiteOpenHelper.getInstance(this).insert_to_history(hitokotoBean);

    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(MainActivity.this, "网络异常，获取失败", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onComplete() {

    }

    public void upDateLP() {
        if (this.mainOneWordView != null) {
            mainOneWordView.upDateLP(getAreaView());
        }
    }

    @Override
    protected void onDestroy() {
        if (!OneWordSQLiteOpenHelper.isDataBasegClosed()) {
            OneWordSQLiteOpenHelper.getInstance(this).close();
        }

        if (SharedPreferencesUtil.isUpdatingOpened(this)) {
            startAutoUpdateService();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!this.pastedNestedScrollView.isOnTop) {
                this.pastedNestedScrollView.scrollToTop();
            } else {
                finish();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    public void killRunningServices() {

        new Thread(new Runnable() {
            @Override
            public void run() {


                ActivityManager myAM = (ActivityManager) MainActivity.this
                        .getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
                if (myList.size() <= 0) {
                    return;
                }
                for (int i = 0; i < myList.size(); i++) {
                    String mName = myList.get(i).service.getClassName().toString();
                    if (mName.equals(OneWordAutoUpdateService.SERVICE_NAME)) {

                        Intent intent = new Intent(MainActivity.this, OneWordAutoUpdateService.class);

                        intent.setAction(CMD_SERVICES_STOP_SERVICE);
                        startService(intent);
                        break;
                    }
                }

            }
        }).run();

    }


    public void startAutoUpdateService() {
        Intent intent = new Intent(this, OneWordAutoUpdateService.class);

        intent.setAction(CMD_SERVICES_START_AUTO_UPDATE);
        startService(intent);
    }

}
