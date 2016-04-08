package me.dotaparty;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by evaldo on 06/04/16.
 */
public class ImageViewCharger extends AsyncTask<String, Void, Drawable> {

    private final ImageView imageView;

    public ImageViewCharger(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Drawable doInBackground(String... params) {
        String url = params[0];
        Drawable image = null;
        try {
            InputStream in = new URL(url).openStream();
            image = Drawable.createFromStream(in, "src");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onPostExecute(Drawable result) {
        imageView.setImageDrawable(result);
    }
}
