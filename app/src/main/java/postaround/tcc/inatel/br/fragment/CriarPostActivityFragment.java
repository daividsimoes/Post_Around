package postaround.tcc.inatel.br.fragment;

import android.location.Location;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import postaround.tcc.inatel.br.Util.UserInformation;
import postaround.tcc.inatel.br.interfaces.RestAPI;
import postaround.tcc.inatel.br.model.Loc;
import postaround.tcc.inatel.br.model.Post;
import postaround.tcc.inatel.br.model.PostPostRes;
import postaround.tcc.inatel.br.postaround.CriarPostActivity;
import postaround.tcc.inatel.br.postaround.GetResponseAsync;
import postaround.tcc.inatel.br.postaround.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class CriarPostActivityFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private EditText titulo;
    private EditText descricao;
    private ImageButton botao;

    private Location mCurrentLocation;
    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;




    private Button btnTakePic;
    private Button btnChosePic;
    private Button btnSend;
    private ImageView imgViewPic;

    private Uri imageUri;
    private static final int CAMERA_PIC_REQUEST = 1;
    private static final int SELECT_PHOTO_REQUEST = 2;


    GetResponseAsync asyncTask = new GetResponseAsync(getActivity());

    public CriarPostActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildGoogleApiClient();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_criar_post, container, false);

        titulo = (EditText) view.findViewById(R.id.editText_criar_descricao_post);
        descricao = (EditText) view.findViewById(R.id.editText_criar_comentario_post);
        botao = (ImageButton) view.findViewById(R.id.button_add_post);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titulo.getText().toString();
                String description = descricao.getText().toString();
                HashMap<String, Double> location = getLocation();
                Double longitude;
                Double latitude;
                List<Double> list = new ArrayList<Double>();
                if (location != null) {
                    list.add(location.get("longitude"));
                    list.add(location.get("latitude"));
                }

                Post post = new Post();
                Loc loc = new Loc();

                post.setDescription(description);
                post.setUser_id(UserInformation.user_id);
                post.setUser_name(UserInformation.user_name);
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

        imgViewPic = (ImageView) view.findViewById(R.id.imgViewPic);

        btnTakePic = (Button) view.findViewById(R.id.btnTakePic);
        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "Image from PostAí");
                    imageUri = getActivity().getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CAMERA_PIC_REQUEST);
                }
                catch (Exception ex)
                {
                    Toast t = Toast.makeText(getActivity(), "Fail", Toast.LENGTH_LONG);
                    t.show();
                }
            }
        });

        btnChosePic = (Button) view.findViewById(R.id.btnGetPicFromFile);
        btnChosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO_REQUEST);
            }
        });

        btnSend = (Button) view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null)
                {
                    try {
                        String path = getRealPathFromURI(imageUri);

                        String urlResult = asyncTask.execute(path).get();
                        //new DownloadImageAsync(imgViewPic).execute(urlResult);

                        Toast t = Toast.makeText(getActivity(), urlResult, Toast.LENGTH_LONG);
                        t.show();
                    }
                    catch (Exception ex)
                    {
                        Toast t = Toast.makeText(getActivity(), "Falha ao carregar imagem.", Toast.LENGTH_LONG);
                        t.show();
                    }
                }
            }
        });

        if(dispositivoPossuiCamera())
            btnTakePic.setVisibility(view.VISIBLE);
        else
            btnTakePic.setVisibility(view.GONE);

        return view;
    }

    // Gerencia o resultado das Intents chamadas para capturar e busca imagem
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST) {
            try {
                imgViewPic.setImageURI(imageUri);
            } catch (Exception e) {
                e.printStackTrace();

                Toast t = Toast.makeText(getActivity(), "Fail", Toast.LENGTH_LONG);
                t.show();
            }
        }
        else if(requestCode == SELECT_PHOTO_REQUEST)
        {
            try {
                imageUri = data.getData();
                imgViewPic.setImageURI(imageUri);

            } catch (Exception e) {
                e.printStackTrace();

                Toast t = Toast.makeText(getActivity(), "Fail", Toast.LENGTH_LONG);
                t.show();
            }
        }
        else
        {
            Toast t = Toast.makeText(getActivity(), "Fail", Toast.LENGTH_LONG);
            t.show();
        }
    }

    // Verifica se o dispositivo possui uma camera
    private boolean dispositivoPossuiCamera() {
        if (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    // Pega o caminho completo da imagem
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void closeActivity() {

        ((CriarPostActivity)getActivity()).closeActivity();


    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private HashMap<String, Double> getLocation() {
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

    /**      * Creating google api client object      * */
    /**      * Method to verify google play services on the device      * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Falha ao receber localização.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();
    }
    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
    /**      * Google api callback methods      */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.w("FAIL: ", "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {
        //getLocation();
        createLocationRequest();
        startLocationUpdates();

    }

    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }
    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }

}

