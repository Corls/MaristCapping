package model;

import java.math.BigDecimal;

/**
 * Created by Administrator on 11/16/2016.
 */
public class Discounts extends Model {
    public int discountId;
    public BigDecimal discountAmount;
    public String description;

    public Discounts() {
        super("Discounts", "DiscountID");
        discountId = -1;
        discountAmount = BigDecimal.ZERO;
        description = "";
    }
    public Discounts(Object[] info) {
        super("Discounts", "DiscountID");
        discountId = (int) info[0];
        discountAmount = new BigDecimal((double) info[1]).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        description = (String) info[2];
    }

    public String getModelId() {
        return Integer.toString(discountId);
    }
}
