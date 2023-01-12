package sample;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

/**
 * This class creates a random quote using quotable.io external api
 * @author Kavishka Timashan
 */
public class Quote{
    String urlString="http://quotable.io/random";
    static String quote;

    /**
     * Constructor
     */
    public Quote() {
        try {
            JSONObject json = new JSONObject(IOUtils.toString(new URL(urlString), Charset.forName("UTF-8")));
            String content = json.getString("content");
            String author = json.getString("author");
            quote = content + "\n" + " - " + author + " - ";

        }catch (UnknownHostException e){
            quote="Not connected";
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * returns the quote as a string
     * @return quote
     */
    @Override
    public String toString() {
        return quote;
    }
}
