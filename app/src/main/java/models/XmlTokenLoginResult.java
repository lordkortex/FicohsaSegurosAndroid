package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 1/11/15.
 */
public class XmlTokenLoginResult {

    private String EsAsegurado;
    private String snActivo;
    private String txtTelefonoAsistencia;
    private String nro_pol;
    private String txt_ramo;
    private String txt_suc;
    private String anio_pol;
    private String txt_estado_pol;
    private String txt_contratante;
    private String strError;
    private List<XmlTokenLoginResultItems> xmlTokenLoginResultItemsList;
    private List<XmlTokenLoginGestiones> xmlTokenLoginGestionesList;
    private List<XmlMotivos> xmlMotivosList;


    public XmlTokenLoginResult(){


    }


    public String getEsAsegurado() {
        return EsAsegurado;
    }

    public void setEsAsegurado(String esAsegurado) {
        EsAsegurado = esAsegurado;
    }

    public String getSnActivo() {
        return snActivo;
    }

    public void setSnActivo(String snActivo) {
        this.snActivo = snActivo;
    }

    public String getNro_pol() {
        return nro_pol;
    }

    public void setNro_pol(String nro_pol) {
        this.nro_pol = nro_pol;
    }

    public String getTxt_ramo() {
        return txt_ramo;
    }

    public void setTxt_ramo(String txt_ramo) {
        this.txt_ramo = txt_ramo;
    }

    public String getTxt_suc() {
        return txt_suc;
    }

    public void setTxt_suc(String txt_suc) {
        this.txt_suc = txt_suc;
    }

    public String getAnio_pol() {
        return anio_pol;
    }

    public void setAnio_pol(String anio_pol) {
        this.anio_pol = anio_pol;
    }

    public String getTxt_estado_pol() {
        return txt_estado_pol;
    }

    public void setTxt_estado_pol(String txt_estado_pol) {
        this.txt_estado_pol = txt_estado_pol;
    }

    public String getTxt_contratante() {
        return txt_contratante;
    }

    public void setTxt_contratante(String txt_contratante) {
        this.txt_contratante = txt_contratante;
    }

    public List<XmlTokenLoginResultItems> getXmlTokenLoginResultItemsList() {
        return xmlTokenLoginResultItemsList;
    }

    public void setXmlTokenLoginResultItemsList(List<XmlTokenLoginResultItems> xmlTokenLoginResultItemsList) {
        this.xmlTokenLoginResultItemsList = xmlTokenLoginResultItemsList;
    }

    public String getStrError() {
        return strError;
    }

    public void setStrError(String strError) {
        this.strError = strError;
    }

    public List<XmlTokenLoginGestiones> getXmlTokenLoginGestionesList() {
        return xmlTokenLoginGestionesList;
    }

    public void setXmlTokenLoginGestionesList(List<XmlTokenLoginGestiones> xmlTokenLoginGestionesList) {
        this.xmlTokenLoginGestionesList = xmlTokenLoginGestionesList;
    }

    public String getTxtTelefonoAsistencia() {
        return txtTelefonoAsistencia;
    }

    public void setTxtTelefonoAsistencia(String txtTelefonoAsistencia) {
        this.txtTelefonoAsistencia = txtTelefonoAsistencia;
    }

    public List<XmlMotivos> getXmlMotivosList() {
        return xmlMotivosList;
    }

    public void setXmlMotivosList(List<XmlMotivos> xmlMotivosList) {
        this.xmlMotivosList = xmlMotivosList;
    }
}
