package top.imlk.oneword.view;

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

import top.imlk.oneword.Hitokoto.HitokotoBean;
import top.imlk.oneword.R;
import top.imlk.oneword.client.MainActivity;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;
import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.SharedPreferencesUtil;

/**
 * Created by imlk on 2018/5/26.
 */
public class OneWordShowPanel extends RelativeLayout implements View.OnClickListener {

    private TextView tvMsgMain;
    private TextView tvMsgFrom;

    private ImageView ivLike;
    private ImageView ivSetIt;

    private HitokotoBean curHitokotoBean;
    private Context context;


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

    public void initContext(Context context) {
        this.context = context;
    }

    public void initView() {
        this.tvMsgMain = findViewById(R.id.tv_msg_main);
        this.tvMsgFrom = findViewById(R.id.tv_msg_from);
        this.ivLike = findViewById(R.id.iv_msg_like);
        this.ivSetIt = findViewById(R.id.iv_msg_set);

        HitokotoBean hitokotoBean = SharedPreferencesUtil.readSavedOneWord(this.context);
        if (hitokotoBean != null) {
            this.curHitokotoBean = hitokotoBean;
            this.tvMsgMain.setText(hitokotoBean.hitokoto);
            this.tvMsgMain.setText(hitokotoBean.from);
        }
    }

    public void updateMsgMain(String str) {
        this.tvMsgMain.setText(TextUtils.isEmpty(str) ? "当前一言" : str);
    }

    public void updateMsgFrom(String str) {
        this.tvMsgFrom.setText("——" + (TextUtils.isEmpty(str) ? "出处" : str));
    }

    public void updateCurHitokotoBean(HitokotoBean hitokotoBean) {
        this.curHitokotoBean = hitokotoBean;
    }

    public void updateLike(boolean like) {
        if (like) {
            this.ivLike.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else {
            this.ivLike.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_msg_like:
                if (this.curHitokotoBean != null) {
                    if (this.curHitokotoBean.like) {
                        this.curHitokotoBean.like = false;
                        ((MainActivity) this.context).oneWordSQLiteOpenHelper.remove_one_item(this.curHitokotoBean, OneWordSQLiteOpenHelper.TABLE_LIKE);
                    } else {
                        this.curHitokotoBean.like = true;
                        ((MainActivity) this.context).oneWordSQLiteOpenHelper.insert_to_like(this.curHitokotoBean);
                    }
                    ((MainActivity) this.context).oneWordSQLiteOpenHelper.update_like(this.curHitokotoBean);
                    this.updateLike(this.curHitokotoBean.like);
                } else {
                    Toast.makeText(context, "还没有拉取任何一条一言哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_msg_set:
                if (this.curHitokotoBean != null) {

                    BroadcastSender.sendSetNewLockScreenInfoBroadcast(context, this.curHitokotoBean);
                } else {
                    Toast.makeText(context, "还没有拉取任何一条一言哦", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
