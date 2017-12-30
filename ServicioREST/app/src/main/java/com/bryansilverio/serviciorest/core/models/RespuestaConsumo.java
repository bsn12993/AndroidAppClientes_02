package com.bryansilverio.serviciorest.core.models;

import java.util.List;

/**
 * Created by Bryan Silverio on 20/12/2017.
 */

public class RespuestaConsumo {

    private String estatus;
    private String mensaje;
    private List<Cliente> clientes;

    public RespuestaConsumo(String estatus, String mensaje, List<Cliente> clientes) {
        this.estatus = estatus;
        this.mensaje = mensaje;
        this.clientes = clientes;
    }

    public String getEstatus() {
        return estatus;
    }

    public String getMensaje() {
        return mensaje;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

}
