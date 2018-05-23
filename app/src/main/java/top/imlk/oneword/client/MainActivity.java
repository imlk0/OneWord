package top.imlk.oneword.client;

import android.graphics.Rect;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.widget.ILockSettings;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import top.imlk.oneword.R;
import top.imlk.oneword.Hitokoto.HitokotoApi;
import top.imlk.oneword.Hitokoto.HitokotoBean;
import top.imlk.oneword.view.MainOneWordView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Observer<HitokotoBean> {

    private ILockSettings mLockSettingsService;

//    private Button btnSetMsg;
//    private Button btnRequestMsg;

//    private EditText etInputMsg;

    private TextView tvMsgMain;
    private TextView tvMsgFrom;

    private HitokotoBean curHitokotoBean;

    private MainOneWordView mainOneWordView;


    private int mUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mUserId = UserHandle.myUserId();

//        this.btnSetMsg = findViewById(R.id.btn_set_msg);
//        this.btnSetMsg.setOnClickListener(this);

//        this.btnRequestMsg = findViewById(R.id.btn_get_net_msg);
//        this.btnRequestMsg.setOnClickListener(this);

        this.tvMsgMain = findViewById(R.id.tv_msg_main);
        this.tvMsgFrom = findViewById(R.id.tv_msg_from);


//        this.etInputMsg = findViewById(R.id.message);

        this.mainOneWordView = findViewById(R.id.ll_main_oneword);


        try {
            updateMsgMain(getMsgFromLockScreen());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "获取当前锁屏一言失败", Toast.LENGTH_LONG).show();
        }

    }

    private ILockSettings getLockSettings() {
        if (this.mLockSettingsService == null) {
            this.mLockSettingsService = ILockSettings.Stub.asInterface(ServiceManager.getService("lock_settings"));
        }
        return this.mLockSettingsService;
    }

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

    public void writeMsgToLockScreen(HitokotoBean hitokotoBean) throws RemoteException {
        getLockSettings().setString("lock_screen_owner_info", hitokotoBean.from, this.mUserId);
    }


    public String getMsgFromLockScreen() throws RemoteException {
        return getLockSettings().getString("lock_screen_owner_info", null, this.mUserId);
    }


    public void updateMsgByBean(HitokotoBean hitokotoBean) {
        this.curHitokotoBean = hitokotoBean;
        updateMsgMain(hitokotoBean.hitokoto);
        updateMsgFrom(hitokotoBean.from);
    }

    public void updateMsgMain(String str) {
        this.tvMsgMain.setText(TextUtils.isEmpty(str) ? "当前一言" : str);
    }

    public void updateMsgFrom(String str) {
        this.tvMsgFrom.setText("——" + (TextUtils.isEmpty(str) ? "出处" : str));
    }

    public Rect getAreaView() {
        Rect outRect = new Rect();
        getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
        return outRect;
    }

    public void gotoPage(int index) {
        this.mainOneWordView.gotoPage(index);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (this.mainOneWordView != null) {
            mainOneWordView.upDateLP(getAreaView());
        }

    }


    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(HitokotoBean hitokotoBean) {
        updateMsgByBean(hitokotoBean);
    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(MainActivity.this, "网络异常，获取失败", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onComplete() {

    }
}
