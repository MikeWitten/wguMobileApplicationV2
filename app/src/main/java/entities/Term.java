package entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity(tableName = "terms")
//These objects needed to be passed through multiple activities, implementing the parcelable
// interface allowed me to pass the objects successfully with some adjustments to the 'parse' methods.
public class Term implements Parcelable {


    @PrimaryKey (autoGenerate = true)
    public Integer termID;
    @ColumnInfo (name = "term_name")
    public String termName;
    @ColumnInfo (name = "start_date")
    public LocalDate startDate;
    @ColumnInfo (name = "end_date")
    public LocalDate endDate;

    public Term(Integer termID, String termName, LocalDate startDate, LocalDate endDate) {
        this.termID = termID;
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public static final Creator<Term> CREATOR = new Creator<Term>() {
        @Override
        public Term createFromParcel(Parcel in) {
            return new Term(in);
        }

        @Override
        public Term[] newArray(int size) {
            return new Term[size];
        }
    };

    public Integer getTermID() {
        return termID;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "Term{" +
                "termName='" + termName + '\'' +
                ", startDate=" + startDate.format(DateTimeFormatter.ISO_LOCAL_DATE) +
                ", endDate=" + endDate.format(DateTimeFormatter.ISO_LOCAL_DATE) +
                '}';
    }



    protected Term(Parcel in) {
        if (in.readByte() == 0) {
            termID = null;
        } else {
            termID = in.readInt();
        }
        termName = in.readString();
        //Get LocalDate objects from strings.
        startDate = LocalDate.parse(in.readString());
        endDate = LocalDate.parse(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        if (termID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(termID);
        }
        //I chose to convert non-parcelable objects to strings for parsing.
        dest.writeString(termName);
        dest.writeString(startDate.format(formatter));
        dest.writeString(endDate.format(formatter));
    }
}
