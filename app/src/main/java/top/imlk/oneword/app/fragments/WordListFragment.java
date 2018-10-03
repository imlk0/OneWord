package top.imlk.oneword.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.dift.ui.SwipeToAction;
import top.imlk.oneword.R;
import top.imlk.oneword.app.adapter.WordListRVAdapter;
import top.imlk.oneword.app.utils.SmootherScrollLayoutManager;
import top.imlk.oneword.app.view.FixExceptionRecycleView;
import top.imlk.oneword.app.viewmodels.MainActivityViewModel;
import top.imlk.oneword.app.viewmodels.WordListViewModel;
import top.imlk.oneword.dao.OneWordDBHelper;
import top.imlk.oneword.util.AppStyleHelper;

public class WordListFragment extends BottomFragment implements OnLoadMoreListener {


    private static final int ITEM_NUM_INCREASE_STEP = 10;


    @BindView(R.id.rv_word_list)
    FixExceptionRecycleView rvWordList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_nothing_notice)
    TextView tvNothingNotice;


    public enum PageType {
        HISTORY_PAGE,
        FAVOR_PAGE,
    }

    private static final String KEY_PAGE_TYPE = "PageType";
    private PageType pageType;

    private MainActivityViewModel mainActivityViewModel;
    private WordListViewModel wordListViewModel;

    private WordListRVAdapter wordListRVAdapter;


    public WordListFragment() {
    }


    public static WordListFragment newInstance(PageType pageType) {
        WordListFragment fragment = new WordListFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_PAGE_TYPE, pageType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

        wordListViewModel = ViewModelProviders.of(this).get(WordListViewModel.class);

        wordListRVAdapter = new WordListRVAdapter(pageType, mainActivityViewModel, wordListViewModel);
        wordListRVAdapter.setLifecycleOwner(this);


        if (getArguments() != null) {
            pageType = (PageType) getArguments().getSerializable(KEY_PAGE_TYPE);
        } else {
            throw new RuntimeException("WordListFragment PageType was not detected");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_word_list, container, false);

        ButterKnife.bind(this, rootView);


        wordListViewModel.getWordBeans().getValue().clear();


        initView();


        wordListViewModel.getWordBeans().observe(this, wordBeans -> {
            if (wordBeans == null || wordBeans.size() == 0) {
                tvNothingNotice.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.INVISIBLE);
            } else {
                tvNothingNotice.setVisibility(View.INVISIBLE);
                refreshLayout.setVisibility(View.VISIBLE);
            }

        });

        refreshLayout.setNoMoreData(false);
        refreshLayout.autoLoadMore();

        return rootView;
    }

    @Override
    public void onDestroyView() {

        wordListViewModel.getWordBeans().removeObservers(this);
        wordListViewModel.getWordBeans().getValue().clear();
        wordListRVAdapter.notifyDataSetChanged();

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onDetach() {
        super.onDetach();


    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {

        int oldLen = wordListViewModel.getWordBeans().getValue().size();

        if (pageType == PageType.HISTORY_PAGE) {
            wordListViewModel.getWordBeans().getValue().addAll(OneWordDBHelper.querySomeOneWordFromHistory(oldLen, ITEM_NUM_INCREASE_STEP));
        } else {
            wordListViewModel.getWordBeans().getValue().addAll(OneWordDBHelper.querySomeOneWordFromFavor(oldLen, ITEM_NUM_INCREASE_STEP));
        }


        wordListRVAdapter.notifyItemRangeInserted(oldLen, wordListViewModel.getWordBeans().getValue().size() - oldLen);


        if (wordListViewModel.getWordBeans().getValue().size() - oldLen < ITEM_NUM_INCREASE_STEP) {
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.finishLoadMore(400);
        }


        if (oldLen == 0) {// 第一次加载
            rvWordList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rvWordList.smoothScrollToPosition(0);
                }
            }, 500);
        }
    }

    private void initView() {

        this.rvWordList.setLayoutManager(new SmootherScrollLayoutManager(getActivity()));
        this.rvWordList.setHasFixedSize(true);
        this.rvWordList.setAdapter(wordListRVAdapter);

        this.refreshLayout.setEnableRefresh(false);
        this.refreshLayout.setOnLoadMoreListener(this);
        this.refreshLayout.setPrimaryColors(
                AppStyleHelper.getColorByAttributeId(getActivity(), R.attr.color_primary_light),
                AppStyleHelper.getColorByAttributeId(getActivity(), R.attr.color_primary_dark));


        final SwipeToAction swipeToAction = new SwipeToAction(this.rvWordList, new SwipeToAction.SwipeListener<WordListRVAdapter.ViewHolder>() {
            @Override
            public boolean swipeLeft(WordListRVAdapter.ViewHolder oneWordItemHolder) {
                return false;
            }

            @Override
            public boolean swipeRight(WordListRVAdapter.ViewHolder oneWordItemHolder) {
                return false;
            }

            @Override
            public void onClick(WordListRVAdapter.ViewHolder oneWordItemHolder) {
//                        Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(WordListRVAdapter.ViewHolder oneWordItemHolder) {
//                        Toast.makeText(context, "LongClicked", Toast.LENGTH_SHORT).show();
            }

        });
    }


}


