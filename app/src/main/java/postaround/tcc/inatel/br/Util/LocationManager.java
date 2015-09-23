package postaround.tcc.inatel.br.Util;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import postaround.tcc.inatel.br.interfaces.LocationObservable;
import postaround.tcc.inatel.br.interfaces.LocationObserver;
import postaround.tcc.inatel.br.model.PostAoRedor;

/**
 * Created by Paulo on 20/09/2015.
 */
public class LocationManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,
        LocationObservable {

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Activity mActivity;
    private Location mCurrentLocation;

    private boolean isConnected = false;

    private List<LocationObserver> observers = new ArrayList<>();

    public LocationManager(Activity activity){
        mActivity = activity;

    }

    public HashMap<String, Double> getLocation() {
        HashMap<String, Double> location = new HashMap<String, Double>();

        if (mCurrentLocation != null) {
            double latitude = mCurrentLocation.getLatitude();
            double longitude = mCurrentLocation.getLongitude();
            location.put("latitude", latitude);
            location.put("longitude", longitude);
            return location;
        } else {
            Log.w("FAIL: ", "(Couldn't get the location. Make sure location is enabled on the device)");
            return null;
        }
    }

    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
        if (resultCode != ConnectionResult.SUCCESS) {
            Toast.makeText(mActivity.getApplicationContext(),
                    "Falha ao receber localização.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void startLocationUpdates() {
        createLocationRequest();

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public GoogleApiClient getmGoogleApiClient(){
        return mGoogleApiClient;
    }

    public boolean isConnected(){
        return isConnected;
    }

    @Override
    public void onConnected(Bundle bundle) {
        isConnected = true;
        createLocationRequest();
        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mCurrentLocation = location;
        notifyObserver();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void addObserver(LocationObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(LocationObserver o) {
        observers.remove(0);
    }

    @Override
    public void notifyObserver() {
        for(LocationObserver observer: observers){
            observer.update();
        }
    }
}
