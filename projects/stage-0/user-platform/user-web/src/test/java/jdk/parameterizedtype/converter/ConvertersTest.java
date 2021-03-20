package jdk.parameterizedtype.converter;


import org.geektimes.configuration.microprofile.config.converter.ByteConverter;
import org.geektimes.configuration.microprofile.config.converter.Converters;
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
        assertEquals(Byte.class, actual);
        System.out.println("============");
//        assertEquals(Short.class, converters.resolveConvertedType(new ShortConverter()));
//        assertEquals(Integer.class, converters.resolveConvertedType(new IntegerConverter()));
//        assertEquals(Long.class, converters.resolveConvertedType(new LongConverter()));
//        assertEquals(Float.class, converters.resolveConvertedType(new FloatConverter()));
//        assertEquals(Double.class, converters.resolveConvertedType(new DoubleConverter()));
//        assertEquals(String.class, converters.resolveConvertedType(new StringConverter()));
    }
}