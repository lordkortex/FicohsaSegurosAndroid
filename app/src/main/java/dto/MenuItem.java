package dto;

import android.graphics.Bitmap;

/**
 * Created by mac on 7/10/15.
 */
public class MenuItem {
    Bitmap image;
    String title;

    public MenuItem(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
    }
    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


}
