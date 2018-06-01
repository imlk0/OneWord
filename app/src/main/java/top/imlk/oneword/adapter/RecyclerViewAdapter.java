package top.imlk.oneword.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import co.dift.ui.SwipeToAction;
import top.imlk.oneword.Hitokoto.HitokotoBean;
import top.imlk.oneword.R;
import top.imlk.oneword.client.MainActivity;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;

/**
 * Created by imlk on 2018/5/21.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter {

    public enum PageType {
        HISTORY_PAGE,
        LIKE_PAGE,
    }


    private Context context;
    private PageType pageType;

    private ArrayList<HitokotoBean> date = new ArrayList<>();


    public RecyclerViewAdapter(Context context, PageType pageType) {
        this.context = context;
        this.pageType = pageType;
    }

    public void updateAndFill() {
        switch (pageType) {
            case HISTORY_PAGE:
                date = OneWordSQLiteOpenHelper.getInstance(context).get_a_bundle_of_item(OneWordSQLiteOpenHelper.TABLE_HISTORY, 20, 0);
                notifyDataSetChanged();
                break;
            case LIKE_PAGE:
                date = OneWordSQLiteOpenHelper.getInstance(context).get_a_bundle_of_item(OneWordSQLiteOpenHelper.TABLE_LIKE, 20, 0);
                notifyDataSetChanged();
                break;
        }

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrameLayout frameLayout = (FrameLayout) FrameLayout.inflate(context, R.layout.item_oneword, null);

        return new OneWordItemHolder(frameLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OneWordItemHolder oneWordItemHolder = ((OneWordItemHolder) holder);

        ((TextView) oneWordItemHolder.itemView.findViewById(R.id.item_oneword_msg)).setText(date.get(position).hitokoto);
        ((TextView) oneWordItemHolder.itemView.findViewById(R.id.item_oneword_from)).setText("——" + date.get(position).from);
    }

    @Override
    public int getItemCount() {
        return date == null ? 0 : date.size();

    }


    public class OneWordItemHolder extends SwipeToAction.ViewHolder<HitokotoBean> {

        public OneWordItemHolder(View v) {
            super(v);
        }

    }

}
