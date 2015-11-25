package postaround.tcc.inatel.br.fragment;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import postaround.tcc.inatel.br.Utils.PostAiButton;
import postaround.tcc.inatel.br.Utils.PostAiTextView;
import postaround.tcc.inatel.br.postaround.R;


public class ConfiguracaoFragment extends Fragment {

    private RadioGroup radioGroup;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private PostAiButton button;
    public AlertDialog.Builder alert;
    public Typeface tf;
    private int groupID;

    public ConfiguracaoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getActivity());
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/WalkwaySemiBold.ttf");
        alert = new AlertDialog.Builder(getActivity());
        prefs = getActivity().getSharedPreferences("raio_confg", getActivity().MODE_PRIVATE);
        editor = prefs.edit();
        groupID = prefs.getInt("raio",R.id.raio_um);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuracao, container, false);

        button = (PostAiButton) view.findViewById(R.id.bt_apagar_conta);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView title = new TextView(getActivity());
                title.setText("Conta");
                title.setTextSize(20);
                title.setPadding(15, 10, 0, 0);
                title.setTypeface(tf);

                TextView content = new TextView(getActivity());
                content.setText("Deseja excluir esta conta?");
                content.setTextSize(20);
                content.setTypeface(tf);
                content.setPadding(35,10,0,0);

                alert.setCustomTitle(title);
                alert.setView(content);
                alert.setCancelable(false);
                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alteraLoginStatus();
                        LoginManager.getInstance().logOut();
                        getFragmentManager().beginTransaction().commit();
                        getActivity().finish();
                    }
                });
                alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }

                });

                final AlertDialog alertdialog = alert.create();
                alert.show();

            }
        });

        radioGroup = (RadioGroup)view.findViewById(R.id.group_raio);

        radioGroup.check(groupID);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                switch (checkId){
                    case R.id.raio_um:
                        editor.putInt("raio",R.id.raio_um);
                        editor.commit();
                        Toast.makeText(getActivity(),"Raio alterado para 300 m",Toast.LENGTH_LONG).show();
                        break;

                    case R.id.raio_dois:
                        editor.putInt("raio",R.id.raio_dois);
                        editor.commit();
                        Toast.makeText(getActivity(),"Raio alterado para 1 Km",Toast.LENGTH_LONG).show();
                        break;

                    case R.id.raio_tres:
                        editor.putInt("raio",R.id.raio_tres);
                        editor.commit();
                        Toast.makeText(getActivity(),"Raio alterado para 2 Km",Toast.LENGTH_LONG).show();
                        break;

                    default:
                        editor.putInt("raio",R.id.raio_um);
                        editor.commit();
                        break;
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void alteraLoginStatus() {
        SharedPreferences prefs = getActivity().getSharedPreferences("loginpreferences", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("apikey","");
        editor.putString("userid","");
        editor.putString("username","");
        editor.putBoolean("firstlogin",true);
        editor.commit();
    }
}
