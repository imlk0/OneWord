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


    public boolean refItalic;

    public boolean startLineInto;

    public boolean refAddLine;

    public boolean toTraditional;

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

        wordViewConfig.refItalic = false;

        wordViewConfig.startLineInto = true;
        wordViewConfig.refAddLine = true;

        wordViewConfig.toTraditional = false;

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
                ", refItalic=" + refItalic +
                ", startLineInto=" + startLineInto +
                ", refAddLine=" + refAddLine +
                ", toTraditional=" + toTraditional +
                '}';
    }

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
        refItalic = in.readByte() != 0;
        startLineInto = in.readByte() != 0;
        refAddLine = in.readByte() != 0;
        toTraditional = in.readByte() != 0;
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
        dest.writeByte((byte) (refItalic ? 1 : 0));
        dest.writeByte((byte) (startLineInto ? 1 : 0));
        dest.writeByte((byte) (refAddLine ? 1 : 0));
        dest.writeByte((byte) (toTraditional ? 1 : 0));
    }
}
