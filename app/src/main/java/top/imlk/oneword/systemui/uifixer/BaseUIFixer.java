package top.imlk.oneword.systemui.uifixer;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import de.robv.android.xposed.XposedBridge;
import top.imlk.oneword.BuildConfig;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.bean.WordViewConfig;
import top.imlk.oneword.systemui.view.OneWordView;
import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.BugUtil;
import top.imlk.oneword.util.OneWordFileStation;


/**
 * Created by imlk on 2018/7/30.
 */
public class BaseUIFixer implements View.OnLongClickListener, View.OnAttachStateChangeListener {

    private OwnerInfoTextViewProxy ownerInfoTextViewProxy;
    private OneWordView oneWordView;
    private TextView mOwnerInfo;


    public BaseUIFixer(TextView mOwnerInfo) {

        this.mOwnerInfo = mOwnerInfo;

        oneWordView = new OneWordView(mOwnerInfo.getContext());
        oneWordView.addOnAttachStateChangeListener(this);

        ownerInfoTextViewProxy = new OwnerInfoTextViewProxy(mOwnerInfo.getContext());

//        XposedBridge.log("oneWordView.setOnClickListener");
    }

    public OwnerInfoTextViewProxy getOwnerInfoTextViewProxy() {
        return ownerInfoTextViewProxy;
    }

    public void fixUI(TextView onerInfoTextView) {

        //添加到现有视图中

        ViewGroup parent = ((ViewGroup) onerInfoTextView.getParent());

        int index = parent.indexOfChild(onerInfoTextView);

        ViewGroup.LayoutParams layoutParams = onerInfoTextView.getLayoutParams();

        onerInfoTextView.setVisibility(View.GONE);

//        parent.removeView(onerInfoTextView);

//        XposedBridge.log("parent.addView(oneWordView, layoutParams);");

        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;


//        try {
//            ViewGroup.LayoutParams layoutParams_0 = (ViewGroup.LayoutParams) XposedHelpers.findMethodBestMatch(parent.getClass(), "generateDefaultLayoutParams").invoke(parent);
//            if (layoutParams_0 != null) {
//                layoutParams = layoutParams_0;
//            }
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            XposedBridge.log("反射调用时发生异常generateDefaultLayoutParams()");
//            XposedBridge.log(e);
//        }


        parent.addView(oneWordView, index + 1, layoutParams);

        ownerInfoTextViewProxy.setVisibility(View.GONE);
        parent.addView(ownerInfoTextViewProxy, index + 2);

        ownerInfoTextViewProxy.setId(onerInfoTextView.getId());
        onerInfoTextView.setId(View.NO_ID);


        ownerInfoTextViewProxy.syncLayout();
    }

//    public void onSetTextSize(int unit, float size) {
//        oneWordView.setTextSize(unit, size);
//    }

//    public void onSetTextColor(int color) {
//        oneWordView.setTextColor(color);
//    }


    public void setOneWord(WordBean word) {
        if (oneWordView != null) {

            oneWordView.setVisibility(View.GONE);
            TransitionManager.beginDelayedTransition((ViewGroup) oneWordView.getParent(), new TransitionSet().addTransition(new Fade(Fade.IN)));
            oneWordView.setOneWord(word);
            oneWordView.setVisibility(View.VISIBLE);

            ownerInfoTextViewProxy.syncLayout();
        }
    }

    public void applyWordViewConfig(WordViewConfig config) {
        if (oneWordView != null) {
            if (config == null) {
                config = WordViewConfig.generateDefaultBean();
            }

            if (config.keyguardLongClick != null && config.keyguardLongClick != WordViewConfig.LongClickEvent.NONE) {
                oneWordView.setOnLongClickListener(this);
            } else {
                oneWordView.setOnLongClickListener(null);
            }


            oneWordView.applyWordViewConfig(config);

            ownerInfoTextViewProxy.syncLayout();
        }
    }

    @Override
    public boolean onLongClick(View v) {

        WordViewConfig config = oneWordView.getCurWordViewConfig();

        if (config != null && config.keyguardLongClick != null) {

            switch (config.keyguardLongClick) {
                case APP:

                    WordBean wordBean = oneWordView.getCurWordBean();
                    if (wordBean != null) {
                        BroadcastSender.startMainActivityWhenClicked(v.getContext(), wordBean);
                    }
                    return true;
                case NEXT:

                    BroadcastSender.switchToNextOnewordManually(v.getContext());

                    return true;
            }
        }


        return false;
    }

    // OneWordView是否attach到了界面上（在状态改变时取消广播接收器，防止内存泄漏）
    @Override
    public void onViewAttachedToWindow(View v) {
        registerBroadcastReceiver(v.getContext().getApplicationContext());
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        unregisterBroadcastReceiver(v.getContext().getApplicationContext());
    }


    public void getAndSetOneWord() {

        WordBean wordBean = OneWordFileStation.readOneWordToJSON();

        setOneWord(wordBean);
    }

    public void getAndApplyWordViewConfig() {
        WordViewConfig wordViewConfig = OneWordFileStation.readWordViewConfigToJSON();

        applyWordViewConfig(wordViewConfig);

    }


    private SystemUICmdBroadcastReceiver systemUICmdBroadcastReceiver;

