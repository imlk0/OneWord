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

/**
 * Created by imlk on 2018/5/21.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;

    private ArrayList<HitokotoBean> date = new ArrayList<>();


    public RecyclerViewAdapter(Context context) {
        this.context = context;

        fillData();
    }

    private void fillData() {
        for (int i = 0; i < 20; ++i) {
            date.add(new HitokotoBean(i, "test " + i, "type " + i, "from " + i, "creator " + i, "create at " + i, i % 2 == 1));
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
        ((TextView) oneWordItemHolder.itemView.findViewById(R.id.item_oneword_from)).setText(date.get(position).from);
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
