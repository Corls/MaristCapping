package model;

import java.math.BigDecimal;

/**
 * Created by Administrator on 11/16/2016.
 */
public class AdminAccount extends Model {
    public int accountId;
    public BigDecimal payRate;

    public AdminAccount() {
        super("AdminAccount", "AccountID");
        accountId = -1;
        payRate = BigDecimal.ZERO;
    }
    public AdminAccount(Object[] info) {
        super("AdminAccount", "AccountID");
        accountId = (int) info[0];
        payRate = new BigDecimal((double) info[1]).setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    public String getModelId() {
        return Integer.toString(accountId);
    }
}