package com.example.househunting.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.househunting.R;
import com.example.househunting.model.RootResponse;
import com.example.househunting.model.house.Data;
import com.example.househunting.network.HouseApiService;
import com.example.househunting.network.RetrofitClient;
import com.example.househunting.utils.EarthDistance;
import com.example.househunting.utils.CalculateDistance;
import com.example.househunting.utils.LoadImage;
import com.example.househunting.utils.Storage;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/** Author: David
 * House adapter
 * */
public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.ViewHolder> {

    private static ArrayList<Data> houseList;
    private int length;
    private Location location;
    private static Context context;
    private boolean isMyHouse = false;
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

    public static class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        private CardView card;
        private ImageView image;
        private TextView textName;
        private TextView address;
        private TextView priceText;
        private TextView distanceText;
        private TextView myhouse;


        public ViewHolder(View view, final OnItemClickListener listener) {
            super(view);
            context = view.getContext();
            card = view.findViewById(R.id.card_house);
            image = view.findViewById(R.id.imageview);
            textName = view.findViewById(R.id.textName);
            address = view.findViewById(R.id.address);
            priceText = view.findViewById(R.id.textPrice);
            myhouse = view.findViewById(R.id.manage_house);
            myhouse.setOnClickListener(this);

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

        public TextView getMyhouse() {return myhouse;}

        public void showManageMenu(View v)
        {

            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.manage_house_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public void onClick(View v) {
            showManageMenu(v);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.delete:
                    deleteHouse(getAbsoluteAdapterPosition());
                    return true;
                case R.id.booked:
                    bookedHouse(getAbsoluteAdapterPosition());
                    return true;
                default:
                    return false;
            }
        }
        private void bookedHouse(int pos) {
            Storage storage = new Storage(context);
            String token = storage.getToken();
            RetrofitClient.getClient("").create(HouseApiService.class)
                    .getBookedHouse(houseList.get(pos).get_id(), token)
                    .enqueue(new Callback<RootResponse>() {

                        @Override
                        public void onResponse(Call<RootResponse> call, Response<RootResponse> response) {
                        }

                        @Override
                        public void onFailure(Call<RootResponse> call, Throwable t) {

                        }
                    });

/** End of david edit */
        }
        private void deleteHouse(int pos) {
            Storage storage = new Storage(context);
            String token = storage.getToken();
            RetrofitClient.getClient("").create(HouseApiService.class)
                    .getDeleteHouse(houseList.get(pos).get_id(), token)
                    .enqueue(new Callback<RootResponse>() {
                        @Override
                        public void onResponse(Call<RootResponse> call, Response<RootResponse> response) {
                        }

                        @Override
                        public void onFailure(Call<RootResponse> call, Throwable t) {

                        }
                    });

/** End of david edit */
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
    public HouseAdapter(ArrayList<Data> _houseList, boolean _isMyHouse) {
        houseList = _houseList;
        length = _houseList.size();
        isMyHouse = _isMyHouse;

    }

    public HouseAdapter(ArrayList<Data> _houseList, Location loc, boolean _isMyHouse) {
        houseList = _houseList;
        length = _houseList.size();
        location = loc;
        isMyHouse =_isMyHouse;


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
        int nearDistance = EarthDistance.distance(location, loc);


        viewHolder.getMyhouse().setVisibility(isMyHouse?View.VISIBLE: View.GONE);
        if(houseList.get(pos).getDistance() == null)
            houseList.get(pos).setDistance(nearDistance);

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

