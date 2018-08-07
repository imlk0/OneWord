package top.imlk.oneword.application.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
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

/**
 * Created by imlk on 2018/5/26.
 */
public class OneWordShowPanel extends RelativeLayout implements View.OnClickListener {

    public TextView tvMsgMain;
    public TextView tvMsgFrom;

    public ImageView ivFavor;
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

        this.ivFavor = findViewById(R.id.iv_msg_favor);
        this.ivFavor.setOnClickListener(this);
        this.ivSetIt = findViewById(R.id.iv_msg_set);
        this.ivSetIt.setOnClickListener(this);

        loadAndShowCurneWord();
    }

    public void loadAndShowCurneWord() {

        WordBean wordBean = OneWordSQLiteOpenHelper.getInstance().queryOneWordFromHistoryByDESC();

        if (wordBean != null) {
            updateCurWordBeanOnUI(wordBean);
        } else {
            showDefaultWord();
        }

    }


    public void showDefaultWord() {
        updateCurWordBeanOnUI(new WordBean("当前一言", "出处"));
    }

    public void updateMsgMain(String str) {
        this.tvMsgMain.setText("  " + str);
    }

    public void updateMsgFrom(String str) {
        this.tvMsgFrom.setText("——" + str);
    }

    public void updateCurWordBeanOnUI(WordBean wordBean) {
        this.curWordBean = wordBean;
        this.updateLike(OneWordSQLiteOpenHelper.getInstance().checkIfInFavor(wordBean.id));
        this.updateMsgMain(wordBean.content);
        this.updateMsgFrom(wordBean.reference);
    }

    private void updateLike(boolean like) {
        if (like) {
            this.ivFavor.setImageResource(R.drawable.ic_favorite_white_48dp);
        } else {
            this.ivFavor.setImageResource(R.drawable.ic_favorite_border_white_48dp);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_msg_favor:
                if (this.curWordBean != null && this.curWordBean.id > 0) {

                    boolean favor = OneWordSQLiteOpenHelper.getInstance().checkIfInFavor(this.curWordBean.id);

                    if (favor) {
                        OneWordSQLiteOpenHelper.getInstance().removeFromFavor(this.curWordBean.id);
                    } else {
                        OneWordSQLiteOpenHelper.getInstance().insertToFavor(this.curWordBean);
                    }

                    this.updateLike(!favor);
                } else {
                    Toast.makeText(mainActivity, "还没有拉取任何一条一言哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_msg_set:
                if (this.curWordBean != null && this.curWordBean.id > 0) {

                    mainActivity.updateAndSetCurWordBean(this.curWordBean);
                } else {
                    Toast.makeText(mainActivity, "还没有拉取任何一条一言哦", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public WordBean getCurBeanCopy() {
        if (curWordBean == null) {
            return null;
        }

        return new WordBean(curWordBean);
    }
}
