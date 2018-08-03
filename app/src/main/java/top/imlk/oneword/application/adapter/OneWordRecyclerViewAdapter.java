package top.imlk.oneword.application.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;

import co.dift.ui.SwipeToAction;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.R;
import top.imlk.oneword.application.client.activity.MainActivity;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;
import top.imlk.oneword.util.BroadcastSender;


/**
 * Created by imlk on 2018/5/21.
 */
public class OneWordRecyclerViewAdapter extends RecyclerView.Adapter implements OnLoadMoreListener {

//    private final RecyclerView recyclerView;

    public enum PageType {
        HISTORY_PAGE,
        FAVOR_PAGE,
    }

    private MainActivity mainActivity;

    private PageType pageType;
    private ArrayList<WordBean> wordBeans = new ArrayList<>();


    public OneWordRecyclerViewAdapter(MainActivity mainActivity, PageType pageType) {
        this.mainActivity = mainActivity;
        this.pageType = pageType;
//        this.recyclerView = recyclerView;
    }

    public void clearAndFill() {
        switch (pageType) {
            case HISTORY_PAGE:
                wordBeans = OneWordSQLiteOpenHelper.getInstance(mainActivity).querySomeOneWordFromHistory(20, 0);
                notifyDataSetChanged();
                break;
            case FAVOR_PAGE:
                wordBeans = OneWordSQLiteOpenHelper.getInstance(mainActivity).querySomeOneWordFromFavor(20, 0);
                notifyDataSetChanged();
                break;
        }

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrameLayout frameLayout;

        if (PageType.HISTORY_PAGE == pageType) {
            frameLayout = (FrameLayout) FrameLayout.inflate(mainActivity, R.layout.item_history_oneword, null);
        } else {
            frameLayout = (FrameLayout) FrameLayout.inflate(mainActivity, R.layout.item_like_oneword, null);
        }

        return new OneWordItemHolder(frameLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OneWordItemHolder oneWordItemHolder = ((OneWordItemHolder) holder);

        oneWordItemHolder.data = wordBeans.get(position);

        ((TextView) oneWordItemHolder.itemView.findViewById(R.id.item_oneword_msg)).setText(wordBeans.get(position).content);
        ((TextView) oneWordItemHolder.itemView.findViewById(R.id.item_oneword_from)).setText("——" + wordBeans.get(position).reference);
        oneWordItemHolder.itemView.findViewById(R.id.item_delete).setOnClickListener(oneWordItemHolder);
        oneWordItemHolder.itemView.findViewById(R.id.item_set).setOnClickListener(oneWordItemHolder);
        oneWordItemHolder.itemView.findViewById(R.id.item_share).setOnClickListener(oneWordItemHolder);
        if (pageType == PageType.HISTORY_PAGE) {
            oneWordItemHolder.itemView.findViewById(R.id.item_favor_state).setOnClickListener(oneWordItemHolder);
            oneWordItemHolder.updateFavorStateImage(OneWordSQLiteOpenHelper.getInstance(mainActivity).checkIfInFavor(oneWordItemHolder.data.id));
        }


    }

    @Override
    public int getItemCount() {
        return wordBeans == null ? 0 : wordBeans.size();

    }


    public class OneWordItemHolder extends SwipeToAction.ViewHolder<WordBean> implements View.OnClickListener {

//        public boolean isClosed = true;


        public OneWordItemHolder(View v) {
            super(v);
        }

        public void updateFavorStateImage(boolean favor) {
            ((ImageView) itemView.findViewById(R.id.item_favor_state)).setImageResource(favor ? R.drawable.ic_favorite_white_48dp : R.drawable.ic_favorite_border_white_48dp);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.item_share:
                    ClipboardManager cm = (ClipboardManager) mainActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(ClipData.newPlainText("一言", this.data.content + "\n——" + this.data.reference));
                    Toast.makeText(mainActivity, "成功复制到剪切板", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.item_set:

                    mainActivity.updateCurWordBeanWithoutSend(this.data);
                    BroadcastSender.sendSetNewLockScreenInfoBroadcast(mainActivity, this.data);

                    break;
                case R.id.item_favor_state:
                    if (pageType == PageType.FAVOR_PAGE) {
                        //不存在的操作
                        break;
                    } else {

                        boolean favor = OneWordSQLiteOpenHelper.getInstance(mainActivity).checkIfInFavor(this.data.id);

                        if (favor) {
                            OneWordSQLiteOpenHelper.getInstance(mainActivity).removeFromFavor(this.data.id);
                        } else {
                            OneWordSQLiteOpenHelper.getInstance(mainActivity).insertToFavor(this.data);
                        }

                        mainActivity.checkIfCurBeanFavorStateChanged(this.data);

                        updateFavorStateImage(!favor);

                        Toast.makeText(mainActivity, favor ? "不喜欢" : "喜欢", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case R.id.item_delete:

                    if (pageType == PageType.FAVOR_PAGE) {

                        OneWordSQLiteOpenHelper.getInstance(mainActivity).removeFromFavor(this.data.id);
                        mainActivity.checkIfCurBeanFavorStateChanged(this.data);
                    } else {
                        OneWordSQLiteOpenHelper.getInstance(mainActivity).removeFromHistory(this.data.id);
                    }

                    int ind = wordBeans.indexOf(this.data);
                    wordBeans.remove(this.data);
                    this.front.setX(0);
                    this.revealRight.setVisibility(View.GONE);
                    this.revealLeft.setVisibility(View.GONE);
                    OneWordRecyclerViewAdapter.this.notifyItemRemoved(ind);


                    break;
            }
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

        int oldLen = wordBeans.size();

        if (pageType == PageType.HISTORY_PAGE) {
            wordBeans.addAll(OneWordSQLiteOpenHelper.getInstance(mainActivity).querySomeOneWordFromHistory(oldLen, 20));
        } else {
            wordBeans.addAll(OneWordSQLiteOpenHelper.getInstance(mainActivity).querySomeOneWordFromFavor(oldLen, 20));
        }

        this.notifyItemRangeInserted(oldLen, wordBeans.size() - oldLen);

        refreshLayout.finishLoadMore(1000);
    }


}
