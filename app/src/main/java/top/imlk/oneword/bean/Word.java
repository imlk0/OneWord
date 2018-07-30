package top.imlk.oneword.bean;

/**
 * Created by imlk on 2018/6/3.
 */
public class Word {
    public String from;
    public String content;
    public String source;// TODO 加入一言API来源

    public Word() {

    }

    public Word(String from, String content) {
        this.from = from;
        this.content = content;
    }
}
