package postaround.tcc.inatel.br.postaround;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import postaround.tcc.inatel.br.Utils.CircleImage;
import postaround.tcc.inatel.br.Utils.CustomTypefaceSpan;
import postaround.tcc.inatel.br.fragment.ConfiguracaoFragment;
import postaround.tcc.inatel.br.fragment.LoginActivityFragment;
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

    private DrawerLayout mDrawerLayout;

    private String userId;
    private String userName;

    private boolean firstLogin;

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle("");

        getUserInfo();

        setSupportActionBar(toolbar);


        ImageButton menu = (ImageButton) toolbar.findViewById(R.id.menu_button);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(1))
                .commit();

        NavigationView navigationView =(NavigationView) findViewById(R.id.navigation_view);
        ImageView profilePicture = (ImageView) navigationView.findViewById(R.id.drawer_profile_image);
        TextView userNameText = (TextView) navigationView.findViewById(R.id.drawer_username);

        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }


        if (firstLogin) {
            userNameText.setText(LoginActivityFragment.userName);
            Picasso.with(this).load("https://graph.facebook.com/" + LoginActivityFragment.userID + "/picture?type=large").transform(new CircleImage()).into(profilePicture);
        }else {
            userNameText.setText(userName);
            Picasso.with(this).load("https://graph.facebook.com/" + userId + "/picture?type=large").transform(new CircleImage()).into(profilePicture);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int position = 0;

                switch (menuItem.getItemId()) {

                    case R.id.posts_ao_redor:
                        menuItem.setChecked(true);
                        position = 0;
                        break;
                    case R.id.meus_posts:
                        menuItem.setChecked(true);
                        position = 1;
                        break;
                    case R.id.configuracao:
                        menuItem.setChecked(true);
                        position = 2;
                        break;
                    case R.id.sobre:
                        menuItem.setChecked(true);
                        position = 3;
                        break;
                    case R.id.sair:
                        menuItem.setChecked(true);
                        position = 4;
                        break;
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
                mDrawerLayout.closeDrawers();

                return false;
            }
        });


    }




    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/WalkwaySemiBold.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        firstLogin = false;
        SharedPreferences prefs = this.getSharedPreferences("loginpreferences", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstlogin",firstLogin);
        editor.commit();
    }

    private void getUserInfo() {
        SharedPreferences prefs = this.getSharedPreferences("loginpreferences", this.MODE_PRIVATE);
        userId = prefs.getString("userid","");
        userName = prefs.getString("username","");
        firstLogin = prefs.getBoolean("firstlogin",false);
        Log.e("FIRST_LOGIN", firstLogin+"");
    }

}
