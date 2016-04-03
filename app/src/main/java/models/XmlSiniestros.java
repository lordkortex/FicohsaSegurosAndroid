package models;

/**
 * Created by mac on 24/3/16.
 */
public class XmlSiniestros {

    private String nro_siniestro;
    private String txt_suc;
    private String txt_ramo;
    private String nro_pol;
    private String cod_contratante;
    private String txt_contratante;
    private String fec_siniestro;
    private String txt_coberturas;

    public XmlSiniestros(){

    }


    public String getNro_siniestro() {
        return nro_siniestro;
    }

    public void setNro_siniestro(String nro_siniestro) {
        this.nro_siniestro = nro_siniestro;
    }

    public String getTxt_suc() {
        return txt_suc;
    }

    public void setTxt_suc(String txt_suc) {
        this.txt_suc = txt_suc;
    }

    public String getTxt_ramo() {
        return txt_ramo;
    }

    public void setTxt_ramo(String txt_ramo) {
        this.txt_ramo = txt_ramo;
    }

    public String getNro_pol() {
        return nro_pol;
    }

    public void setNro_pol(String nro_pol) {
        this.nro_pol = nro_pol;
    }

    public String getCod_contratante() {
        return cod_contratante;
    }

    public void setCod_contratante(String cod_contratante) {
        this.cod_contratante = cod_contratante;
    }

    public String getTxt_contratante() {
        return txt_contratante;
    }

    public void setTxt_contratante(String txt_contratante) {
        this.txt_contratante = txt_contratante;
    }

    public String getFec_siniestro() {
        return fec_siniestro;
    }

    public void setFec_siniestro(String fec_siniestro) {
        this.fec_siniestro = fec_siniestro;
    }

    public String getTxt_coberturas() {
        return txt_coberturas;
    }

    public void setTxt_coberturas(String txt_coberturas) {
        this.txt_coberturas = txt_coberturas;
    }
}
