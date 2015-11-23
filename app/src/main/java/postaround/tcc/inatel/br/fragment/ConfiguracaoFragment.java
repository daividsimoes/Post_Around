package postaround.tcc.inatel.br.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.RadioGroup;
import android.widget.Toast;

import postaround.tcc.inatel.br.postaround.R;


public class ConfiguracaoFragment extends Fragment {

    private RadioGroup radioGroup;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private int groupID;

    public ConfiguracaoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getActivity().getSharedPreferences("raio_confg", getActivity().MODE_PRIVATE);
        editor = prefs.edit();
        groupID = prefs.getInt("raio",R.id.raio_um);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuracao, container, false);
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

}
