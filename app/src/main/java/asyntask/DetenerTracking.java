package asyntask;

import android.app.ProgressDialog;
import android.content.Context;
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

import activities.FicohsaConstants;

/**
 * Created by mac on 5/4/16.
 */
public class DetenerTracking  extends AsyncTask<String , Void, String> {
    private Context context;
    private static String SOAP_ACTION1 = "http://tempuri.org/detenerTracking";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME1 = "detenerTracking";
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

    public DetenerTracking(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        final String inputValues = params[0].toString();
        final String[] separatedInputValues = inputValues.split(";");
        final String pToken = separatedInputValues[0].toString();
        final String pIdGestion = separatedInputValues[1].toString();
        final String pMotivo =  separatedInputValues[2].toString();
        String xml = "";


        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.setOutputSoapObject(request);

            envelope.headerOut = new Element[1];
            envelope.headerOut[0] = buildAuthHeader();


            request.addProperty("pToken", pToken);
            request.addProperty("pIdGestion", pIdGestion);
            request.addProperty("pIdMotivo", pMotivo);
            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URLWS,20000);
            androidHttpTransport.call(SOAP_ACTION1, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            //SoapFault12 result = (SoapFault12)envelope.bodyIn;
            //SoapObject root = (SoapObject) result.getProperty(0);

            if(result.hasProperty("detenerTrackingResult") && !result.getProperty("detenerTrackingResult").toString().equals("anyType{}")){
                xml = result.getProperty("detenerTrackingResult").toString();
            }else{
                xml = "Proceso Exitoso";
            }

        } catch (IOException e) {
            xml = FicohsaConstants.GENERIC_ERROR;//e.getMessage().toString();
        } catch (XmlPullParserException e) {
            xml = FicohsaConstants.GENERIC_ERROR;//e.getMessage().toString();
        } catch (Exception e) {
            xml = FicohsaConstants.GENERIC_ERROR;//e.getMessage().toString();
        }


        return xml;
    }

    @Override
    protected void onPostExecute(String xml) {


        //Toast.makeText(context, xml, Toast.LENGTH_LONG).show();


        Brockerdialog.setCancelable(true);
        Brockerdialog.dismiss();
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