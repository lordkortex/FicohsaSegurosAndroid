package asyntask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault12;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mac on 27/10/15.
 */
public class CrearCoordenadaWebService extends AsyncTask<String , Void, String> {
    private Context context;
    /*private static String SOAP_ACTION1 = "http://tempuri.org/insertarCoordenada";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME1 = "insertarCoordenada";
    private static String URLWS = "http://lordkortex-001-site1.smarterasp.net/pediasureService.asmx?wsdl";*/


    private static String SOAP_ACTION1 = "http://tempuri.org/setLocation";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME1 = "setLocation";
    private static String URLWS = "http://hdavid87-001-site1.btempurl.com/WebServices/wsFicohsaApp.asmx";

    //private ProgressDialog Brockerdialog;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        /*Brockerdialog = new ProgressDialog(context);
        Brockerdialog.setMessage("Obteniendo Datos ...");
        Brockerdialog.setCancelable(false);
        Brockerdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Brockerdialog.show();*/

    }

    public CrearCoordenadaWebService(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        final String inputValues = params[0].toString();
        final String[] separatedInputValues = inputValues.split(";");
        String xml = "";


        if(separatedInputValues.length == 4){
            final String idgestion = separatedInputValues[0].toString();
            final String coorX = separatedInputValues[1].toString();
            final String coorY =  separatedInputValues[2].toString();
            final String password =  separatedInputValues[3].toString();

            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                envelope.setOutputSoapObject(request);
                //request.addProperty("coorX", coorX);
                //request.addProperty("coorY", coorY);

                request.addProperty("pToken", password);
                request.addProperty("pIdGestion", idgestion);
                request.addProperty("pIdAsistencia", "0");
                request.addProperty("pLatitud", coorX);
                request.addProperty("pLongitud", coorY);

                envelope.dotNet = true;
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URLWS,20000);
                androidHttpTransport.call(SOAP_ACTION1, envelope);
                SoapObject result = (SoapObject)envelope.bodyIn;
                //SoapFault12 result = (SoapFault12)envelope.bodyIn;
                xml="";//result.getProperty(0).toString();
            } catch (IOException e) {
                xml = "Tiempo de Espera agotado.";//e.getMessage().toString();
            } catch (XmlPullParserException e) {
                xml = "Tiempo de Espera agotado.";//e.getMessage().toString();
            } catch (Exception e) {
                xml = "Tiempo de Espera agotado.";//e.getMessage().toString();
            }
        }

        return xml;
    }

    @Override
    protected void onPostExecute(String xml) {
        if(!xml.equals("")){
            Toast.makeText(context, "En estos momentos tu dispositivo no tiene conexion a internet.", Toast.LENGTH_LONG).show();
        }

    }



}