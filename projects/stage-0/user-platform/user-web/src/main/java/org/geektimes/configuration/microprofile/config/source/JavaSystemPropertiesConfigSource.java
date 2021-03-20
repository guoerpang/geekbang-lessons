package org.geektimes.configuration.microprofile.config.source;

import java.util.Map;

/**
 * @author GH
 */
public class JavaSystemPropertiesConfigSource extends MapBasedConfigSource {

    public JavaSystemPropertiesConfigSource(){
        super("Java System Properties",400);
    }

    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        configData.putAll(System.getProperties());
    }
}
