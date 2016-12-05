package model;

/**
 * Created by Administrator on 11/16/2016.
 */
public class Accounts extends Model {
    public int accountId;
    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public String email;
    public String address;
    public int zip;

    public Accounts() {
        super("Accounts", "AccountID");
        accountId = -1;
        username = "";
        password = "";
        firstName = "";
        lastName = "";
        email = "";
        address = "";
        zip = 0;
    }
    public Accounts(Object[] info) {
        super("Accounts", "AccountID");
        accountId = (int) info[0];
        username = (String) info[1];
        password = (String) info[2];
        firstName = (String) info[3];
        lastName = (String) info[4];
        email = (String) info[5];
        address = (String) info[6];
        zip = (int) info[7];
    }

    public String getModelId() {
        return Integer.toString(accountId);
    }
}
