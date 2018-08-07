package top.imlk.oneword.application.client.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;

import top.imlk.oneword.R;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.systemui.view.OneWordView;

public class AdjustStyleActivity extends AppCompatActivity {

    public static String ADJUST_SAMPLE_ONEWORD = "ADJUST_SAMPLE_ONEWORD";

    LinearLayout llOnewordviewContent;

    OneWordView oneWordView;
    WordBean wordBean;

    EditText etContent;
    EditText etReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_style);


        llOnewordviewContent = findViewById(R.id.ll_onewordview_content);
        oneWordView = new OneWordView(this);
        llOnewordviewContent.addView(oneWordView);
        etContent = findViewById(R.id.et_content);
        etReference = findViewById(R.id.et_reference);

        addListener();

        wordBean = getIntent().getParcelableExtra(ADJUST_SAMPLE_ONEWORD);
        if (wordBean == null) {
            wordBean = new WordBean("星星只有在夜里才璀璨夺目啊。", "四月是你的谎言");
        }

        initEditText();

    }

    private void notifyUpdateShowing() {
        oneWordView.setOneWord(wordBean);
    }

    private void initEditText() {
        etContent.setText(wordBean.content);
        etReference.setText(wordBean.reference);
    }

    private void addListener() {
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
                notifyUpdateShowing();
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
                notifyUpdateShowing();
            }
        });

    }


}
