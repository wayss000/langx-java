package com.jn.langx.management.connector.local.jndi;

import com.jn.langx.management.Configuration;
import com.jn.langx.management.JMXConnection;
import com.jn.langx.management.JMXConnectionFactory;
import com.jn.langx.management.connector.local.JMXLocalConnection;

import javax.management.MBeanServer;
import javax.naming.InitialContext;

public class JndiLocalConnectionFactory implements JMXConnectionFactory {
    private static final String DEFAULT_CONTEXT_FACTORY = "com.sun.InitialContextFactroy";

    @Override
    public JMXConnection getConnection(final Configuration config) {
        final JndiConfiguration jndiConfig = (JndiConfiguration) config;
        final String jndi = jndiConfig.getServerJndi();
        MBeanServer server = null;
        try {
            String jndiFactroyClass = jndiConfig.getJndiFactoryClass();
            if (jndiFactroyClass == null || jndiFactroyClass.isEmpty()) {
                jndiFactroyClass = "com.sun.InitialContextFactroy";
                jndiConfig.setProperty("java.naming.factory.initial", jndiFactroyClass);
            }
            final InitialContext context = new InitialContext(jndiConfig.getProperties());
            server = (MBeanServer) context.lookup(jndi);
        } catch (Throwable t) {
        }
        if (server != null) {
            return new JMXLocalConnection(server);
        }
        return null;
    }
}