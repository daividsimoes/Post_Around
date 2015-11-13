package postaround.tcc.inatel.br.postaround;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.facebook.FacebookSdk;


public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        loginStatus();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loginStatus() {
        SharedPreferences prefs = this.getSharedPreferences("loginpreferences", this.MODE_PRIVATE);
        String login = prefs.getString("apikey", "");
        if (login.equals("")) {
            Log.d("TAG", ">>>" + "Signed Out");
            setContentView(R.layout.activity_login);
        } else {
            Log.d("TAG", ">>>" + "Signed In");
            Intent intent = new Intent(this, NavigationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();
        }
    }
}
