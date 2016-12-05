package model;

import java.math.BigDecimal;

/**
 * Created by Administrator on 11/17/2016.
 */
public class Shipping extends Model {
    public String trackingNumber;
    public String service;
    public BigDecimal shipCost;

    public Shipping() {
        super("Shipping", "TrackingNumber");
        trackingNumber = "";
        service = "";
        shipCost = BigDecimal.ZERO;
    }
    public Shipping(Object[] info) {
        super("Shipping", "TrackingNumber");
        trackingNumber = (String) info[0];
        service = (String) info[1];
        shipCost = new BigDecimal((double) info[2]).setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    public String getModelId() {
        return trackingNumber;
    }
}
