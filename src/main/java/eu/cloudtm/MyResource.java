
package eu.cloudtm;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.*;

// The Java class will be hosted at the URI path "/myresource"
@Path("/stop")
public class MyResource {

    private final static String STOP_ACTION = "stop";

    private static Log log = LogFactory.getLog(MyResource.class);

    @GET @Path("/{ip}/{port}/{component}")
    @Produces("application/json")
    public String killSlave(
            @DefaultValue("-1") @PathParam("ip") String ip,
            @DefaultValue("-1") @PathParam("port") String port,
            @DefaultValue("-1") @PathParam("component") String component) {

        if( ip.equals("-1") || port.equals("-1") || component.equals("-1") )
            throw new WebApplicationException();

        log.info("Received: { " +
                ip + ", " +
                port + ", " +
                component +
                " }" );

        SlaveDTO slaveToKill = new SlaveDTO();
        slaveToKill.setHost(ip);
        slaveToKill.setPort(port);
        slaveToKill.setAction(STOP_ACTION);
        slaveToKill.setComponent(component);

        try {
            StopJmxRequest.doRequest(slaveToKill);
            slaveToKill.setResult(StopJmxRequest.Result.SUCCESS.toString());
        } catch (Exception e) {
            log.warn(e.getMessage());
            slaveToKill.setResult(StopJmxRequest.Result.ERROR.toString());
        }
        String ret = new Gson().toJson(slaveToKill);
        log.info("RETURN: " + ret);
        return ret;
    }
}
