package top.imlk.oneword.app.adapter;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.dift.ui.SwipeToAction;
import top.imlk.oneword.R;
import top.imlk.oneword.app.fragments.WordListFragment.PageType;
import top.imlk.oneword.app.viewmodels.MainActivityViewModel;
import top.imlk.oneword.app.viewmodels.WordItemViewModel;
import top.imlk.oneword.app.viewmodels.WordListViewModel;
import top.imlk.oneword.dao.OneWordDBHelper;
import top.imlk.oneword.util.AppStatus;
import top.imlk.oneword.util.ShareUtil;


/**
 * Created by imlk on 2018/5/21.
 */
public class WordListRVAdapter extends RecyclerView.Adapter {


    private RecyclerView recyclerView;

    private PageType pageType;

    private MainActivityViewModel activityViewModel;
    private WordListViewModel wordListViewModel;

    private LifecycleOwner lifecycleOwner;

    public WordListRVAdapter(PageType pageType, MainActivityViewModel activityViewModel, WordListViewModel wordListViewModel) {
        this.pageType = pageType;
        this.activityViewModel = activityViewModel;
        this.wordListViewModel = wordListViewModel;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
        ViewHolder holder = new ViewHolder(rootView);
        ButterKnife.bind(holder, rootView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = ((ViewHolder) holder);

        viewHolder.recoverToDefaultForm();

        viewHolder.data.setWordBean(wordListViewModel.getWordBeans().getValue().get(position));

    }

    @Override
    public int getItemCount() {
        return wordListViewModel.getWordBeans().getValue() == null ? 0 : wordListViewModel.getWordBeans().getValue().size();

    }

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }


    public class ViewHolder extends SwipeToAction.ViewHolder<WordItemViewModel> implements View.OnClickListener {

        @BindView(R.id.iv_set)
        ImageView ivSet;
        @BindView(R.id.iv_favor)
        ImageView ivFavor;
        @BindView(R.id.iv_share)
        ImageView ivShare;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        @BindView(R.id.iv_quote)
        ImageView ivQuote;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_reference)
        TextView tvReference;
        @BindView(R.id.tv_target_text)
        TextView tvTargetText;
        @BindView(R.id.ll_target_tag)
        LinearLayout llTargetTag;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.data = new WordItemViewModel();


            if (PageType.HISTORY_PAGE != pageType) {
                ivFavor.setVisibility(View.GONE);
            }


            data.getWordBean().observe(lifecycleOwner, (wordBean) -> {

                tvContent.setText(wordBean.content);
                tvReference.setText("——" + wordBean.reference);


                if (TextUtils.isEmpty(wordBean.target_text)) {
                    llTargetTag.setVisibility(View.GONE);
                    tvTargetText.setText("");
                } else {
                    llTargetTag.setVisibility(View.VISIBLE);
                    tvTargetText.setText(wordBean.target_text);
                }

                data.setFavorStatus(OneWordDBHelper.checkIfInFavor(wordBean.id));

            });
            data.getFavorStatus().observe(lifecycleOwner, (favor) -> {
                ivFavor.setImageResource(favor ? R.drawable.ic_favorite_white_48dp : R.drawable.ic_favorite_border_white_48dp);
                activityViewModel.setCurWordBeanFavorStatusIfChanged(this.data.getWordBean().getValue(), favor);
            });
            data.getRemoveStatus().observe(lifecycleOwner, (removed) -> {
                if (removed) {
                    activityViewModel.checkIfCurWordBeanRemoved(data.getWordBean().getValue());
                }
            });

        }


        @OnClick({R.id.ll_target_tag, R.id.iv_delete, R.id.iv_set, R.id.iv_share, R.id.iv_favor})
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.iv_share:

                    ShareUtil.shareOneWord(AppStatus.getRunningApplication(), this.data.getWordBean().getValue());
                    break;

                case R.id.iv_set:

                    activityViewModel.setCurWordBean(this.data.getWordBean().getValue());
                    activityViewModel.dispatchWordBean(this.data.getWordBean().getValue());

                    break;
                case R.id.iv_favor:
                    if (pageType == PageType.FAVOR_PAGE) {
                        //不存在的操作
                        break;
                    } else {

                        boolean favor = this.data.reverseFavorStatusAndDoSave();

                        Toast.makeText(AppStatus.getRunningApplication(), favor ? "喜欢" : "取消喜欢", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case R.id.iv_delete:

                    if (pageType == PageType.FAVOR_PAGE) {
                        this.data.setFavorStatus(false);

                    } else {
                        this.data.removeFromHistory();
                    }

                    int ind = wordListViewModel.getWordBeans().getValue().indexOf(this.data);
                    wordListViewModel.getWordBeans().getValue().remove(this.data);
                    WordListRVAdapter.this.notifyItemRemoved(ind);

                    break;
                case R.id.ll_target_tag:
                    if (!TextUtils.isEmpty(data.getWordBean().getValue().target_url)) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getWordBean().getValue().target_url));
                        AppStatus.getRunningApplication().startActivity(intent);
                    }
                    break;
            }
        }


        public void recoverToDefaultForm() {
            this.front.setX(0);
            this.revealRight.setVisibility(View.GONE);
            this.revealLeft.setVisibility(View.GONE);
        }

    }

}
