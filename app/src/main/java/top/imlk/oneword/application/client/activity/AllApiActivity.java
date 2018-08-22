package top.imlk.oneword.application.client.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toolbar;

import top.imlk.oneword.R;
import top.imlk.oneword.application.adapter.AllApisRVAdapter;
import top.imlk.oneword.util.AppStyleHelper;

public class AllApiActivity extends BaseToolBarActivity {

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

}
