package com.bryansilverio.serviciorest.core.util;

import android.app.ProgressDialog;
import android.content.DialogInterface;

/**
 * Created by Bryan Silverio on 27/12/2017.
 */

public class AlertDialog {

    public AlertDialog(){}

    public void LoadingProgressDialog(final ProgressDialog progressDialog, String titulo, String mensaje, int tiempo){
        progressDialog.setMax(tiempo);
        progressDialog.setMessage(mensaje);
        progressDialog.setTitle(titulo);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }).start();
    }

    public void LoadAlert(android.support.v7.app.AlertDialog.Builder builder, String titulo, int icono, String mensaje){
        builder.setTitle(titulo);
        builder.setMessage(mensaje)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }


}


