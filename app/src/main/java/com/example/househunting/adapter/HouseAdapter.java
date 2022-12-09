package com.example.househunting.adapter;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.househunting.HouseDetailActivity;
import com.example.househunting.R;
import com.example.househunting.fragments.HomeFragment;
import com.example.househunting.model.house.Data;
import com.example.househunting.utils.EarthDistance;
import com.example.househunting.utils.CalculateDistance;
import com.example.househunting.utils.LoadImage;
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
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView card;
        private ImageView image;
        private TextView textName;
        private TextView address;
        private TextView priceText;
        private TextView distanceText;


        public ViewHolder(View view, final OnItemClickListener listener) {
            super(view);
            context = view.getContext();
            card = view.findViewById(R.id.card_house);
            image = view.findViewById(R.id.imageview);
            textName = view.findViewById(R.id.textName);
            address = view.findViewById(R.id.address);
            priceText = view.findViewById(R.id.textPrice);
            distanceText = view.findViewById(R.id.distance);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION);{
                            listener.onItemClick(position);
                        }
                    }

                }
            });

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

        return new ViewHolder(view, mListener);
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
  
        ArrayList<Double> coordinates = houseList.get(pos).getLocation().getCoordinates();
        float distance = CalculateDistance.getDistance( coordinates.get(1), coordinates.get(0));
        viewHolder.getAddress().setText(String.valueOf(distance) + " " + context.getString(R.string.near));
        viewHolder.getTextName().setText(houseList.get(pos).getLocation().getAddress());
        viewHolder.getPriceText().setText(NumberFormat.getInstance().format(houseList.get(pos).getPriceMonthly()) + context.getString(R.string.currency));
        LoadImage.loadImage(context, houseList.get(pos).getImageCover(), viewHolder.getImage(), R.drawable.card_back);
    }

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

