package postaround.tcc.inatel.br.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import postaround.tcc.inatel.br.postaround.GetResponseAsync;
import postaround.tcc.inatel.br.postaround.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CriarPostActivityFragment extends Fragment {

    private static final int CAMERA_PIC_REQUEST = 1;
    private static final int SELECT_PHOTO_REQUEST = 2;

    private Button btnTakePic;
    private Button btnChosePic;
    private Button btnSend;
    private ImageView imgViewPic;

    private Uri imageUri;

    GetResponseAsync asyncTask = new GetResponseAsync(getActivity());

    public CriarPostActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_criar_post, container, false);

        //imageview = (ImageView) view.findViewById(R.id.imagemView);

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
}
