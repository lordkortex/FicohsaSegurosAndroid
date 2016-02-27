package models;

import java.util.List;

/**
 * Created by mac on 1/11/15.
 */
public class XmlTokenLoginResultItems {

    private String id_item;
    private String cod_asegurado;
    private String txt_asegurado;
    private String txt_chasis;
    private String txt_motor;
    private String txt_placa;
    private String fec_comp;
    private String sn_activo;
    private List<XmlTokenLoginResultItemsCoberturas> xmlTokenLoginResultItemsCoberturas;


    public XmlTokenLoginResultItems(){


    }

    public String getId_item() {
        return id_item;
    }

    public void setId_item(String id_item) {
        this.id_item = id_item;
    }

    public String getCod_asegurado() {
        return cod_asegurado;
    }

    public void setCod_asegurado(String cod_asegurado) {
        this.cod_asegurado = cod_asegurado;
    }

    public String getTxt_asegurado() {
        return txt_asegurado;
    }

    public void setTxt_asegurado(String txt_asegurado) {
        this.txt_asegurado = txt_asegurado;
    }

    public String getTxt_chasis() {
        return txt_chasis;
    }

    public void setTxt_chasis(String txt_chasis) {
        this.txt_chasis = txt_chasis;
    }

    public String getTxt_motor() {
        return txt_motor;
    }

    public void setTxt_motor(String txt_motor) {
        this.txt_motor = txt_motor;
    }

    public String getTxt_placa() {
        return txt_placa;
    }

    public void setTxt_placa(String txt_placa) {
        this.txt_placa = txt_placa;
    }

    public String getFec_comp() {
        return fec_comp;
    }

    public void setFec_comp(String fec_comp) {
        this.fec_comp = fec_comp;
    }

    public String getSn_activo() {
        return sn_activo;
    }

    public void setSn_activo(String sn_activo) {
        this.sn_activo = sn_activo;
    }

    public List<XmlTokenLoginResultItemsCoberturas> getXmlTokenLoginResultItemsCoberturas() {
        return xmlTokenLoginResultItemsCoberturas;
    }

    public void setXmlTokenLoginResultItemsCoberturas(List<XmlTokenLoginResultItemsCoberturas> xmlTokenLoginResultItemsCoberturas) {
        this.xmlTokenLoginResultItemsCoberturas = xmlTokenLoginResultItemsCoberturas;
    }
}
