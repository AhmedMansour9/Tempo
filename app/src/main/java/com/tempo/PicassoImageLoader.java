package com.tempo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.veinhorn.scrollgalleryview.loader.MediaLoader;


/**
 * Created by veinhorn on 2/4/18.
 */

public class PicassoImageLoader implements MediaLoader {
    private String url;
    private Integer thumbnailWidth;
    private Integer thumbnailHeight;

    public PicassoImageLoader(String url) {
        this.url = url;
    }

    public PicassoImageLoader(String url, Integer thumbnailWidth, Integer thumbnailHeight) {
        this.url = url;
        this.thumbnailWidth = thumbnailWidth;
        this.thumbnailHeight = thumbnailHeight;
    }

    @Override
    public boolean isImage() {
        return true;
    }

    @Override
    public void loadMedia(Context context, final ImageView imageView, final MediaLoader.SuccessCallback callback) {
//        Picasso.with(context)
//                .load(url)
//                .placeholder(R.drawable.placeholder_image)
//                .into(imageView, new ImageCallback(callback));
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().override(500, 500))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            holder.ProgrossSpare.setVisibility(View.GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            holder.ProgrossSpare.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public void loadThumbnail(Context context, final ImageView thumbnailView, final MediaLoader.SuccessCallback callback) {
//        Picasso.with(context)
//                .load(url)
//                .resize(thumbnailWidth == null ? 100 : thumbnailWidth,
//                        thumbnailHeight == null ? 100 : thumbnailHeight)
//                .placeholder(R.drawable.placeholder_image)
//                .centerInside()
//                .into(thumbnailView, new ImageCallback(callback));


        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().override(thumbnailWidth == null ? 100 : thumbnailWidth,
                        thumbnailHeight == null ? 100 : thumbnailHeight).placeholder(R.drawable.placeholder_image)
)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            holder.ProgrossSpare.setVisibility(View.GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            holder.ProgrossSpare.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(thumbnailView);

    }

//    private static class ImageCallback implements Callback {
//        private final MediaLoader.SuccessCallback callback;
//
//        public ImageCallback(SuccessCallback callback) {
//            this.callback = callback;
//        }
//
//        @Override
//        public void onSuccess() {
//            callback.onSuccess();
//        }
//
//        @Override
//        public void onError() {
//
//        }
//    }
}
