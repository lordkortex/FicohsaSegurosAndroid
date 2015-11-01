package asyntask;

import android.content.Context;
import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by mac on 31/10/15.
 */
public class LoginWebService  extends AsyncTask<String , Void, String> {
    private Context context;
    private static String SOAP_ACTION1 = "http://tempuri.org/tokenLogin";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME1 = "tokenLogin";
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

    public CoordinateDataWebService(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        final String inputValues = params[0].toString();
        final String[] separatedInputValues = inputValues.split(";");
        final String pToken = separatedInputValues[0].toString();
        final String pTokenMovilAndroid =  separatedInputValues[1].toString();
        String xml = "";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.setOutputSoapObject(request);
            request.addProperty("pToken", pToken);
            request.addProperty("pTokenMovilAndroid", pTokenMovilAndroid);
            request.addProperty("pTokenMoviliOs", "");



            envelope.dotNet = true;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URLWS,20000);
            androidHttpTransport.call(SOAP_ACTION1, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            //SoapFault12 result = (SoapFault12)envelope.bodyIn;
            SoapObject root = (SoapObject) result.getProperty(0);
            parseResponse(root);

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


    }


    private void parseResponse(SoapObject root){

        SoapObject s_deals = (SoapObject) root.getProperty("FOO_DEALS");

        StringBuilder stringBuilder = new StringBuilder();

        System.out.println("********Count : "+ s_deals.getPropertyCount());

        for (int i = 0; i < s_deals.getPropertyCount(); i++)
        {
            Object property = s_deals.getProperty(i);
            if (property instanceof SoapObject)
            {
                SoapObject category_list = (SoapObject) property;
                String CATEGORY = category_list.getProperty("CATEGORY").toString();
                String CATEGORY_URL = category_list.getProperty("CATEGORY_URL").toString();
                String CATEGORY_ICON = category_list.getProperty("CATEGORY_ICON").toString();
                String CATEGORY_COUNT = category_list.getProperty("CATEGORY_COUNT").toString();
                String SUPERTAG = category_list.getProperty("SUPERTAG").toString();
                String TYPE = category_list.getProperty("TYPE").toString();
                stringBuilder.append
                        (
                                "Row value of: " +(i+1)+"\n"+
                                        "Category: "+CATEGORY+"\n"+
                                        "Category URL: "+CATEGORY_URL+"\n"+
                                        "Category_Icon: "+CATEGORY_ICON+"\n"+
                                        "Category_Count: "+CATEGORY_COUNT+"\n"+
                                        "SuperTag: "+SUPERTAG+"\n"+
                                        "Type: "+TYPE+"\n"+
                                        "******************************"
                        );
                stringBuilder.append("\n");
            }
        }
    }



}