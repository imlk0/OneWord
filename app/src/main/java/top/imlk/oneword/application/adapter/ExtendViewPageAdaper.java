package top.imlk.oneword.application.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import top.imlk.oneword.R;
import top.imlk.oneword.application.client.activity.MainActivity;
import top.imlk.oneword.application.view.OneWordListShowPage;
import top.imlk.oneword.application.view.SettingPage;

/**
 * Created by imlk on 2018/5/20.
 */
public class ExtendViewPageAdaper extends PagerAdapter {

//    private ArrayList<ViewGroup> data;

    private MainActivity mainActivity;

    public OneWordListShowPage historyPage;
    public OneWordListShowPage favorPage;
    public SettingPage settingPage;

    public static int PAGE_COUNT = 3;


    public ExtendViewPageAdaper(MainActivity mainActivity) {

        this.mainActivity = mainActivity;

//        this.data = new ArrayList<>();

        historyPage = (OneWordListShowPage) View.inflate(mainActivity, R.layout.page_oneword_show_list, null);
        favorPage = (OneWordListShowPage) View.inflate(mainActivity, R.layout.page_oneword_show_list, null);
        settingPage = (SettingPage) View.inflate(mainActivity, R.layout.page_setting, null);

        settingPage.init(mainActivity);
        initHistoryPage();
        initFavorPage();
    }

    public void clearAndReloadDataOfRecyclerView(int index) {
        switch (index) {
            case 0:
                historyPage.clearAndReloadData();
                break;
            case 1:
                favorPage.clearAndReloadData();
                break;
        }
    }


    private void initHistoryPage() {
        historyPage.oneWordRecyclerViewAdapter = new OneWordRecyclerViewAdapter(mainActivity, OneWordRecyclerViewAdapter.PageType.HISTORY_PAGE);
        historyPage.recyclerView.setAdapter(historyPage.oneWordRecyclerViewAdapter);
        historyPage.refreshLayout.setOnLoadMoreListener(historyPage.oneWordRecyclerViewAdapter);

    }

    private void initFavorPage() {
        favorPage.oneWordRecyclerViewAdapter = new OneWordRecyclerViewAdapter(mainActivity, OneWordRecyclerViewAdapter.PageType.FAVOR_PAGE);
        favorPage.recyclerView.setAdapter(favorPage.oneWordRecyclerViewAdapter);
        favorPage.refreshLayout.setOnLoadMoreListener(favorPage.oneWordRecyclerViewAdapter);

    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        switch (position) {
            case 0:
                container.removeView(historyPage);
                break;
            case 1:
                container.removeView(favorPage);
                break;
            case 2:
                container.removeView(settingPage);
                break;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        switch (position) {
            case 0:
                container.addView(historyPage);
                return historyPage;
            case 1:
                container.addView(favorPage);
                return favorPage;
            case 2:
                container.addView(settingPage);
                return settingPage;
        }

        return null;
    }


    public void upDateLP(int height) {

        try {

            ViewGroup.LayoutParams layoutParams;

            layoutParams = historyPage.getLayoutParams();
            layoutParams.height = height;
            historyPage.setLayoutParams(layoutParams);

            layoutParams = favorPage.getLayoutParams();
            layoutParams.height = height;
            favorPage.setLayoutParams(layoutParams);

            layoutParams = settingPage.getLayoutParams();
            layoutParams.height = height;
            settingPage.setLayoutParams(layoutParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
