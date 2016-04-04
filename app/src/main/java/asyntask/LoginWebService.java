package asyntask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.mac.myapplication.backend.registration.Registration;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import activities.FicohsaConstants;
import activities.GestionesActivity;
import activities.LoginActivity;
import activities.MainActivity;
import dto.XmlContainer;
import models.XmlLocalizacionGestion;
import models.XmlMotivos;
import models.XmlTokenLoginGestiones;
import models.XmlTokenLoginResult;
import models.XmlTokenLoginResultItems;
import models.XmlTokenLoginResultItemsCoberturas;

/**
 * Created by mac on 31/10/15.
 */
public class LoginWebService extends AsyncTask<String, Void, String> {

    private static Registration regService = null;
    private GoogleCloudMessaging gcm;
    String regId ="";
    private static final String SENDER_ID = "274403199680";

    private Context context;
    private static String SOAP_ACTION1 = "http://tempuri.org/tokenLogin";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME1 = "tokenLogin";
    //private static String URLWS = "http://hdavid87-001-site1.btempurl.com/WebServices/wsFicohsaApp.asmx";
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

    public LoginWebService(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        // Esta seccion es para conectar con Google cloud message
        if (regService == null) {
            Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("https://iron-arbor-843.appspot.com/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                                throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            regService = builder.build();
        }

        String msg = "";

        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            regId = gcm.register(SENDER_ID);
            msg = "Device registered, registration ID=" + regId;

            regService.register(regId).execute();

        } catch (IOException ex) {
            msg = "Error: " + ex.getMessage();
            Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
        }

        final String inputValues = params[0].toString();
        final String[] separatedInputValues = inputValues.split(";");
        final String pToken = separatedInputValues[0].toString();
        String response = "";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.setOutputSoapObject(request);
            request.addProperty("pToken", pToken);
            request.addProperty("pTokenMovilAndroid", regId);
            request.addProperty("pTokenMoviliOs", "");

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
            response = gson.toJson(xmlTokenLoginResult);

        } catch (IOException e) {
            response = FicohsaConstants.GENERIC_ERROR;//e.getMessage().toString();
        } catch (XmlPullParserException e) {
            response = FicohsaConstants.GENERIC_ERROR;//e.getMessage().toString();
        } catch (Exception e) {
            response = FicohsaConstants.GENERIC_ERROR;//e.getMessage().toString();
        }


        //xmlTokenLoginResult = parseResponseDummy();
        //Gson gson = new Gson();
        //response = gson.toJson(xmlTokenLoginResult);

