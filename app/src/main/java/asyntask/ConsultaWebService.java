package asyntask;

import android.app.FragmentManager;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import activities.DebitosActivity;
import activities.EstadoCuentaActivity;
import activities.EstadoSiniestroActivity;
import activities.FicohsaConstants;
import activities.LoginActivity;
import activities.MainActivity;
import activities.PolizasActivity;
import app.hn.com.ficohsaseguros.R;
import models.XmlDebitos;
import models.XmlEstadoCuenta;
import models.XmlMotivos;
import models.XmlSiniestros;
import models.XmlTokenLoginGestiones;
import models.XmlTokenLoginResult;
import models.XmlTokenLoginResultItems;
import models.XmlTokenLoginResultItemsCoberturas;

/**
 * Created by mac on 31/10/15.
 */
public class ConsultaWebService extends AsyncTask<String, Void, String> {

    private static Registration regService = null;
    private GoogleCloudMessaging gcm;
    String regId ="";
    private static final String SENDER_ID = "274403199680";

    private Context context;
    private static String SOAP_ACTION1 = "http://tempuri.org/consultaDatos";
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME1 = "consultaDatos";
    //private static String URLWS = "http://hdavid87-001-site1.btempurl.com/WebServices/wsFicohsaApp.asmx";
    private static String URLWS = "http://207.248.66.2/WebServices/wsFicohsaApp.asmx?wsdl";



    private ProgressDialog Brockerdialog;
    private String pActivityToCall;


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

