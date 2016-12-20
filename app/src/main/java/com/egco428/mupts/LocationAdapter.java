package com.egco428.mupts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thammarit on 17/12/2559.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {

    /*Recycler view adapter of MainsActivity that show the list of
    * Mahidol Faculty*/

    private List<Location> locationList;
    private Context context;

    /*Declare MyViewHolder class to keep our TextView and ImageView
    * with the recycler view that we have created*/

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView location;
        public ImageView locationImg;

        public MyViewHolder(View view){
            super(view);
            location = (TextView) view.findViewById(R.id.locationText);
            locationImg = (ImageView) view.findViewById(R.id.locationImage);
        }
    }

    /*Contructor that initiliaze value in parameter*/

    public LocationAdapter(Context applicationContext, ArrayList<Location> locationList){
        this.locationList = locationList;
        this.context = applicationContext;
    }

    /*Inflate our recycler view with our recycler custom layout
    * that we have created*/

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_row_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    /*Set Text and ImageResource with TextView and ImageView that we have
    * created in holder of MyViewHolder by posttion*/

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Location location = locationList.get(position);
        holder.location.setText(location.getLocation());
        holder.locationImg.setImageResource(location.getImageLocation());


        //set OnClickListener to each item in recycler view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Location " + holder.getAdapterPosition() + " " + ":" + " "
                        + location.getLocation());

                /*Click each recycler view in MainActivity and sen intent to PhotoActivity
                (Activity that keep picture in side each Mahidol's faculty)*/
                Intent locationIntent = new Intent(view.getContext(), PhotoActivity.class);
                //send intent getLocation (variable that store name of the Faculty)
                locationIntent.putExtra("LOCATION", location.getLocation()); // will later user to get data form Firebase database
                view.getContext().startActivity(locationIntent); //Start or desired activity
            }
        });
    }
    @Override
    public int getItemCount() {
        //return number of locationList
        return locationList.size();
    }
}
