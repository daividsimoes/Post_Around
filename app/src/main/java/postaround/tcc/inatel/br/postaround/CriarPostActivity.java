package postaround.tcc.inatel.br.postaround;

import android.app.Fragment;
import android.location.Location;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import postaround.tcc.inatel.br.Util.UserInformation;
import postaround.tcc.inatel.br.interfaces.RestAPI;
import postaround.tcc.inatel.br.model.Loc;
import postaround.tcc.inatel.br.model.Post;
import postaround.tcc.inatel.br.model.PostPostRes;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CriarPostActivity extends AppCompatActivity implements OnMapReadyCallback{

    private EditText mDescription;
    private ImageButton mSendButton;

    private Location mLocation;

    private GoogleMap mGoogleMap;
    private Circle mCircle;
    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.criar_post);

        mDescription = (EditText) findViewById(R.id.description);
        mSendButton = (ImageButton) findViewById(R.id.send_button);

        mSendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String description = mDescription.getText().toString();
                HashMap<String, Double> location = getLocation();
                List<Double> list = new ArrayList<Double>();
                if (location != null) {
                    list.add(location.get("longitude"));
                    list.add(location.get("latitude"));
                }

                Post post = new Post();
                Loc loc = new Loc();

                post.setDescription(description);
                post.setUser_id(UserInformation.user_id);
                loc.setCoordinates(list);
                loc.setType("Point");
                post.setLoc(loc);

                RestAdapter retrofit = new RestAdapter.Builder()
                        .setEndpoint("http://api-tccpostaround.rhcloud.com/api")
                        .build();

                RestAPI restAPI = retrofit.create(RestAPI.class);

                restAPI.postPost(post, new Callback<PostPostRes>() {
                    @Override
                    public void success(PostPostRes post, Response response) {
                        Log.v("Resposta : ", response.getBody().toString());
                        if (post != null) {
                            Log.v("res:", "" + post.getMessage());
                            closeActivity();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.v("Erro : ", error.getMessage());
                    }
                });

            }
        });

        ((MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment)).getMapAsync(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_criar_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void closeActivity(){
        this.finish();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        mGoogleMap = googleMap;

        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mLocation = location;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (mMarker == null || mCircle == null) {
                    drawMarkerWithCircleAndZoom(latLng);
                } else {
                    updateMarker(latLng);
                }
            }
        });


    }

    private void updateMarker(LatLng position) {
        mCircle.setCenter(position);
        mMarker.setPosition(position);
    }

    private void drawMarkerWithCircleAndZoom(LatLng position){
        double radiusInMeters = 300.0;
        int strokeColor = 0xffF57C00;
        int shadeColor = 0x44FFCC80;

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters)
                .fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        mCircle = mGoogleMap.addCircle(circleOptions);

        MarkerOptions markerOptions = new MarkerOptions().position(position);
        mMarker = mGoogleMap.addMarker(markerOptions);

        CameraUpdate center=
                CameraUpdateFactory.newLatLng(position);
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

        mGoogleMap.moveCamera(center);
        mGoogleMap.animateCamera(zoom);
    }

    private HashMap<String, Double> getLocation() {
        HashMap<String, Double> location = new HashMap<String, Double>();
        if (mLocation != null) {
            double latitude = mLocation.getLatitude();
            double longitude = mLocation.getLongitude();
            location.put("latitude", latitude);
            location.put("longitude", longitude);
            return location;
        } else {
            Log.w("FAIL: ", "(Couldn't get the location. Make sure location is enabled on the device)");
            return null;
        }
    }
}
