package top.imlk.oneword.application.client.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import top.imlk.oneword.R;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.bean.WordViewConfig;
import top.imlk.oneword.systemui.view.OneWordView;
import top.imlk.oneword.util.OneWordFileStation;

/**
 * Created by imlk on 2018/8/22.
 */
public abstract class BaseOnewordEditActivity extends BaseEditActivity {

    protected LinearLayout llOnewordviewContainer;

    protected OneWordView oneWordView;
    protected WordBean wordBean;

    protected WordViewConfig config;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    protected void notifyWordBeanChanged() {
        oneWordView.setOneWord(wordBean);
    }

    protected void notifyWordViewConfigChanged() {
        oneWordView.applyWordViewConfig(config);
    }


    public void initConfig() {
        config = OneWordFileStation.readWordViewConfigJSON();
        if (config == null) {
            config = WordViewConfig.generateDefaultBean();
        }

    }

    /**
     * EditText
     */
    protected EditText etContent;
    protected EditText etReference;

    protected void initEditText() {
        etContent = findViewById(R.id.et_content);
        etReference = findViewById(R.id.et_reference);

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                wordBean.content = s.toString();
                notifyWordBeanChanged();
            }
        });

        etReference.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                wordBean.reference = s.toString();
                notifyWordBeanChanged();
            }
        });

    }

}
