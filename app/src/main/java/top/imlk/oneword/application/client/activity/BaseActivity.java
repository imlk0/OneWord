package top.imlk.oneword.application.client.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by imlk on 2018/8/8.
 */
public abstract class BaseActivity extends AppCompatActivity {


    protected void alertDoesNotSave() {

        new AlertDialog.Builder(this).setTitle("确定要退出编辑吗").setMessage("要放弃此次修改，退出编辑吗？").setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(true).show();
    }

}
