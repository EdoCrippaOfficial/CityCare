package inc.elevati.imycity.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Class that represents a report; Parcelable interface is implemented to
 * permit a report to be passed as an argument to a ReportDialog
 */
public class Report implements Parcelable {

    /** String representing the name appendix to retrieve full image from cloud storage */
    public static final String IMAGE_FULL = "_img";

    /** String representing the name appendix to retrieve thumbnail from cloud storage */
    public static final String IMAGE_THUMBNAIL = "_thumb";

    /** Constant representing an accepted report */
    public static final int STATUS_ACCEPTED = 1;

    /** Constant representing a refused report */
    public static final int STATUS_REFUSED = 2;

    /** Constant representing a completed report */
    public static final int STATUS_COMPLETED = 3;

    /** Constant representing a waiting report */
    public static final int STATUS_WAITING = 4;

    /** Report fields, self-descriptive */
    private String id, userId, userName, title, description, reply, operatorId;

    /** The report creation time, in milliseconds from January 1 1970, 00:00 UTC */
    private long timestamp;

    /** The number of stars that this report received */
    private int nStars;

    /** GPS latitude and longitude of the report */
    private long latitude, longitude;

    /** The report status (STATUS_ACCEPTED, STATUS_REFUSED, STATUS_COMPLETED, STATUS_WAITING) */
    private int status;

    /**
     * Public constructor used when new report is created
     * @param id the report id (that also refers to image in storage)
     * @param title the report title
     * @param description the report description
     * @param timestamp the report creation time, in milliseconds from January 1 1970, 00:00 UTC
     * @param userId the id of the user who created this report
     * @param latitude the GPS latitude of the report
     * @param longitude the GPS longitude of the report
     */
    public Report(String id, String title, String description, long timestamp, String userId, String userName, long latitude, long longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.userId = userId;
        this.userName = userName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = STATUS_WAITING;
        this.nStars = 0;
    }

    /**
     * Public constructor used when report is retrieved from database
     * @param id the report id (that also refers to image in storage)
     * @param userId the id of the user who created this report
     * @param title the report title
     * @param description the report description
     * @param reply the reply from operator
     * @param operatorId the operator id
     * @param timestamp the report creation time, in milliseconds from January 1 1970, 00:00 UTC
     * @param nStars the number of stars that this report received
     * @param latitude the GPS latitude of the report
     * @param longitude the GPS longitude of the report
     * @param status the report status
     */
    public Report(String id, String userId, String userName, String title, String description, String reply,
                  String operatorId, long timestamp, int nStars, long latitude, long longitude, int status) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.title = title;
        this.description = description;
        this.reply = reply;
        this.operatorId = operatorId;
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
        this.id = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.timestamp = in.readLong();
        this.userId = in.readString();
        this.userName = in.readString();
        this.reply = in.readString();
        this.operatorId = in.readString();
        this.nStars = in.readInt();
        this.latitude = in.readLong();
        this.longitude = in.readLong();
        this.status = in.readInt();
    }

    /**
     * Method that retrieves the storage reference to this report image
     * @param type the image type required (IMAGE_FULL or IMAGE_THUMBNAIL)
     * @return he storage reference to this report image
     */
    public StorageReference getImageReference(String type) {
        return FirebaseStorage.getInstance().getReference("images/" + id + type);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
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

    public String getUserName() {
        return userName;
    }

    public String getReply() {
        return reply;
    }

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
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeLong(timestamp);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(reply);
        dest.writeString(operatorId);
        dest.writeInt(nStars);
        dest.writeLong(latitude);
        dest.writeLong(longitude);
        dest.writeInt(status);
    }

    /** Required by Parcelable interface */
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