    private void registerBroadcastReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastSender.CMD_BROADCAST_SET_NEW_WORDBEAN);
        intentFilter.addAction(BroadcastSender.CMD_BROADCAST_RELOAD_WORDBEAN);
        intentFilter.addAction(BroadcastSender.CMD_BROADCAST_SET_NEW_WORDVIEWCONFIG);
        intentFilter.addAction(BroadcastSender.CMD_BROADCAST_RELOAD_WORDVIEWCONFIG);
        systemUICmdBroadcastReceiver = new SystemUICmdBroadcastReceiver();
        context.registerReceiver(systemUICmdBroadcastReceiver, intentFilter);
        XposedBridge.log("SystemUICmdBroadcastReceiver registered");

    }

    private void unregisterBroadcastReceiver(Context context) {
        context.unregisterReceiver(systemUICmdBroadcastReceiver);
        systemUICmdBroadcastReceiver = null;
        XposedBridge.log("SystemUICmdBroadcastReceiver unregistered");
    }

    class SystemUICmdBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {//此处context具体类型与注册时的context有关

            XposedBridge.log("SystemUICmdBroadcastReceiver -> onReceive" + intent.getAction());

            if (intent.getAction() == null) {

                XposedBridge.log("action is null!!!");
                return;
            }

            int senderVersionCode = intent.getIntExtra(BroadcastSender.ONEWORD_APP_VERSION_CODE, 0);
            if (senderVersionCode != BuildConfig.VERSION_CODE) {
                XposedBridge.log("版本号不匹配，MODULE_PORT_VERSION_CODE：" + BuildConfig.VERSION_CODE + "\nONEWORD_APP_VERSION_CODE：" + senderVersionCode);
                Toast.makeText(context, "模块版本号不匹配，您可能需要重启手机", Toast.LENGTH_SHORT).show();
                if (!BuildConfig.DEBUG) {
                    return;
                }
            }


            try {
                switch (intent.getAction()) {
                    case BroadcastSender.CMD_BROADCAST_SET_NEW_WORDBEAN:

//                        KeyguardStatusViewHelper.setOwnerInfo(intent.getStringExtra(BroadcastSender.THE_NEW_WORDBEAN));

                        WordBean wordBean = intent.getParcelableExtra(BroadcastSender.THE_NEW_WORDBEAN);

                        XposedBridge.log("wordbean received");
                        XposedBridge.log(String.valueOf(wordBean));

                        if (wordBean == null) {
                            XposedBridge.log("bad wordbean");
                            break;
                        }

//                        XposedBridge.log("resolved");

                        setOneWord(wordBean);

//                        Toast.makeText(context, "一言已收到", Toast.LENGTH_SHORT).show();

                        if (intent.getBooleanExtra(BroadcastSender.IS_IN_MAIN_PROCESS, false)) {
                            Toast.makeText(context, "一言更新完成", Toast.LENGTH_SHORT).show();
                        }
                        XposedBridge.log("wordbean updated");

                        break;

                    case BroadcastSender.CMD_BROADCAST_RELOAD_WORDBEAN:

                        getAndSetOneWord();

                        XposedBridge.log("wordbean updated byself");

                        break;

                    case BroadcastSender.CMD_BROADCAST_SET_NEW_WORDVIEWCONFIG:


                        WordViewConfig config = intent.getParcelableExtra(BroadcastSender.THE_NEW_WORDVIEWCONFIG);


                        XposedBridge.log("wordviewconfig received");
                        XposedBridge.log(String.valueOf(config));

                        if (config == null) {
                            XposedBridge.log("bad wordviewconfig");
                            break;
                        }

//                        XposedBridge.log("resolved");

                        applyWordViewConfig(config);


                        if (intent.getBooleanExtra(BroadcastSender.IS_IN_MAIN_PROCESS, false)) {
                            Toast.makeText(context, "布局配置应用完成", Toast.LENGTH_SHORT).show();
                        }
                        XposedBridge.log("wordviewconfig updated");

                        break;

                    case BroadcastSender.CMD_BROADCAST_RELOAD_WORDVIEWCONFIG:

                        getAndApplyWordViewConfig();

                        break;

                }
            } catch (Throwable e) {
//                XposedBridge.log(e);

                String logPath = BugUtil.printAndSaveCrashThrow2File(e);

                Toast.makeText(context, String.format("不小心搞崩了,灰常抱歉，日志在\n%s", logPath), Toast.LENGTH_LONG).show();

            }
        }


//        public static void reportException(Context context, Exception e) {
//            e.printStackTrace();
//            //  错误上报
//
//        }
    }

    @SuppressLint("AppCompatCustomView")
    public class OwnerInfoTextViewProxy extends TextView {

        public OwnerInfoTextViewProxy(Context context) {
            super(context);
        }

        @Override
        public void setTranslationX(float translationX) {
            oneWordView.setTranslationX(translationX);
        }

        @Override
        public void setTranslationY(float translationY) {
            oneWordView.setTranslationY(translationY);
        }

        @Override
        public void setTranslationZ(float translationZ) {
            oneWordView.setTranslationZ(translationZ);
        }

        @Override
        public void setLayoutParams(ViewGroup.LayoutParams params) {
            oneWordView.setLayoutParams(params);
        }

        @Override
        public void setVisibility(int visibility) {

        }

        @Override
        public ViewGroup.LayoutParams getLayoutParams() {
            return oneWordView.getLayoutParams();
        }

        public void setFakeHeight(int pixels) {
            mBottom = pixels;
        }

        public void setFakeWith(int pixels) {
            mBottom = pixels;
        }


        public void syncLayout() {
            oneWordView.post(new Runnable() {
                @Override
                public void run() {

                    ownerInfoTextViewProxy.setFakeHeight(oneWordView.getHeight());
                    ownerInfoTextViewProxy.setFakeWith(oneWordView.getWidth());

                }
            });
        }

    }

}
