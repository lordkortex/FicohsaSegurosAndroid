package models;

/**
 * Created by mac on 28/3/16.
 */
public class XmlNotificaciones {

    private String id_notificacion;
    private String txt_titulo;
    private String txt_desc;
    private String fec_comp;
    private String imagenB64;



    public XmlNotificaciones(){

    }

    public String getId_notificacion() {
        return id_notificacion;
    }

    public void setId_notificacion(String id_notificacion) {
        this.id_notificacion = id_notificacion;
    }

    public String getTxt_titulo() {
        return txt_titulo;
    }

    public void setTxt_titulo(String txt_titulo) {
        this.txt_titulo = txt_titulo;
    }

    public String getTxt_desc() {
        return txt_desc;
    }

    public void setTxt_desc(String txt_desc) {
        this.txt_desc = txt_desc;
    }

    public String getFec_comp() {
        return fec_comp;
    }

    public void setFec_comp(String fec_comp) {
        this.fec_comp = fec_comp;
    }

    public String getImagenB64() {
        return imagenB64;
    }

    public void setImagenB64(String imagenB64) {
        this.imagenB64 = imagenB64;
    }
}
