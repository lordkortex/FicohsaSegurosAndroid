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
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import activities.MainActivity;

/**
 * Created by mac on 1/11/15.
 */
public class CrearGestionWebservice extends AsyncTask<String , Void, String> {
    private Context context;
    private static String SOAP_ACTION1 = "http://tempuri.org/nuevaGestion";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME1 = "nuevaGestion";
    private static String URLWS = "http://hdavid87-001-site1.btempurl.com/WebServices/wsFicohsaApp.asmx";


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

    public CrearGestionWebservice(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        final String inputValues = params[0].toString();
        final String[] separatedInputValues = inputValues.split(";");
        final String pToken = separatedInputValues[0].toString();
        final String pIdTipoAsistencia =  separatedInputValues[1].toString();
        final String pLatitud = separatedInputValues[2].toString();
        final String pLongitud =  separatedInputValues[3].toString();
        final String pTokenAndroid = separatedInputValues[4].toString();
        final String pTokenIos =  separatedInputValues[5].toString();
        String xml = "";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.setOutputSoapObject(request);
            request.addProperty("pToken", pToken);
            request.addProperty("pIdTipoAsistencia", pIdTipoAsistencia);
            request.addProperty("pLatitud", pLatitud);
            request.addProperty("pLongitud", pLongitud);
            request.addProperty("pTokenAndroid", pTokenAndroid);
            request.addProperty("pTokenIos", pTokenIos);
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URLWS,20000);
            androidHttpTransport.call(SOAP_ACTION1, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            //SoapFault12 result = (SoapFault12)envelope.bodyIn;
            SoapObject root = (SoapObject) result.getProperty(0);
            xml = root.getProperty("strError").toString();

        } catch (IOException e) {
            xml = "Tiempo de Espera agotado.";//e.getMessage().toString();
        } catch (XmlPullParserException e) {
            xml = "Tiempo de Espera agotado.";//e.getMessage().toString();
        } catch (Exception e) {
            xml = "Tiempo de Espera agotado.";//e.getMessage().toString();
        }


        return xml;
    }

    @Override
    protected void onPostExecute(String xml) {


        Toast.makeText(context, xml, Toast.LENGTH_LONG).show();


        Brockerdialog.setCancelable(true);
        Brockerdialog.dismiss();
    }



}