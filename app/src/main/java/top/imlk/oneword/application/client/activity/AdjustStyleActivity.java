package top.imlk.oneword.application.client.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.android.colorpicker.ColorPanelView;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import top.imlk.oneword.R;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.bean.WordViewConfig;
import top.imlk.oneword.systemui.view.OneWordView;
import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.OneWordFileStation;

public class AdjustStyleActivity extends BaseOnewordEditActivity implements ColorPickerDialogListener {

    public static String ADJUST_SAMPLE_ONEWORD = "ADJUST_SAMPLE_ONEWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_adjust_style);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        llOnewordviewContainer = findViewById(R.id.ll_onewordview_container);
        oneWordView = new OneWordView(this);
        llOnewordviewContainer.addView(oneWordView);


        wordBean = getIntent().getParcelableExtra(ADJUST_SAMPLE_ONEWORD);
        if (wordBean == null) {
            wordBean = new WordBean("星星只有在夜里才璀璨夺目啊。", "四月是你的谎言");
        }


        initConfig();

        notifyWordBeanChanged();
        notifyWordViewConfigChanged();

        initEditText();
        initTextSizeModule();
        initTextColorModule();
        initDisModule();
//        initTransModule();
        initAlignModule();

        initGuardWidthModule();
        initItalicModule();
        initLongClickEventModule();
        initStartLineIntoModule();
        initRefAddLineModule();
        initToTraditionalModule();


        initFAB();

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                syncValue();
            }
        });
    }

    FloatingActionButton fabDefaultConfig;
    FloatingActionButton fabSaveConfig;

    protected void initFAB() {

        fabDefaultConfig = findViewById(R.id.fab_default_config);
        fabSaveConfig = findViewById(R.id.fab_save_config);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fab_default_config:
                        config = WordViewConfig.generateDefaultBean();
                        syncValue();
                        notifyWordBeanChanged();
                        Toast.makeText(AdjustStyleActivity.this, "恢复当前布局为默认配置", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.fab_save_config:
                        Toast.makeText(AdjustStyleActivity.this, "正在应用当前布局", Toast.LENGTH_SHORT).show();
                        BroadcastSender.sendUseNewWordViewConfigBroadcast(AdjustStyleActivity.this, config);
                        OneWordFileStation.saveWordViewConfigToJSON(config);
                        finish();
                        break;
                }
            }
        };
        fabDefaultConfig.setOnClickListener(listener);
        fabSaveConfig.setOnClickListener(listener);
    }

    private void syncValue() {

//        tvDisL.setText(String.valueOf(config.disL));
//        tvDisT.setText(String.valueOf(config.disT));
//        tvDisR.setText(String.valueOf(config.disR));
//        tvDisB.setText(String.valueOf(config.disB));
//        tvDisC.setText(String.valueOf(config.disC));
        sbDisL.setProgress(config.disL);
        sbDisT.setProgress(config.disT);
        sbDisR.setProgress(config.disR);
        sbDisB.setProgress(config.disB);
        sbDisC.setProgress(config.disC + 50);

        cpTextColor.setColor(config.textColor);
        tvTextColor.setText(Integer.toHexString(config.textColor).toUpperCase());

//        tvTextSize.setText(String.valueOf(config.textSize));
        sbTextSize.setProgress(config.textSize);

//        sbTransX.setProgress(config.transX + 400);
//        sbTransY.setProgress(config.transY + 400);

        sbConPos.setProgress(config.conPos);
        sbRefPos.setProgress(config.refPos);

        cbGuardWidth.setChecked(config.guardWidth);

        cbItalicRef.setChecked(config.refItalic);

        tvLongClickEvent.setText(config.keyguardLongClick.msg);

        cbStartLineInto.setChecked(config.startLineInto);

        cbRefAddLine.setChecked(config.refAddLine);
        cbToTraditional.setChecked(config.toTraditional);


        etContent.setText(wordBean.content);
        etReference.setText(wordBean.reference);
    }


    /**
     * 长按事件
     */

    TextView tvLongClickEvent;
    LinearLayout llLongClickEvent;

    private void initLongClickEventModule() {
        tvLongClickEvent = findViewById(R.id.tv_keyguard_long_click);
        llLongClickEvent = findViewById(R.id.ll_keyguard_long_click);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AdjustStyleActivity.this)
                        .setTitle("选择长按锁屏一言后的操作")
                        .setItems(WordViewConfig.LongClickEvent.msgs(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvLongClickEvent.setText(WordViewConfig.LongClickEvent.values()[which].msg);
                                config.keyguardLongClick = WordViewConfig.LongClickEvent.values()[which];

                                notifyWordViewConfigChanged();

                            }
                        })
                        .setCancelable(true)
                        .show();
            }
        };

        llLongClickEvent.setOnClickListener(listener);
    }


    /**
     * 保守宽度
     */

    CheckBox cbGuardWidth;
    LinearLayout llGuardWidth;

    private void initGuardWidthModule() {
        cbGuardWidth = findViewById(R.id.cb_guard_width);
        llGuardWidth = findViewById(R.id.ll_guard_width);

        llGuardWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbGuardWidth.setChecked(!cbGuardWidth.isChecked());
            }
        });

        cbGuardWidth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                config.guardWidth = isChecked;
                notifyWordViewConfigChanged();
            }
        });
    }


    /**
     * 整体平移
     */
