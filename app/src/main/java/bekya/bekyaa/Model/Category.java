package bekya.bekyaa.Model;

import android.support.annotation.NonNull;

/**
 * Created by kunda on 10/2/2017.
 */

public class Category {
    private String catogories;
    private String img;
    private String category;

    public Category() {

    }

    public Category(String catogories, String img) {
        this.catogories = catogories;
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCatogories() {
        return catogories;
    }

    public void setCatogories(String catogories) {
        this.catogories = catogories;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @NonNull
    @Override
    public String toString() {
        return catogories;
    }
}
