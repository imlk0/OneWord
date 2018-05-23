package top.imlk.oneword.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import top.imlk.oneword.R;
import top.imlk.oneword.adapter.BottomNavigatorAdapter;
import top.imlk.oneword.adapter.ExtendViewPageAdaper;
import top.imlk.oneword.client.MainActivity;

/**
 * Created by imlk on 2018/5/20.
 */
public class MainOneWordView extends LinearLayout implements NestedScrollingParent {

    private NestedScrollingParentHelper nestedScrollingParentHelper;

    private LinearLayout llMsgBottom;


    private LinearLayout llPageMain;
    private LinearLayout llPageExtend;

    private MainActivity context;

    private MagicIndicator bottomMagicIndicator;

    private BottomNavigatorAdapter bottomNavigatorAdapter;

    private ExtendViewPageAdaper extendViewPageAdaper;

    private ViewPager viewPager;

    public MainOneWordView(Context context) {
        super(context);
        init(context);

    }

    public MainOneWordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public MainOneWordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MainOneWordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);

    }

    private void init(Context context) {
        this.context = (MainActivity) context;
        this.nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }

    public void upDateLP(Rect rect) {


        if (llPageMain != null) {

            ViewGroup.LayoutParams layoutParams = llPageMain.getLayoutParams();
            layoutParams.height = rect.height();
            llPageMain.setLayoutParams(layoutParams);

        }
        if (llPageExtend != null) {

            ViewGroup.LayoutParams layoutParams = llPageExtend.getLayoutParams();
            layoutParams.height = rect.height() - (this.llMsgBottom == null ? 0 : this.llMsgBottom.getHeight());
            llPageExtend.setLayoutParams(layoutParams);
        }

        if (bottomNavigatorAdapter != null) {
            bottomNavigatorAdapter.upDateLP(rect);
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();


        llMsgBottom = findViewById(R.id.ll_msg_bottom);
        if (llMsgBottom == null) {
            return;//第一次调用
        }

        llPageMain = findViewById(R.id.ll_page_main);

        llPageExtend = findViewById(R.id.ll_page_extend);

        bottomMagicIndicator = findViewById(R.id.magi_bottom);

        CommonNavigator commonNavigator = new CommonNavigator(this.context);

        bottomNavigatorAdapter = new BottomNavigatorAdapter(this.context);
        commonNavigator.setAdapter(bottomNavigatorAdapter);

        bottomMagicIndicator.setNavigator(commonNavigator);


        viewPager = findViewById(R.id.vp_page_extend);

        extendViewPageAdaper = new ExtendViewPageAdaper(this.context);
        viewPager.setAdapter(extendViewPageAdaper);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                bottomMagicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                bottomMagicIndicator.onPageSelected(position == 2 ? 3 : position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                bottomMagicIndicator.onPageScrollStateChanged(state);
            }
        });

        viewPager.setCurrentItem(0);


    }


    public void gotoPage(int index){
        viewPager.setCurrentItem(index);
    }


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(View child) {

    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {

    }
}
