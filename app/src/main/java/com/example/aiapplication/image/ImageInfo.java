package com.example.aiapplication.image;


import android.graphics.Bitmap;

import java.util.Optional;

public class ImageInfo {

    private static final ImageInfo IMAGE_INFO = new ImageInfo();

    private Optional<Bitmap> bitmap;

    private ImageInfo() {
        bitmap = Optional.empty();
    }

    public static ImageInfo getInstance() {
        return IMAGE_INFO;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = Optional.of(bitmap);
    }

    public Optional<Bitmap> getBitmap() {
        return bitmap;
    }
}
