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

import top.imlk.oneword.net.Hitokoto.HitokotoBean;
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

    public HitokotoBean curHitokotoBean;
    private Context context;


    public OneWordShowPanel(Context context) {
        super(context);
        updateContext(context);
    }

    public OneWordShowPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        updateContext(context);
    }

    public OneWordShowPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        updateContext(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OneWordShowPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        updateContext(context);
    }

    public void updateContext(Context context) {
        this.context = context;
    }

    public void initView() {
        this.tvMsgMain = findViewById(R.id.tv_msg_main);
        this.tvMsgFrom = findViewById(R.id.tv_msg_from);

        this.ivLike = findViewById(R.id.iv_msg_like);
        this.ivLike.setOnClickListener(this);
        this.ivSetIt = findViewById(R.id.iv_msg_set);
        this.ivSetIt.setOnClickListener(this);

        HitokotoBean hitokotoBean = SharedPreferencesUtil.readSavedOneWord(this.context);

        if (hitokotoBean != null) {
            updateStateByBean(hitokotoBean);
        }
    }


    public void updateStateByBean(HitokotoBean hitokotoBean) {
        if (OneWordSQLiteOpenHelper.getInstance(context).query_one_item_exist(OneWordSQLiteOpenHelper.TABLE_LIKE, hitokotoBean)) {
            hitokotoBean.like = true;
        }
        this.updateCurHitokotoBean(hitokotoBean);
        this.updateMsgMain(hitokotoBean.hitokoto);
        this.updateMsgFrom(hitokotoBean.from);
        this.updateLike(hitokotoBean.like);
    }


    public void afterRecyclerViewChangeLikeStateOperate(HitokotoBean hitokotoBean) {
        if (hitokotoBean == null) {
            return;
        }
        if (hitokotoBean.id == curHitokotoBean.id && hitokotoBean.type != null && hitokotoBean.type.equals(curHitokotoBean.type)) {
            updateStateByBean(hitokotoBean);
            HitokotoBean saved = SharedPreferencesUtil.readSavedOneWord(context);
            if (saved != null && hitokotoBean.id == saved.id && hitokotoBean.type != null && hitokotoBean.type.equals(saved.type)) {
                SharedPreferencesUtil.saveCurOneWord(context, hitokotoBean);
            }
        }
    }


    public void updateMsgMain(String str) {
        this.tvMsgMain.setText(TextUtils.isEmpty(str) ? "  当前一言" : "  " + str);
    }

    public void updateMsgFrom(String str) {
        this.tvMsgFrom.setText("——" + (TextUtils.isEmpty(str) ? "出处 " : str + " "));
    }

    public void updateCurHitokotoBean(HitokotoBean hitokotoBean) {
        this.curHitokotoBean = hitokotoBean;
    }

    public void updateLike(boolean like) {
        if (like) {
            this.ivLike.setImageResource(R.drawable.ic_favorite_white_48dp);
        } else {
            this.ivLike.setImageResource(R.drawable.ic_favorite_border_white_48dp);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_msg_like:
                if (this.curHitokotoBean != null) {
                    if (this.curHitokotoBean.like) {
                        this.curHitokotoBean.like = false;
                        OneWordSQLiteOpenHelper.getInstance(context).remove_one_item(OneWordSQLiteOpenHelper.TABLE_LIKE, this.curHitokotoBean);
                    } else {
                        this.curHitokotoBean.like = true;
                        OneWordSQLiteOpenHelper.getInstance(context).insert_one_item(OneWordSQLiteOpenHelper.TABLE_LIKE, this.curHitokotoBean);
                    }
                    OneWordSQLiteOpenHelper.getInstance(context).refresh_like_state(this.curHitokotoBean);
                    this.updateLike(this.curHitokotoBean.like);
                } else {
                    Toast.makeText(context, "还没有拉取任何一条一言哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_msg_set:
                if (this.curHitokotoBean != null) {

                    Toast.makeText(context, "设置锁屏一言中...", Toast.LENGTH_SHORT).show();

                    SharedPreferencesUtil.saveCurOneWord(context, curHitokotoBean);
                    BroadcastSender.sendSetNewLockScreenInfoBroadcast(context, this.curHitokotoBean);
                } else {
                    Toast.makeText(context, "还没有拉取任何一条一言哦", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
