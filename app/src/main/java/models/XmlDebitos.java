package models;

/**
 * Created by mac on 28/3/16.
 */
public class XmlDebitos {

    private String nro_cta_tarj ;
    private String nro_cuota ;
    private String nro_comprobante ;
    private String fecha_generado ;
    private String prima_total ;
    private String prima_pagada ;
    private String prima_vencida ;
    private String txt_estado_intento ;


    public XmlDebitos(){

    }

    public String getNro_cta_tarj() {
        return nro_cta_tarj;
    }

    public void setNro_cta_tarj(String nro_cta_tarj) {
        this.nro_cta_tarj = nro_cta_tarj;
    }

    public String getNro_cuota() {
        return nro_cuota;
    }

    public void setNro_cuota(String nro_cuota) {
        this.nro_cuota = nro_cuota;
    }

    public String getNro_comprobante() {
        return nro_comprobante;
    }

    public void setNro_comprobante(String nro_comprobante) {
        this.nro_comprobante = nro_comprobante;
    }

    public String getFecha_generado() {
        return fecha_generado;
    }

    public void setFecha_generado(String fecha_generado) {
        this.fecha_generado = fecha_generado;
    }

    public String getPrima_total() {
        return prima_total;
    }

    public void setPrima_total(String prima_total) {
        this.prima_total = prima_total;
    }

    public String getPrima_pagada() {
        return prima_pagada;
    }

    public void setPrima_pagada(String prima_pagada) {
        this.prima_pagada = prima_pagada;
    }

    public String getPrima_vencida() {
        return prima_vencida;
    }

    public void setPrima_vencida(String prima_vencida) {
        this.prima_vencida = prima_vencida;
    }

    public String getTxt_estado_intento() {
        return txt_estado_intento;
    }

    public void setTxt_estado_intento(String txt_estado_intento) {
        this.txt_estado_intento = txt_estado_intento;
    }
}
