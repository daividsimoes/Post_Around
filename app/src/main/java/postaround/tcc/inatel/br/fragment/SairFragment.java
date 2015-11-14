package postaround.tcc.inatel.br.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;


import java.io.FileDescriptor;
import java.io.PrintWriter;

import postaround.tcc.inatel.br.postaround.NavigationActivity;
import postaround.tcc.inatel.br.postaround.R;


public class SairFragment extends Fragment {

    public SairFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getActivity());
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        final Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/WalkwaySemiBold.ttf");

        TextView title = new TextView(getActivity());
        title.setText("Sair");
        title.setTextSize(50);
        title.setTypeface(tf);

        TextView content = new TextView(getActivity());
        content.setText("Deseja sair?");
        title.setTextSize(17);
        content.setTypeface(tf);

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
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new PostAoRedorFragment())
                        .commit();
            }

        });

        final AlertDialog alertdialog = alert.create();
        alert.show();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_sair, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
    }


    @Override
    public void onDestroyView(){

        super.onDestroyView();
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }


    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onStop(){
        super.onStop();
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