    public ConsultaWebService(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        final String inputValues = params[0].toString();
        final String[] separatedInputValues = inputValues.split(";");
        final String pToken = separatedInputValues[0].toString();
        pActivityToCall = separatedInputValues[1].toString();
        String response = "";

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.setOutputSoapObject(request);
            request.addProperty("pToken", pToken);
            //request.addProperty("pTokenMovilAndroid", regId);
            //request.addProperty("pTokenMoviliOs", "");

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
            response = "Tiempo de Espera agotado.";//e.getMessage().toString();
        } catch (XmlPullParserException e) {
            response = "Tiempo de Espera agotado.";//e.getMessage().toString();
        } catch (Exception e) {
            response = "Tiempo de Espera agotado.";//e.getMessage().toString();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {


        if(response.equals("")){

            switch (pActivityToCall) {
                case "0":
                    Intent ourintentNotify = new Intent(context, PolizasActivity.class);
                    this.context.startActivity(ourintentNotify);

                    break;
                case "1":
                    Intent ourintentEstadoCuenta = new Intent(context, EstadoCuentaActivity.class);
                    this.context.startActivity(ourintentEstadoCuenta);
                    break;
                case "2":
                    Intent ourintentEstadoSiniestro = new Intent(context, EstadoSiniestroActivity.class);
                    this.context.startActivity(ourintentEstadoSiniestro);
                    break;
                case "3":
                    Intent ourintentDebitos = new Intent(context, DebitosActivity.class);
                    this.context.startActivity(ourintentDebitos);
                    break;

            }



        }else{
            Toast.makeText(context,response, Toast.LENGTH_LONG).show();

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

                //SoapObject asegurado = (SoapObject) root.getProperty("asegurado");

                String nro_pol = root.getProperty("nro_pol").toString();
                String txt_ramo = root.getProperty("txt_ramo").toString();
                String txt_suc = root.getProperty("txt_suc").toString();
                String anio_pol = root.getProperty("anio_pol").toString();
                String txt_estado_pol = root.getProperty("txt_estado_pol").toString();
                String txt_agente = root.getProperty("txt_agente").toString();
                String cod_contratante = root.getProperty("cod_contratante").toString();
                String txt_contratante = root.getProperty("txt_contratante").toString();
                String fec_vig_desde = root.getProperty("fec_vig_desde").toString();
                String fec_vig_hasta = root.getProperty("fec_vig_hasta").toString();
                String saldo_total = root.getProperty("saldo_total").toString();
                String saldo_vencido = root.getProperty("saldo_vencido").toString();
                //String txt_moneda = root.getProperty("txt_moneda").toString();

                xmlTokenLoginResult.setAnio_pol(anio_pol);
                xmlTokenLoginResult.setNro_pol(nro_pol);
                xmlTokenLoginResult.setTxt_contratante(txt_contratante);
                xmlTokenLoginResult.setTxt_estado_pol(txt_estado_pol);
                xmlTokenLoginResult.setTxt_ramo(txt_ramo);
                xmlTokenLoginResult.setTxt_suc(txt_suc);
                //xmlTokenLoginResult.setStrError(strError);


                if(root.hasProperty("items")){

                    SoapObject items = (SoapObject) root.getProperty("items");
                    List<XmlTokenLoginResultItems> xmlTokenLoginResultItemsList = new ArrayList<XmlTokenLoginResultItems>();
                    xmlTokenLoginResult.setXmlTokenLoginResultItemsList(xmlTokenLoginResultItemsList);

                    for (int i = 0; i < items.getPropertyCount(); i++) {
                        SoapObject item = (SoapObject) items.getProperty(i);

                        XmlTokenLoginResultItems xmlTokenLoginResultItems = new XmlTokenLoginResultItems();

                        //String id_item = item.getProperty("id_item").toString();
                        String cod_asegurado = item.getProperty("cod_asegurado").toString();
                        String txt_asegurado = item.getProperty("txt_asegurado").toString();
                        String txt_tipo_vehiculo = item.getProperty("txt_tipo_vehiculo").toString();
                        String txt_placa = item.getProperty("txt_placa").toString();
                        String txt_motor = item.getProperty("txt_motor").toString();
                        String txt_chasis = item.getProperty("txt_chasis").toString();
                        //String sn_activo = item.getProperty("sn_activo").toString();
                        String txt_marca = item.getProperty("txt_marca").toString();
                        String txt_modelo = item.getProperty("txt_modelo").toString();
                        String txt_color = item.getProperty("txt_color").toString();
                        String aaaa_modelo = item.getProperty("aaaa_modelo").toString();


                        //xmlTokenLoginResultItems.setId_item(id_item);
                        xmlTokenLoginResultItems.setCod_asegurado(cod_asegurado);
                        xmlTokenLoginResultItems.setTxt_asegurado(txt_asegurado);
                        xmlTokenLoginResultItems.setTxt_chasis(txt_chasis);
                        xmlTokenLoginResultItems.setTxt_motor(txt_motor);
                        xmlTokenLoginResultItems.setTxt_placa(txt_placa);

                        xmlTokenLoginResultItems.setTxt_marca(txt_marca);
                        xmlTokenLoginResultItems.setTxt_modelo(txt_modelo);
                        xmlTokenLoginResultItems.setTxt_color(txt_color);
                        xmlTokenLoginResultItems.setAaaa_modelo(aaaa_modelo);
                        //xmlTokenLoginResultItems.setSn_activo(sn_activo);
                        //xmlTokenLoginResultItems.setFec_comp(fec_comp);

                        xmlTokenLoginResultItemsList.add(xmlTokenLoginResultItems);


                        if(item.hasProperty("coberturas")){
                            SoapObject coberturas = (SoapObject) item.getProperty("coberturas");
                            List<XmlTokenLoginResultItemsCoberturas> xmlTokenLoginResultItemsCoberturasList = new ArrayList<XmlTokenLoginResultItemsCoberturas>();
                            xmlTokenLoginResult.setXmlTokenLoginResultItemsCoberturas(xmlTokenLoginResultItemsCoberturasList);


                            for (int h = 0; h < coberturas.getPropertyCount(); h++) {
                                SoapObject cobertura = (SoapObject) coberturas.getProperty(h);

                                /*String cod_cobertura = cobertura.getProperty("cod_cobertura").toString();
                                String txt_desc = cobertura.getProperty("txt_desc").toString();
                                String txt_desc_corta = cobertura.getProperty("txt_desc_corta").toString();
                                String txt_suma = "";//cobertura.getProperty("txt_suma").toString();
                                String txt_deducible = cobertura.getProperty("txt_deducible").toString();
                                String txt_moneda = cobertura.getProperty("txt_moneda").toString();
                                String imp_suma = "";//cobertura.getProperty("imp_suma").toString();
                                String imp_deducible = cobertura.getProperty("imp_deducible").toString();*/

                                String cod_cobertura = cobertura.getProperty("cod_cobertura").toString();
                                String txt_desc = cobertura.getProperty("txt_desc").toString();
                                String txt_desc_corta = cobertura.getProperty("txt_desc_corta").toString();
                                String txt_cubre = cobertura.getProperty("txt_cubre").toString();
                                String imp_deducible = cobertura.getProperty("imp_deducible").toString();
                                String txt_tipo_deducible = cobertura.getProperty("txt_tipo_deducible").toString();
                                String txt_aplicacion = cobertura.getProperty("txt_aplicacion").toString();
                                String txt_moneda = cobertura.getProperty("txt_moneda").toString();



                                XmlTokenLoginResultItemsCoberturas xmlTokenLoginResultItemsCoberturas = new XmlTokenLoginResultItemsCoberturas();

                                xmlTokenLoginResultItemsCoberturas.setCod_cobertura(cod_cobertura);
                                xmlTokenLoginResultItemsCoberturas.setTxt_desc(txt_desc);
                                xmlTokenLoginResultItemsCoberturas.setTxt_desc_corta(txt_desc_corta);
                                xmlTokenLoginResultItemsCoberturas.setTxt_suma("");
                                xmlTokenLoginResultItemsCoberturas.setTxt_deducible(txt_tipo_deducible);
                                xmlTokenLoginResultItemsCoberturas.setTxt_moneda(txt_moneda);
                                xmlTokenLoginResultItemsCoberturas.setImp_deducible(imp_deducible);
                                xmlTokenLoginResultItemsCoberturas.setImp_suma("");
                                xmlTokenLoginResultItemsCoberturas.setTxt_aplicacion(txt_aplicacion);
                                xmlTokenLoginResultItemsCoberturas.setTxt_cubre(txt_cubre);
                                xmlTokenLoginResultItemsCoberturasList.add(xmlTokenLoginResultItemsCoberturas);

                            }

                        }



                        if(root.hasProperty("estCuenta")){
                            SoapObject estadoCuentas = (SoapObject) root.getProperty("estCuenta");
                            List<XmlEstadoCuenta> xmlEstadoCuentas = new ArrayList<XmlEstadoCuenta>();
                            xmlTokenLoginResult.setXmlEstadoCuentas(xmlEstadoCuentas);


                            for (int h = 0; h < estadoCuentas.getPropertyCount(); h++) {
                                SoapObject estadoCuentasProperty = (SoapObject) estadoCuentas.getProperty(h);

                                String nro_cuota = estadoCuentasProperty.getProperty("nro_cuota").toString();
                                String nro_comprobante = estadoCuentasProperty.getProperty("nro_comprobante").toString();
                                String fecha_vencimiento = estadoCuentasProperty.getProperty("fecha_vencimiento").toString();
                                String imp_prima_total = estadoCuentasProperty.getProperty("imp_prima_total").toString();
                                String fecha_proceso = estadoCuentasProperty.getProperty("fecha_proceso").toString();
                                String txt_estado = estadoCuentasProperty.getProperty("txt_estado").toString();
                                String imp_prima_deposito = estadoCuentasProperty.getProperty("imp_prima_deposito").toString();
                                String imp_prima_pagada = estadoCuentasProperty.getProperty("imp_prima_pagada").toString();
                                String txt_moneda = estadoCuentasProperty.getProperty("txt_moneda").toString();
                                String fecha_cobranza = estadoCuentasProperty.getProperty("fecha_cobranza").toString();

                                XmlEstadoCuenta xmlTokenLoginResultItemsEstadoCuentas = new XmlEstadoCuenta();

                                xmlTokenLoginResultItemsEstadoCuentas.setNro_cuota(nro_cuota);
                                xmlTokenLoginResultItemsEstadoCuentas.setNro_comprobante(nro_comprobante);
                                xmlTokenLoginResultItemsEstadoCuentas.setFecha_vencimiento(fecha_vencimiento);
                                xmlTokenLoginResultItemsEstadoCuentas.setImp_prima_total(imp_prima_total);
                                xmlTokenLoginResultItemsEstadoCuentas.setFecha_proceso(fecha_proceso);
                                xmlTokenLoginResultItemsEstadoCuentas.setTxt_estado(txt_estado);
                                xmlTokenLoginResultItemsEstadoCuentas.setImp_prima_deposito(imp_prima_deposito);
                                xmlTokenLoginResultItemsEstadoCuentas.setImp_prima_pagada(imp_prima_pagada);
                                xmlTokenLoginResultItemsEstadoCuentas.setTxt_moneda(txt_moneda);
                                xmlTokenLoginResultItemsEstadoCuentas.setFecha_cobranza(fecha_cobranza);
                                xmlEstadoCuentas.add(xmlTokenLoginResultItemsEstadoCuentas);

                            }

                        }


                        if(root.hasProperty("debitos")){
                            SoapObject debitos = (SoapObject) root.getProperty("debitos");
                            List<XmlDebitos> xmlDebitos= new ArrayList<XmlDebitos>();
                            xmlTokenLoginResult.setXmlDebitos(xmlDebitos);


                            for (int h = 0; h < debitos.getPropertyCount(); h++) {
                                SoapObject debitosProperty = (SoapObject) debitos.getProperty(h);

                                String nro_cta_tarj = debitosProperty.getProperty("nro_cta_tarj").toString();
                                String nro_cuota = debitosProperty.getProperty("nro_cuota").toString();
                                String nro_comprobante = debitosProperty.getProperty("nro_comprobante").toString();
                                String fecha_generado = debitosProperty.getProperty("fecha_generado").toString();
                                String prima_total = debitosProperty.getProperty("prima_total").toString();
                                String prima_pagada = debitosProperty.getProperty("prima_pagada").toString();
                                String prima_vencida = debitosProperty.getProperty("prima_vencida").toString();
                                String txt_estado_intento = debitosProperty.getProperty("txt_estado_intento").toString();

                                XmlDebitos xmlTokenLoginResultItemsDebitos = new XmlDebitos();

                                xmlTokenLoginResultItemsDebitos.setNro_cuota(nro_cuota);
                                xmlTokenLoginResultItemsDebitos.setNro_comprobante(nro_comprobante);
                                xmlTokenLoginResultItemsDebitos.setNro_cta_tarj(nro_cta_tarj);
                                xmlTokenLoginResultItemsDebitos.setFecha_generado(fecha_generado);
                                xmlTokenLoginResultItemsDebitos.setPrima_total(prima_total);
                                xmlTokenLoginResultItemsDebitos.setPrima_pagada(prima_pagada);
                                xmlTokenLoginResultItemsDebitos.setPrima_vencida(prima_vencida);
                                xmlTokenLoginResultItemsDebitos.setTxt_estado_intento(txt_estado_intento);
                                xmlDebitos.add(xmlTokenLoginResultItemsDebitos);

                            }

                        }


                        if(root.hasProperty("siniestros")){
                            SoapObject siniestros = (SoapObject) root.getProperty("siniestros");
                            List<XmlSiniestros> xmlSiniestros = new ArrayList<XmlSiniestros>();
                            xmlTokenLoginResult.setXmlSiniestros(xmlSiniestros);


                            for (int h = 0; h < siniestros.getPropertyCount(); h++) {
                                SoapObject siniestrosProperty = (SoapObject) siniestros.getProperty(h);



                                String nro_siniestro = siniestrosProperty.getProperty("nro_siniestro").toString();
                                String stxt_suc = siniestrosProperty.getProperty("txt_suc").toString();
                                String stxt_ramo = siniestrosProperty.getProperty("txt_ramo").toString();
                                String snro_pol = siniestrosProperty.getProperty("nro_pol").toString();
                                String scod_contratante = siniestrosProperty.getProperty("cod_contratante").toString();
                                String stxt_contratante = siniestrosProperty.getProperty("txt_contratante").toString();
                                String stxt_coberturas = siniestrosProperty.getProperty("txt_coberturas").toString();

                                XmlSiniestros xmlSiniestrosItem = new XmlSiniestros();

                                xmlSiniestrosItem.setNro_siniestro(nro_siniestro);
                                xmlSiniestrosItem.setTxt_suc(stxt_suc);
                                xmlSiniestrosItem.setTxt_ramo(stxt_ramo);
                                xmlSiniestrosItem.setNro_pol(snro_pol);
                                xmlSiniestrosItem.setCod_contratante(scod_contratante);
                                xmlSiniestrosItem.setTxt_contratante(stxt_contratante);
                                xmlSiniestrosItem.setTxt_coberturas(stxt_coberturas);
                                xmlSiniestros.add(xmlSiniestrosItem);

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