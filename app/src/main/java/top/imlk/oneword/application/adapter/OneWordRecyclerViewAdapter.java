package top.imlk.oneword.application.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import top.imlk.oneword.util.ShareUtil;


/**
 * Created by imlk on 2018/5/21.
 */
public class OneWordRecyclerViewAdapter extends RecyclerView.Adapter implements OnLoadMoreListener {

    private RecyclerView recyclerView;

    private static final int ITEM_NUM_INCREASE_STEP = 10;

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

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public void clearData() {
        switch (pageType) {
            case HISTORY_PAGE:
                wordBeans.clear();
                notifyDataSetChanged();

                break;
            case FAVOR_PAGE:
                wordBeans.clear();
                notifyDataSetChanged();

                break;
        }

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        OneWordItemHolder holder;
        if (PageType.HISTORY_PAGE == pageType) {

            holder = new OneWordItemHolder(View.inflate(mainActivity, R.layout.item_history_oneword, null));

            holder.ivFavor = holder.itemView.findViewById(R.id.iv_favor);
            holder.ivFavor.setOnClickListener(holder);

        } else {
            holder = new OneWordItemHolder(View.inflate(mainActivity, R.layout.item_like_oneword, null));

        }


        holder.tvContent = holder.itemView.findViewById(R.id.tv_content);
        holder.tvReference = holder.itemView.findViewById(R.id.tv_reference);
        holder.ivDelete = holder.itemView.findViewById(R.id.iv_delete);
        holder.ivSet = holder.itemView.findViewById(R.id.iv_set);
        holder.ivShare = holder.itemView.findViewById(R.id.iv_share);

        holder.tvTargetName = holder.itemView.findViewById(R.id.tv_target_text);
        holder.llTargetTag = holder.itemView.findViewById(R.id.ll_target_tag);
        holder.llTargetTag.setOnClickListener(holder);

        holder.ivDelete.setOnClickListener(holder);
        holder.ivSet.setOnClickListener(holder);
        holder.ivShare.setOnClickListener(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OneWordItemHolder oneWordItemHolder = ((OneWordItemHolder) holder);

        oneWordItemHolder.recoverToDefaultForm();

        oneWordItemHolder.data = wordBeans.get(position);


        oneWordItemHolder.tvContent.setText(oneWordItemHolder.data.content);
        oneWordItemHolder.tvReference.setText("——" + oneWordItemHolder.data.reference);


        if (TextUtils.isEmpty(oneWordItemHolder.data.target_text)) {
            oneWordItemHolder.llTargetTag.setVisibility(View.GONE);
            oneWordItemHolder.tvTargetName.setText("");
        } else {
            oneWordItemHolder.llTargetTag.setVisibility(View.VISIBLE);
            oneWordItemHolder.tvTargetName.setText(oneWordItemHolder.data.target_text);
        }


        if (pageType == PageType.HISTORY_PAGE) {
            oneWordItemHolder.updateFavorStateImage(OneWordSQLiteOpenHelper.getInstance().checkIfInFavor(oneWordItemHolder.data.id));
        }

    }

    @Override
    public int getItemCount() {
        return wordBeans == null ? 0 : wordBeans.size();

    }


    public class OneWordItemHolder extends SwipeToAction.ViewHolder<WordBean> implements View.OnClickListener {

//        public boolean isClosed = true;

        public TextView tvContent;
        public TextView tvReference;
        public ImageView ivDelete;
        public ImageView ivSet;
        public ImageView ivShare;
        public ImageView ivFavor;


        public LinearLayout llTargetTag;
        public TextView tvTargetName;


        public OneWordItemHolder(View v) {
            super(v);
        }

        public void updateFavorStateImage(boolean favor) {
            ivFavor.setImageResource(favor ? R.drawable.ic_favorite_white_48dp : R.drawable.ic_favorite_border_white_48dp);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.iv_share:

                    ShareUtil.shareOneWord(mainActivity, this.data);
                    break;

                case R.id.iv_set:

                    mainActivity.updateAndSetCurWordBean(this.data);

                    break;
                case R.id.iv_favor:
                    if (pageType == PageType.FAVOR_PAGE) {
                        //不存在的操作
                        break;
                    } else {

                        boolean favor = OneWordSQLiteOpenHelper.getInstance().checkIfInFavor(this.data.id);

                        if (favor) {
                            OneWordSQLiteOpenHelper.getInstance().removeFromFavor(this.data.id);
                        } else {
                            OneWordSQLiteOpenHelper.getInstance().insertToFavor(this.data);
                        }

                        mainActivity.checkIfCurBeanFavorStateChanged(this.data);

                        updateFavorStateImage(!favor);

                        Toast.makeText(mainActivity, favor ? "不喜欢" : "喜欢", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case R.id.iv_delete:

                    if (pageType == PageType.FAVOR_PAGE) {

                        OneWordSQLiteOpenHelper.getInstance().removeFromFavor(this.data.id);
                        mainActivity.checkIfCurBeanFavorStateChanged(this.data);
                    } else {
                        OneWordSQLiteOpenHelper.getInstance().removeFromHistory(this.data.id);
                        mainActivity.checkIfCurBeanRemoved(this.data);
                    }

                    int ind = wordBeans.indexOf(this.data);
                    wordBeans.remove(this.data);
                    OneWordRecyclerViewAdapter.this.notifyItemRemoved(ind);


                    break;
                case R.id.ll_target_tag:
                    if (!TextUtils.isEmpty(data.target_url)) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.target_url));
                        mainActivity.startActivity(intent);
                    }
                    break;
            }
        }


        public void recoverToDefaultForm() {
            this.front.setX(0);
            this.revealRight.setVisibility(View.GONE);
            this.revealLeft.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

        int oldLen = wordBeans.size();

        if (pageType == PageType.HISTORY_PAGE) {
            wordBeans.addAll(OneWordSQLiteOpenHelper.getInstance().querySomeOneWordFromHistory(oldLen, ITEM_NUM_INCREASE_STEP));
        } else {
            wordBeans.addAll(OneWordSQLiteOpenHelper.getInstance().querySomeOneWordFromFavor(oldLen, ITEM_NUM_INCREASE_STEP));
        }


        this.notifyItemRangeInserted(oldLen, wordBeans.size() - oldLen);


        if (wordBeans.size() - oldLen < ITEM_NUM_INCREASE_STEP) {
//            refreshLayout.setNoMoreData(true);
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.finishLoadMore(400);
        }


        if (oldLen == 0) {
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.smoothScrollToPosition(0);
                }
            }, 500);
        }
    }


}
