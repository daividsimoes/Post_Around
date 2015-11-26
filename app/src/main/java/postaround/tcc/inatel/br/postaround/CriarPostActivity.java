package postaround.tcc.inatel.br.postaround;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import postaround.tcc.inatel.br.async.GetResponseAsync;
import postaround.tcc.inatel.br.model.Loc;
import postaround.tcc.inatel.br.model.Post;

public class CriarPostActivity extends AppCompatActivity implements OnMapReadyCallback{

    private static final int CAMERA_RESULT = 1;
    private static final int SELECT_PHOTO_RESULT = 2;
    private static final int CAMERA_PIC_REQUEST = 3;
    private static final int SELECT_PHOTO_REQUEST = 4;

    private EditText mDescription;
    private ImageButton mSendButton;
    private ImageButton mImageButton;
    private ImageButton mImageButtonBack;
    private CardView fotoCard;
    private CardView postImageCard;

    private Location mLocation;

    private GoogleMap mGoogleMap;
    private Circle mCircle;
    private Marker mMarker;

    private Uri mImageUri;
    private ImageView mImgViewPic;

    private GetResponseAsync asyncTask;

    private String userId;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_post);

        getUserInfo();

        asyncTask = new GetResponseAsync(this);

        mDescription = (EditText) findViewById(R.id.description);
        mSendButton = (ImageButton) findViewById(R.id.send_button);
        mImageButton = (ImageButton) findViewById(R.id.imageButton);
        mImgViewPic = (ImageView) findViewById(R.id.postImage);
        mImageButtonBack = (ImageButton) findViewById(R.id.menu_button);
        fotoCard = (CardView) findViewById(R.id.fotoCard);
        postImageCard = (CardView) findViewById(R.id.postImageCard);

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
                startActivityForResult(intent, 0);
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String description = mDescription.getText().toString();
                if (description.equals(null) || description.equals("")) {
                    Toast.makeText(getApplication(), "Post deve possuir uma descrição", Toast.LENGTH_LONG).show();
                } else {
                    HashMap<String, Double> location = getLocation();
                    List<Double> list = new ArrayList<Double>();
                    if (location != null) {
                        list.add(location.get("longitude"));
                        list.add(location.get("latitude"));
                    }

                    Post post = new Post();
                    Loc loc = new Loc();

                    post.setDescription(description);
                    post.setUser_id(userId);
                    post.setUser_name(userName);
                    loc.setCoordinates(list);
                    loc.setType("Point");
                    post.setLoc(loc);

                    String path = null;
                    if (mImageUri != null) {
                        try {
                            path = getRealPathFromURI(mImageUri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    post.setImage_url(path);
                    asyncTask.execute(post);
                }
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
        SharedPreferences prefs;
        int raio;
        String maxDis;
        if( (prefs = this.getSharedPreferences("raio_confg", this.MODE_PRIVATE))!= null){
            raio = prefs.getInt("raio",R.id.raio_um);
        }else{
            raio = R.id.raio_um;
        }
        switch (raio){
            case  R.id.raio_um:
                maxDis = "300";
                break;

            case  R.id.raio_dois:
                maxDis = "1000";
                break;

            case  R.id.raio_tres:
                maxDis = "2000";
                break;

            default:
                maxDis = "300";
                break;
        }

        double radiusInMeters = Double.parseDouble(maxDis);
        int strokeColor = 0xffF57C00;
        int shadeColor = 0x44FFCC80;

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters)
                .fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        mCircle = mGoogleMap.addCircle(circleOptions);

        MarkerOptions markerOptions = new MarkerOptions().position(position);
        mMarker = mGoogleMap.addMarker(markerOptions);

        CameraUpdate center=
                CameraUpdateFactory.newLatLng(position);
        CameraUpdate zoom;
        if(radiusInMeters == 300.0){
            zoom=CameraUpdateFactory.zoomTo(15);
        }else if (radiusInMeters == 1000.0){
            zoom=CameraUpdateFactory.zoomTo(14);
        }else {
            zoom=CameraUpdateFactory.zoomTo(13);
        }

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
        Log.w("actResult", "-----------------------------");
        Log.w("actResult REQUEST CODE", requestCode + "");
        Log.w("actResult RESULT CODE", resultCode + "");
        Log.w("actResult ok", this.RESULT_OK+"");

        if (resultCode == CAMERA_RESULT) {
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
        } else if (resultCode == SELECT_PHOTO_RESULT) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO_REQUEST);
        } else if(resultCode == this.RESULT_OK) {
            if (requestCode == CAMERA_PIC_REQUEST) {
                try {

                    mImgViewPic.setImageURI(mImageUri);
                    postImageCard.setVisibility(View.VISIBLE);
                    mImgViewPic.setVisibility(View.VISIBLE);
                    fotoCard.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();

                    Toast t = Toast.makeText(this, "Falha ao obter imagem.", Toast.LENGTH_LONG);
                    t.show();
                }
            } else if (requestCode == SELECT_PHOTO_REQUEST) {
                try {
                    mImageUri = data.getData();
                    mImgViewPic.setImageURI(mImageUri);
                    postImageCard.setVisibility(View.VISIBLE);
                    mImgViewPic.setVisibility(View.VISIBLE);
                    fotoCard.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();

                    Toast t = Toast.makeText(this, "Falha ao obter imagem.", Toast.LENGTH_LONG);
                    t.show();
                }
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

    private void getUserInfo() {
        SharedPreferences prefs = this.getSharedPreferences("loginpreferences", this.MODE_PRIVATE);
        userId = prefs.getString("userid","");
        userName = prefs.getString("username","");
    }
}
