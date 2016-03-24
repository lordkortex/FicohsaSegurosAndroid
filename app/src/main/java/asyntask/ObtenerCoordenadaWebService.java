package asyntask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import activities.MainActivity;
import activities.MapActivity;
import dto.XmlContainer;
import models.XmlLocalizacionGestion;
import models.XmlLocalizacionPuntos;
import models.XmlLocalizacionToken;
import models.XmlTokenLoginGestiones;
import models.XmlTokenLoginResult;
import models.XmlTokenLoginResultItems;
import models.XmlTokenLoginResultItemsCoberturas;

/**
 * Created by mac on 1/11/15.
 */
public class ObtenerCoordenadaWebService extends AsyncTask<String, Void, String> {
    private Context context;
    private static String SOAP_ACTION1 = "http://tempuri.org/getLocation";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME1 = "getLocation";
    //private static String URLWS = "http://hdavid87-001-site1.btempurl.com/WebServices/wsFicohsaApp.asmx";
    private static String URLWS = "http://207.248.66.2/WebServices/wsFicohsaApp.asmx?wsdl";

    private ProgressDialog Brockerdialog;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Brockerdialog = new ProgressDialog(context);
        Brockerdialog.setMessage("Obteniendo Datos ...");
        Brockerdialog.setCancelable(false);
        Brockerdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Brockerdialog.show();

    }

    public ObtenerCoordenadaWebService(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        final String pToken = params[0].toString();
        String response = "";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.headerOut = new Element[1];
            envelope.headerOut[0] = buildAuthHeader();
            envelope.setOutputSoapObject(request);
            request.addProperty("pToken", pToken);


            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URLWS, 20000);
            androidHttpTransport.call(SOAP_ACTION1, envelope);
            SoapObject result = (SoapObject) envelope.bodyIn;
            //SoapFault12 result = (SoapFault12)envelope.bodyIn;
            SoapObject root = (SoapObject) result.getProperty(0);
            XmlLocalizacionGestion xmlTokenLoginResult = parseResponse(root);
            //response = xmlTokenLoginResult.getStrError();

        } catch (IOException e) {
            response = "Tiempo de Espera agotado.";//e.getMessage().toString();
        } catch (XmlPullParserException e) {
            response = "Tiempo de Espera agotado.";//e.getMessage().toString();
        } catch (Exception e) {
            response = "Tiempo de Espera agotado.";//e.getMessage().toString();
        }


        return response;
    }

    @Override
    protected void onPostExecute(String xml) {

        //Toast.makeText(context, xml, Toast.LENGTH_LONG).show();
        if(xml.equals("")){
            Intent ourintent = new Intent(context, MapActivity.class);
            this.context.startActivity(ourintent);
        }else{
            Toast.makeText(context, "En estos momentos tu dispositivo no tiene conexion a internet.", Toast.LENGTH_LONG).show();
        }

        //finish();

        Brockerdialog.setCancelable(true);
        Brockerdialog.dismiss();
    }


    private XmlLocalizacionGestion parseResponse(SoapObject root) {

        XmlLocalizacionGestion xmlLocalizacionGestion = new XmlLocalizacionGestion();

        if(root.hasProperty("gestiones")){
            SoapObject gestiones = (SoapObject) root.getProperty("gestiones");
            SoapObject localizacionGestion = (SoapObject) gestiones.getProperty("localizacionGestion");

            String id_gestion = localizacionGestion.getProperty("id_gestion").toString();
            String txt_tipo = localizacionGestion.getProperty("txt_tipo").toString();
            String latitudDestino = localizacionGestion.getProperty("latitudDestino").toString();
            String longitudDestino = localizacionGestion.getProperty("longitudDestino").toString();

            xmlLocalizacionGestion.setId_gestion(id_gestion);
            xmlLocalizacionGestion.setLatitudDestino(latitudDestino);
            xmlLocalizacionGestion.setLongitudDestino(longitudDestino);
            xmlLocalizacionGestion.setTxt_tipo(txt_tipo);


            if(localizacionGestion.hasProperty("token")){

                SoapObject token = (SoapObject) localizacionGestion.getProperty("token");
                List<XmlLocalizacionToken> xmlLocalizacionTokenList = new ArrayList<XmlLocalizacionToken>();
                xmlLocalizacionGestion.setXmlLocalizacionTokenList(xmlLocalizacionTokenList);

                for (int i = 0; i < token.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) token.getProperty(i);

                    XmlLocalizacionToken xmlLocalizacionToken = new XmlLocalizacionToken();

                    String txt_token = item.getProperty("txt_token").toString();
                    String txt_nombre = item.getProperty("txt_nombre").toString();

                    xmlLocalizacionToken.setTxt_nombre(txt_token);
                    xmlLocalizacionToken.setTxt_token(txt_nombre);

                    xmlLocalizacionTokenList.add(xmlLocalizacionToken);


                    if(item.hasProperty("puntos")){
                        SoapObject puntos = (SoapObject) item.getProperty("puntos");
                        List<XmlLocalizacionPuntos> xmlLocalizacionPuntosList = new ArrayList<XmlLocalizacionPuntos>();
                        xmlLocalizacionToken.setXmlLocalizacionPuntosList(xmlLocalizacionPuntosList);


                        for (int h = 0; h < puntos.getPropertyCount(); h++) {
                            SoapObject cobertura = (SoapObject) puntos.getProperty(h);

                            String latitud = cobertura.getProperty("latitud").toString();
                            String longitud = cobertura.getProperty("longitud").toString();

                            XmlLocalizacionPuntos xmlLocalizacionPuntos = new XmlLocalizacionPuntos();

                            xmlLocalizacionPuntos.setLatitud(latitud);
                            xmlLocalizacionPuntos.setLongitud(longitud);

                            xmlLocalizacionPuntosList.add(xmlLocalizacionPuntos);

                        }

                    }
                }

            }


            XmlContainer.xmlLocalizacionGestion = xmlLocalizacionGestion;

        }


        return xmlLocalizacionGestion;

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