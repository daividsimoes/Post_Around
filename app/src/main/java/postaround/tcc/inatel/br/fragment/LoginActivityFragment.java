package postaround.tcc.inatel.br.fragment;

import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.service.textservice.SpellCheckerService;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;

import com.facebook.Profile;
import com.facebook.applinks.FacebookAppLinkResolver;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.Serializable;
import java.util.Arrays;

import postaround.tcc.inatel.br.model.LoginModel;
import postaround.tcc.inatel.br.postaround.BuildConfig;

import postaround.tcc.inatel.br.postaround.NavigationActivity;
import postaround.tcc.inatel.br.postaround.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {

    private LoginModel loginModel;
    private Intent intent;

    private CallbackManager mCallbackManager;
    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object,
                                                GraphResponse response) {

                            if (BuildConfig.DEBUG) {
                                FacebookSdk.setIsDebugEnabled(true);
                                FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

                                //System.out.println("AccessToken.getCurrentAccessToken.getToken)" + AccessToken.getCurrentAccessToken().getToken().toString());
                               //Profile.getCurrentProfile().getName().toString();
                               //Profile.getCurrentProfile().getProfilePictureUri(120,120);


                                try {
                                    loginModel.setId(object.getLong("id"));
                                    loginModel.setAccessToken(AccessToken.getCurrentAccessToken().getToken());
                                    loginModel.setName(object.getString("name"));
                                    loginModel.setEmail(object.getString("email"));
                                    loginModel.setUrlFotoPerfil("https://graph.facebook.com/" + loginModel.getId() + "/picture?type=large");
                                  //  Log.e("OBJETO", object.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.e("ID",String.valueOf(loginModel.getId()));
                                Log.e("Access Token",loginModel.getAccessToken());
                                Log.e("Nome", loginModel.getName());

                                intent = new Intent(getActivity(), NavigationActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            Log.e("onCancel", "CANCEL");
        }

        @Override
        public void onError(FacebookException e) {
            Log.e("onError ", e.getMessage());
        }
    };

    public LoginActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        loginModel = new LoginModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        LoginButton loginButton = (LoginButton)view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, mFacebookCallback);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
