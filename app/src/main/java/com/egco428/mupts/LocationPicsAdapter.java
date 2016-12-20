package com.egco428.mupts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Thammarit on 18/12/2559.
 */

public class LocationPicsAdapter extends RecyclerView.Adapter<LocationPicsAdapter.MyViewHolder> {

    /*Class LocationPicsAdapter that create Recycler view to PhotoActivity */

    private List<LocationPics> locationPicsList;
    private Context context;
    private String getLocationText;

    /*Declare MyViewHolder class to keep our TextView and ImageView
    * with the recycler view that we have created*/

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView img;


        public MyViewHolder(View view){
            super(view);
            username = (TextView) view.findViewById(R.id.nameTxt);
            img = (ImageView) view.findViewById(R.id.LocationImg);
        }
    }

    /*Contructor that initiliaze value in parameter*/

    public LocationPicsAdapter(Context applicationContext, List<LocationPics> locationPicsList, String getLocationText){
        this.locationPicsList = locationPicsList;
        this.context = applicationContext;
        this.getLocationText = getLocationText;
    }

    /*Inflate our recycler view with our recycler custom layout
    * that we have created*/
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.locationpics_row_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    /*Set text to our TextView and set imageUrl by Picasso (third party gradle library) that
    have been uploaded to the server
      */
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final LocationPics locationPics = locationPicsList.get(position);
        holder.username.setText(locationPics.getUsername());

        PicassoClient.downloadImage(context,locationPics.getUrl(),holder.img);

        /*Set onclicklistener to each photo in recyclerview*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mapIntent = new Intent(view.getContext(), MapsActivity.class); //intent to MapsActivity
                mapIntent.putExtra("LONAME",getLocationText); //sent getLocationText to MapsActivity
                mapIntent.putExtra("POSITION", String.valueOf(position)); //sent position to MapsActivity (to find and get data from pic's node by postition oder in firebase database)
                view.getContext().startActivity(mapIntent); //start MapsActivity

            }
        });
    }

    @Override
    public int getItemCount() {

        //return size of locationPicsList
        return locationPicsList.size();
    }
}
