package model;

import java.math.BigDecimal;

/**
 * Created by Administrator on 11/17/2016.
 */
public class Products extends Model {
    public int pid;
    public int categoryId;
    public String product;
    public String description;
    public String color;
    public BigDecimal retailPrice;
    public BigDecimal wholesalePrice;
    public int qtyRemaining;
    public int qtySold;
    public String size;
    public String image;

    public Products() {
        super("Products", "Pid");
        pid = -1;
        categoryId = -1;
        product = "";
        description = "";
        color = "";
        retailPrice = BigDecimal.ZERO;
        wholesalePrice = BigDecimal.ZERO;
        qtyRemaining = 0;
        qtySold = 0;
        size = "N/A";
        image = "";
    }
    public Products(Object[] info) {
        super("Products", "Pid");
        pid = (int) info[0];
        categoryId = (int) info[1];
        product = (String) info[2];
        description = (String) info[3];
        color = (String) info[4];
        retailPrice = new BigDecimal((double) info[5]).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        wholesalePrice = new BigDecimal((double) info[6]).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        qtyRemaining = (int) info[7];
        qtySold = (int) info[8];
        size = (String) info[9];
        image = (String) info[10];
    }

    public String getModelId() {
        return Integer.toString(pid);
    }
}