//
//    private SeekBar sbTransX;
//    private SeekBar sbTransY;
//
//    private TextView tvTransX;
//    private TextView tvTransY;
//
//    private void initTransModule() {
//        sbTransX = findViewById(R.id.sb_trans_x);
//        tvTransX = findViewById(R.id.tv_trans_x);
//
//        sbTransY = findViewById(R.id.sb_trans_y);
//        tvTransY = findViewById(R.id.tv_trans_y);
//
//        sbTransX.setMax(800);
//        sbTransY.setMax(800);
//
//        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//                switch (seekBar.getId()) {
//                    case R.id.sb_trans_x:
//                        config.transX = progress - 400;
//                        tvTransX.setText(String.valueOf(config.transX));
//                        break;
//                    case R.id.sb_trans_y:
//                        config.transY = progress - 400;
//                        tvTransY.setText(String.valueOf(config.transY));
//                        break;
//                }
//                notifyWordViewConfigChanged();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        };
//
//        sbTransX.setOnSeekBarChangeListener(listener);
//        sbTransY.setOnSeekBarChangeListener(listener);
//
//    }


    /**
     * 简体转繁体
     */

    CheckBox cbToTraditional;
    LinearLayout llToTraditional;

    private void initToTraditionalModule() {
        cbToTraditional = findViewById(R.id.cb_to_traditional);
        llToTraditional = findViewById(R.id.ll_to_traditional);

        llToTraditional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbToTraditional.setChecked(!cbToTraditional.isChecked());
            }
        });

        cbToTraditional.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                config.toTraditional = isChecked;
                notifyWordViewConfigChanged();
            }
        });
    }


    /**
     * 来源前加破折号
     */

    CheckBox cbRefAddLine;
    LinearLayout llRefAddLine;

    private void initRefAddLineModule() {
        cbRefAddLine = findViewById(R.id.cb_ref_add_line);
        llRefAddLine = findViewById(R.id.ll_ref_add_line);

        llRefAddLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbRefAddLine.setChecked(!cbRefAddLine.isChecked());
            }
        });

        cbRefAddLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                config.refAddLine = isChecked;
                notifyWordViewConfigChanged();
            }
        });
    }


    /**
     * 首行缩进
     */

    CheckBox cbStartLineInto;
    LinearLayout llStartLineInto;

    private void initStartLineIntoModule() {
        cbStartLineInto = findViewById(R.id.cb_startline_into);
        llStartLineInto = findViewById(R.id.ll_startline_into);

        llStartLineInto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbStartLineInto.setChecked(!cbStartLineInto.isChecked());
            }
        });

        cbStartLineInto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                config.startLineInto = isChecked;
                notifyWordViewConfigChanged();
            }
        });
    }


    /**
     * 来源斜体
     */

    CheckBox cbItalicRef;
    LinearLayout llItalicRef;

    private void initItalicModule() {
        cbItalicRef = findViewById(R.id.cb_italic_ref);
        llItalicRef = findViewById(R.id.ll_italic_ref);

        llItalicRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbItalicRef.setChecked(!cbItalicRef.isChecked());
            }
        });

        cbItalicRef.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                config.refItalic = isChecked;
                notifyWordViewConfigChanged();
            }
        });
    }


    /**
     * 文字对齐
     */

    private SeekBar sbConPos;
    private TextView tvConPos;
    private SeekBar sbRefPos;
    private TextView tvRefPos;

    private void initAlignModule() {
        sbConPos = findViewById(R.id.sb_con_pos);
        tvConPos = findViewById(R.id.tv_con_pos);
        sbRefPos = findViewById(R.id.sb_ref_pos);
        tvRefPos = findViewById(R.id.tv_ref_pos);

        sbConPos.setMax(2);
        sbRefPos.setMax(2);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView textView = null;

                if (seekBar == sbConPos) {
                    config.conPos = progress;
                    textView = tvConPos;
                } else if (seekBar == sbRefPos) {
                    config.refPos = progress;
                    textView = tvRefPos;
                }

                if (textView != null) {
                    switch (progress) {
                        default:
                        case 0:
                            textView.setText("靠左");
                            break;
                        case 1:
                            textView.setText("居中");
                            break;
                        case 2:
                            textView.setText("靠右");
                            break;
                    }

                    notifyWordViewConfigChanged();
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };


        sbConPos.setOnSeekBarChangeListener(listener);
        sbRefPos.setOnSeekBarChangeListener(listener);

    }


    /**
     * 边距
     */

    private SeekBar sbDisL;
    private SeekBar sbDisT;
    private SeekBar sbDisR;
    private SeekBar sbDisB;
    private SeekBar sbDisC;

    private TextView tvDisL;
    private TextView tvDisT;
    private TextView tvDisR;
    private TextView tvDisB;
    private TextView tvDisC;

    private void initDisModule() {
        sbDisL = findViewById(R.id.sb_dis_left);
        tvDisL = findViewById(R.id.tv_dis_left);

        sbDisT = findViewById(R.id.sb_dis_top);
        tvDisT = findViewById(R.id.tv_dis_top);

        sbDisR = findViewById(R.id.sb_dis_right);
        tvDisR = findViewById(R.id.tv_dis_right);

        sbDisB = findViewById(R.id.sb_dis_bottom);
        tvDisB = findViewById(R.id.tv_dis_bottom);

        sbDisC = findViewById(R.id.sb_dis_center);
        tvDisC = findViewById(R.id.tv_dis_center);

        sbDisL.setMax(200);
        sbDisT.setMax(200);
        sbDisR.setMax(200);
        sbDisB.setMax(200);
        sbDisC.setMax(200);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (seekBar.getId()) {
                    case R.id.sb_dis_left:
                        config.disL = progress;
                        tvDisL.setText(String.valueOf(config.disL));
                        break;
                    case R.id.sb_dis_top:
                        config.disT = progress;
                        tvDisT.setText(String.valueOf(config.disT));
                        break;
                    case R.id.sb_dis_right:
                        config.disR = progress;
                        tvDisR.setText(String.valueOf(config.disR));
                        break;
                    case R.id.sb_dis_bottom:
                        config.disB = progress;
                        tvDisB.setText(String.valueOf(config.disB));
                        break;
                    case R.id.sb_dis_center:
                        config.disC = progress - 50;
                        tvDisC.setText(String.valueOf(config.disC));
                        break;
                }

                notifyWordViewConfigChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };


        sbDisL.setOnSeekBarChangeListener(listener);
        sbDisT.setOnSeekBarChangeListener(listener);
        sbDisR.setOnSeekBarChangeListener(listener);
        sbDisB.setOnSeekBarChangeListener(listener);
        sbDisC.setOnSeekBarChangeListener(listener);

    }


    /**
     * TextColor
     */

    private LinearLayout llTextColor;
    private ColorPanelView cpTextColor;
    private TextView tvTextColor;

    private void initTextColorModule() {
        llTextColor = findViewById(R.id.ll_text_color);
        cpTextColor = findViewById(R.id.cp_text_color);
        tvTextColor = findViewById(R.id.tv_text_color);

        llTextColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog.newBuilder().setDialogType(ColorPickerDialog.TYPE_PRESETS).setShowAlphaSlider(true).setColor(config.textColor).show(AdjustStyleActivity.this);
            }
        });

    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        cpTextColor.setColor(color);
        tvTextColor.setText(Integer.toHexString(color).toUpperCase());
        config.textColor = color;
        notifyWordViewConfigChanged();
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

    /**
     * TextSize
     */
    private SeekBar sbTextSize;
    private TextView tvTextSize;

    private void initTextSizeModule() {
        sbTextSize = findViewById(R.id.sb_text_size);
        tvTextSize = findViewById(R.id.tv_text_size);


        sbTextSize.setMax(49);
        sbTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                config.textSize = progress;
                tvTextSize.setText(String.valueOf(progress));
                notifyWordViewConfigChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        alertDoesNotSave();
    }

}
