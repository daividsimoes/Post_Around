package postaround.tcc.inatel.br.postaround;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.concurrent.ExecutionException;

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
    private ImageButton mImageButton;
    private CardView fotoCard;
    private ImageButton mImageButtonBack;

    private Location mLocation;

    private GoogleMap mGoogleMap;
    private Circle mCircle;
    private Marker mMarker;

    private Uri mImageUri;
    private ImageView mImgViewPic;
    private static final int CAMERA_PIC_REQUEST = 1;
    private static final int SELECT_PHOTO_REQUEST = 2;

    GetResponseAsync asyncTask = new GetResponseAsync(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.criar_post);


        mDescription = (EditText) findViewById(R.id.description);
        mSendButton = (ImageButton) findViewById(R.id.send_button);
        mImageButton = (ImageButton) findViewById(R.id.imageButton);
        mImgViewPic = (ImageView) findViewById(R.id.postImage);
        fotoCard = (CardView) findViewById(R.id.fotoCard);
        mImageButtonBack = (ImageButton) findViewById(R.id.menu_button);

        mImageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
                startActivityForResult(intent,0);
            }
        });

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_PIC_REQUEST){
            try {
                mImgViewPic.setVisibility(View.VISIBLE);
                fotoCard.setVisibility(View.GONE);
                mImgViewPic.setImageURI(mImageUri);
                sendPictureToCloudnary(mImageUri);
            } catch (Exception e) {
                e.printStackTrace();

                Toast t = Toast.makeText(this, "Fail", Toast.LENGTH_LONG);
                t.show();
            }
        }else if(requestCode == SELECT_PHOTO_REQUEST){

            try {
                mImageUri = data.getData();
                mImgViewPic.setImageURI(mImageUri);

            } catch (Exception e) {
                e.printStackTrace();

                Toast t = Toast.makeText(this, "Fail", Toast.LENGTH_LONG);
                t.show();
            }

        }else if(resultCode==3){
            try {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "Image from PostAí");
                mImageUri = this.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
            }
            catch (Exception ex)
            {
                Toast t = Toast.makeText(this, "Fail", Toast.LENGTH_LONG);
                t.show();
            }
        }else if(resultCode == 4){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO_REQUEST);
        }

    }

    private void sendPictureToCloudnary(Uri imageUri) {
        if (imageUri != null) {
            try {
                String path = getRealPathFromURI(imageUri);

                asyncTask.execute(path);
                //new DownloadImageAsync(imgViewPic).execute(urlResult);

                // Toast.makeText(this, urlResult, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = this.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
