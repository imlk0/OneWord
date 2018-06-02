package top.imlk.oneword.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.dift.ui.SwipeToAction;
import top.imlk.oneword.Hitokoto.HitokotoBean;
import top.imlk.oneword.R;
import top.imlk.oneword.client.MainActivity;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;

import static top.imlk.oneword.dao.OneWordSQLiteOpenHelper.TABLE_HISTORY;
import static top.imlk.oneword.dao.OneWordSQLiteOpenHelper.TABLE_LIKE;

/**
 * Created by imlk on 2018/5/21.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter {


    private final RecyclerView recyclerView;

    public enum PageType {
        HISTORY_PAGE,
        LIKE_PAGE,
    }

    private Context context;

    private PageType pageType;
    private ArrayList<HitokotoBean> dateList = new ArrayList<>();


    public RecyclerViewAdapter(Context context, PageType pageType, RecyclerView recyclerView) {
        this.context = context;
        this.pageType = pageType;
        this.recyclerView = recyclerView;
    }

    public void updateAndFill() {
        switch (pageType) {
            case HISTORY_PAGE:
                dateList = OneWordSQLiteOpenHelper.getInstance(context).get_a_bundle_of_item(OneWordSQLiteOpenHelper.TABLE_HISTORY, 20, 0);
                notifyDataSetChanged();
                break;
            case LIKE_PAGE:
                dateList = OneWordSQLiteOpenHelper.getInstance(context).get_a_bundle_of_item(OneWordSQLiteOpenHelper.TABLE_LIKE, 20, 0);
                notifyDataSetChanged();
                break;
        }

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrameLayout frameLayout;

        if (PageType.HISTORY_PAGE == pageType) {
            frameLayout = (FrameLayout) FrameLayout.inflate(context, R.layout.item_history_oneword, null);
        } else {
            frameLayout = (FrameLayout) FrameLayout.inflate(context, R.layout.item_like_oneword, null);
        }

        return new OneWordItemHolder(frameLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OneWordItemHolder oneWordItemHolder = ((OneWordItemHolder) holder);

        ((OneWordItemHolder) holder).data = dateList.get(position);

        ((TextView) oneWordItemHolder.itemView.findViewById(R.id.item_oneword_msg)).setText(dateList.get(position).hitokoto);
        ((TextView) oneWordItemHolder.itemView.findViewById(R.id.item_oneword_from)).setText("——" + dateList.get(position).from);
        oneWordItemHolder.itemView.findViewById(R.id.item_delete).setOnClickListener(oneWordItemHolder);
        oneWordItemHolder.itemView.findViewById(R.id.item_set).setOnClickListener(oneWordItemHolder);
        oneWordItemHolder.itemView.findViewById(R.id.item_share).setOnClickListener(oneWordItemHolder);
        if (pageType == PageType.HISTORY_PAGE) {
            oneWordItemHolder.itemView.findViewById(R.id.item_like_state).setOnClickListener(oneWordItemHolder);
        }


    }

    @Override
    public int getItemCount() {
        return dateList == null ? 0 : dateList.size();

    }


    public class OneWordItemHolder extends SwipeToAction.ViewHolder<HitokotoBean> implements View.OnClickListener {

        public OneWordItemHolder(View v) {
            super(v);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.item_share:
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(ClipData.newPlainText("一言", this.data.hitokoto + "\n——" + this.data.from));
                    Toast.makeText(context, "成功复制到剪切板", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.item_set:
                    ((MainActivity) context).oneWordShowPanel.updateStateByBean(this.data);
                    ((MainActivity) context).pastedNestedScrollView.scrollToTop();
                    OneWordSQLiteOpenHelper.getInstance(context).insert_one_item(TABLE_HISTORY, this.data);
                    break;
                case R.id.item_like_state:
                    if (pageType == PageType.LIKE_PAGE) {
                        //不存在的操作
                        break;
                    } else {

                        this.data.like = !this.data.like;

                        OneWordSQLiteOpenHelper.getInstance(context).refresh_like_state(this.data);

                        if (this.data.like) {

                            OneWordSQLiteOpenHelper.getInstance(context).insert_one_item(TABLE_LIKE, this.data);
                        } else {
                            OneWordSQLiteOpenHelper.getInstance(context).remove_one_item(TABLE_LIKE, this.data);
                        }

                        ((MainActivity) context).oneWordShowPanel.afterRecyclerViewChangeLikeStateOperate(this.data);

                    }

                    break;

                case R.id.item_delete:

                    if (pageType == PageType.LIKE_PAGE) {
                        this.data.like = false;
                        OneWordSQLiteOpenHelper.getInstance(context).refresh_like_state(this.data);
                        OneWordSQLiteOpenHelper.getInstance(context).remove_one_item(TABLE_LIKE, this.data);
                        ((MainActivity) context).oneWordShowPanel.afterRecyclerViewChangeLikeStateOperate(this.data);
                    } else {
                        OneWordSQLiteOpenHelper.getInstance(context).remove_one_item(TABLE_HISTORY, this.data);
                    }

                    int ind = dateList.indexOf(this.data);
                    dateList.remove(this.data);
                    RecyclerViewAdapter.this.notifyItemRemoved(ind);


                    break;
            }
        }
    }


}
