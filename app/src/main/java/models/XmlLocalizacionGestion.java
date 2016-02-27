package models;

import java.util.List;

/**
 * Created by mac on 1/11/15.
 */
public class XmlLocalizacionGestion {

    private String id_gestion;
    private String txt_tipo;
    private String latitudDestino;
    private String longitudDestino;
    private List<XmlLocalizacionToken> xmlLocalizacionTokenList;

  public XmlLocalizacionGestion(){


  }

    public String getId_gestion() {
        return id_gestion;
    }

    public void setId_gestion(String id_gestion) {
        this.id_gestion = id_gestion;
    }

    public String getTxt_tipo() {
        return txt_tipo;
    }

    public void setTxt_tipo(String txt_tipo) {
        this.txt_tipo = txt_tipo;
    }

    public String getLatitudDestino() {
        return latitudDestino;
    }

    public void setLatitudDestino(String latitudDestino) {
        this.latitudDestino = latitudDestino;
    }

    public String getLongitudDestino() {
        return longitudDestino;
    }

    public void setLongitudDestino(String longitudDestino) {
        this.longitudDestino = longitudDestino;
    }

    public List<XmlLocalizacionToken> getXmlLocalizacionTokenList() {
        return xmlLocalizacionTokenList;
    }

    public void setXmlLocalizacionTokenList(List<XmlLocalizacionToken> xmlLocalizacionTokenList) {
        this.xmlLocalizacionTokenList = xmlLocalizacionTokenList;
    }
}
