package ucsc.group12.agentmate.ui;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by NRV on 10/7/2014.
 */
public class LocationService extends Service
{
    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;



    Intent intent;
    int counter = 0;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int res = super.onStartCommand(intent, flags, startId);
        //Toast.makeText(getApplicationContext(), "Service LOC Started", Toast.LENGTH_LONG).show();

        Intent intent3 = new Intent();

        intent3.setAction("group12.tutorialspoint.StartLoc");
        sendBroadcast(intent3);

        return START_STICKY;
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        Log.d("GEO","Online");
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        Intent intent2 = new Intent();

        intent2.setAction("group12.tutorialspoint.StartLoc");
        sendBroadcast(intent2);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, listener);
        Log.d("GEO","Active");
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }





    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }



    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }




    public class MyLocationListener implements LocationListener
    {

        public void onLocationChanged(final Location loc)
        {
            //Log.i("**************************************", "Location changed"+loc.getLongitude()+" "+loc.getLatitude()+" "+loc.getProvider()+" "+loc.getTime());
            //dbc.upgradeLocation(String.valueOf(loc.getLongitude()),String.valueOf(loc.getLatitude()),String.valueOf(loc.getTime()));
            //Log.i("**************************************", "Location changed"+dbc.getFormLocation());

            Intent intent = new Intent();
            intent.putExtra("new_location"," New Location is "+loc.getAltitude()+" "+loc.getLongitude()+" "+loc.getLatitude());
            intent.putExtra("updated_time",loc.getTime());
            intent.putExtra("speed",loc.getSpeed());

            intent.setAction("group12.tutorialspoint.CUSTOM_INTENT");
            sendBroadcast(intent);

        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();///If GPS disabled this will set as location msg
            Intent intent = new Intent();
            intent.putExtra("new_location"," GPS Disabled");
            intent.putExtra("updated_time", Calendar.getInstance().getTime().toString());
            intent.putExtra("speed",0.0);
            intent.setAction("group12.tutorialspoint.CUSTOM_INTENT");
            sendBroadcast(intent);
        }


        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

    }
}