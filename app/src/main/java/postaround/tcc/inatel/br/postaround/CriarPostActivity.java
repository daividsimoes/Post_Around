package postaround.tcc.inatel.br.postaround;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
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

import android.app.AlertDialog;

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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import postaround.tcc.inatel.br.Utils.UserInformation;
import postaround.tcc.inatel.br.adapter.CropImageAdapter;
import postaround.tcc.inatel.br.async.GetResponseAsync;
import postaround.tcc.inatel.br.interfaces.RestAPI;
import postaround.tcc.inatel.br.model.AsyncTaskArguments;
import postaround.tcc.inatel.br.model.CropImage;
import postaround.tcc.inatel.br.model.Loc;
import postaround.tcc.inatel.br.model.Post;
import postaround.tcc.inatel.br.model.PostPostRes;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CriarPostActivity extends AppCompatActivity implements OnMapReadyCallback{

    private static final int CAMERA_RESULT = 1;
    private static final int SELECT_PHOTO_RESULT = 2;
    private static final int CAMERA_PIC_REQUEST = 3;
    private static final int SELECT_PHOTO_REQUEST = 4;
    private static final int CROP_FROM_CAMERA = 5;

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

    GetResponseAsync asyncTask = new GetResponseAsync(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_post);

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
                startActivityForResult(intent, 0);
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

                String path = null;
                if (mImageUri != null) {
                    try {
                        path = getRealPathFromURI(mImageUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                asyncTask.execute(new AsyncTaskArguments(path, post));

                /*RestAdapter retrofit = new RestAdapter.Builder()
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
                });*/
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
        Log.w("actResult", "-----------------------------");
        Log.w("actResult REQUEST CODE", requestCode + "");
        Log.w("actResult RESULT CODE", resultCode+"");
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
        } else if(data != null) {
            if (requestCode == CAMERA_PIC_REQUEST) {
                try {
                    doCrop();
                } catch (Exception e) {
                    e.printStackTrace();

                    Toast t = Toast.makeText(this, "Fail", Toast.LENGTH_LONG);
                    t.show();
                }
            } else if (requestCode == SELECT_PHOTO_REQUEST) {
                try {
                    mImageUri = data.getData();
                    doCrop();
                } catch (Exception e) {
                    e.printStackTrace();

                    Toast t = Toast.makeText(this, "Fail", Toast.LENGTH_LONG);
                    t.show();
                }
            } else if (requestCode == CROP_FROM_CAMERA) {
                mImageUri = data.getData();
                mImgViewPic.setImageURI(mImageUri);

                mImgViewPic.setVisibility(View.VISIBLE);
                fotoCard.setVisibility(View.GONE);
            }
        }
    }

    private void doCrop() {
        final ArrayList<CropImage> cropOptions = new ArrayList<CropImage>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(
                intent, 0);

        int size = list.size();

        if (size == 0) {

            Toast.makeText(this, "Can not find image crop app",
                    Toast.LENGTH_SHORT).show();

            return;
        } else {
            intent.setData(mImageUri);

            //intent.putExtra("outputX", 200);
            //intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 21);
            intent.putExtra("aspectY", 9);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            /**
             * There is posibility when more than one image cropper app exist,
             * so we have to check for it first. If there is only one app, open
             * then app.
             */
            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                /**
                 * If there are several app exist, create a custom chooser to
                 * let user selects the app.
                 */
                for (ResolveInfo res : list) {
                    final CropImage co = new CropImage();

                    co.title = getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);

                    co.appIntent
                            .setComponent(new ComponentName(
                                    res.activityInfo.packageName,
                                    res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropImageAdapter adapter = new CropImageAdapter(
                        getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Crop App");
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                startActivityForResult(
                                        cropOptions.get(item).appIntent,
                                        CROP_FROM_CAMERA);
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (mImageUri != null) {
                            getContentResolver().delete(mImageUri, null,
                                    null);
                            mImageUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    private void sendPictureToCloudnary(Uri imageUri) {
        if (imageUri != null) {
            try {
                String path = getRealPathFromURI(imageUri);

                //asyncTask.execute(path);
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
