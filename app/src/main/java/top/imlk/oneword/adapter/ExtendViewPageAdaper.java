package top.imlk.oneword.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;

import co.dift.ui.SwipeToAction;
import top.imlk.oneword.R;
import top.imlk.oneword.util.StyleHelper;

/**
 * Created by imlk on 2018/5/20.
 */
public class ExtendViewPageAdaper extends PagerAdapter {

    private ArrayList<ViewGroup> data;

    private Context context;


    public ExtendViewPageAdaper(Context context) {

        this.context = context;

        this.data = new ArrayList<>();

        data.add((ViewGroup) LinearLayout.inflate(context, R.layout.history_page, null));
        data.add((ViewGroup) LinearLayout.inflate(context, R.layout.like_page, null));
        data.add((ViewGroup) LinearLayout.inflate(context, R.layout.setting_page, null));

        initViewGroup(0);
        initViewGroup(1);
        initViewGroup(2);
    }

    public void clearAndFillRecyclerView(int index) {
        switch (index) {
            case 0:
                ((RecyclerViewAdapter) ((RecyclerView) data.get(index).findViewById(R.id.rv_on_history_page)).getAdapter()).clearAndFill();
                break;
            case 1:
                ((RecyclerViewAdapter) ((RecyclerView) data.get(index).findViewById(R.id.rv_on_like_page)).getAdapter()).clearAndFill();
                break;
        }
    }

    public void initViewGroup(int index) {
        switch (index) {
            case 0:
                final RecyclerView recyclerView = data.get(0).findViewById(R.id.rv_on_history_page);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setHasFixedSize(true);
                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(context, RecyclerViewAdapter.PageType.HISTORY_PAGE);
                RefreshLayout refreshLayout = data.get(0).findViewById(R.id.refreshLayout);
                refreshLayout.setEnableRefresh(false);
                refreshLayout.setPrimaryColors(
                        StyleHelper.getColorByAttributeId(context, R.attr.primary_light),
                        StyleHelper.getColorByAttributeId(context, R.attr.colorPrimaryDark));
                refreshLayout.setOnLoadMoreListener(recyclerViewAdapter);
                recyclerView.setAdapter(recyclerViewAdapter);
                final SwipeToAction swipeToAction = new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener<RecyclerViewAdapter.OneWordItemHolder>() {
                    @Override
                    public boolean swipeLeft(RecyclerViewAdapter.OneWordItemHolder oneWordItemHolder) {
                        return false;
                    }

                    @Override
                    public boolean swipeRight(RecyclerViewAdapter.OneWordItemHolder oneWordItemHolder) {
                        return false;
                    }

                    @Override
                    public void onClick(RecyclerViewAdapter.OneWordItemHolder oneWordItemHolder) {
//                        Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongClick(RecyclerViewAdapter.OneWordItemHolder oneWordItemHolder) {
//                        Toast.makeText(context, "LongClicked", Toast.LENGTH_SHORT).show();
                    }

                });

                break;
            case 1:

                RecyclerView recyclerView_1 = data.get(1).findViewById(R.id.rv_on_like_page);
                recyclerView_1.setLayoutManager(new LinearLayoutManager(context));
                recyclerView_1.setHasFixedSize(true);
                RecyclerViewAdapter recyclerViewAdapter_1 = new RecyclerViewAdapter(context, RecyclerViewAdapter.PageType.LIKE_PAGE);
                RefreshLayout refreshLayout_1 = data.get(1).findViewById(R.id.refreshLayout);
                refreshLayout_1.setEnableRefresh(false);
                refreshLayout_1.setPrimaryColors(
                        StyleHelper.getColorByAttributeId(context, R.attr.primary_light),
                        StyleHelper.getColorByAttributeId(context, R.attr.colorPrimaryDark));
                refreshLayout_1.setOnLoadMoreListener(recyclerViewAdapter_1);
                recyclerView_1.setAdapter(recyclerViewAdapter_1);
                SwipeToAction swipeToAction_1 = new SwipeToAction(recyclerView_1, new SwipeToAction.SwipeListener<RecyclerViewAdapter.OneWordItemHolder>() {
                    @Override
                    public boolean swipeLeft(RecyclerViewAdapter.OneWordItemHolder oneWordItemHolder) {
                        return false;
                    }

                    @Override
                    public boolean swipeRight(RecyclerViewAdapter.OneWordItemHolder oneWordItemHolder) {
                        return false;
                    }

                    @Override
                    public void onClick(RecyclerViewAdapter.OneWordItemHolder oneWordItemHolder) {
//                        Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongClick(RecyclerViewAdapter.OneWordItemHolder oneWordItemHolder) {
//                        Toast.makeText(context, "LongClicked", Toast.LENGTH_SHORT).show();
                    }

                });
                break;
            case 2:
                break;
        }
    }

    public void upDateLP(int height) {

        try {

            ViewGroup.LayoutParams layoutParams;

            layoutParams = data.get(0).getLayoutParams();
            layoutParams.height = height;
            data.get(0).setLayoutParams(layoutParams);

            layoutParams = data.get(1).getLayoutParams();
            layoutParams.height = height;
            data.get(1).setLayoutParams(layoutParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(data.get(position));
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(data.get(position));
        return data.get(position);
    }


}
