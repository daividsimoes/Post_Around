package postaround.tcc.inatel.br.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import postaround.tcc.inatel.br.postaround.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CriarPostActivityFragment extends Fragment {

    private static final int CAMERA_PIC_REQUEST = 1;

    private Button btnSelectImagem;
    private ImageView imageview;
    private Uri imageUri;

    public CriarPostActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_criar_post, container, false);

        imageview = (ImageView) view.findViewById(R.id.imagemView);

        btnSelectImagem = (Button) view.findViewById(R.id.selectImagem);
        btnSelectImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //getActivity().startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = getActivity().getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
            }
        });

        if(dispositivoPossuiCamera())
            btnSelectImagem.setVisibility(view.VISIBLE);
        else
            btnSelectImagem.setVisibility(view.GONE);

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST) {

            try {
                Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                        getActivity().getContentResolver(), imageUri);
                imageview.setImageBitmap(thumbnail);
                String imageurl = getRealPathFromURI(imageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*if (resultCode == getActivity().RESULT_OK) {
                Toast t = Toast.makeText(getActivity(), "SUCESSO", Toast.LENGTH_LONG);
                t.show();

                Bitmap image = (Bitmap) data.getExtras().get("data");
                imageview.setImageBitmap(image);
            }
            else {
                Toast t = Toast.makeText(getActivity(), "FALHA2", Toast.LENGTH_LONG);
                t.show();
            }*/
        }
        else
        {
            Toast t = Toast.makeText(getActivity(), "FALHA1", Toast.LENGTH_LONG);
            t.show();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //Verifica se o dispositivo possui uma camera
    private boolean dispositivoPossuiCamera() {
        if (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }
}
