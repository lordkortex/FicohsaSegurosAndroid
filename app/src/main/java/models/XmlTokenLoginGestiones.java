package models;

/**
 * Created by mac on 1/11/15.
 */
public class XmlTokenLoginGestiones {

    private String id_gestion;
    private String id_item;
    private String fec_evento;
    private String txt_obs;
    private String sn_siniestro;
    private String sn_movil;
    private String fec_comp;
    private String latitud;
    private String longitud;

    public XmlTokenLoginGestiones(){


    }


    public String getId_gestion() {
        return id_gestion;
    }

    public void setId_gestion(String id_gestion) {
        this.id_gestion = id_gestion;
    }

    public String getId_item() {
        return id_item;
    }

    public void setId_item(String id_item) {
        this.id_item = id_item;
    }

    public String getFec_evento() {
        return fec_evento;
    }

    public void setFec_evento(String fec_evento) {
        this.fec_evento = fec_evento;
    }

    public String getTxt_obs() {
        return txt_obs;
    }

    public void setTxt_obs(String txt_obs) {
        this.txt_obs = txt_obs;
    }

    public String getSn_siniestro() {
        return sn_siniestro;
    }

    public void setSn_siniestro(String sn_siniestro) {
        this.sn_siniestro = sn_siniestro;
    }

    public String getSn_movil() {
        return sn_movil;
    }

    public void setSn_movil(String sn_movil) {
        this.sn_movil = sn_movil;
    }

    public String getFec_comp() {
        return fec_comp;
    }

    public void setFec_comp(String fec_comp) {
        this.fec_comp = fec_comp;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
