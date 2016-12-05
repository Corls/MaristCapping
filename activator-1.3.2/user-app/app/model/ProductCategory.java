package model;

/**
 * Created by Administrator on 11/17/2016.
 */
public class ProductCategory extends Model {
    public int categoryId;
    public String category;

    public ProductCategory() {
        super("ProductCategory", "CategoryID");
        categoryId = -1;
        category = "";
    }
    public ProductCategory(Object[] info) {
        super("ProductCategory", "CategoryID");
        categoryId = (int) info[0];
        category = (String) info[1];
    }

    public String getModelId() {
        return Integer.toString(categoryId);
    }
}
