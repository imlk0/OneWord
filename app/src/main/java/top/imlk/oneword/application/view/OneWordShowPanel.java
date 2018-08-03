package top.imlk.oneword.application.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import top.imlk.oneword.application.client.activity.MainActivity;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.R;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;
import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.SharedPreferencesUtil;

/**
 * Created by imlk on 2018/5/26.
 */
public class OneWordShowPanel extends RelativeLayout implements View.OnClickListener {

    public TextView tvMsgMain;
    public TextView tvMsgFrom;

    public ImageView ivLike;
    public ImageView ivSetIt;

    public WordBean curWordBean;
    private MainActivity mainActivity;


    public OneWordShowPanel(Context context) {
        super(context);
    }

    public OneWordShowPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OneWordShowPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OneWordShowPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void init(MainActivity mainActivity) {
        this.mainActivity = mainActivity;


        this.tvMsgMain = findViewById(R.id.tv_msg_main);
        this.tvMsgFrom = findViewById(R.id.tv_msg_from);

        this.ivLike = findViewById(R.id.iv_msg_favor);
        this.ivLike.setOnClickListener(this);
        this.ivSetIt = findViewById(R.id.iv_msg_set);
        this.ivSetIt.setOnClickListener(this);

        WordBean wordBean = SharedPreferencesUtil.readSavedOneWord(mainActivity);

        if (wordBean != null) {
            updateCurWordBeanOnUI(wordBean);
        }
    }


    public void updateMsgMain(String str) {
        this.tvMsgMain.setText(TextUtils.isEmpty(str) ? "  当前一言" : "  " + str);
    }

    public void updateMsgFrom(String str) {
        this.tvMsgFrom.setText("——" + (TextUtils.isEmpty(str) ? "出处 " : str + " "));
    }

    public void updateCurWordBeanOnUI(WordBean wordBean) {
        this.curWordBean = wordBean;
        this.updateLike(OneWordSQLiteOpenHelper.getInstance(mainActivity).checkIfInFavor(wordBean.id));
        this.updateMsgMain(wordBean.content);
        this.updateMsgFrom(wordBean.reference);
    }

    private void updateLike(boolean like) {
        if (like) {
            this.ivLike.setImageResource(R.drawable.ic_favorite_white_48dp);
        } else {
            this.ivLike.setImageResource(R.drawable.ic_favorite_border_white_48dp);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_msg_favor:
                if (this.curWordBean != null) {

                    boolean favor = OneWordSQLiteOpenHelper.getInstance(mainActivity).checkIfInFavor(this.curWordBean.id);

                    if (favor) {
                        OneWordSQLiteOpenHelper.getInstance(mainActivity).removeFromFavor(this.curWordBean.id);
                    } else {
                        OneWordSQLiteOpenHelper.getInstance(mainActivity).insertToFavor(this.curWordBean);
                    }

                    this.updateLike(!favor);
                } else {
                    Toast.makeText(mainActivity, "还没有拉取任何一条一言哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_msg_set:
                if (this.curWordBean != null) {

//                    SharedPreferencesUtil.saveCurOneWord(context, curWordBean);
                    BroadcastSender.sendSetNewLockScreenInfoBroadcast(mainActivity, this.curWordBean);
                } else {
                    Toast.makeText(mainActivity, "还没有拉取任何一条一言哦", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
