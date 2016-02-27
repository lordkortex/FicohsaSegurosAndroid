package models;

import java.util.List;

/**
 * Created by mac on 1/11/15.
 */
public class XmlLocalizacionToken {

    private String txt_token;
    private String txt_nombre;
    private List<XmlLocalizacionPuntos> xmlLocalizacionPuntosList;


    public XmlLocalizacionToken(){


    }

    public String getTxt_token() {
        return txt_token;
    }

    public void setTxt_token(String txt_token) {
        this.txt_token = txt_token;
    }

    public String getTxt_nombre() {
        return txt_nombre;
    }

    public void setTxt_nombre(String txt_nombre) {
        this.txt_nombre = txt_nombre;
    }

    public List<XmlLocalizacionPuntos> getXmlLocalizacionPuntosList() {
        return xmlLocalizacionPuntosList;
    }

    public void setXmlLocalizacionPuntosList(List<XmlLocalizacionPuntos> xmlLocalizacionPuntosList) {
        this.xmlLocalizacionPuntosList = xmlLocalizacionPuntosList;
    }
}
