package top.imlk.oneword.app.viewmodels;


import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;
import top.imlk.oneword.app.activity.MainActivity;
import top.imlk.oneword.bean.ApiBean;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.dao.OneWordDBHelper;
import top.imlk.oneword.net.OneWordApi;
import top.imlk.oneword.net.WordRequestObserver;
import top.imlk.oneword.util.AppStatus;
import top.imlk.oneword.util.BroadcastSender;
import top.imlk.oneword.util.OneWordFileStation;

/**
 * Created by imlk on 2018/9/27.
 */
public class MainActivityViewModel extends ViewModel implements WordRequestObserver {

    protected MutableLiveData<WordBean> curWordBran = new MutableLiveData<>();
    protected MutableLiveData<Boolean> curWordBeanFavorStatus = new MutableLiveData<>();

    protected MutableLiveData<MainActivity.FragmentName> curFragmentName = new MutableLiveData<>();


    public void setCurWordBean(WordBean wordBean) {
        this.curWordBran.setValue(wordBean);
        this.curWordBeanFavorStatus.setValue(wordBean.id > 0 ? OneWordDBHelper.checkIfInFavor(wordBean.id) : false);
    }

    public MutableLiveData<WordBean> getCurWordBean() {
        return curWordBran;
    }


    public MutableLiveData<Boolean> getCurWordBeanFavorStatus() {
        return curWordBeanFavorStatus;
    }

    public void setCurWordBeanFavorStatus(boolean favor) {

        if (favor) {
            OneWordDBHelper.insertToFavor(this.curWordBran.getValue());
        } else {
            OneWordDBHelper.removeFromFavor(this.curWordBran.getValue().id);
        }

        this.curWordBeanFavorStatus.setValue(favor);
    }

    public boolean reverseFavorStatusAndDoSave() {
        boolean favor = !curWordBeanFavorStatus.getValue();

        this.setCurWordBeanFavorStatus(favor);
        return favor;
    }


    public void setCurWordBeanFavorStatusIfChanged(WordBean wordBean, boolean favor) {
        if (checkIfCurWordBeanFavorChanged(wordBean, favor)) {
            this.curWordBeanFavorStatus.setValue(favor);
        }
    }

    private boolean checkIfCurWordBeanFavorChanged(WordBean wordBean, boolean favor) {
//        boolean favorStatus = OneWordDBHelper.checkIfInFavor(wordBean.id);

        return curWordBran.getValue() != null && curWordBran.getValue().id == wordBean.id && getCurWordBeanFavorStatus().getValue() != favor;
    }

    public void checkIfCurWordBeanRemoved(WordBean wordBean) {
        if (wordBean.id > 0 && curWordBran.getValue() != null && curWordBran.getValue().id == wordBean.id) {
            curWordBran.setValue(null);
        }
    }

    public void dispatchWordBean(WordBean wordBean) {
        Toast.makeText(AppStatus.getRunningApplication(), "设置锁屏一言中...", Toast.LENGTH_SHORT).show();
        OneWordFileStation.saveOneWordToJSON(wordBean);
        BroadcastSender.sendUseNewOneWordBroadcast(AppStatus.getRunningApplication(), wordBean);

    }


    public MutableLiveData<MainActivity.FragmentName> getCurFragmentName() {
        return curFragmentName;
    }

    public void setCurFragmentName(MainActivity.FragmentName fragmentName) {
        this.curFragmentName.setValue(fragmentName);
    }


    public void startAnOneWordRequest() {
        try {
            OneWordApi.requestOneWord(this);

        } catch (Exception e) {
            Toast.makeText(AppStatus.getRunningApplication(), "发生错误:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            finishStatus.setValue(false);
        }
    }

    private MutableLiveData<Boolean> finishStatus = new MutableLiveData<>();

    public MutableLiveData<Boolean> getResponseStatus() {
        return finishStatus;
    }

    @Override
    public void onStart(ApiBean apiBean) {
        Toast.makeText(AppStatus.getRunningApplication(), String.format("正在从\n%s\n%s\n拉取一言数据", apiBean.name, apiBean.url), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAcquire(ApiBean apiBean, WordBean wordBean) {

        int id = OneWordDBHelper.queryIdOfOneWordInAllOneWord(wordBean);
        if (id <= 0) {
            id = OneWordDBHelper.insertOneWordWithoutCheck(wordBean);
        }
        wordBean.id = id;

        OneWordDBHelper.insertToHistory(wordBean);

        // 获取到一言后不再自动设置
        setCurWordBean(wordBean);

        finishStatus.setValue(true);

    }

    @Override
    public void onError(ApiBean apiBean, Throwable e) {
//        Toast.makeText(MainActivity.this, "发生异常，获取失败", Toast.LENGTH_SHORT).show();
        Toast.makeText(AppStatus.getRunningApplication(), "请求：" + apiBean.name + " 时出错\n" + e.getMessage() + (e.getCause() == null ? "" : "\ncause:\n" + e.getCause()), Toast.LENGTH_LONG).show();

        finishStatus.setValue(false);

    }

}
