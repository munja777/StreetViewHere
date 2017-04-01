package munja777.streetview;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Timer;
import java.util.TimerTask;


public class StreetViewActivity extends FragmentActivity {

    protected LocationManager mLocationManager;
    Location mLocation;
    Double lat, lon;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, mLocationListener);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, mLocationListener);
    }

    private void toast(String str) {  Toast.makeText(this, str, Toast.LENGTH_LONG).show();  }

    // Modified from Google Documentation: toast and open streetview when location changed
    final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {

            toast(getLocation().toString());

            final SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
                    (SupportStreetViewPanoramaFragment)
                            getSupportFragmentManager().findFragmentById(R.id.streetviewpanorama);

            streetViewPanoramaFragment.getStreetViewPanoramaAsync(
                    new OnStreetViewPanoramaReadyCallback() {

                        @Override
                        public void onStreetViewPanoramaReady(final StreetViewPanorama panorama) {
                                panorama.setPosition(getLocation());
                        }
                    });
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    // getLocation from Google Documentation
    public LatLng getLocation() {

        Criteria criteria = new Criteria();
        String bestProvider = mLocationManager.getBestProvider(criteria, false);
        mLocation = mLocationManager.getLastKnownLocation(bestProvider);
        lat = mLocation.getLatitude ();
        lon = mLocation.getLongitude ();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return null;

        try {
            return new LatLng(lat, lon);
        }
        catch (NullPointerException e){
            return null;
        }
    }
    /*
    I store this here for future releases, just not to forget it:

    new Timer().scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    panorama.setPosition(getLocation());
                                }
                            }, 0, 10000);
     */

}

