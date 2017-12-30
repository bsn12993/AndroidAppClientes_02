package com.bryansilverio.serviciorest;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.bryansilverio.serviciorest.core.Operacion;
import com.bryansilverio.serviciorest.core.adapters.RVAdapter;
import com.bryansilverio.serviciorest.core.models.Cliente;
import com.bryansilverio.serviciorest.core.models.RespuestaConsumo;
import com.bryansilverio.serviciorest.core.services.ClienteServices;
import com.bryansilverio.serviciorest.core.util.AlertDialog;
import com.bryansilverio.serviciorest.core.util.InternetConnection;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private List<Cliente> clientes;
    private Cliente cliente=null;
    private ProgressDialog progressDialog;
    private CheckBox checkBox;
    private RVAdapter rvAdaptar;
    private RespuestaConsumo respuestaConsumo;
    private Bundle parametros;
    private Button verDetalle;
    private android.support.v7.app.AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        builder=new android.support.v7.app.AlertDialog.Builder(this);
        checkBox=(CheckBox)findViewById(R.id.chk);

        recyclerView=(RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GetClienteAsyncTask getClienteAsyncTask=new GetClienteAsyncTask();
        progressDialog=new ProgressDialog(this);
        if(InternetConnection.isOnline(this)){
            if(InternetConnection.isConnectedWife(this) || InternetConnection.isConnectedMobile(this))
                getClienteAsyncTask.execute();
            else
                Toast.makeText(this,"No se identifico la red",Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(this,"No es posible conectarse a una red",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.icon_crear:
                Intent iCrear=new Intent(this,CreateActivity.class);
                Operacion insert=Operacion.INSERT;
                parametros=new Bundle();
                parametros.putString("accion", insert.name());
                iCrear.putExtras(parametros);
                startActivity(iCrear);
                break;
            case R.id.icon_borrar:
                List<Integer> lstId=rvAdaptar.integerList;
                if(lstId.size()>0 && lstId!=null){
                    DeleteClienteAsyncTask deleteClienteAsyncTask=new DeleteClienteAsyncTask();
                    deleteClienteAsyncTask.execute(lstId);
                }else
                    Toast.makeText(this,"No hay elementos seleccionado",Toast.LENGTH_SHORT).show();
                break;
            case R.id.icon_editar:
                int idEditar=rvAdaptar.idSelected;
                if(idEditar!=0){
                    for (Cliente c:respuestaConsumo.getClientes()){
                        if(c.getId()==idEditar){
                            cliente=new Cliente();
                            cliente.setId(idEditar);
                            cliente.setNombre(c.getNombre());
                            cliente.setTelefono(c.getTelefono());
                        }
                    }
                    if(cliente!=null){
                        parametros=new Bundle();
                        parametros.putInt("id",cliente.getId());
                        parametros.putString("nombre",cliente.getNombre());
                        parametros.putString("telefono",cliente.getTelefono());
                        Operacion update=Operacion.UPDATE;
                        parametros.putString("accion", update.name());
                        Intent iEditar=new Intent(this,CreateActivity.class);
                        iEditar.putExtras(parametros);
                        startActivity(iEditar);
                    }
                }else
                    Toast.makeText(this,"No hay elementos seleccionado",Toast.LENGTH_SHORT).show();
                break;
            case R.id.icon_detalle:
                int idDetalle=rvAdaptar.idSelected;
                Log.d("idDetalle--------->",""+idDetalle);
                if(idDetalle!=0){
                    Intent iDetalle=new Intent(this,DetailsActivity.class);
                    startActivity(iDetalle);
                }else
                    Toast.makeText(this,"No hay elemento seleccionado",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    /**
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
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }).start();
    }
     **/

    public class GetClienteAsyncTask extends AsyncTask<String,Integer,RespuestaConsumo> {

        @Override
        protected RespuestaConsumo doInBackground(String... params) {
            respuestaConsumo=new ClienteServices().getClientes();
            if(respuestaConsumo!=null){
                return respuestaConsumo;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            AlertDialog dialog=new AlertDialog();
            dialog.LoadingProgressDialog(progressDialog,"Cargando Clientes","Loading",100);
        }

        @Override
        protected void onPostExecute(RespuestaConsumo consumo) {
            //super.onPostExecute(consumo);
            if(consumo!=null){
                if(consumo.getClientes().size()>0){
                    if(!consumo.getEstatus().equals("Error")){
                        rvAdaptar=new RVAdapter(consumo.getClientes(),getApplicationContext());
                        recyclerView.setAdapter(rvAdaptar);
                    }else{
                        Toast.makeText(getApplicationContext(),consumo.getEstatus()+": "+consumo.getMensaje(),Toast.LENGTH_SHORT);
                    }
                    progressDialog.dismiss();
                }else {
                    progressDialog.dismiss();
                    builder.setTitle("Consulta");
                    builder.setMessage("No se encontraron registros para mostrar")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }
            }else {
                progressDialog.dismiss();
                builder.setTitle("Error de Conexion");
                builder.setMessage("Ocurrio un error de conexion con el servicio")
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
    }

    public class DeleteClienteAsyncTask extends AsyncTask<List<Integer>,Boolean,Boolean>{

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            AlertDialog dialog=new AlertDialog();
            dialog.LoadingProgressDialog(progressDialog,"Eliminando Cliente","Loading",100);
        }

        @Override
        protected Boolean doInBackground(List<Integer>... params) {

            if(params[0].size()>0 && params[0]!=null){
                boolean resultado=new ClienteServices().deleteCliente(params[0]);
                return resultado;
            }else {
                Toast.makeText(getApplicationContext(),"No hay elemento seleccionado",Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //super.onPostExecute(aBoolean);
            if(!aBoolean)
                Toast.makeText(getApplicationContext(),"Ocurrio un error al eliminar",Toast.LENGTH_SHORT).show();
            else {
                respuestaConsumo=new ClienteServices().getClientes();
                GetClienteAsyncTask getClienteAsyncTask=new GetClienteAsyncTask();
                progressDialog=new ProgressDialog(getApplicationContext());
                getClienteAsyncTask.execute();
            }
            progressDialog.dismiss();
        }
    }
}