        return response;
    }

    @Override
    protected void onPostExecute(String json) {

         Boolean isLoggedIn = false;

                if(xmlTokenLoginResult.getStrError() != null){
                    if( xmlTokenLoginResult.getStrError().equals("")){
                        isLoggedIn = true;
                     }else{
                        Toast.makeText(context, xmlTokenLoginResult.getStrError(), Toast.LENGTH_LONG).show();
                    }
                }else{
                    isLoggedIn = true;
                }

        if(isLoggedIn){
            SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = GetPrefs.edit();
            editor.putString(FicohsaConstants.IS_LOGGED, "TRUE");
            editor.putString(FicohsaConstants.JSON, json);
            editor.putString(FicohsaConstants.TOKEN_ANDROID, regId);
            editor.commit();

            Intent ourintent = new Intent(context, MainActivity.class);
            this.context.startActivity(ourintent);
            LoginActivity.activity.finish();

        }


        Brockerdialog.setCancelable(true);
        Brockerdialog.dismiss();
    }


    private XmlTokenLoginResult parseResponse(SoapObject root) {

        XmlTokenLoginResult xmlTokenLoginResult = new XmlTokenLoginResult();

        if(root.hasProperty("strError") && !root.getProperty("strError").toString().equals("anyType{}")) {
            String strError = root.getProperty("strError").toString();
           xmlTokenLoginResult.setStrError(strError);
        }else{
            String EsAsegurado = root.getProperty("EsAsegurado").toString();
            String snActivo = root.getProperty("snActivo").toString();
            String txtTelefonoAsistencia = root.getProperty("txtTelefonoAsistencia").toString();

            xmlTokenLoginResult.setEsAsegurado(EsAsegurado);
            xmlTokenLoginResult.setSnActivo(snActivo);
            xmlTokenLoginResult.setTxtTelefonoAsistencia(txtTelefonoAsistencia);


            if(root.hasProperty("asegurado")){
                SoapObject asegurado = (SoapObject) root.getProperty("asegurado");

                String nro_pol = asegurado.getProperty("nro_pol").toString();
                String txt_ramo = asegurado.getProperty("txt_ramo").toString();
                String txt_suc = asegurado.getProperty("txt_suc").toString();
                String anio_pol = asegurado.getProperty("anio_pol").toString();
                String txt_estado_pol = asegurado.getProperty("txt_estado_pol").toString();
                String txt_contratante = asegurado.getProperty("txt_contratante").toString();

                xmlTokenLoginResult.setAnio_pol(anio_pol);
                xmlTokenLoginResult.setNro_pol(nro_pol);
                xmlTokenLoginResult.setTxt_contratante(txt_contratante);
                xmlTokenLoginResult.setTxt_estado_pol(txt_estado_pol);
                xmlTokenLoginResult.setTxt_ramo(txt_ramo);
                xmlTokenLoginResult.setTxt_suc(txt_suc);
                //xmlTokenLoginResult.setStrError(strError);


                if(asegurado.hasProperty("items")){

                    SoapObject items = (SoapObject) asegurado.getProperty("items");
                    List<XmlTokenLoginResultItems> xmlTokenLoginResultItemsList = new ArrayList<XmlTokenLoginResultItems>();
                    xmlTokenLoginResult.setXmlTokenLoginResultItemsList(xmlTokenLoginResultItemsList);

                    for (int i = 0; i < items.getPropertyCount(); i++) {
                        SoapObject item = (SoapObject) items.getProperty(i);

                        XmlTokenLoginResultItems xmlTokenLoginResultItems = new XmlTokenLoginResultItems();

                        String id_item = item.getProperty("id_item").toString();
                        String cod_asegurado = item.getProperty("cod_asegurado").toString();
                        String txt_asegurado = item.getProperty("txt_asegurado").toString();
                        String txt_chasis = item.getProperty("txt_chasis").toString();
                        String txt_motor = item.getProperty("txt_motor").toString();
                        String txt_placa = item.getProperty("txt_placa").toString();
                        String sn_activo = item.getProperty("sn_activo").toString();
                        String fec_comp = item.getProperty("fec_comp").toString();

                        xmlTokenLoginResultItems.setId_item(id_item);
                        xmlTokenLoginResultItems.setCod_asegurado(cod_asegurado);
                        xmlTokenLoginResultItems.setTxt_asegurado(txt_asegurado);
                        xmlTokenLoginResultItems.setTxt_chasis(txt_chasis);
                        xmlTokenLoginResultItems.setTxt_motor(txt_motor);
                        xmlTokenLoginResultItems.setTxt_placa(txt_placa);
                        xmlTokenLoginResultItems.setSn_activo(sn_activo);
                        xmlTokenLoginResultItems.setFec_comp(fec_comp);

                        xmlTokenLoginResultItemsList.add(xmlTokenLoginResultItems);


                        if(item.hasProperty("coberturas")){
                            SoapObject coberturas = (SoapObject) item.getProperty("coberturas");
                            List<XmlTokenLoginResultItemsCoberturas> xmlTokenLoginResultItemsCoberturasList = new ArrayList<XmlTokenLoginResultItemsCoberturas>();
                            xmlTokenLoginResult.setXmlTokenLoginResultItemsCoberturas(xmlTokenLoginResultItemsCoberturasList);


                            for (int h = 0; h < coberturas.getPropertyCount(); h++) {
                                SoapObject cobertura = (SoapObject) coberturas.getProperty(h);

                                String cod_cobertura = cobertura.getProperty("cod_cobertura").toString();
                                String txt_desc = cobertura.getProperty("txt_desc").toString();
                                String txt_desc_corta = cobertura.getProperty("txt_desc_corta").toString();
                                String txt_suma = cobertura.getProperty("txt_suma").toString();
                                String txt_deducible = cobertura.getProperty("txt_deducible").toString();
                                String txt_moneda = cobertura.getProperty("txt_moneda").toString();
                                String imp_suma = cobertura.getProperty("imp_suma").toString();
                                String imp_deducible = cobertura.getProperty("imp_deducible").toString();

                                XmlTokenLoginResultItemsCoberturas xmlTokenLoginResultItemsCoberturas = new XmlTokenLoginResultItemsCoberturas();

                                xmlTokenLoginResultItemsCoberturas.setCod_cobertura(cod_cobertura);
                                xmlTokenLoginResultItemsCoberturas.setTxt_desc(txt_desc);
                                xmlTokenLoginResultItemsCoberturas.setTxt_desc_corta(txt_desc_corta);
                                xmlTokenLoginResultItemsCoberturas.setTxt_suma(txt_suma);
                                xmlTokenLoginResultItemsCoberturas.setTxt_deducible(txt_deducible);
                                xmlTokenLoginResultItemsCoberturas.setTxt_moneda(txt_moneda);
                                xmlTokenLoginResultItemsCoberturas.setImp_deducible(imp_deducible);
                                xmlTokenLoginResultItemsCoberturas.setImp_suma(imp_suma);
                                xmlTokenLoginResultItemsCoberturasList.add(xmlTokenLoginResultItemsCoberturas);

                            }

                        }
                    }

                }

            }

            if(root.hasProperty("gestiones")){

                List<XmlTokenLoginGestiones> xmlTokenLoginGestionesList = new ArrayList<XmlTokenLoginGestiones>();
                SoapObject gestiones = (SoapObject) root.getProperty("gestiones");



                for (int h = 0; h < gestiones.getPropertyCount(); h++) {
                    SoapObject gestion = (SoapObject) gestiones.getProperty(h);

                    XmlTokenLoginGestiones xmlTokenLoginGestiones = new XmlTokenLoginGestiones();

                    String id_gestion = gestion.getProperty("id_gestion").toString();
                    String id_item = gestion.getProperty("id_item").toString();
                    String fec_evento = gestion.getProperty("fec_evento").toString();
                    String fec_comp = gestion.getProperty("fec_comp").toString();
                    String sn_movil = gestion.getProperty("sn_movil").toString();
                    String sn_siniestro = gestion.getProperty("sn_siniestro").toString();
                    String txt_obs = gestion.getProperty("txt_obs").toString();
                    String latitud = gestion.getProperty("latitud").toString();
                    String longitud = gestion.getProperty("longitud").toString();

                    xmlTokenLoginGestiones.setId_gestion(id_gestion);
                    xmlTokenLoginGestiones.setId_item(id_item);
                    xmlTokenLoginGestiones.setFec_evento(fec_evento);
                    xmlTokenLoginGestiones.setFec_comp(fec_comp);
                    xmlTokenLoginGestiones.setSn_movil(sn_movil);
                    xmlTokenLoginGestiones.setSn_siniestro(sn_siniestro);
                    xmlTokenLoginGestiones.setTxt_obs(txt_obs);
                    xmlTokenLoginGestiones.setLatitud(latitud);
                    xmlTokenLoginGestiones.setLongitud(longitud);

                    xmlTokenLoginGestionesList.add(xmlTokenLoginGestiones);

                }

                xmlTokenLoginResult.setXmlTokenLoginGestionesList(xmlTokenLoginGestionesList);


            }

            SoapObject tracking = (SoapObject) root.getProperty("tracking");
            SoapObject listaMotivos = (SoapObject) tracking.getProperty("listaMotivos");

            List<XmlMotivos> xmlMotivosList = new ArrayList<XmlMotivos>();


            if(listaMotivos.hasProperty("motivosDetener")){
                for (int h = 0; h < listaMotivos.getPropertyCount(); h++) {
                    SoapObject motivosList = (SoapObject) listaMotivos.getProperty(h);
                    XmlMotivos xmlMotivos = new XmlMotivos();
                    String id_motivo = motivosList.getProperty("id_motivo").toString();
                    String txt_motivo = motivosList.getProperty("txt_motivo").toString();

                    xmlMotivos.setId_motivo(id_motivo);
                    xmlMotivos.setTxt_motivo(txt_motivo);
                    xmlMotivosList.add(xmlMotivos);


                    //XmlContainer.xmlMotivosList.put(id_motivo,txt_motivo);

                }
            }

            //XmlContainer.xmlMotivosList = xmlMotivosList;
            xmlTokenLoginResult.setXmlMotivosList(xmlMotivosList);

        }


        return xmlTokenLoginResult;

    }


    private XmlTokenLoginResult parseResponseDummy() {

        String EsAsegurado = "true";
        String snActivo = "1";
        String txtTelefonoAsistencia = "92000000";


        String nro_pol = "99";
        String txt_ramo = "Autos";
        String txt_suc = "SPS";
        String anio_pol = "2014";
        String txt_estado_pol = "1";
        String txt_contratante = "Contratantes";
        String strError = "";

        XmlTokenLoginResult xmlTokenLoginResult = new XmlTokenLoginResult();
        xmlTokenLoginResult.setEsAsegurado(EsAsegurado);
        xmlTokenLoginResult.setSnActivo(snActivo);
        xmlTokenLoginResult.setAnio_pol(anio_pol);
        xmlTokenLoginResult.setNro_pol(nro_pol);
        xmlTokenLoginResult.setTxt_contratante(txt_contratante);
        xmlTokenLoginResult.setTxt_estado_pol(txt_estado_pol);
        xmlTokenLoginResult.setTxt_ramo(txt_ramo);
        xmlTokenLoginResult.setTxt_suc(txt_suc);
        xmlTokenLoginResult.setStrError(strError);
        xmlTokenLoginResult.setTxtTelefonoAsistencia(txtTelefonoAsistencia);


        List<XmlTokenLoginResultItems> xmlTokenLoginResultItemsList = new ArrayList<XmlTokenLoginResultItems>();
        xmlTokenLoginResult.setXmlTokenLoginResultItemsList(xmlTokenLoginResultItemsList);


        for (int i = 0; i < 2; i++) {

                XmlTokenLoginResultItems xmlTokenLoginResultItems = new XmlTokenLoginResultItems();

                String id_item = "1";
                String cod_asegurado = "2";
                String txt_asegurado = "";
                String txt_chasis = "";
                String txt_motor = "";
                String txt_placa = "";
                String sn_activo = "";
                String fec_comp = "";



                xmlTokenLoginResultItems.setId_item(id_item);
                xmlTokenLoginResultItems.setCod_asegurado(cod_asegurado);
                xmlTokenLoginResultItems.setTxt_asegurado(txt_asegurado);
                xmlTokenLoginResultItems.setTxt_chasis(txt_chasis);
                xmlTokenLoginResultItems.setTxt_motor(txt_motor);
                xmlTokenLoginResultItems.setTxt_placa(txt_placa);
                xmlTokenLoginResultItems.setSn_activo(sn_activo);
                xmlTokenLoginResultItems.setFec_comp(fec_comp);

                xmlTokenLoginResultItemsList.add(xmlTokenLoginResultItems);


                /*if(item.hasProperty("coberturas")){
                    SoapObject coberturas = (SoapObject) item.getProperty("coberturas");
                    List<XmlTokenLoginResultItemsCoberturas> xmlTokenLoginResultItemsCoberturasList = new ArrayList<XmlTokenLoginResultItemsCoberturas>();
                    xmlTokenLoginResultItems.setXmlTokenLoginResultItemsCoberturas(xmlTokenLoginResultItemsCoberturasList);


                    for (int h = 0; h < coberturas.getPropertyCount(); h++) {
                        SoapObject cobertura = (SoapObject) coberturas.getProperty(h);

                        String cod_cobertura = cobertura.getProperty("cod_cobertura").toString();
                        String txt_desc = cobertura.getProperty("txt_desc").toString();
                        String txt_desc_corta = cobertura.getProperty("txt_desc_corta").toString();
                        String txt_suma = cobertura.getProperty("txt_suma").toString();
                        String txt_deducible = cobertura.getProperty("txt_deducible").toString();
                        String txt_moneda = cobertura.getProperty("txt_moneda").toString();
                        String imp_suma = cobertura.getProperty("imp_suma").toString();
                        String imp_deducible = cobertura.getProperty("imp_deducible").toString();

                        XmlTokenLoginResultItemsCoberturas xmlTokenLoginResultItemsCoberturas = new XmlTokenLoginResultItemsCoberturas();

                        xmlTokenLoginResultItemsCoberturas.setCod_cobertura(cod_cobertura);
                        xmlTokenLoginResultItemsCoberturas.setTxt_desc(txt_desc);
                        xmlTokenLoginResultItemsCoberturas.setTxt_desc_corta(txt_desc_corta);
                        xmlTokenLoginResultItemsCoberturas.setTxt_suma(txt_suma);
                        xmlTokenLoginResultItemsCoberturas.setTxt_deducible(txt_deducible);
                        xmlTokenLoginResultItemsCoberturas.setTxt_moneda(txt_moneda);
                        xmlTokenLoginResultItemsCoberturas.setImp_deducible(imp_deducible);
                        xmlTokenLoginResultItemsCoberturas.setImp_suma(imp_suma);
                        xmlTokenLoginResultItemsCoberturasList.add(xmlTokenLoginResultItemsCoberturas);

                    }

                }*/
            }



        /*if(root.hasProperty("gestiones")){

            List<XmlTokenLoginGestiones> xmlTokenLoginGestionesList = new ArrayList<XmlTokenLoginGestiones>();
            SoapObject gestiones = (SoapObject) root.getProperty("gestiones");



            for (int h = 0; h < gestiones.getPropertyCount(); h++) {
                SoapObject gestion = (SoapObject) gestiones.getProperty(h);

                XmlTokenLoginGestiones xmlTokenLoginGestiones = new XmlTokenLoginGestiones();

                String id_gestion = gestion.getProperty("id_gestion").toString();
                String id_item = gestion.getProperty("id_item").toString();
                String fec_evento = gestion.getProperty("fec_evento").toString();
                String fec_comp = gestion.getProperty("fec_comp").toString();
                String sn_movil = gestion.getProperty("sn_movil").toString();
                String sn_siniestro = gestion.getProperty("sn_siniestro").toString();
                String txt_obs = gestion.getProperty("txt_obs").toString();
                String latitud = gestion.getProperty("latitud").toString();
                String longitud = gestion.getProperty("longitud").toString();

                xmlTokenLoginGestiones.setId_gestion(id_gestion);
                xmlTokenLoginGestiones.setId_item(id_item);
                xmlTokenLoginGestiones.setFec_evento(fec_evento);
                xmlTokenLoginGestiones.setFec_comp(fec_comp);
                xmlTokenLoginGestiones.setSn_movil(sn_movil);
                xmlTokenLoginGestiones.setSn_siniestro(sn_siniestro);
                xmlTokenLoginGestiones.setTxt_obs(txt_obs);
                xmlTokenLoginGestiones.setLatitud(latitud);
                xmlTokenLoginGestiones.setLongitud(longitud);

                xmlTokenLoginGestionesList.add(xmlTokenLoginGestiones);

            }

            xmlTokenLoginResult.setXmlTokenLoginGestionesList(xmlTokenLoginGestionesList);


        }*/

        //SoapObject tracking = (SoapObject) root.getProperty("tracking");
        //SoapObject listaMotivos = (SoapObject) tracking.getProperty("listaMotivos");



        //XmlContainer.xmlMotivosList = xmlMotivosList;
        //xmlTokenLoginResult.setXmlMotivosList(xmlMotivosList);





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