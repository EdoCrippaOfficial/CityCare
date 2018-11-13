package inc.elevati.imycity.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Report implements Parcelable {

    public enum ImageType {

        FULL("/img"), THUMBNAIL("/thumb");

        String path;

        ImageType(String path) {
            this.path = path;
        }
    }

    private String title, description, imageName;
    private long timestamp;

    public Report(String title, String description, String imageName, long timestamp) {
        this.title = title;
        this.description = description;
        this.imageName = imageName;
        this.timestamp = timestamp;
    }

    private Report(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.imageName = in.readString();
        this.timestamp = in.readLong();
    }

    public StorageReference getImageReference(ImageType type) {
        return FirebaseStorage.getInstance().getReference("images/" + imageName + type.path);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageName() {
        return imageName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(imageName);
        dest.writeLong(timestamp);
    }

    public static final Parcelable.Creator<Report> CREATOR = new Parcelable.Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };
}
