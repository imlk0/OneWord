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
                .setMessage(
                        "- 禁用模块后锁屏仍然 残留一言 的旧版用户，在 系统设置->锁屏或安全->锁定屏幕消息 里可清除掉残留的内容，新版更改了储存机制，将不再有残留\n" +
                                "\n" +
                                "- 想要在锁屏上嵌入一言，需要Xposed框架支持\n" +
                                "- 即使没有Xposed，也能开启通知上的一言，效果其实也不错\n\n" +
                                "- 若有问题请到本应用在酷安的评论区反馈，最好带上SystemUI.apk（最近比较忙，我不一定会去适配）\n\n" +
                                "- \\(@^0^@)/\n" +
                                "- 如果你喜欢，请不要吝惜票票支持一波\n" +
                                "- 希望各位玩得开心"
                )
                .show();


    }


    public static void showModuleNotStartUpDialog(Context context) {

        new LovelyInfoDialog(context)
                .setTopColor(AppStyleHelper.getColorByAttributeId(context, R.attr.color_primary))
                .setIcon(R.drawable.ic_extension_black_48dp)
                .setTitle("模块未被启用")
                .setMessage("模块似乎并未被启用，这种情况下，将无法在锁屏上嵌入一言，请前往XposedInstaller模块列表勾选启用锁屏一言模块。\n\n\n你也可以开启通知栏的一言，那也会是挺不错的体验，适用于没有安装Xposed框架或者不起作用的设备")
                .show();


    }


    public static void showDonateDialog(final Context context) {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(context, R.layout.item_dialog_choose_donate_by, R.id.tv_item_name, new String[]{"支付宝(推荐，点击拉起支付宝)", "微信", "QQ"});
        new LovelyChoiceDialog(context)
                .setTopColor(AppStyleHelper.getColorByAttributeId(context, R.attr.color_primary))
                .setTitle("赏点小费吧")
                .setIcon(R.drawable.ic_local_atm_white_48dp)
                .setMessage("这个应用以及其中的网易云音乐接口都是我完成的\n\n如果这个软件让您喜欢的话，就请不要吝惜吧！\n\n帮助开发者将更多的想法变成现实\n每份捐赠都是给我的最大鼓励\n\n谢谢了")
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
                                        .setMessage("这是我的支付二维码，请截图扫码捐赠，谢谢\n建议五元起捐，不设上限(￣y▽,￣)╭")
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
                                        .setMessage("这是我的支付二维码，请截图扫码捐赠，谢谢\n建议五元起捐，不设上限(￣y▽,￣)╭")
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
        titleAndSummaries.add(new TitleAndSummary("SwipeToAction(fork)", "https://github.com/vcalvello/SwipeToAction"));
        titleAndSummaries.add(new TitleAndSummary("AndroidDonate", "https://github.com/didikee/AndroidDonate"));
        titleAndSummaries.add(new TitleAndSummary("material-design-icons", "https://github.com/google/material-design-icons"));
        titleAndSummaries.add(new TitleAndSummary("ColorPicker", "https://github.com/jaredrummler/ColorPicker"));
        titleAndSummaries.add(new TitleAndSummary("AutoFitTextView(fork)", "https://github.com/AndroidDeveloperLB/AutoFitTextView"));
        titleAndSummaries.add(new TitleAndSummary("android-opencc", "https://github.com/qichuan/android-opencc"));


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
