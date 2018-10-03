package top.imlk.oneword.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imlk on 2018/8/8.
 */
public class WordViewConfig implements Parcelable {

    public int textSize = 14;//SP
    public int textColor = 0xB3FFFFFF;//defaut 0xB3FFFFFF

    public int disL = 20;
    public int disT = 10;
    public int disR = 20;
    public int disB = 10;
    public int disC = 0;


    //     0:Gravity.LEFT;
    //     1:Gravity.CENTER_HORIZONTAL;
    //     2:Gravity.RIGHT;
    public int conPos = 1;
    public int refPos = 2;


    public boolean guardWidth = false;


    public boolean refItalic = false;

    public boolean startLineInto = true;

    public boolean refAddLine = true;

    public boolean toTraditional = false;

    public LongClickEvent keyguardLongClick = LongClickEvent.NEXT;

    public int maxHeight = 0;// 默认改为不进行限制


    protected WordViewConfig(Parcel in) {
        textSize = in.readInt();
        textColor = in.readInt();
        disL = in.readInt();
        disT = in.readInt();
        disR = in.readInt();
        disB = in.readInt();
        disC = in.readInt();
        conPos = in.readInt();
        refPos = in.readInt();
        guardWidth = in.readByte() != 0;
        refItalic = in.readByte() != 0;
        startLineInto = in.readByte() != 0;
        refAddLine = in.readByte() != 0;
        toTraditional = in.readByte() != 0;
        keyguardLongClick = in.readParcelable(LongClickEvent.class.getClassLoader());
        maxHeight = in.readInt();
    }

    public static final Creator<WordViewConfig> CREATOR = new Creator<WordViewConfig>() {
        @Override
        public WordViewConfig createFromParcel(Parcel in) {
            return new WordViewConfig(in);
        }

        @Override
        public WordViewConfig[] newArray(int size) {
            return new WordViewConfig[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(textSize);
        dest.writeInt(textColor);
        dest.writeInt(disL);
        dest.writeInt(disT);
        dest.writeInt(disR);
        dest.writeInt(disB);
        dest.writeInt(disC);
        dest.writeInt(conPos);
        dest.writeInt(refPos);
        dest.writeByte((byte) (guardWidth ? 1 : 0));
        dest.writeByte((byte) (refItalic ? 1 : 0));
        dest.writeByte((byte) (startLineInto ? 1 : 0));
        dest.writeByte((byte) (refAddLine ? 1 : 0));
        dest.writeByte((byte) (toTraditional ? 1 : 0));
        dest.writeParcelable(keyguardLongClick, 0);
        dest.writeInt(maxHeight);
    }


    public enum LongClickEvent implements Parcelable {
        NONE("无操作"),
        NEXT("换个句子"),
        APP("进入APP"),;


        //        public int id;
        public String msg;


        private LongClickEvent(String msg) {
            this.msg = msg;
        }


        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.ordinal());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        private static String[] mmsgs;

        static {
            LongClickEvent[] events = LongClickEvent.values();
            mmsgs = new String[events.length];
            for (int i = 0; i < mmsgs.length; ++i) {
                mmsgs[i] = events[i].msg;
            }
        }

        public static String[] msgs() {
            return mmsgs;
        }


        public static final Creator<LongClickEvent> CREATOR = new Creator<LongClickEvent>() {
            @Override
            public LongClickEvent createFromParcel(Parcel in) {
                return LongClickEvent.values()[in.readInt()];
            }

            @Override
            public LongClickEvent[] newArray(int size) {
                return new LongClickEvent[size];
            }
        };
    }

    public WordViewConfig() {

    }


    public static WordViewConfig generateDefaultBean() {
        WordViewConfig wordViewConfig = new WordViewConfig();

        return wordViewConfig;
    }

    @Override
    public String toString() {
        return "WordViewConfig{" +
                "textSize=" + textSize +
                ", textColor=" + textColor +
                ", disL=" + disL +
                ", disT=" + disT +
                ", disR=" + disR +
                ", disB=" + disB +
                ", disC=" + disC +
                ", conPos=" + conPos +
                ", refPos=" + refPos +
                ", guardWidth=" + guardWidth +
                ", refItalic=" + refItalic +
                ", startLineInto=" + startLineInto +
                ", refAddLine=" + refAddLine +
                ", toTraditional=" + toTraditional +
                ", keyguardLongClick=" + keyguardLongClick +
                ", maxHeight=" + maxHeight +
                '}';
    }
}
