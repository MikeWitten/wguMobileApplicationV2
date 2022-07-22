package entities;

import static database.Converters.fromAssessmentString;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity(tableName = "assessments")
//These objects needed to be passed through multiple activities, implementing the parcelable
// interface allowed me to pass the objects successfully with some adjustments to the 'parse' methods.
public class Assessment implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Integer assID;
    @ColumnInfo
    public Integer classID;
    @ColumnInfo
    public String assTitle;
    @ColumnInfo
    public AssessmentType assType;
    @ColumnInfo
    public LocalDate startDate;
    @ColumnInfo
    public LocalDate endDate;

    public Assessment(Integer assID, Integer classID, String assTitle, AssessmentType assType,
                      LocalDate startDate, LocalDate endDate) {
        this.assID = assID;
        this.classID = classID;
        this.assTitle = assTitle;
        this.assType = assType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    protected Assessment(Parcel in) {

        if (in.readByte() == 0) {
            assID = null;
        } else {
            assID = in.readInt();
        }
        if (in.readByte() == 0) {
            classID = null;
        } else {
            classID = in.readInt();
        }
        //Convert non-parcelable parameters into the object.
        startDate = LocalDate.parse(in.readString());
        endDate = LocalDate.parse(in.readString());
        assType = fromAssessmentString(in.readString());
        assTitle = in.readString();
    }

    public static final Creator<Assessment> CREATOR = new Creator<Assessment>() {
        @Override
        public Assessment createFromParcel(Parcel in) {
            return new Assessment(in);
        }

        @Override
        public Assessment[] newArray(int size) {
            return new Assessment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        if (assID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(assID);
        }
        if (classID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(classID);
        }
        //Convert non-parcelable parameters to strings so that they can be converted.
        dest.writeString(startDate.format(formatter));
        dest.writeString(endDate.format(formatter));
        dest.writeString(assType.name());
        dest.writeString(assTitle);
    }

}
