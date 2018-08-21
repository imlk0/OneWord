package top.imlk.oneword.application.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

import top.imlk.oneword.R;
import top.imlk.oneword.application.client.activity.EditApiActivity;
import top.imlk.oneword.bean.ApiBean;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;
import top.imlk.oneword.util.ShareUtil;

/**
 * Created by imlk on 2018/8/6.
 */
public class AllApisRVAdapter extends RecyclerView.Adapter<AllApisRVAdapter.ApiItemHolder> {

    private Context context;

    class ApiItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, View.OnLongClickListener, PopupMenu.OnMenuItemClickListener {

        protected ImageView iv_icon;
        protected TextView tv_title;
        protected TextView tv_summary;
        protected CheckBox cb_enabled;

        protected PopupMenu popupMenu;

        protected ApiBean apiBean;

        public ApiItemHolder(View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_summary = itemView.findViewById(R.id.tv_summary);
            cb_enabled = itemView.findViewById(R.id.cb_enabled);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            cb_enabled.setOnCheckedChangeListener(this);
        }


        @Override
        public void onClick(View v) {
            Intent intent;

            switch (getItemViewType()) {
                case 0:
                    intent = new Intent(context, EditApiActivity.class);
                    intent.putExtra(EditApiActivity.EDITING_API_ID, apiBean.id);
                    context.startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(context, EditApiActivity.class);
                    context.startActivity(intent);
                    break;
                case 2:

                    showPraseReceivedAPIDialog();
                    break;

            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            apiBean.enabled = isChecked;
            OneWordSQLiteOpenHelper.getInstance().inserAApi(apiBean);
        }

        @Override
        public boolean onLongClick(View v) {
            switch (getItemViewType()) {
                case 0:

                    popupMenu = new PopupMenu(context, itemView);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_all_api_rv_context, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(this);

                    popupMenu.show();

                    return true;
            }
            return false;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.item_edit:
                    this.onClick(itemView);
                    return true;
                case R.id.iv_share:
                    ShareUtil.shareAPI(context, apiBean);
                    return true;
            }
            return false;
        }
    }

    private ArrayList<ApiBean> data = new ArrayList<>();


    public AllApisRVAdapter(Context context) {
        this.context = context;
    }


    public void updataData() {
        data.clear();
        data.addAll(OneWordSQLiteOpenHelper.getInstance().queryAllApi());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ApiItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LinearLayout linearLayout;
        ApiItemHolder apiItemHolder = null;


        switch (viewType) {

            case 0:
                linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_rv_allapi, parent, false);

                apiItemHolder = new ApiItemHolder(linearLayout);

                apiItemHolder.iv_icon.setImageResource(R.drawable.ic_book_black_24dp);

                break;
            case 1:
                linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_rv_allapi, parent, false);

                apiItemHolder = new ApiItemHolder(linearLayout);

                apiItemHolder.iv_icon.setImageResource(R.drawable.ic_add_black_24dp);
                apiItemHolder.tv_summary.setVisibility(View.GONE);
                apiItemHolder.cb_enabled.setVisibility(View.GONE);
                apiItemHolder.tv_title.setText("新增API");

                break;
            case 2:
                linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_rv_allapi, parent, false);

                apiItemHolder = new ApiItemHolder(linearLayout);

                apiItemHolder.iv_icon.setImageResource(R.drawable.ic_add_black_24dp);
                apiItemHolder.tv_summary.setVisibility(View.GONE);
                apiItemHolder.cb_enabled.setVisibility(View.GONE);
                apiItemHolder.tv_title.setText("导入分享的API");

                break;
        }

        return apiItemHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ApiItemHolder holder, int position) {

        if (position < data.size()) {
            ApiBean apiBean = data.get(position);
            holder.apiBean = apiBean;
            holder.tv_title.setText(apiBean.name);
            holder.tv_summary.setText(apiBean.url + "  " + apiBean.req_method + "\n参数: " + apiBean.req_args_json);
            holder.cb_enabled.setChecked(apiBean.enabled);
        }

    }

    @Override
    public int getItemCount() {
        return data.size() + 2;
    }


    @Override
    public int getItemViewType(int position) {
        if (position < data.size()) {
            return 0;
        } else {
            return position + 1 - data.size();
        }

    }


    protected void showPraseReceivedAPIDialog() {

        final View contentView = View.inflate(context, R.layout.dialog_prase_api_content, null);

        new AlertDialog.Builder(context).setView(contentView).setTitle("请黏贴获取到的API字符串").setPositiveButton("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = ((EditText) contentView.findViewById(R.id.et_api_str)).getText().toString();
                if (!TextUtils.isEmpty(str)) {
                    ApiBean apiBean = ShareUtil.parseReceivedAPI(context, str);
                    if (apiBean != null) {

                        Intent intent = new Intent(context, EditApiActivity.class);
                        intent.putExtra(EditApiActivity.TO_BE_ADDED_APIBEAN, apiBean);
                        context.startActivity(intent);

                    }
                }
                dialog.dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
}
