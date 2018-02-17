package smilegate.blackpants.univscanner.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.pixplicity.easyprefs.library.Prefs;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smilegate.blackpants.univscanner.data.model.RefreshToken;
import smilegate.blackpants.univscanner.data.remote.ApiUtils;
import smilegate.blackpants.univscanner.data.remote.UserApiService;

/**
 * Created by user on 2018-01-26.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private UserApiService mUserApiService;
    private String mUserToken;
    private String mRefreshToken;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        Prefs.putString("registrationToken", refreshedToken);
        mRefreshToken = refreshedToken;
        getUserToken();
    }

    private void getUserToken() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        mUserToken = task.getResult().getToken();
                        Prefs.putString("userToken", mUserToken);
                        sendRegistrationToServer();
                    } else {

                    }
                }
            });
        }

   }

   public void sendRegistrationToServer() {
       String uid = FirebaseAuth.getInstance().getUid();
       mUserApiService = ApiUtils.getUserApiService();

       RefreshToken refreshToken = new RefreshToken(mUserToken, mRefreshToken);

       mUserApiService.setRefreshRegistrationToken(uid, refreshToken).enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               if(response.body()!=null) {
                   Log.d(TAG, "send refresh registration token to server : success");
               } else {
                   Log.d(TAG, "send refresh registration token to server : onResponse: fail : "+response.message());
               }
           }
           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {
               Log.d(TAG, "send refresh registration token to server : fail : "+t.getMessage());
           }
       });

   }
}
