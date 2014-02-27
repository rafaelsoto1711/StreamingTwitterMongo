/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package recolecciontweetmongo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.kohsuke.args4j.ExampleMode.ALL;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import twitter4j.*;
import twitter4j.auth.AccessToken;

public class RecoleccionTweetMongo {

/*
    Felipe
    public final static String OAUTH_CONSUMER_KEY = "2J6YxWjj7zaVt979uoZtA";
    public final static String OAUTH_CONSUMER_SECRET = "8cIMS0nopUvQ8IVQZIUAx1SE2F56YoIC4PtcEDjn9E";
    public final static String OAUTH_ACCESS_TOKEN = "145084142-F54lBJdshyuLHf43ROpsUqzYt2NIbVqewjLqVdDu";
    public final static String OAUTH_ACCESS_TOKEN_SECRET = "XKCKw6YkZknPXR9A1PgjjiJzQf0MkWBIsz2pobN3VI";*/
    
    //Pablo
    /*public final static String OAUTH_CONSUMER_KEY = "6gFxe71PNhiOsuaUDPuXw";
    public final static String OAUTH_CONSUMER_SECRET = "GtSDKy0YwmkmdqTXorrXxIbWKkSQPPclT6OzNTIM7g";
    public final static String OAUTH_ACCESS_TOKEN = "759514-kNRMOWUrPhqPcFyLNhcUuQV6ICl4FkbYFUtcdusGlnJ";
    public final static String OAUTH_ACCESS_TOKEN_SECRET = "9yGlUT56DQEfKqS9z5M3l9SafgH1RURz815t10DMvMEEc";*/
 
    public final static String OAUTH_CONSUMER_KEY ="asd";
    public final static String OAUTH_CONSUMER_SECRET = "asd";
    public final static String OAUTH_ACCESS_TOKEN = "asd-asd";
    public final static String OAUTH_ACCESS_TOKEN_SECRET = "asd";
    
    @Option(name="-t",usage="Nombre tabla donde se guardan los tweets")
    private String NombreTabla;
    
    @Option(name="-p",usage="Lista de palabras separadas por ;")
    private String BolsaPalabras;  
    
    List<String> ListadePalabras = new ArrayList<String>();
        
    public static void main(String[] args) throws IOException, CmdLineException{
        
         new RecoleccionTweetMongo().doMain(args);      
                
    }
    
    public void doMain(String[] args) throws IOException, CmdLineException {
        
        CmdLineParser parser = new CmdLineParser(this);

        parser.setUsageWidth(80);
        parser.parseArgument(args);
              
        if(NombreTabla == null || BolsaPalabras == null ){
        
            System.out.println("ERROR - FALTAN ARGUMENTOS");
            System.out.println("Ejemplo:");
            System.out.println("java -jar RTCW.jar -t NombreTabla -p palabras,separadas,por,coma");
            System.exit(0);
            
        }
        
            while(BolsaPalabras.contains(",")){
                int comaFinal = BolsaPalabras.indexOf(",");
                String palabraTemporal = BolsaPalabras.substring(0, comaFinal);
                BolsaPalabras = BolsaPalabras.substring(comaFinal+1, BolsaPalabras.length());
                ListadePalabras.add(palabraTemporal);

            }
        
        ListadePalabras.add(BolsaPalabras);
            
        String[] keywords = new String[ ListadePalabras.size() ];
        ListadePalabras.toArray( keywords );    
        
        
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

        twitterStream.setOAuthConsumer(OAUTH_CONSUMER_KEY,
                        OAUTH_CONSUMER_SECRET);

        AccessToken accessToken = new AccessToken(OAUTH_ACCESS_TOKEN,
                        OAUTH_ACCESS_TOKEN_SECRET);

        twitterStream.setOAuthAccessToken(accessToken);
                
        
        FilterQuery fq = new FilterQuery();
        
        fq.track(keywords);
        
        double[][] bb = {{-57.891497,-81.174317}, {-17.834536,-67.311036 }};
        fq.locations(bb);
        
        MongoConnection mongo=new MongoConnection(NombreTabla);
        mongo.setupMongo();

        StatusListener listener = new EarthQuakeListener(mongo);
        
        twitterStream.addListener(listener);
        twitterStream.filter(fq);
       
        
    }
    
}
