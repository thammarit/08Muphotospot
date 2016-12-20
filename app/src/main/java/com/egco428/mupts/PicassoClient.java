package com.egco428.mupts;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Thammarit on 18/12/2559.
 */

/*Class PicassoClient created for put image into imageview from the url of images (Url of pictures in firebase storage)*/
public class PicassoClient {

    public static void downloadImage(Context c, String url, ImageView img)
    {

        if(url != null && url.length()>0)
        {
            Picasso.with(c).load(url).placeholder(R.drawable.oth).into(img); //if url was found then load the picture into imageview
        }else {
            Picasso.with(c).load(R.drawable.oth).into(img); //if not found picture or still loading, then set image to other picture
        }
    }
}
