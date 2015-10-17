package util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by mac on 11/10/15.
 */
public class XpathUtil {

    public static List<String> getListObjects(final String xml, final int position, final String expresion,final Boolean duplicatedValues, final Context context) {
        SharedPreferences GetPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String xmlSesion = "";
        if (GetPrefs.contains(xml)) {
            xmlSesion = GetPrefs.getString(xml, xml);
        }
        NodeList nodes = XpathUtil.getXptathResult(xmlSesion, "/NewDataSet/" + xml + expresion);

        List<String> planlistValores = new ArrayList<String>();
        Set<String> uniqueValues = new HashSet<String>();

        planlistValores.add("");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            NodeList venueChildNodes = node.getChildNodes();
            NodeList nodeItem = venueChildNodes.item(position).getChildNodes();
            if(nodeItem.getLength()>0){
                String valor = nodeItem.item(0).getNodeValue();
                planlistValores.add(valor);
                uniqueValues.add(valor);
            }
        }

        if (!duplicatedValues){
            planlistValores.clear();
            planlistValores.add("");
            planlistValores.addAll(uniqueValues);
        }

        return planlistValores;
    }



    public static  NodeList getXptathResult(final String xml, final String expression  ) {
        NodeList nodes = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(new ByteArrayInputStream(xml.getBytes()));
            XPath xPath = XPathFactory.newInstance().newXPath();
            Object result = xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
            nodes = (NodeList) result;
        } catch (SAXException e) {
        } catch (IOException e) {
        } catch (ParserConfigurationException e) {
        } catch (XPathExpressionException e) {
        }

        return nodes;

    }

    public static String  buildXmlNotificacion(final String mensaje,final String hora){

        StringBuilder Cadena = new StringBuilder();

        Cadena.append("<notificacion>");
        Cadena.append("<mensaje>" + mensaje + "</mensaje>");
        Cadena.append("<hora>" + hora + " </hora>");
        Cadena.append("</notificacion>");

        return Cadena.toString();
    }

}
