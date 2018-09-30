package top.imlk.oneword.app.viewmodels;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModel;
import top.imlk.oneword.bean.WordBean;
import top.imlk.oneword.util.LiveListData;

/**
 * Created by imlk on 2018/9/29.
 */
public class WordListViewModel extends ViewModel {

    public WordListViewModel() {
//        Log.d("WordListViewModel", "创建");
        wordBeans.setValue(new ArrayList<WordBean>());
    }

    private LiveListData<WordBean> wordBeans = new LiveListData<>();


    public LiveListData<WordBean> getWordBeans() {
        return wordBeans;
    }

    public void setWordBeans(List<WordBean> wordBeans) {
        this.wordBeans.setValue(wordBeans);
    }
}
