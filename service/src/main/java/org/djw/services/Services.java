package org.djw.services;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class Services extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a new instance of HelloWorldResource.
        Router router = new Router(getContext());

        router.attach("/dbinfo", DBResource.class);
        router.attach("/dbinfo/{action}", DBResource.class);
        router.attach("/dbinfo/{action}/{country}", DBResource.class);
        // router.attach("/lookup", LookupResource.class);
        // router.attach("/lookup/{LookupType}", LookupResource.class);
        // router.attach("/lookup/{LookupType}/{Country}", LookupResource.class);
        router.attach("/test", TestResource.class);
        // router.attach("/recos", RecoResource.class);
        // router.attach("/recos/{Country}/{Year}", RecoResource.class);
        // router.attach("/util", UtilResource.class);
        // router.attach("/chart/{Country}", ChartResource.class);        
        return router;
    }

}
