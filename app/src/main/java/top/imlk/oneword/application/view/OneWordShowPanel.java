package top.imlk.oneword.application.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import top.imlk.oneword.application.client.activity.MainActivity;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.R;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;

/**
 * Created by imlk on 2018/5/26.
 */
public class OneWordShowPanel extends LinearLayout implements View.OnClickListener {

    private TextView tvContent;
    private TextView tvReference;

    private LinearLayout llTargetTag;
    private TextView tvTargetText;

    private ImageView ivFavor;
    private ImageView ivSetIt;

    private WordBean curWordBean;
    private MainActivity mainActivity;


    public OneWordShowPanel(Context context) {
        this(context, null);
    }

    public OneWordShowPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OneWordShowPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OneWordShowPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void init(MainActivity mainActivity) {
        this.mainActivity = mainActivity;


        this.tvContent = findViewById(R.id.tv_content);
        this.tvReference = findViewById(R.id.tv_reference);

        this.tvTargetText = findViewById(R.id.tv_target_text);
        this.llTargetTag = findViewById(R.id.ll_target_tag);
        this.llTargetTag.setOnClickListener(this);


        this.ivFavor = findViewById(R.id.iv_msg_favor);
        this.ivFavor.setOnClickListener(this);
        this.ivSetIt = findViewById(R.id.iv_msg_set);
        this.ivSetIt.setOnClickListener(this);


        if (curWordBean == null) {
            loadAndShowCurneWord();
        }
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

    public void updateContent(String str) {
        this.tvContent.setText(str);
    }

    public void updateReference(String str) {
        if (TextUtils.isEmpty(str)) {
            this.tvReference.setVisibility(GONE);
            this.tvReference.setText("");
        } else {
//            this.tvReference.setVisibility(VISIBLE);
            this.tvReference.setText("——" + str);
        }
    }

    public void updateTarget(String str) {
        if (TextUtils.isEmpty(str)) {
            llTargetTag.setVisibility(GONE);
            tvTargetText.setText("");
        } else {
            llTargetTag.setVisibility(VISIBLE);
            tvTargetText.setText(str);
        }
    }

    public void updateCurWordBeanOnUI(WordBean wordBean) {

        this.curWordBean = wordBean;
        this.updateLike(OneWordSQLiteOpenHelper.getInstance().checkIfInFavor(wordBean.id));
        this.updateContent(wordBean.content);
        this.updateReference(wordBean.reference);
        this.updateTarget(wordBean.target_text);
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
            case R.id.ll_target_tag:
                if (!TextUtils.isEmpty(curWordBean.target_url)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(curWordBean.target_url));
                    mainActivity.startActivity(intent);
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

    public void checkIfCurBeanFavorStateChanged(WordBean wordBean) {
        if (curWordBean != null && wordBean.id == curWordBean.id) {
            updateCurWordBeanOnUI(wordBean);
        }
    }

    public void checkIfCurBeanRemoved(WordBean wordBean) {
        if (curWordBean != null && wordBean.id == curWordBean.id) {
            loadAndShowCurneWord();
        }
    }

}
