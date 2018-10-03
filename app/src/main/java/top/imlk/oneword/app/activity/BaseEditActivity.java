package top.imlk.oneword.app.activity;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

/**
 * Created by imlk on 2018/8/8.
 */
public abstract class BaseEditActivity extends BaseToolBarActivity {


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
