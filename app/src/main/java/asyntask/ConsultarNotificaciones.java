package asyntask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;


import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


import activities.DebitosActivity;
import activities.EstadoCuentaActivity;
import activities.EstadoSiniestroActivity;
import activities.FicohsaConstants;
import activities.LoginActivity;
import activities.MainActivity;
import activities.NotificacionesActivity;
import activities.PolizasActivity;
import models.XmlMotivos;
import models.XmlNotificaciones;
import models.XmlTokenLoginGestiones;
import models.XmlTokenLoginResult;
import models.XmlTokenLoginResultItems;
import models.XmlTokenLoginResultItemsCoberturas;

/**
 * Created by mac on 28/3/16.
 */
public class ConsultarNotificaciones  extends AsyncTask<String, Void, String> {


    private Context context;
    private static String SOAP_ACTION1 = "http://tempuri.org/getNotificaciones";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME1 = "getNotificaciones";
    private static String URLWS = "http://207.248.66.2/WebServices/wsFicohsaApp.asmx?wsdl";



    private ProgressDialog Brockerdialog;


    private static List<XmlTokenLoginGestiones> xmlLocalizacionGestionList = new ArrayList<XmlTokenLoginGestiones>();
    private static XmlTokenLoginResult xmlTokenLoginResult;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Brockerdialog = new ProgressDialog(context);
        Brockerdialog.setMessage("Obteniendo Datos ...");
        Brockerdialog.setCancelable(false);
        Brockerdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Brockerdialog.show();

    }

    public ConsultarNotificaciones(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {


        final String inputValues = params[0].toString();
        final String[] separatedInputValues = inputValues.split(";");
        final String pToken = separatedInputValues[0].toString();
        String response = "";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.setOutputSoapObject(request);
            request.addProperty("pToken", pToken);

            envelope.headerOut = new Element[1];
            envelope.headerOut[0] = buildAuthHeader();


            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URLWS, 20000);
            androidHttpTransport.call(SOAP_ACTION1, envelope);
            SoapObject result = (SoapObject) envelope.bodyIn;
            SoapObject root = (SoapObject) result.getProperty(0);
            xmlTokenLoginResult = parseResponse(root);
            xmlLocalizacionGestionList = xmlTokenLoginResult.getXmlTokenLoginGestionesList();

            Gson gson = new Gson();
            String jsonResponse = gson.toJson(xmlTokenLoginResult);


            if(xmlTokenLoginResult.getStrError() != null){
                if( !xmlTokenLoginResult.getStrError().equals("")){
                    response = xmlTokenLoginResult.getStrError();
                }else{
                    SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = GetPrefs.edit();
                    editor.putString(FicohsaConstants.JSON, jsonResponse);
                    editor.commit();
                }
            }else{
                SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = GetPrefs.edit();
                editor.putString(FicohsaConstants.JSON, jsonResponse);
                editor.commit();
            }


        } catch (IOException e) {
            response = FicohsaConstants.GENERIC_ERROR;//e.getMessage().toString();
        } catch (XmlPullParserException e) {
            response = FicohsaConstants.GENERIC_ERROR;//e.getMessage().toString();
        } catch (Exception e) {
            response = FicohsaConstants.GENERIC_ERROR;//e.getMessage().toString();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {

        if(response.equals("")){
            Intent ourintentNotify = new Intent(context, NotificacionesActivity.class);
            this.context.startActivity(ourintentNotify);
        }else{
            //Toast.makeText(context,response, Toast.LENGTH_LONG).show();

        }


        Brockerdialog.setCancelable(true);
        Brockerdialog.dismiss();
    }


    private XmlTokenLoginResult parseResponse(SoapObject root) {

        //XmlTokenLoginResult xmlTokenLoginResult = new XmlTokenLoginResult();

        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = "";
        if (GetPrefs.contains(FicohsaConstants.JSON)) {
            json = GetPrefs.getString(FicohsaConstants.JSON, "");
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new StringReader(json));
            xmlTokenLoginResult = gson.fromJson(br, XmlTokenLoginResult.class);
        }

        if(root.hasProperty("strError") && !root.getProperty("strError").toString().equals("anyType{}")) {
            String strError = root.getProperty("strError").toString();
            xmlTokenLoginResult.setStrError(strError);
        }else{

            List<XmlNotificaciones> xmlNotificacionesList = new ArrayList<XmlNotificaciones>();

            if(root.hasProperty("clNotificacionMovil")){
                SoapObject listaNotificacionesCl = (SoapObject) root.getProperty("clNotificacionMovil");

                if(listaNotificacionesCl.hasProperty("notificacionMovil")){
                    SoapObject listaNotificaciones = (SoapObject) listaNotificacionesCl.getProperty("notificacionMovil");


                    for (int h = 0; h < listaNotificaciones.getPropertyCount(); h++) {
                        //SoapObject notificacionesList = (SoapObject) listaNotificaciones.getProperty(h);
                        XmlNotificaciones xmlNotificaciones = new XmlNotificaciones();
                        String id_notificacion = listaNotificaciones.getProperty("id_notificacion").toString();
                        String txt_titulo = listaNotificaciones.getProperty("txt_titulo").toString();
                        String txt_desc = listaNotificaciones.getProperty("txt_desc").toString();
                        String fec_comp = listaNotificaciones.getProperty("fec_comp").toString();
                        String imagenB64 = listaNotificaciones.getProperty("imagenB64").toString();

                        xmlNotificaciones.setId_notificacion(id_notificacion);
                        xmlNotificaciones.setTxt_desc(txt_desc);
                        xmlNotificaciones.setTxt_titulo(txt_titulo);
                        xmlNotificaciones.setFec_comp(fec_comp);
                        xmlNotificaciones.setImagenB64(imagenB64);
                        xmlNotificacionesList.add(xmlNotificaciones);


                        //XmlContainer.xmlMotivosList.put(id_motivo,txt_motivo);
                }
                }
            }

            //XmlContainer.xmlMotivosList = xmlMotivosList;
            xmlTokenLoginResult.setXmlNotificaciones(xmlNotificacionesList);

        }


        return xmlTokenLoginResult;

    }



    private Element buildAuthHeader() {
        Element h = new Element().createElement(NAMESPACE, "Authentication");
        Element username = new Element().createElement(NAMESPACE, "User");
        username.addChild(Node.TEXT, "uapp");
        h.addChild(Node.ELEMENT, username);
        Element pass = new Element().createElement(NAMESPACE, "Password");
        pass.addChild(Node.TEXT, "gfe4532ki9");
        h.addChild(Node.ELEMENT, pass);

        return h;
    }


}