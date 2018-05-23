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

import java.lang.reflect.Field;
import java.util.ArrayList;

import co.dift.ui.SwipeToAction;
import top.imlk.oneword.R;

/**
 * Created by imlk on 2018/5/20.
 */
public class ExtendViewPageAdaper extends PagerAdapter {

    private ArrayList<ViewGroup> data;

    private Context context;

    public ExtendViewPageAdaper(Context context) throws ClassNotFoundException, NoSuchFieldException {

        this.context = context;

        this.data = new ArrayList<>();

        data.add((ViewGroup) LinearLayout.inflate(context, R.layout.history_page, null));
        data.add((ViewGroup) LinearLayout.inflate(context, R.layout.like_page, null));
        data.add((ViewGroup) LinearLayout.inflate(context, R.layout.about_page, null));

        initViewGroup(0);
        initViewGroup(1);
        initViewGroup(2);
    }


    public void initViewGroup(int index) {
        switch (index) {
            case 0:
                RecyclerView recyclerView = data.get(0).findViewById(R.id.rv_on_history_page);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setHasFixedSize(true);// TODO
                recyclerView.setAdapter(new RecyclerViewAdapter(context));
                SwipeToAction swipeToAction = new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener() {
                    @Override
                    public boolean swipeLeft(Object itemData) {
                        return false;
                    }

                    @Override
                    public boolean swipeRight(Object itemData) {
                        return false;
                    }

                    @Override
                    public void onClick(Object itemData) {
                        Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongClick(Object itemData) {
                        Toast.makeText(context,"LongClicked",Toast.LENGTH_SHORT).show();
                    }
                });

                recyclerView.


                break;
            case 1:
                RecyclerView recyclerView_1 = data.get(1).findViewById(R.id.rw_on_like_page);
                recyclerView_1.setLayoutManager(new LinearLayoutManager(context));
                recyclerView_1.setHasFixedSize(true);// TODO
                recyclerView_1.setAdapter(new RecyclerViewAdapter(context));
                SwipeToAction swipeToAction_1 = new SwipeToAction(recyclerView_1, new SwipeToAction.SwipeListener() {
                    @Override
                    public boolean swipeLeft(Object itemData) {
                        return false;
                    }

                    @Override
                    public boolean swipeRight(Object itemData) {
                        return false;
                    }

                    @Override
                    public void onClick(Object itemData) {
                        Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongClick(Object itemData) {
                        Toast.makeText(context,"LongClicked",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 2:
                break;
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
