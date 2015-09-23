package postaround.tcc.inatel.br.postaround;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import postaround.tcc.inatel.br.fragment.ConfiguracaoFragment;
import postaround.tcc.inatel.br.fragment.MeusPostsFragment;
import postaround.tcc.inatel.br.fragment.NavigationDrawerFragment;
import postaround.tcc.inatel.br.fragment.PostAoRedorFragment;
import postaround.tcc.inatel.br.fragment.SairFragment;
import postaround.tcc.inatel.br.fragment.SobreFragment;

public class NavigationActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.post_ao_redor);
                break;

            case 2:
                mTitle = getString(R.string.meus_posts);
                break;

            case 3:
                mTitle = getString(R.string.configuracoes);
                break;

            case 4:
                mTitle = getString(R.string.sobre);
                break;

            case 5:
                mTitle = getString(R.string.sair);
                break;

            default:
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }


    /**
     * A placeholder postaround.tcc.inatel.br.fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The postaround.tcc.inatel.br.fragment argument representing the section number for this
         * postaround.tcc.inatel.br.fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private Context context = getActivity().getBaseContext();

        /**
         * Returns a new instance of this postaround.tcc.inatel.br.fragment for the given section
         * number.
         */
        public static Fragment newInstance(int sectionNumber) {
            Fragment fragment;
            switch (sectionNumber){
                case 1:
                    fragment = new PostAoRedorFragment();
                    break;

                case 2:
                    fragment = new MeusPostsFragment();
                    break;

                case 3:
                    fragment = new ConfiguracaoFragment();
                    break;

                case 4:
                    fragment = new SobreFragment();
                    break;

                case 5:
                   fragment = new SairFragment();
                    break;

                default:
                    fragment = new PlaceholderFragment();
                    break;

            }
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_navigation, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((NavigationActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

    }

}
