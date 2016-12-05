package model;

import java.math.BigDecimal;

/**
 * Created by Administrator on 11/16/2016.
 */
public class ZipLookup extends Model {
    public int zip;
    public String city;
    public String state;
    public BigDecimal taxAmount;

    public ZipLookup() {
        super("ZipLookup", "Zip");
        zip = 0;
        city = "";
        state = "";
        taxAmount = BigDecimal.ZERO;
    }
    public ZipLookup(Object[] info) {
        super("ZipLookup", "Zip");
        zip = (int) info[0];
        city = (String) info[1];
        state = (String) info[2];
        taxAmount = (BigDecimal) info[3];
    }

    public String getModelId() {
        return Integer.toString(zip);
    }
}
