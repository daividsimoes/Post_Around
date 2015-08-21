package postaround.tcc.inatel.br.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.Arrays;

import postaround.tcc.inatel.br.postaround.BuildConfig;

import postaround.tcc.inatel.br.postaround.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {

    private String name;
    private String email;

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

                                System.out.println("AccessToken.getCurrentAccessToken()" + AccessToken.getCurrentAccessToken().toString());
                               // name = Profile.getCurrentProfile().getName().toString();


                                try {
                                    name = object.getString("name");
                                    email = object.getString("email");
                                    Log.e("OBJETO",object.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    name = "NULL";
                                    email = "NULL";
                                }
                                Log.e("Nome", name);
                                Log.e("Email",email);
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
