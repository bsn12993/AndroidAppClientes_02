package com.bryansilverio.serviciorest;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bryansilverio.serviciorest.core.Operacion;
import com.bryansilverio.serviciorest.core.models.Cliente;
import com.bryansilverio.serviciorest.core.services.ClienteServices;
import com.bryansilverio.serviciorest.core.util.AlertDialog;

public class CreateActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText nombre;
    private EditText telefono;
    private Button btn_crear;
    private Cliente cliente;
    private ProgressDialog progressDialog;
    public Bundle parametros=null;
    private ClienteServices clienteServices;
    private android.support.v7.app.AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        builder=new android.support.v7.app.AlertDialog.Builder(this);

        nombre=(EditText)findViewById(R.id.txt_nombre);
        telefono=(EditText)findViewById(R.id.txt_telefono);
        btn_crear=(Button)findViewById(R.id.btn_crear);

        parametros=this.getIntent().getExtras();

        if(parametros!=null){
            if(parametros.get("accion").equals(Operacion.UPDATE.name())){
                nombre.setTag(parametros.get("id").toString());
                nombre.setText(parametros.get("nombre").toString());
                telefono.setText(parametros.get("telefono").toString());
                btn_crear.setText("Editar");
            }
        }


        btn_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            progressDialog=new ProgressDialog(CreateActivity.this);
            progressDialog.setTitle("Creando");
            progressDialog.setMessage("Loading");

            if(parametros!=null){
                if(parametros.get("accion").equals(Operacion.INSERT.name())){
                    cliente=new Cliente();
                    cliente.setNombre(nombre.getText().toString());
                    cliente.setTelefono(telefono.getText().toString());
                    PostClienteAsyncTask postClienteAsyncTask=new PostClienteAsyncTask();
                    if(!TextUtils.isEmpty(cliente.getNombre()) && !cliente.getTelefono().isEmpty())
                        postClienteAsyncTask.execute(cliente);
                    else {
                        if(TextUtils.isEmpty(cliente.getNombre())){
                            nombre.setError("Nombre no puede estar vacio");
                            return;
                        }
                        if(TextUtils.isEmpty(cliente.getTelefono())){
                            telefono.setError("Telefono no puede estar vacio");
                            return;
                        }
                    }
                }else if(parametros.get("accion").equals(Operacion.UPDATE.name())){
                    cliente=new Cliente();
                    cliente.setId(Integer.parseInt(nombre.getTag().toString()));
                    cliente.setNombre(nombre.getText().toString());
                    cliente.setTelefono(telefono.getText().toString());
                    PutClienteAsyncTask putClienteAsyncTask=new PutClienteAsyncTask();
                    if(!TextUtils.isEmpty(cliente.getNombre()) && !TextUtils.isEmpty(cliente.getTelefono()))
                        putClienteAsyncTask.execute(cliente);
                    else{
                        if(TextUtils.isEmpty(cliente.getNombre())){
                            nombre.setError("Nombre no puede estar vacio");
                            return;
                        }
                        if(TextUtils.isEmpty(cliente.getTelefono())){
                            telefono.setError("Telefono no puede estar vacio");
                            return;
                        }
                    }
                }
            }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent iHome=new Intent(this,MainActivity.class);
            startActivity(iHome);
        }
        return true;
    }

    /**
    public void PostCliente(Cliente cliente, ProgressDialog progressDialog){
        progressDialog.show();
        clienteServices=new ClienteServices();
        boolean respuesta=clienteServices.postCliente(cliente);
        if(!respuesta)Toast.makeText(this,"Ocurrio un error",Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(this,"Se creo cliente",Toast.LENGTH_SHORT).show();
            clearFields();
        }
        progressDialog.dismiss();
    }

    public void PutCliente(Cliente cliente, ProgressDialog progressDialog){
        progressDialog.show();
        clienteServices=new ClienteServices();
        boolean respuesta=clienteServices.putCliente(cliente);
        if(!respuesta)Toast.makeText(this,"Ocurrio un error",Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(this,"Se modifico cliente",Toast.LENGTH_SHORT).show();
            clearFields();
        }
        progressDialog.dismiss();
        Intent iHome=new Intent(this,MainActivity.class);
        startActivity(iHome);
    }
    **/

    public void clearFields(){
        nombre.setTag("");
        nombre.setText("");
        telefono.setText("");
    }

    public class PostClienteAsyncTask extends AsyncTask<Cliente,Cliente,Boolean>{

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            AlertDialog dialog=new AlertDialog();
            dialog.LoadingProgressDialog(progressDialog,"Guardando Cliente","Loading",100);
        }

        @Override
        protected Boolean doInBackground(Cliente... params) {
            clienteServices=new ClienteServices();
            boolean respuesta=clienteServices.postCliente(params[0]);
            return respuesta;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //super.onPostExecute(aBoolean);
            if(!aBoolean)Toast.makeText(getApplicationContext(),"Ocurrio un error",Toast.LENGTH_SHORT).show();
            else{
                builder.setTitle("Creado");
                builder.setMessage("Se ha creado registro con exito")
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Intent iHome=new Intent(CreateActivity.this,MainActivity.class);
                                startActivity(iHome);
                            }
                        });
                builder.show();
                clearFields();
                progressDialog.dismiss();
            }
         }
    }

    public class PutClienteAsyncTask extends AsyncTask<Cliente,Cliente,Boolean>{

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            AlertDialog dialog=new AlertDialog();
            dialog.LoadingProgressDialog(progressDialog,"Actualizar Cliente","Loading",100);
        }

        @Override
        protected Boolean doInBackground(Cliente... params) {
            clienteServices=new ClienteServices();
            boolean respuesta=clienteServices.putCliente(cliente);
            return respuesta;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //super.onPostExecute(aBoolean);
            if(!aBoolean){
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Ocurrio un error",Toast.LENGTH_SHORT).show();
            }
            else{
                builder.setTitle("Actualizado");
                builder.setMessage("Se ha actualizado el registro correctamente")
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Intent iHome=new Intent(CreateActivity.this,MainActivity.class);
                                startActivity(iHome);
                            }
                        });
                builder.show();
                clearFields();
                progressDialog.dismiss();
            }
            //progressDialog.dismiss();
            //Intent iHome=new Intent(CreateActivity.this,MainActivity.class);
            //startActivity(iHome);
        }
    }

}
