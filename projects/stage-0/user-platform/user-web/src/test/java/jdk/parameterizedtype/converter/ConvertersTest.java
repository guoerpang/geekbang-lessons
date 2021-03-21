package jdk.parameterizedtype.converter;


import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.geektimes.configuration.microprofile.config.DefaultConfigProviderResolver;
import org.geektimes.configuration.microprofile.config.converter.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * {@link Converters} Test
 */
public class ConvertersTest {

    private Converters converters;

    @Before
    public void init() {
        converters = new Converters();
    }

    @Test
    public void testResolveConvertedType() {
        Class<?> actual = converters.resolveConvertedType(new ByteConverter());
        assertEquals(Short.class, converters.resolveConvertedType(new ShortConverter()));
        assertEquals(Integer.class, converters.resolveConvertedType(new IntegerConverter()));
        assertEquals(Long.class, converters.resolveConvertedType(new LongConverter()));
        assertEquals(Float.class, converters.resolveConvertedType(new FloatConverter()));
        assertEquals(Double.class, converters.resolveConvertedType(new DoubleConverter()));
        assertEquals(String.class, converters.resolveConvertedType(new StringConverter()));
    }


    @Test
    public void testDefaultConfig() {
        ConfigBuilder builder = new DefaultConfigProviderResolver().getBuilder();
        builder.addDefaultSources();
        builder.addDiscoveredConverters();
        Config build = builder.build();
        String javaVersion = build.getValue("java.version", String.class);
        System.out.println("java.version：" + javaVersion);

    }

}
