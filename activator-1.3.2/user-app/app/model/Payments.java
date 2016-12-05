package model;

import java.util.Date;

/**
 * Created by Administrator on 11/17/2016.
 */
public class Payments extends Model {
    public int payId;
    public boolean paid;
    public Date datePaid;

    public Payments() {
        super("Payments", "PayID");
        payId = -1;
        paid = false;
        datePaid = null;
    }
    public Payments(Object[] info) {
        super("Payments", "PayID");
        payId = (int) info[0];
        paid = (boolean) info[1];
        datePaid = (Date) info[2];
    }

    public String getModelId() {
        return Integer.toString(payId);
    }
}
