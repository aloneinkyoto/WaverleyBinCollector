package com.mk.bincollector;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by matt.killen on 12/10/16.
 */
public class BinParser {

    private String htmlString;
    private static final String TAG="Binparser";

    ///
    // curl "http://www.waverley.gov.uk/CollectionDetails.php" --data "Address=11&Postcode=GU9+0RH"
    //
    String url = "http://www.waverley.gov.uk/CollectionDetails.php";
    String postData = "Address=11&Postcode=GU9+0RH";

    public void init(){
        htmlString = performPostCall(url, postData);
    }

    enum Bin {
        BLACK_BIN,
        BLUE_BIN,
        BROWN_BIN
    };
    public String getBinDay(Bin b){
        try {
            StringBuilder sb = new StringBuilder();
            Document doc = Jsoup.parseBodyFragment(htmlString);
            Elements addressDiv = doc.select("div[id=address1div]");

            int i=0;
            String selector = "";
            switch(b){
                case BLACK_BIN: selector = "rubbish and food waste"; break;
                case BLUE_BIN:  selector = "recycling and food waste"; break;
                case BROWN_BIN: selector = "garden waste"; break;

                default: break;
            }

            for(Element e : addressDiv){

                Elements elements = e.select("p:matchesOwn(Your next " + selector + " collection is:)");

                for(Element x : elements){
                    String s = x.nextElementSibling().text();
                    sb.append(s + "\n");
                    Log.w(TAG, s);
                }
            }

            return sb.toString();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return "Error";
    }

    public String  performPostCall(String requestURL, String requestParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(requestParams);

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


    private String testString = "<h3>Select your address to view collection day details.</h3>\n" +
            "<a href=\"#\" id=\"address1\" style=\"text-decoration:none; font-family:Arial;\">11 West Avenue, Farnham, Surrey, GU9 0RH</a>\n" +
            "<div id=\"address1div\" title=\"Your Collection Details\" style=\"display:none;\">\n" +
            "<div>\n" +
            "<ul style=\"list-style: none;\">\n" +
            "<li>\n" +
            "<table>\n" +
            "<tbody>\n" +
            "<tr align=\"justify\">\n" +
            "<td>\n" +
            "<img src=\"http://www.waverley.gov.uk/images/blackbin.jpg\" alt=\"bin\" align=\"middle\" height=\"80\" />\n" +
            "</td>\n" +
            "<td style=\"vertical-align:middle;\">\n" +
            "<p style=\"font-family:Arial;\">Your next rubbish and food waste collection is:</p>\n" +
            "<p style=\"font-family:Arial;\">Monday 3 October 2016</p>\n" +
            "<a href=\"http://www.waverley.gov.uk/downloads/file/5135/rubbish_and_recycling_calendar_2016_-_calendar_b\">Click to view your collection calendar</a>\n" +
            "</td>\n" +
            "</tr>\n" +
            "</tbody>\n" +
            "</table>\n" +
            "</li>\n" +
            "</ul>\n" +
            "</div>\n" +
            "<div>\n" +
            "<ul style=\"list-style: none;\">\n" +
            "<li>\n" +
            "<table>\n" +
            "<tbody>\n" +
            "<tr align=\"justify\">\n" +
            "<td>\n" +
            "<img src=\"http://www.waverley.gov.uk/images/bluebin.jpg\" alt=\"bin\" align=\"middle\" height=\"80\" />\n" +
            "</td>\n" +
            "<td style=\"vertical-align:middle;\">\n" +
            "<p style=\"font-family:Arial;\">Your next recycling and food waste collection is:</p>\n" +
            "<p style=\"font-family:Arial;\">Monday 10 October 2016</p>\n" +
            "<a href=\"http://www.waverley.gov.uk/downloads/file/5135/rubbish_and_recycling_calendar_2016_-_calendar_b\">Click to view your collection calendar</a>\n" +
            "</td>\n" +
            "</tr>\n" +
            "</tbody>\n" +
            "</table>\n" +
            "</li>\n" +
            "</ul>\n" +
            "</div>\n" +
            "<div>\n" +
            "<ul style=\"list-style: none;\">\n" +
            "<li>\n" +
            "<table>\n" +
            "<tbody>\n" +
            "<tr align=\"justify\">\n" +
            "<td>\n" +
            "<img src=\"http://www.waverley.gov.uk/images/brownbin.jpg\" alt=\"bin\" align=\"middle\" height=\"80\" />\n" +
            "</td>\n" +
            "<td style=\"vertical-align:middle;\">\n" +
            "<p style=\"font-family:Arial;\">Your next garden waste collection is:</p>\n" +
            "<p style=\"font-family:Arial;\">Wednesday 12 October 2016</p>\n" +
            "<a href=\"http://www.waverley.gov.uk/gardenwastecalendar1\">Click to view your collection calendar</a>\n" +
            "</td>\n" +
            "</tr>\n" +
            "</tbody>\n" +
            "</table>\n" +
            "</li>\n" +
            "</ul>\n" +
            "</div>\n" +
            "</div>\n" +
            "<br />\n" +
            "<a href=\"#\" id=\"address2\" style=\"text-decoration:none; font-family:Arial;\">11A West Avenue, Farnham, Surrey, GU9 0RH</a>\n" +
            "<div id=\"address2div\" title=\"Your Collection Details\" style=\"display:none;\">\n" +
            "<div>\n" +
            "<ul style=\"list-style: none;\">\n" +
            "<li>\n" +
            "<table>\n" +
            "<tbody>\n" +
            "<tr align=\"justify\">\n" +
            "<td>\n" +
            "<img src=\"http://www.waverley.gov.uk/images/blackbin.jpg\" alt=\"bin\" align=\"middle\" height=\"80\" />\n" +
            "</td>\n" +
            "<td style=\"vertical-align:middle;\">\n" +
            "<p style=\"font-family:Arial;\">Your next rubbish and food waste collection is:</p>\n" +
            "<p style=\"font-family:Arial;\">Monday 3 October 2016</p>\n" +
            "<a href=\"http://www.waverley.gov.uk/downloads/file/5135/rubbish_and_recycling_calendar_2016_-_calendar_b\">Click to view your collection calendar</a>\n" +
            "</td>\n" +
            "</tr>\n" +
            "</tbody>\n" +
            "</table>\n" +
            "</li>\n" +
            "</ul>\n" +
            "</div>\n" +
            "<div>\n" +
            "<ul style=\"list-style: none;\">\n" +
            "<li>\n" +
            "<table>\n" +
            "<tbody>\n" +
            "<tr align=\"justify\">\n" +
            "<td>\n" +
            "<img src=\"http://www.waverley.gov.uk/images/bluebin.jpg\" alt=\"bin\" align=\"middle\" height=\"80\" />\n" +
            "</td>\n" +
            "<td style=\"vertical-align:middle;\">\n" +
            "<p style=\"font-family:Arial;\">Your next recycling and food waste collection is:</p>\n" +
            "<p style=\"font-family:Arial;\">Monday 10 October 2016</p>\n" +
            "<a href=\"http://www.waverley.gov.uk/downloads/file/5135/rubbish_and_recycling_calendar_2016_-_calendar_b\">Click to view your collection calendar</a>\n" +
            "</td>\n" +
            "</tr>\n" +
            "</tbody>\n" +
            "</table>\n" +
            "</li>\n" +
            "</ul>\n" +
            "</div>\n" +
            "</div>";
}
