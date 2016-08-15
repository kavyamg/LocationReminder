package kav.com.projectmap;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity {
    EditText txt;
    //public static SQLiteDatabase db;
    public String location;
    public DbHelper dbhelp;
    private GoogleMap mMap;
   public String txt1;
    LatLng latLng;
    Address address;
    EditText location_tf;
    ArrayList<DbHelper.data> array= new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        dbhelp = new DbHelper(this);
        dbhelp.getWritableDatabase();
        setUpMapIfNeeded();


    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    public void onSearch(View view) {
         location_tf = (EditText) findViewById(R.id.TFaddress);
        location = location_tf.getText().toString();
        List<Address> addressList = null;
        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                address = addressList.get(0);
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng,15 );
                mMap.moveCamera(update);

                AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
                alertbox.setTitle("Enter message");
                txt = new EditText(this);
                alertbox.setView(txt);
                alertbox.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        txt1 = txt.getText().toString();

                        mMap.addMarker(new MarkerOptions().position(latLng).title(txt1));
                        dbhelp.insert(location, txt1);
                        Constants.LANDMARKS.put(txt1,new LatLng (address.getLatitude(),address.getLongitude()));
                        Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                                           }
                });

                alertbox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
//                    dbhelp.insert(location, null);
                    }
                });
                alertbox.show();

            } catch (IndexOutOfBoundsException e){
                Toast.makeText(MapsActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
            }

            //c.HashMap

        }
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }

        array=dbhelp.getAllLabels();
        for (int i=0;i<array.size();i++){
            Log.d("hey",array.get(i).a + "\t" + array.get(i).s);


            List<Address> addressList = null;
            if (array.get(i).a != null || !array.get(i).a.equals("")) {
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(array.get(i).a, 1);


                } catch (IOException e) {
                    e.printStackTrace();
                }

                address = addressList.get(0);
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng,12 );
                mMap.moveCamera(update);
                mMap.addMarker(new MarkerOptions().position(latLng).title(array.get(i).s));
            }
        }
    }

    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        }
    

    public void next(View view) {

        Intent intent = new Intent(MapsActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
