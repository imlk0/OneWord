package top.imlk.oneword.client;

import android.graphics.Rect;
import android.os.UserHandle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import top.imlk.oneword.R;
import top.imlk.oneword.Hitokoto.HitokotoBean;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;
import top.imlk.oneword.util.SharedPreferencesUtil;
import top.imlk.oneword.view.MainOneWordView;
import top.imlk.oneword.view.OneWordShowPanel;
import top.imlk.oneword.view.PastedNestedScrollView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Observer<HitokotoBean> {

//    private ILockSettings mLockSettingsService;

    public OneWordSQLiteOpenHelper oneWordSQLiteOpenHelper;

//    private Button btnSetMsg;
//    private Button btnRequestMsg;

//    private EditText etInputMsg;


    private PastedNestedScrollView pastedNestedScrollView;

    private MainOneWordView mainOneWordView;

    private OneWordShowPanel oneWordShowPanel;

    private int mUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.mUserId = UserHandle.myUserId();


        this.pastedNestedScrollView = findViewById(R.id.root_pasted_scroll_view);

        this.oneWordSQLiteOpenHelper = new OneWordSQLiteOpenHelper(this);

//        this.etInputMsg = findViewById(R.id.message);

        this.mainOneWordView = findViewById(R.id.ll_main_oneword);
        this.mainOneWordView.initContext(this);

        this.oneWordShowPanel = findViewById(R.id.one_word_show_panel);
        this.oneWordShowPanel.initContext(this);
        this.oneWordShowPanel.initView();
//        upDateLP();

        // TODO
//        try {
//            updateMsgMain(getMsgFromLockScreen());
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "获取当前锁屏一言失败", Toast.LENGTH_LONG).show();
//        }

    }

//    private ILockSettings getLockSettings() {
//        if (this.mLockSettingsService == null) {
//            this.mLockSettingsService = ILockSettings.Stub.asInterface(ServiceManager.getService("lock_settings"));
//        }
//        return this.mLockSettingsService;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_set_msg:
//
//                try {
//                    Toast.makeText(this, "OK!!", Toast.LENGTH_LONG).show();
//                    writeMsgToLockScreen(etInputMsg.getText().toString());
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//                break;
//            case R.id.btn_get_net_msg:
//                try {
//                    HitokotoApi.getAnime(MainActivity.this);
//                } catch (Exception e) {
//                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//                break;
//            case R.id.btn_write_to_lock_screen:
//                if (this.curHitokotoBean != null) {
//                    try {
//                        writeMsgToLockScreen(curHitokotoBean);
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                }
        }
    }

//    public void writeMsgToLockScreen(String str) throws RemoteException {
//        getLockSettings().setString("lock_screen_owner_info", str, this.mUserId);
//    }

//    public void writeMsgToLockScreen(HitokotoBean hitokotoBean) throws RemoteException {
//        getLockSettings().setString("lock_screen_owner_info", hitokotoBean.from, this.mUserId);
//    }


//    public String getMsgFromLockScreen() throws RemoteException {
//        return getLockSettings().getString("lock_screen_owner_info", null, this.mUserId);
//    }


    public void updateStateByBean(HitokotoBean hitokotoBean) {

        this.oneWordShowPanel.updateCurHitokotoBean(hitokotoBean);
        this.oneWordShowPanel.updateMsgMain(hitokotoBean.hitokoto);
        this.oneWordShowPanel.updateMsgFrom(hitokotoBean.from);
        this.oneWordShowPanel.updateLike(hitokotoBean.like);

    }


    public Rect getAreaView() {
        Rect outRect = new Rect();
        getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
        return outRect;
    }

    public void gotoPage(int index) {

        this.pastedNestedScrollView.scrollToBottom();
        this.pastedNestedScrollView.canScroll = true;

        this.mainOneWordView.gotoPage(index);
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

        if (this.oneWordSQLiteOpenHelper.query_one_item(hitokotoBean, OneWordSQLiteOpenHelper.TABLE_LIKE) != -1) {
            hitokotoBean.like = true;
        }

        this.oneWordSQLiteOpenHelper.insert_to_history(hitokotoBean);

        updateStateByBean(hitokotoBean);

        SharedPreferencesUtil.saveCurOneWord(this, hitokotoBean);

        // TODO

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
}
