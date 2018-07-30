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
import top.imlk.oneword.bean.Word;

/**
 * Created by imlk on 2018/6/3.
 */
public class WordArrayAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private Context context;
    private ArrayList<Word> tittleAndSummaries;

    public WordArrayAdapter(Context context, ArrayList<Word> tittleAndSummaries) {
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

        ((TextView) convertView.findViewById(R.id.from)).setText(tittleAndSummaries.get(position).from);
        ((TextView) convertView.findViewById(R.id.content)).setText(tittleAndSummaries.get(position).content);

        return convertView;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tittleAndSummaries.get(position).content)));
    }
}
