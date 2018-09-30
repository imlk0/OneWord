package top.imlk.oneword.app.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.dao.OneWordDBHelper;

/**
 * Created by imlk on 2018/9/29.
 */
public class WordItemViewModel extends ViewModel {

    protected MutableLiveData<WordBean> wordBran = new MutableLiveData<>();
    protected MutableLiveData<Boolean> favorStatus = new MutableLiveData<>();

    protected MutableLiveData<Boolean> removeStatus = new MutableLiveData<>();


    public void setWordBean(WordBean wordBean) {
        this.wordBran.setValue(wordBean);
        this.favorStatus.setValue(OneWordDBHelper.checkIfInFavor(this.wordBran.getValue().id));
        this.removeStatus.setValue(false);
    }

    public MutableLiveData<WordBean> getWordBean() {
        return wordBran;
    }


    public MutableLiveData<Boolean> getFavorStatus() {
        return favorStatus;
    }

    public void setFavorStatus(boolean favor) {
        if (favor) {
            OneWordDBHelper.insertToFavor(this.wordBran.getValue());
        } else {
            OneWordDBHelper.removeFromFavor(this.wordBran.getValue().id);
        }

        this.favorStatus.setValue(favor);
    }

    public boolean reverseFavorStatusAndDoSave() {
        boolean favor = !favorStatus.getValue();

        this.setFavorStatus(favor);
        return favor;
    }

    public MutableLiveData<Boolean> getRemoveStatus() {
        return removeStatus;
    }

    public void removeFromHistory() {
        OneWordDBHelper.removeFromHistory(this.wordBran.getValue().id);
        removeStatus.setValue(true);
    }


}
