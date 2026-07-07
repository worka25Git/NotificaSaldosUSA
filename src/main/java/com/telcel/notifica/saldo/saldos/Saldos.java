package com.telcel.notifica.saldo.saldos;

public class Saldos {
    private long monto;
    private String cliente;
    private String bolsa;
    private int region;
    private String mensaje;
    private String telefono;
    private String idCliente;
    private String correo;
    private String tocaNot;


    public Saldos() {}

    public Saldos(String aplicacion, long saldo) {
        this.cliente = aplicacion;
        this.monto = saldo;
    }

    public Saldos(String cliente, String correo, String telefono) {
        this.cliente = cliente;
        this.telefono = telefono;
        this.correo = correo;
    }

    public Saldos(String cliente, String mensaje) {
        this.cliente = cliente;
        this.mensaje = mensaje;
    }

    public Saldos(String cliente, String telefono, String correo, String tocaNot) {
        this.cliente = cliente;
        this.telefono = telefono;
        this.correo = correo;
        this.tocaNot = tocaNot;
    }

    public long getMonto() {
        return monto;
    }

    public void setMonto(long monto) {
        this.monto = monto;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getBolsa() {
        return bolsa;
    }

    public void setBolsa(String bolsa) {
        this.bolsa = bolsa;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTocaNot() {
        return tocaNot;
    }

    public void setTocaNot(String tocaNot) {
        this.tocaNot = tocaNot;
    }




}