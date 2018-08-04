package top.imlk.oneword.bean;

/**
 * Created by imlk on 2018/8/3.
 */
public class ApiBean {
    public int id;
    public String name;
    public String url;
    public String req_method;
    public String req_args_json;
    public String resp_form;
    public boolean enabled;

    public ApiBean(int id, String name, String url, String req_method, String req_args_json, String resp_form, boolean enabled) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.req_method = req_method;
        this.req_args_json = req_args_json;
        this.resp_form = resp_form;
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "ApiBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", req_method='" + req_method + '\'' +
                ", req_args_json='" + req_args_json + '\'' +
                ", resp_form='" + resp_form + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
