package top.imlk.oneword.application.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import top.imlk.oneword.R;
import top.imlk.oneword.util.ShowDialogUtil;

/**
 * Created by imlk on 2018/6/3.
 */
public class ItemArrayAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private Context context;
    private ArrayList<ShowDialogUtil.TitleAndSummary> tittleAndSummaries;

    public ItemArrayAdapter(Context context, ArrayList<ShowDialogUtil.TitleAndSummary> tittleAndSummaries) {
        this.context = context;

        this.tittleAndSummaries = tittleAndSummaries;
    }

    @Override
    public int getCount() {
        return tittleAndSummaries == null ? 0 : tittleAndSummaries.size();
    }

    @Override
    public Object getItem(int position) {
        return tittleAndSummaries == null ? null : tittleAndSummaries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LinearLayout.inflate(context, R.layout.item_title_and_summary, null);
        }

        ((TextView) convertView.findViewById(R.id.tv_title)).setText(tittleAndSummaries.get(position).title);
        ((TextView) convertView.findViewById(R.id.tv_summary)).setText(tittleAndSummaries.get(position).summary);

        return convertView;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tittleAndSummaries.get(position).summary)));
    }
}
