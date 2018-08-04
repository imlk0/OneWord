package top.imlk.oneword.application.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;


import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import top.imlk.oneword.R;
import top.imlk.oneword.application.adapter.BottomNavigatorAdapter;
import top.imlk.oneword.application.adapter.ExtendViewPageAdaper;
import top.imlk.oneword.application.client.activity.MainActivity;

/**
 * Created by imlk on 2018/5/20.
 */
public class MainOneWordView extends LinearLayout {


    public LinearLayout llMsgBottom;
    public LinearLayout llPageMain;
    public LinearLayout llPageExtend;

    private MainActivity mainActivity;

    public MagicIndicator bottomMagicIndicator;

    public BottomNavigatorAdapter bottomNavigatorAdapter;

    public ExtendViewPageAdaper extendViewPageAdaper;

    public ViewPager viewPager;

    public MainOneWordView(Context context) {
        super(context);
    }

    public MainOneWordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MainOneWordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MainOneWordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

//        if (extendViewPageAdaper != null) {
//            extendViewPageAdaper.upDateLP(rect.height() - (this.llMsgBottom == null ? 0 : this.llMsgBottom.getHeight()));
//        }


        if (bottomNavigatorAdapter != null) {
            bottomNavigatorAdapter.upDateLP(rect);
        }
    }

    public void init(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        llMsgBottom = findViewById(R.id.ll_msg_bottom);
//        if (llMsgBottom == null) {
//            return;//第一次调用
//        }


        llPageMain = findViewById(R.id.ll_page_main);
        llPageExtend = findViewById(R.id.ll_page_extend);
        bottomMagicIndicator = findViewById(R.id.magi_bottom);

        CommonNavigator commonNavigator = new CommonNavigator(mainActivity);

        bottomNavigatorAdapter = new BottomNavigatorAdapter(mainActivity);
        commonNavigator.setAdapter(bottomNavigatorAdapter);

        bottomMagicIndicator.setNavigator(commonNavigator);
        bottomMagicIndicator.onPageSelected(-1);

        llMsgBottom.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (llMsgBottom.getHeight() != 0) {
                    llMsgBottom.getViewTreeObserver().removeOnPreDrawListener(this);
                    MainOneWordView.this.mainActivity.upDateLP();//更新ui
                }
                return true;
            }
        });


        viewPager = findViewById(R.id.vp_page_extend);

        extendViewPageAdaper = new ExtendViewPageAdaper(mainActivity);
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


    public void gotoPage(int index) {
        bottomMagicIndicator.onPageSelected(index == 2 ? 3 : index);
        viewPager.setCurrentItem(index);
        if (index == 0 || index == 1) {
            extendViewPageAdaper.clearAndReloadDataOfRecyclerView(index);
        }
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        ((MainActivity) this.context).upDateLP();
//
//    }

    // implement reference NestedScrollingParent

//    @Override
//    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
//        return true;
//    }
//
//    @Override
//    public void onNestedScrollAccepted(View child, View target, int axes) {
//        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
//    }
//
//    @Override
//    public void onStopNestedScroll(View child) {
//
//    }
//
//    @Override
//    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//
//    }
//
//    @Override
//    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
//
//    }
//
//    @Override
//    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
//        return false;
//}
//
//    @Override
//    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
//        return false;
//    }
//
//    @Override
//    public int getNestedScrollAxes() {
//        return nestedScrollingParentHelper.getNestedScrollAxes();
//    }
//


}
