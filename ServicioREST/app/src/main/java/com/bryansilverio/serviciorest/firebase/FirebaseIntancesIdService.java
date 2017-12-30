package com.bryansilverio.serviciorest.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Bryan Silverio on 28/12/2017.
 */

public class FirebaseIntancesIdService extends FirebaseInstanceIdService{
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token= FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN:--> ","TOKEN:--> "+token);
    }
}
