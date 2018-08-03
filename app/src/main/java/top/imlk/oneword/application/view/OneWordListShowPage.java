package top.imlk.oneword.application.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

import co.dift.ui.SwipeToAction;
import top.imlk.oneword.R;
import top.imlk.oneword.application.adapter.OneWordRecyclerViewAdapter;
import top.imlk.oneword.util.StyleHelper;

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

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        recyclerView = findViewById(R.id.rv_on_history_page);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setPrimaryColors(
                StyleHelper.getColorByAttributeId(getContext(), R.attr.primary_light),
                StyleHelper.getColorByAttributeId(getContext(), R.attr.colorPrimaryDark));
        refreshLayout.setOnLoadMoreListener(oneWordRecyclerViewAdapter);

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
}
