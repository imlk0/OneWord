package top.imlk.oneword.application.client.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import top.imlk.oneword.R;
import top.imlk.oneword.bean.ApiBean;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.net.WordRequestObserver;
import top.imlk.oneword.dao.OneWordSQLiteOpenHelper;
import top.imlk.oneword.net.OneWordApi;

public class EditApiActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    public static final String EDITING_API_ID = "EDITING_API_ID";
    public static final String TO_BE_ADDED_APIBEAN = "TO_BE_ADDED_APIBEAN";

    private ApiBean apiBean;

    Toolbar toolbar;

    EditText etName;
    EditText etURL;
    EditText etReqMethod;
    EditText etReqArgJson;
    EditText etRespFormJson;
    ImageView ivHelpReqArg;
    ImageView ivHelpRespForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_api);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setOnMenuItemClickListener(this);
        toolbar.inflateMenu(R.menu.menu_edit_api_activity);

        int id = getIntent().getIntExtra(EDITING_API_ID, -1);

        etName = findViewById(R.id.et_name);
        etURL = findViewById(R.id.et_url);
        etReqMethod = findViewById(R.id.et_req_method);
        etReqArgJson = findViewById(R.id.et_req_arg_json);
        etRespFormJson = findViewById(R.id.et_resp_form);
        ivHelpReqArg = findViewById(R.id.iv_help_req_arg);
        ivHelpRespForm = findViewById(R.id.iv_help_resp_form);

        ivHelpReqArg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpReqArg();
            }
        });

        ivHelpRespForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpRespForm();
            }
        });


        if (id <= 0) {
            toolbar.setTitle("新建一言API");
            toolbar.getMenu().findItem(R.id.item_delete).setVisible(false);
            apiBean = getIntent().getParcelableExtra(TO_BE_ADDED_APIBEAN);

            if (apiBean == null) {
                apiBean = new ApiBean();
            }

        } else {
            apiBean = OneWordSQLiteOpenHelper.getInstance().queryApiById(id);
            toolbar.setTitle("编辑 " + apiBean.name);
        }

        etName.setText(apiBean.name);
        etURL.setText(apiBean.url);
        etReqMethod.setText(apiBean.req_method);
        etReqArgJson.setText(apiBean.req_args_json);
        etRespFormJson.setText(apiBean.resp_form);

    }


    private void showHelpReqArg() {

        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setPositiveButton("明白了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setMessage("API请求参数，JSON格式，例如:\n" +
                        "若URL为\"https://v1.hitokoto.cn\"，请求方式为\"GET\"，\n" +
                        "将参数设置为\n" +
                        "{\n" +
                        "  \"c\": \"a\"\n" +
                        "}\n" +
                        "则将解析为：\n" +
                        "https://v1.hitokoto.cn/?c=a\n" +
                        "\n" +
                        "请注意符号全角和半角的区别")
                .show();
    }

    private void showHelpRespForm() {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setPositiveButton("明白了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setMessage("API响应格式，作为解析请求结果的模板，\n" +
                        "\n" +
                        "分隔符模式：\n" +
                        "键入分隔符，将根据分隔符最后一次出现的位置把请求结果分割成一言正文和一言出处\n" +
                        "例如，对于请求结果：\n" +
                        "露下旗濛濛，寒金鸣夜刻。——李贺《塞下曲》\n" +
                        "可填写分隔符为：\n" +
                        "——\n" +
                        "将匹配出\n" +
                        "正文：\n" +
                        "露下旗濛濛，寒金鸣夜刻。\n" +
                        "出处：\n" +
                        "李贺《塞下曲》\n" +
                        "\n" +
                        "\n" +
                        "------\n" +
                        "JSON模式：\n" +
                        "例如，对于请求结果:\n" +
                        "{\n" +
                        "  \"id\": 3697,\n" +
                        "  \"hitokoto\": \"我喜欢的人…也能喜欢上自己，我认为这就是奇迹。\",\n" +
                        "  \"type\": \"a\",\n" +
                        "  \"from\": \"月色真美\",\n" +
                        "  \"creator\": \"Jerx2y\",\n" +
                        "  \"created_at\": \"1530183116\"\n" +
                        "}\n" +
                        "可用占位符填充关键位置，将模板填写为\n" +
                        "{\n" +
                        "  \"hitokoto\": \"[content]\",\n" +
                        "  \"from\": \"[reference]\",\n" +
                        "}\n" +
                        "占位符有:\n" +
                        "[content]：表示一言的内容，必须\n" +
                        "[reference]：表示一言内容的出处，可选\n" +
                        "[target_url]：表示与这条一言有关的链接，在锁屏上点击一言后将发起链接跳转，可选（其实这个选项还没写好\uD83D\uDE04）")
                .show();
    }


    private void alertDeleteSave() {

        new AlertDialog.Builder(this).setTitle("删除？？").setMessage("您确定要删除吗？这是不可逆的操作，如果你只是想禁用此api，可以在api列表中取消勾选").setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OneWordSQLiteOpenHelper.getInstance().removeApiById(apiBean.id);
                finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(true).show();
    }


    @Override
    public void onBackPressed() {
        alertDoesNotSave();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_save:
                apiBean.name = etName.getText().toString();
                apiBean.url = etURL.getText().toString();
                apiBean.req_method = etReqMethod.getText().toString();
                apiBean.req_args_json = etReqArgJson.getText().toString();
                apiBean.resp_form = etRespFormJson.getText().toString();

                OneWordSQLiteOpenHelper.getInstance().inserAApi(apiBean);
                finish();
                return true;
            case R.id.item_delete:
                alertDeleteSave();
                return true;
            case R.id.item_test:

                OneWordApi.requestOneWordByAPI(new WordRequestObserver() {
                    @Override
                    public void onStart(ApiBean apiBean) {
                        Toast.makeText(EditApiActivity.this, String.format("尝试请求:\n%s\n%s\n%s", apiBean.name, apiBean.url, apiBean.req_method), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAcquire(ApiBean apiBean, WordBean wordBean) {

                        Toast.makeText(EditApiActivity.this, "解析结果-内容（必须）:\n" + wordBean.content, Toast.LENGTH_LONG).show();
                        Toast.makeText(EditApiActivity.this, "解析结果-出处（可选）:\n" + wordBean.reference, Toast.LENGTH_LONG).show();
                        Toast.makeText(EditApiActivity.this, "解析结果-相关链接（可选）:\n" + wordBean.target_url, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(ApiBean apiBean, Throwable e) {
                        Toast.makeText(EditApiActivity.this, "发生异常，获取失败", Toast.LENGTH_SHORT).show();
                        Toast.makeText(EditApiActivity.this, e.getMessage() + "\n" + e.getCause(), Toast.LENGTH_LONG).show();
                    }


                }, new ApiBean(-1,
                        etName.getText().toString(),
                        etURL.getText().toString(),
                        etReqMethod.getText().toString(),
                        etReqArgJson.getText().toString(),
                        etRespFormJson.getText().toString(),
                        true));
                return true;
        }
        return false;
    }
}
