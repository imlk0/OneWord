package top.imlk.oneword.app.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lb.auto_fit_textview.AutoResizeTextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import top.imlk.oneword.R;
import top.imlk.oneword.app.activity.MainActivity;
import top.imlk.oneword.app.viewmodels.MainActivityViewModel;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.util.AppStatus;
import top.imlk.oneword.util.ShareUtil;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class WordViewFragment extends Fragment {

    @BindView(R.id.tv_content)
    AutoResizeTextView tvContent;
    @BindView(R.id.tv_reference)
    AutoResizeTextView tvReference;
    @BindView(R.id.tv_target_text)
    TextView tvTargetText;
    @BindView(R.id.ll_target_tag)
    LinearLayout llTargetTag;
    @BindView(R.id.iv_favor)
    ImageView ivFavor;
    @BindView(R.id.iv_set)
    ImageView ivSet;
    @BindView(R.id.one_word_show_panel)
    LinearLayout oneWordShowPanel;


    private MainActivityViewModel mainActivityViewModel;

    public WordViewFragment() {
        // Required empty public constructor
    }

    public static WordViewFragment newInstance() {
        WordViewFragment fragment = new WordViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_word_view, container, false);
        ButterKnife.bind(this, rootView);

        mainActivityViewModel.getCurWordBean().observe(this, (wordBean) -> {

//            TransitionManager.beginDelayedTransition(oneWordShowPanel, new TransitionSet().addTransition(new Fade(Fade.IN)));

            this.tvContent.setText(wordBean.content);
            if (TextUtils.isEmpty(wordBean.reference)) {
                this.tvReference.setVisibility(GONE);
                this.tvReference.setText("");
            } else {
                this.tvReference.setText("——" + wordBean.reference);
            }

            if (TextUtils.isEmpty(wordBean.target_text)) {
                llTargetTag.setVisibility(GONE);
                tvTargetText.setText("");
            } else {
                llTargetTag.setVisibility(VISIBLE);
                tvTargetText.setText(wordBean.target_text);
            }

            mainActivityViewModel.setCurFragmentName(MainActivity.FragmentName.WordViewFragment);

        });
        mainActivityViewModel.getCurWordBeanFavorStatus().observe(this, (favor) -> {
            if (favor) {
                ivFavor.setImageResource(R.drawable.ic_favorite_white_48dp);
            } else {
                ivFavor.setImageResource(R.drawable.ic_favorite_border_white_48dp);
            }
        });


        return rootView;
    }

    @Override
    public void onDestroyView() {
        mainActivityViewModel.getCurWordBean().removeObservers(this);
        mainActivityViewModel.getCurWordBeanFavorStatus().removeObservers(this);

        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @OnClick({R.id.iv_favor, R.id.iv_set, R.id.ll_target_tag})
    public void onClick(View v) {
        WordBean curWordBean = mainActivityViewModel.getCurWordBean().getValue();

        switch (v.getId()) {
            case R.id.iv_favor:

                if (curWordBean != null && curWordBean.id > 0) {

                    mainActivityViewModel.reverseFavorStatusAndDoSave();

                } else {
                    Toast.makeText(getActivity(), "还没有拉取任何一条一言哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_set:
                if (curWordBean != null && curWordBean.id > 0) {

                    mainActivityViewModel.dispatchWordBean(curWordBean);

                } else {
                    Toast.makeText(getActivity(), "还没有拉取任何一条一言哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_target_tag:
                if (!TextUtils.isEmpty(curWordBean.target_url)) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(curWordBean.target_url));
                        AppStatus.getRunningApplication().startActivity(intent);
                    } catch (ActivityNotFoundException activityNotFoundException) {
                        Toast.makeText(AppStatus.getRunningApplication(), "没有找到能打开此链接的应用：\n" + curWordBean.target_url, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @OnLongClick({R.id.tv_content, R.id.tv_reference, R.id.one_word_show_panel})
    public boolean shareOneWordOnLongCLick(View v) {

        if (mainActivityViewModel.getCurWordBean().getValue() != null) {
            ShareUtil.shareOneWord(getActivity(), mainActivityViewModel.getCurWordBean().getValue());
        }
        return true;
    }
}
