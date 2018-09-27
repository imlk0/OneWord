package top.imlk.oneword.application.client.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import top.imlk.oneword.R;
import top.imlk.oneword.application.adapter.AllApisRVAdapter;
import top.imlk.oneword.dao.OneWordDBHelper;

public class AllApiActivity extends BaseToolBarActivity implements Toolbar.OnMenuItemClickListener {

    protected RecyclerView recyclerView;
    protected AllApisRVAdapter allApisRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_api);

        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(this);
        toolbar.inflateMenu(R.menu.menu_all_api_activity);


        recyclerView = findViewById(R.id.rv_apis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        allApisRVAdapter = new AllApisRVAdapter(this);
        recyclerView.setAdapter(allApisRVAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        allApisRVAdapter.updataData();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_recover:
                alertRecoverAllApi();
                return true;
        }
        return false;
    }


    private void alertRecoverAllApi() {

        new AlertDialog.Builder(this).setTitle("恢复所有自带API").setMessage("这种操作会把所有api清除（包括自己添加的api），然后再导入所有自带api\n\n确定要这样操作吗？").setPositiveButton("是的，操作", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OneWordDBHelper.clearAllApi();
                OneWordDBHelper.insertInternalApi();
                allApisRVAdapter.updataData();
            }
        }).setNegativeButton("这可不行", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(true).show();
    }

}
