package com.bryansilverio.serviciorest.core.services;

import android.util.Log;

import com.bryansilverio.serviciorest.core.models.Cliente;
import com.bryansilverio.serviciorest.core.models.RespuestaConsumo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bryan Silverio on 20/12/2017.
 */

public class ClienteServices {

    private URL url = null;
    private HttpURLConnection conn;
    Gson gson=new Gson();

    public RespuestaConsumo getClientes() {
        RespuestaConsumo respuestaConsumo=null;
        Cliente cliente=null;
        List<Cliente> clientes=null;
        try {
            url = new URL("http://192.168.0.105:16737/Api/Clientes/Clientes");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            String json = "";
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            conn.disconnect();

            if(conn.getResponseCode()!=200){
                respuestaConsumo=new RespuestaConsumo("Error","No se encontraron Resultados",new ArrayList<Cliente>());
            }else {
                System.out.println(response);
                json = response.toString();
                JSONArray jsonArray=new JSONArray(json);
                if (jsonArray.length()>0){
                    clientes=new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        cliente = new Cliente();
                        cliente.setId(jsonArray.getJSONObject(i).getInt("Id"));
                        cliente.setNombre(jsonArray.getJSONObject(i).getString("Nombre"));
                        cliente.setTelefono(jsonArray.getJSONObject(i).getString("Telefono"));
                        clientes.add(cliente);
                    }
                    respuestaConsumo=new RespuestaConsumo("OK","",clientes);
                } else {
                    respuestaConsumo=new RespuestaConsumo("Error","No se encontraron Resultados",new ArrayList<Cliente>());
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respuestaConsumo;
    }

    public boolean postCliente(Cliente cliente){
        boolean resultado=false;
        String json=gson.toJson(cliente);

        try{
            url=new URL("http://192.168.0.105:16737/Api/Clientes/Cliente");
            conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Accept", "application/json");
            OutputStream outputStream=conn.getOutputStream();
            outputStream.write(json.getBytes());
            outputStream.flush();
            conn.connect();

            if(conn.getResponseCode()!=200){
                Log.e("POS---->","No inserto");
                resultado = false;
            }else {
                Log.d("POST--->","Inserto correctamente");
                resultado = true;
            }
            conn.disconnect();
            outputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public boolean deleteCliente(List<Integer> id){
        boolean resultado=false;
        List<Cliente> lstId=new ArrayList<>();
        String json=null;
        Cliente cliente=null;
        for (int i=0;i<id.size();i++){
            cliente=new Cliente();
            cliente.setId(id.get(i));
            lstId.add(cliente);
        }
        try {
            url=new URL("http://192.168.0.105:16737/Api/Clientes/Cliente");
            conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept","application/json");
            conn.setRequestProperty("Content-Type","application/json");
            conn.connect();
            OutputStream outputStream=null;

            json=gson.toJson(lstId);
            outputStream=conn.getOutputStream();
            outputStream.write(json.getBytes());
            outputStream.flush();

            if(conn.getResponseCode()!=200){
                resultado=false;
            }else {
                resultado=true;
            }

            conn.disconnect();
            outputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public boolean putCliente(Cliente cliente){
        boolean resultado=false;
        String json=gson.toJson(cliente);

        try{
            url=new URL("http://192.168.0.105:16737/Api/Clientes/Cliente");
            conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Accept", "application/json");
            OutputStream outputStream=conn.getOutputStream();
            outputStream.write(json.getBytes());
            outputStream.flush();
            conn.connect();

            if(conn.getResponseCode()!=200){
                resultado = false;
            }else {
                resultado = true;
            }
            conn.disconnect();
            outputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }
}
