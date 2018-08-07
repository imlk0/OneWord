package top.imlk.oneword.util;

import android.app.Activity;
import android.content.Context;
import android.didikee.donate.AlipayDonate;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.ViewConfigurator;

import java.util.ArrayList;

import top.imlk.oneword.R;
import top.imlk.oneword.application.adapter.ItemArrayAdapter;
import top.imlk.oneword.application.client.service.OneWordAutoRefreshService;

/**
 * Created by imlk on 2018/6/3.
 */
public class ShowDialogUtil {
    public static void showSelectRefreshModeDialog(final Context context) {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(context, R.layout.item_dialog_choose_refresh_mode, R.id.tv_item_name, context.getResources().getStringArray(R.array.auto_update_time_item));
        new LovelyChoiceDialog(context)
                .setTopColor(AppStyleHelper.getColorByAttributeId(context, R.attr.color_primary))
                .setTitle("选择刷新频率 当前：" + context.getResources().getStringArray(R.array.auto_update_time_item)[SharedPreferencesUtil.getRefreshMode(context).ordinal()])
                .setIcon(R.drawable.ic_av_timer_white_48dp)
                .setMessage("从下面的选项中选一个刷新频率，若您启用了自动刷新选项，那么在应用退出后，锁屏一言将会在的时间里自动刷新")
                .setItems(arrayAdapter, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        Log.e("SettingPage", "position -> " + position);
                        SharedPreferencesUtil.setRefreshMode(context, OneWordAutoRefreshService.Mode.values()[position]);
                    }

                })
                .setCancelable(true)
                .show();
    }


