package com.example.househunting.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.househunting.HouseDetail;
import com.example.househunting.R;
import com.example.househunting.model.house.Data;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/** Author: David
 * House adapter
 * */
public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.ViewHolder> {

    private ArrayList<Data> houseList;
    private int length;
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


        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            card = view.findViewById(R.id.card_house);
            image = view.findViewById(R.id.imageview);
            textName = view.findViewById(R.id.textName);
            address = view.findViewById(R.id.address);
            priceText = view.findViewById(R.id.textPrice);
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

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, HouseDetail.class);
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
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Log.d("Test", "================"+houseList.size());
        viewHolder.getTextName().setText(houseList.get(pos).getOwnerInfo().getNames());
        viewHolder.getAddress().setText(houseList.get(pos).getLocation().getAddress());
        viewHolder.getPriceText().setText(Integer.toString(houseList.get(pos).getPriceMonthly()));
        new DownloadImageFromInternet((ImageView) viewHolder.getImage()).execute(houseList.get(pos).getImageCover());
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

