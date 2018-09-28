package top.imlk.oneword.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imlk on 2018/8/8.
 */
public class WordViewConfig implements Parcelable {

    public int textSize;//SP defaut 14
    public int textColor;//defaut 0xB3FFFFFF

    public int disL;
    public int disT;
    public int disR;
    public int disB;
    public int disC;


    //     0:Gravity.LEFT;
    //     1:Gravity.CENTER_HORIZONTAL;
    //     2:Gravity.RIGHT;
    public int conPos;
    public int refPos;


    public boolean guardWidth;


    public boolean refItalic;

    public boolean startLineInto;

    public boolean refAddLine;

    public boolean toTraditional;

    public LongClickEvent keyguardLongClick = LongClickEvent.NONE;

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
    }


    public enum LongClickEvent implements Parcelable {
        NONE("无操作"),
        NEXT("换个句子（开启自动刷新后有效）"),
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

        wordViewConfig.textSize = 14;
        wordViewConfig.textColor = 0xB3FFFFFF;


        wordViewConfig.disL = 20;
        wordViewConfig.disT = 10;
        wordViewConfig.disR = 20;
        wordViewConfig.disB = 10;
        wordViewConfig.disC = 0;

        wordViewConfig.conPos = 1;
        wordViewConfig.refPos = 2;

        wordViewConfig.guardWidth = false;

        wordViewConfig.keyguardLongClick = LongClickEvent.NONE;

        wordViewConfig.refItalic = false;

        wordViewConfig.startLineInto = true;
        wordViewConfig.refAddLine = true;

        wordViewConfig.toTraditional = false;

        return wordViewConfig;
    }
}
