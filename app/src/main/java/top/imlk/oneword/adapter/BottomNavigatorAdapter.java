package top.imlk.oneword.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;

import top.imlk.oneword.Hitokoto.HitokotoApi;
import top.imlk.oneword.R;
import top.imlk.oneword.client.MainActivity;
import top.imlk.oneword.view.BottomNavigatorItem;

/**
 * Created by imlk on 2018/5/20.
 */
public class BottomNavigatorAdapter extends CommonNavigatorAdapter implements View.OnClickListener, CommonPagerTitleView.OnPagerTitleChangeListener {

    private Context context;

    private ArrayList<CommonPagerTitleView> data;

    public BottomNavigatorAdapter(Context context) {
        this.context = context;

        data = new ArrayList<>();

        CommonPagerTitleView commonPagerTitleView;


        commonPagerTitleView = new CommonPagerTitleView(context);
        commonPagerTitleView.setContentView(((BottomNavigatorItem) LinearLayout.inflate(context, R.layout.item_bottom_selector, null)).setImage(R.drawable.ic_view_list_white_24dp).setBgColor(R.color.btn_bg_color_unselected).setText("历史").setIndex(0));
        data.add(commonPagerTitleView);
        commonPagerTitleView = new CommonPagerTitleView(context);
        commonPagerTitleView.setContentView(((BottomNavigatorItem) LinearLayout.inflate(context, R.layout.item_bottom_selector, null)).setImage(R.drawable.ic_favorite_border_white_24dp).setBgColor(R.color.btn_bg_color_unselected).setText("喜欢的").setIndex(1));
        data.add(commonPagerTitleView);
        commonPagerTitleView = new CommonPagerTitleView(context);
        commonPagerTitleView.setContentView(((BottomNavigatorItem) LinearLayout.inflate(context, R.layout.item_bottom_selector, null)).setImage(R.drawable.ic_refresh_white_24dp).setBgColor(R.color.btn_bg_color_unselected).setText("再来一条").setIndex(2));
        data.add(commonPagerTitleView);
        commonPagerTitleView = new CommonPagerTitleView(context);
        commonPagerTitleView.setContentView(((BottomNavigatorItem) LinearLayout.inflate(context, R.layout.item_bottom_selector, null)).setImage(R.drawable.ic_info_outline_white_24dp).setBgColor(R.color.btn_bg_color_unselected).setText("关于应用").setIndex(3));
        data.add(commonPagerTitleView);

        for (int i = 0; i < data.size(); ++i) {
            data.get(i).setOnClickListener(this);
            data.get(i).setOnPagerTitleChangeListener(this);
        }

    }


    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, int i) {
        return data.get(i);
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        return null;
    }

    @Override
    public void onClick(View v) {
        //TODO

//        ((CommonPagerTitleView) v).getChildAt(0).setBackgroundResource(R.color.btn_bg_color_selected);

        if (v == data.get(0)) {
            ((MainActivity) context).gotoPage(0);
        } else if (v == data.get(1)) {
            ((MainActivity) context).gotoPage(1);
        } else if (v == data.get(2)) {

            try {
                HitokotoApi.getAnime((MainActivity) context);
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (v == data.get(3)) {
            ((MainActivity) context).gotoPage(2);

        }

    }


    @Override
    public void onSelected(int index, int totalCount) {
        this.data.get(index).getChildAt(0).setBackgroundResource(R.color.btn_bg_color_selected);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        this.data.get(index).getChildAt(0).setBackgroundResource(R.color.btn_bg_color_unselected);
    }

    @Override
    public void onLeave(int index, int totalCount, float enterPercent, boolean leftToRight) {

    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

    }


    public void upDateLP(Rect rect) {//设置底部按钮宽度均摊
        for (int i = 0; i < data.size(); ++i) {
            ViewGroup.LayoutParams layoutParams = data.get(i).getLayoutParams();
            layoutParams.width = rect.width() / data.size();
            data.get(i).setLayoutParams(layoutParams);
        }
    }
}