//    public static void showSelectOneWordTypeDialog(final Context context) {
//
//        String[] items = context.getResources().getStringArray(R.array.oneword_type);
//
//        new LovelyChoiceDialog(context)
//                .setTopColor(AppStyleHelper.getColorByAttributeId(context, R.attr.color_primary_bg))
//                .setTitle("选择你想要的类型的一言（多选）")
//                .setMessage("您可以选择你想要看到的一言类型，若什么都不选则默认全选")
//                .setIcon(R.drawable.ic_clear_all_white_48dp)
//                .setItemsMultiChoice(items, SharedPreferencesUtil.readOneWordTypes(context), new LovelyChoiceDialog.OnItemsSelectedListener<String>() {
//                    @Override
//                    public void onItemsSelected(List<Integer> positions, List<String> items) {
//                        boolean[] selectState = new boolean[HitokotoApi.Parameter.c_Array_ALL.length];
//                        for (int i = 0; i < selectState.length; ++i) {
//                            selectState[i] = false;
//                        }
//                        for (int i = 0; i < positions.size(); ++i) {
//                            selectState[positions.get(i)] = true;
//                        }
//
//                        SharedPreferencesUtil.saveOneWordTypes(context, selectState);
//                        HitokotoApi.refreshCustomArray(selectState);
//                    }
//                })
//                .setConfirmButtonText("确定")
//                .show();
//    }

    public static void showAboutAppDialog(Context context) {

        new LovelyInfoDialog(context)
                .setTopColor(AppStyleHelper.getColorByAttributeId(context, R.attr.color_primary))
                .setIcon(R.drawable.ic_info_outline_white_48dp)
                .setTitle("食用说明")
                .setMessage("- 这个应用是基于Xposed的，所以Xposed框架是必须的\n" +
                        "- 应用对 锁屏界面（属于SystemUI） 进行了hook\n" +
                        "- 是基于AOSP的SystemUI.apk的源码开发的，理论上 原生和类原生 的系统才可以使用，MIUI之类的没有试过\n" +
                        "- 默认开启后台自动更新锁屏一言（每次锁屏后刷新）\n" +
                        "- 再说一次！应用基于Xposed框架！！！\n" +
                        "- 若有问题请到本应用在酷安的评论区反馈，最好带上SystemUI.apk（最近比较忙，我不一定会去适配）\n\n" +
                        "- \\(@^0^@)/\n" +
                        "- 如果你喜欢，请不要吝惜票票支持一波\n" +
                        "- 希望各位玩得开心"
                )
                .show();


    }

    public static void showDonateDialog(final Context context) {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(context, R.layout.item_dialog_choose_donate_by, R.id.tv_item_name, new String[]{"支付宝(推荐，点击拉起支付宝)", "微信", "QQ"});
        new LovelyChoiceDialog(context)
                .setTopColor(AppStyleHelper.getColorByAttributeId(context, R.attr.color_primary))
                .setTitle("赏点小费吧")
                .setIcon(R.drawable.ic_local_atm_white_48dp)
                .setMessage("个人开发者，迷上了Xposed，在学习之余开发了这个软件，如果这个软件让您喜欢的话，就请不要吝惜吧！\n每元捐赠都是给我的最大鼓励\n（话说，捐一分钱这种骚操作还是别玩吧\n/_ \\，惊喜夹杂着失望的感觉真**）\n谢谢了")
                .setItems(arrayAdapter, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        switch (position) {
                            case 0:
                                boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(context);
                                if (hasInstalledAlipayClient) {
                                    AlipayDonate.startAlipayClient(((Activity) context), "FKX093049UCVM4EEN8WV84");
                                    Toast.makeText(context, "蟹蟹，你的鼓励是我的最大动力", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "您好像没有安装支付宝勒，换个支付方式？？？？", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:

                                new LovelyCustomDialog(context)
                                        .setView(R.layout.item_donate)
                                        .setTopColor(AppStyleHelper.getColorByAttributeId(context, R.attr.color_primary))
                                        .setTitle("感谢您使用微信来捐赠")
                                        .setMessage("这是我的支付二维码，截图捐赠，谢谢\n建议一元起捐，不设上限(￣y▽,￣)╭")
                                        .setIcon(R.drawable.ic_local_atm_white_48dp)
                                        .configureView(new ViewConfigurator<View>() {
                                            @Override
                                            public void configureView(View v) {
                                                ((ImageView) v.findViewById(R.id.donate_pic)).setImageResource(R.drawable.donate_wechat);
                                            }
                                        })
                                        .setCancelable(true)
                                        .show();

                                break;
                            case 2:
                                new LovelyCustomDialog(context)
                                        .setView(R.layout.item_donate)
                                        .setTopColor(AppStyleHelper.getColorByAttributeId(context, R.attr.color_primary))
                                        .setTitle("感谢您使用QQ来捐赠")
                                        .setMessage("这是我的支付二维码，截图捐赠，谢谢\n建议一元起捐，不设上限(￣y▽,￣)╭")
                                        .setIcon(R.drawable.ic_local_atm_white_48dp)
                                        .configureView(new ViewConfigurator<View>() {
                                            @Override
                                            public void configureView(View v) {
                                                ((ImageView) v.findViewById(R.id.donate_pic)).setImageResource(R.drawable.donate_qq);
                                            }
                                        })
                                        .setCancelable(true)
                                        .show();

                                break;
                        }
                    }

                })
                .setCancelable(true)
                .show();
    }


    public static void showOpenSourceProjectDialog(final Context context) {

        ArrayList<TitleAndSummary> titleAndSummaries = new ArrayList<>();

        titleAndSummaries.add(new TitleAndSummary("Xposed", "http://repo.xposed.info/"));
        titleAndSummaries.add(new TitleAndSummary("OkHttp", "https://github.com/square/okhttp"));
//        titleAndSummaries.add(new TitleAndSummary("Retrofit", "https://github.com/square/retrofit"));
        titleAndSummaries.add(new TitleAndSummary("RxAndroid", "https://github.com/ReactiveX/RxAndroid"));
        titleAndSummaries.add(new TitleAndSummary("RxJava", "https://github.com/ReactiveX/RxJava"));
        titleAndSummaries.add(new TitleAndSummary("Gson", "https://github.com/google/gson"));
        titleAndSummaries.add(new TitleAndSummary("MagicIndicator", "https://github.com/hackware1993/MagicIndicator"));
        titleAndSummaries.add(new TitleAndSummary("LovelyDialog", "https://github.com/yarolegovich/LovelyDialog"));
        titleAndSummaries.add(new TitleAndSummary("SmartRefreshLayout", "https://github.com/scwang90/SmartRefreshLayout"));
        titleAndSummaries.add(new TitleAndSummary("SwipeToAction", "https://github.com/KB5201314/SwipeToAction"));
        titleAndSummaries.add(new TitleAndSummary("AndroidDonate", "https://github.com/didikee/AndroidDonate"));
        titleAndSummaries.add(new TitleAndSummary("material-design-icons", "https://github.com/google/material-design-icons"));


        ItemArrayAdapter itemArrayAdapter = new ItemArrayAdapter(context, titleAndSummaries);

        ListView listView = new ListView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500);
        listView.setAdapter(itemArrayAdapter);
        listView.setLayoutParams(layoutParams);
        listView.setOnItemClickListener(itemArrayAdapter);

        new LovelyCustomDialog(context)
                .setView(listView)
                .setTopColor(AppStyleHelper.getColorByAttributeId(context, R.attr.color_primary))
                .setIcon(R.drawable.ic_polymer_white_48dp)
                .setTitle("向以下开源项目致敬")
                .setMessage("在开发过程中，这些开源项目使我学到了很多")
                .setCancelable(true)
                .show();
    }


    public static class TitleAndSummary {
        public String title;
        public String summary;

        public TitleAndSummary(String title, String summary) {
            this.title = title;
            this.summary = summary;
        }
    }
}
