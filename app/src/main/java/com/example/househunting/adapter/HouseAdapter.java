package com.example.househunting.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.househunting.HouseDetailActivity;
import com.example.househunting.R;
import com.example.househunting.fragments.HomeFragment;
import com.example.househunting.model.house.Data;
import com.example.househunting.utils.EarthDistance;
import com.example.househunting.utils.LoadImage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/** Author: David
 * House adapter
 * */
public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.ViewHolder> {

    private ArrayList<Data> houseList;
    private int length;
    private Location location;
    private static Context context;



    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView card;
        private ImageView image;
        private TextView textName;
        private TextView address;
        private TextView priceText;
        private TextView distanceText;


        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            card = view.findViewById(R.id.card_house);
            image = view.findViewById(R.id.imageview);
            textName = view.findViewById(R.id.textName);
            address = view.findViewById(R.id.address);
            priceText = view.findViewById(R.id.textPrice);
            distanceText = view.findViewById(R.id.distance);
            view.setOnClickListener(this);

        }

        public CardView getCardView() {
            return card;
        }

        public ImageView getImage() {
            return image;
        }

        public TextView getTextName() {
            return textName;
        }

        public TextView getAddress() {
            return address;
        }

        public TextView getPriceText() {
            return priceText;
        }

        public TextView getDistanceText() {
            return distanceText;
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, HouseDetailActivity.class);
            intent.putExtra("houseId", "");
            context.startActivity(intent);
//            Log.d(TAG, "onClick+++++++++++++++++++++++++++++ ");
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param _houseList Array[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public HouseAdapter(ArrayList<Data> _houseList) {
        houseList = _houseList;
        length = _houseList.size();
    }

    public HouseAdapter(ArrayList<Data> _houseList, Location loc) {
        houseList = _houseList;
        length = _houseList.size();
        location = loc;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.house_card, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        int pos = position;
        Location loc = new Location("dummyprovider");
        loc.setLatitude(houseList.get(pos).getLocation().getCoordinates().get(0));
        loc.setLongitude(houseList.get(pos).getLocation().getCoordinates().get(1));
        int distance = EarthDistance.distance(location, loc);

        if(houseList.get(pos).getDistance() == null)
            houseList.get(pos).setDistance(distance);

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextName().setText(houseList.get(pos).getLocation().getAddress());
        viewHolder.getDistanceText().setText("Distance: "+ formatter.format(houseList.get(pos).getDistance())+" km");
        viewHolder.getAddress().setText(houseList.get(pos).getBedRooms()+" Bed rooms");
        viewHolder.getPriceText().setText("~$ "+Integer.parseInt(formatter.format(houseList.get(pos).getPriceMonthly()/1000)));
        LoadImage.loadImage(context, houseList.get(pos).getImageCover(), viewHolder.getImage(), R.drawable.card_back);
//        new DownloadImageFromInternet((ImageView) viewHolder.getImage()).execute(houseList.get(pos).getImageCover());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return length;
    }
    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        public ImageView imageView;
        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView=imageView;
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage=BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}

