package models;

/**
 * Created by mac on 24/3/16.
 */
public class XmlEstadoCuenta {

    private String nro_cuota;
    private String nro_comprobante;
    private String fecha_vencimiento;
    private String imp_prima_total;
    private String fecha_proceso;
    private String txt_estado;
    private String imp_prima_deposito;
    private String imp_prima_pagada;
    private String txt_moneda;
    private String fecha_cobranza;


    public XmlEstadoCuenta() {


    }

    public String getNro_comprobante() {
        return nro_comprobante;
    }

    public void setNro_comprobante(String nro_comprobante) {
        this.nro_comprobante = nro_comprobante;
    }

    public String getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(String fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public String getImp_prima_total() {
        return imp_prima_total;
    }

    public void setImp_prima_total(String imp_prima_total) {
        this.imp_prima_total = imp_prima_total;
    }

    public String getFecha_proceso() {
        return fecha_proceso;
    }

    public void setFecha_proceso(String fecha_proceso) {
        this.fecha_proceso = fecha_proceso;
    }

    public String getTxt_estado() {
        return txt_estado;
    }

    public void setTxt_estado(String txt_estado) {
        this.txt_estado = txt_estado;
    }

    public String getImp_prima_deposito() {
        return imp_prima_deposito;
    }

    public void setImp_prima_deposito(String imp_prima_deposito) {
        this.imp_prima_deposito = imp_prima_deposito;
    }

    public String getImp_prima_pagada() {
        return imp_prima_pagada;
    }

    public void setImp_prima_pagada(String imp_prima_pagada) {
        this.imp_prima_pagada = imp_prima_pagada;
    }

    public String getTxt_moneda() {
        return txt_moneda;
    }

    public void setTxt_moneda(String txt_moneda) {
        this.txt_moneda = txt_moneda;
    }

    public String getFecha_cobranza() {
        return fecha_cobranza;
    }

    public void setFecha_cobranza(String fecha_cobranza) {
        this.fecha_cobranza = fecha_cobranza;
    }

    public String getNro_cuota() {
        return nro_cuota;
    }

    public void setNro_cuota(String nro_cuota) {
        this.nro_cuota = nro_cuota;
    }
}
