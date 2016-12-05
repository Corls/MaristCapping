package model;

import java.util.Date;

/**
 * Created by Administrator on 11/16/2016.
 */
public class CustomerAccount extends Model {
    public int accountId;
    public int creditCardNumber;
    public Date creditCardExp;
    public int creditCardCvv;

    public CustomerAccount() {
        super("CustomerAccount", "AccountID");
        accountId = -1;
        creditCardNumber = 0;
        creditCardExp = null;
        creditCardCvv = 0;
    }
    public CustomerAccount(Object[] info) {
        super("CustomerAccount", "AccountID");
        accountId = (int) info[0];
        creditCardNumber = (int) info[1];
        creditCardExp = (Date) info[2];
        creditCardCvv = (int) info[3];
    }

    public String getModelId() {
        return Integer.toString(accountId);
    }
}
