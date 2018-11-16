package inc.elevati.imycity.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Class that represents a report; Parcelable interface is implemented to
 * permit a report to be passed as an argument to a ReportDialog
 */
public class Report implements Parcelable {

    /**
     * Constants that identify the type of image requested
     */
    public static final String IMAGE_FULL = "/img";
    public static final String IMAGE_THUMBNAIL = "/thumb";

    public static final int STATUS_ACCEPTED = 1;
    public static final int STATUS_REFUSED = 2;
    public static final int STATUS_COMPLETED = 3;
    public static final int STATUS_WAITING = 4;


    private String id, userId, title, description, reply, operatorId;
    private String imageName;

    /**
     * The report creation time, in milliseconds from January 1 1970, 00:00 UTC
     */
    private long timestamp;

    /**
     * The number of stars that this report received
     */
    private int nStars;

    /**
     * GPS latitude and longitude of the report
     */
    private long latitude, longitude;

    private int status;

    /**
     * Public constructor used when new report is created
     * @param title the report title
     * @param description the report description
     * @param imageName the image name in storage
     * @param timestamp the report creation time, in milliseconds from January 1 1970, 00:00 UTC
     * @param userId the id of the user who created this report
     */
    public Report(String title, String description, String imageName, long timestamp, String userId, long latitude, long longitude) {
        this.id = imageName;
        this.title = title;
        this.description = description;
        this.imageName = imageName;
        this.timestamp = timestamp;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = STATUS_WAITING;
        this.nStars = 0;
    }

    /**
     * Public constructor used when report is retrieved from database
     * @param id this report id
     * @param userId the id of the user who created this report
     * @param title the report title
     * @param description the report description
     * @param reply the reply from operator
     * @param operatorId the operator id
     * @param imageName the image name in storage
     * @param timestamp the report creation time, in milliseconds from January 1 1970, 00:00 UTC
     * @param nStars the number of stars that this report received
     * @param latitude the GPS latitude of the report
     * @param longitude the GPS longitude of the report
     * @param status the report status
     */
    public Report(String id, String userId, String title, String description, @Nullable String reply, @Nullable String operatorId,
                  String imageName, long timestamp, int nStars, long latitude, long longitude, int status) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.reply = reply;
        this.operatorId = operatorId;
        this.imageName = imageName;
        this.timestamp = timestamp;
        this.nStars = nStars;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    /**
     * Constructor required by Parcelable interface
     * @param in the Parcel object that represents this report
     */
    private Report(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.imageName = in.readString();
        this.timestamp = in.readLong();
    }

    /**
     * Method that retrieves the storage reference to this report image
     * @param type the image type required (IMAGE_FULL or IMAGE_THUMBNAIL)
     * @return he storage reference to this report image
     */
    public StorageReference getImageReference(String type) {
        return FirebaseStorage.getInstance().getReference("images/" + imageName + type);
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

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    @Nullable
    public String getReply() {
        return reply;
    }

    @Nullable
    public String getOperatorId() {
        return operatorId;
    }

    public int getnStars() {
        return nStars;
    }

    public long getLatitude() {
        return latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Method required by Parcelable interface
     * @param dest the Parcel object to be written
     * @param flags write flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(imageName);
        dest.writeLong(timestamp);
    }

    /**
     * Required by Parcelable interface
     */
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
