package eu.cloudtm;

import org.omg.PortableInterceptor.SUCCESSFUL;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

/**
 * // TODO: Document this
 *
 * @author Pedro Ruivo
 * @since 4.0
 */
public class StopJmxRequest {

    private String host;
    private String port;
    private String component;

    private static final String COMPONENT_PREFIX = "org.radargun:stage=";

    public enum Result {
        SUCCESS("Success"), ERROR("Error");


        private final String text;

        private Result(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private static ObjectName benchmarkComponent;
    private static MBeanServerConnection mBeanServerConnection;


    public static void doRequest(SlaveDTO slave) throws IOException, MalformedObjectNameException, MBeanException, InstanceNotFoundException, ReflectionException {

        String connectionUrl = "service:jmx:rmi:///jndi/rmi://" +
                slave.getHost() + ":" + slave.getPort() + "/jmxrmi";

        JMXConnector connector = JMXConnectorFactory.connect(new JMXServiceURL(connectionUrl));
        mBeanServerConnection = connector.getMBeanServerConnection();
        benchmarkComponent = new ObjectName(COMPONENT_PREFIX + slave.getComponent());


        if (benchmarkComponent == null) {
            throw new NullPointerException("Component does not exists");
        }

        mBeanServerConnection.invoke(benchmarkComponent, "stopBenchmark", new Object[0], new String[0]);
    }

}
