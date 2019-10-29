package org.djw;

import org.djw.services.Services;
import org.djw.tools.utils.Banner;
import org.restlet.Component;
import org.restlet.data.*;
import org.apache.log4j.Logger;

public class Main{
    
    final static Logger logger = Logger.getLogger(Main.class);
	public static void main(String[] args) throws Exception {

        boolean canRun = checkParams();

        if (canRun){
            // Create a new Component.  
            Component component = new Component();

            // Add a new HTTP server listening on port 8080.
            component.getServers().add(Protocol.HTTP, 9090);  

            // Attach the sample application.  
            component.getDefaultHost().attach("/services",new Services());  

            // Start the component.  
            component.start();  
            // Pre cache data
            // logger.info("Caching data...");
            // PreCache preCache = new PreCache();
            // boolean cachesuccess = preCache.pinCache();
            // String cacheLoadMsg = "";
            // if (cachesuccess){
            //    cacheLoadMsg = "...data caching complete.";
            // } else {
            //     cacheLoadMsg = "...ERROR LOADING CACHE.";
            //     return;
            // }
            // logger.info(cacheLoadMsg);
            Banner banner = new Banner();
            String bannerText = banner.getBanner();
            logger.info(bannerText);
        } else {
            logger.fatal("Unable to start. Env variables not set");
        }
    } 
    
    public static boolean checkParams(){
        if (System.getenv("url") == null) return false;
        if (System.getenv("dbName") == null) return false;
        if (System.getenv("driver") == null) return false;
        if (System.getenv("userName") == null) return false;
        if (System.getenv("password") == null) return false;
        if (System.getenv("db_descriptor") == null) return false;

        logger.info("url: " + System.getenv("url"));
        logger.info("dbName: " + System.getenv("dbName"));
        logger.info("driver: " + System.getenv("driver"));
        logger.info("userName: " + System.getenv("userName"));
        logger.info("db_descriptor: " + System.getenv("db_descriptor"));

        return true;
    }
}
