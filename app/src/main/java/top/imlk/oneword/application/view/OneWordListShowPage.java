package top.imlk.oneword.application.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

import co.dift.ui.SwipeToAction;
import top.imlk.oneword.R;
import top.imlk.oneword.application.adapter.OneWordRecyclerViewAdapter;
import top.imlk.oneword.util.AppStyleHelper;

/**
 * Created by imlk on 2018/8/3.
 */
public class OneWordListShowPage extends LinearLayout {

    public RecyclerView recyclerView;
    public OneWordRecyclerViewAdapter oneWordRecyclerViewAdapter;
    public RefreshLayout refreshLayout;

    public OneWordListShowPage(Context context) {
        super(context);
    }

    public OneWordListShowPage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OneWordListShowPage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OneWordListShowPage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    static class SmootherScrollLayoutManager extends LinearLayoutManager {

        public SmootherScrollLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView,
                                           RecyclerView.State state, final int position) {
            LinearSmoothScroller smoothScroller =
                    new LinearSmoothScroller(recyclerView.getContext()) {
                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                            return 150f / displayMetrics.densityDpi;
                        }
                    };

            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        recyclerView = findViewById(R.id.rv_on_history_page);
        recyclerView.setLayoutManager(new SmootherScrollLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);


        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setPrimaryColors(
                AppStyleHelper.getColorByAttributeId(getContext(), R.attr.color_primary_light),
                AppStyleHelper.getColorByAttributeId(getContext(), R.attr.colorPrimaryDark));

        final SwipeToAction swipeToAction = new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener<OneWordRecyclerViewAdapter.OneWordItemHolder>() {
            @Override
            public boolean swipeLeft(OneWordRecyclerViewAdapter.OneWordItemHolder oneWordItemHolder) {
                return false;
            }

            @Override
            public boolean swipeRight(OneWordRecyclerViewAdapter.OneWordItemHolder oneWordItemHolder) {
                return false;
            }

            @Override
            public void onClick(OneWordRecyclerViewAdapter.OneWordItemHolder oneWordItemHolder) {
//                        Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(OneWordRecyclerViewAdapter.OneWordItemHolder oneWordItemHolder) {
//                        Toast.makeText(context, "LongClicked", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void clearAndReloadData() {
        oneWordRecyclerViewAdapter.clearData();
        refreshLayout.setNoMoreData(false);
        refreshLayout.autoLoadMore();
    }

}
