package model;

/**
 * Created by Administrator on 11/16/2016.
 */
public class Guest extends Model {
    public int gid;
    public String firstName;
    public String lastName;
    public String email;
    public String shipAddress;
    public int zip;

    public Guest() {
        super("Guest", "Gid");
        gid = -1;
        firstName = "";
        lastName = "";
        email = "";
        shipAddress = "";
        zip = 0;
    }
    public Guest(Object[] info) {
        super("Guest", "Gid");
        gid = (int) info[0];
        firstName = (String) info[1];
        lastName = (String) info[2];
        shipAddress = (String) info[3];
        email = (String) info[4];
        zip = (int) info[5];
    }

    public String getModelId() {
        return Integer.toString(gid);
    }
}
