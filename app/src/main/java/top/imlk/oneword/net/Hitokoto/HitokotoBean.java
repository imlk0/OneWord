package top.imlk.oneword.net.Hitokoto;

/**
 * Created by imlk on 2018/5/19.
 */
public class HitokotoBean {

    public int id;

    public String hitokoto;

    public String type;

    public String from;

    public String creator;

    public String created_at;

    public boolean like;

    public HitokotoBean(int id, String hitokoto, String type, String from, String creator, String created_at, boolean like) {
        this.id = id;
        this.hitokoto = hitokoto;
        this.type = type;
        this.from = from;
        this.creator = creator;
        this.created_at = created_at;
        this.like = like;
    }



}
