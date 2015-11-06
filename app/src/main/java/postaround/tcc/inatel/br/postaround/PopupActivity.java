package postaround.tcc.inatel.br.postaround;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class PopupActivity extends AppCompatActivity {

    private static final int CAMERA_CHOOSE = 3;
    private static final int SELECT_PHOTO_CHOOSE = 4;

    private RelativeLayout camera;
    private RelativeLayout album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_content);
        setTitle("Usar imagem a partir de...");

        camera = (RelativeLayout) findViewById(R.id.botaoCamera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(CAMERA_CHOOSE);
                finish();
            }
        });

        album = (RelativeLayout) findViewById(R.id.botaoAlbum);
        album.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setResult(SELECT_PHOTO_CHOOSE);
                finish();
            }
        });

    }
}
