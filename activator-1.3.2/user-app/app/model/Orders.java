package model;

import java.util.Date;

/**
 * Created by Administrator on 11/16/2016.
 */
public class Orders extends Model {
    public int oid;
    public int pid;
    public int gid;
    public int accountId;
    public String trackingNumber;
    public int payId;
    public int discountId;
    public Date orderDate;
    public int qtyOrdered;

    public Orders() {
        super("Orders", "Oid");
        oid = -1;
        pid = -1;
        gid = -1;
        accountId = -1;
        trackingNumber = "";
        payId = -1;
        discountId = -1;
        orderDate = null;
        qtyOrdered = 0;
    }
    public Orders(Object[] info) {
        super("Orders", "Oid");
        oid = (int) info[0];
        pid = (int) info[1];
        gid = (int) info[2];
        accountId = (int) info[3];
        trackingNumber = (String) info[4];
        payId = (int) info[5];
        discountId = (int) info[6];
        orderDate = (Date) info[7];
        qtyOrdered = (int) info[8];
    }

    public String getModelId() {
        return Integer.toString(oid);
    }
}
