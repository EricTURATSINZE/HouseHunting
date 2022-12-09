package com.example.househunting;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.househunting.services.LocationService;

public class LocationDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Location")
                .setMessage("Latitude: "+ LocationService.getLocation().getLatitude() +"\nLongitude: " + LocationService.getLocation().getLongitude())
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        startActivity(new Intent(LocationDialog.this, RegisterHouseFirstStep.class));
                    }
                });

        return builder.create();
    }
}
