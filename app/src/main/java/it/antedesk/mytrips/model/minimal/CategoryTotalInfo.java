package it.antedesk.mytrips.model.minimal;

import android.arch.persistence.room.ColumnInfo;

public class CategoryTotalInfo {

    private int total;
    private String category;
    @ColumnInfo(name = "category_id")
    private String categoryId;

    public CategoryTotalInfo(int total, String category, String categoryId) {
        this.total = total;
        this.category = category;
        this.categoryId = categoryId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
