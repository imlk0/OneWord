package top.imlk.oneword.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imlk on 2018/8/3.
 */
public class ApiBean implements Parcelable {
    public int id;
    public String name;
    public String url;
    public String req_method;
    public String req_args_json;
    public String resp_form;
    public boolean enabled;

    public ApiBean() {
        id = -1;
        enabled = true;
    }


    public ApiBean(int id, String name, String url, String req_method, String req_args_json, String resp_form, boolean enabled) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.req_method = req_method;
        this.req_args_json = req_args_json;
        this.resp_form = resp_form;
        this.enabled = enabled;
    }

    protected ApiBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        url = in.readString();
        req_method = in.readString();
        req_args_json = in.readString();
        resp_form = in.readString();
        enabled = in.readByte() != 0;

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


    public static final Creator<ApiBean> CREATOR = new Creator<ApiBean>() {
        @Override
        public ApiBean createFromParcel(Parcel in) {
            return new ApiBean(in);
        }

        @Override
        public ApiBean[] newArray(int size) {
            return new ApiBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(req_method);
        dest.writeString(req_args_json);
        dest.writeString(resp_form);
        dest.writeByte((byte) (enabled ? 1 : 0));
    }
}
