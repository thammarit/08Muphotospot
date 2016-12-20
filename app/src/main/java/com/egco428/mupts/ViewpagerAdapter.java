package com.egco428.mupts;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Thammarit on 20/12/2559.
 */

class ViewpagerAdapter extends PagerAdapter {

    private Context context;
    private int[] imageId = {R.drawable.whatfor, R.drawable.using, R.drawable.locate};
    private String[] str_text = {"You want to take a photo with the wonderful view but you know the place. This app might help you","You can share the photo of your place that you want everyone to knows and also save photo of their place in your device","It's will show the location of the place and check it out!! "};
    ViewpagerAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {

        return imageId.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        View viewItem = inflater.inflate(R.layout.viewpager_item_layout, container, false);
        ImageView imageView = (ImageView) viewItem.findViewById(R.id.viewpagerImg);
        imageView.setImageResource(imageId[position]);
        TextView textView = (TextView) viewItem.findViewById(R.id.viewpagerText);
        textView.setText(str_text[position]);
        container.addView(viewItem);

        return viewItem;

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
