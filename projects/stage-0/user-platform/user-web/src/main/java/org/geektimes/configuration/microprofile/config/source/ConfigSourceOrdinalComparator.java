package org.geektimes.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Comparator;

/**
 * @author GH
 */
public class ConfigSourceOrdinalComparator implements Comparator<ConfigSource> {

    /**
     * Singleton instance {@link ConfigSourceOrdinalComparator}
     */
    public static final Comparator<ConfigSource> INSTANCE = new ConfigSourceOrdinalComparator();

    private ConfigSourceOrdinalComparator() {
    }

    @Override
    public int compare(ConfigSource o1, ConfigSource o2) {
        return Integer.compare(o1.getOrdinal(), o2.getOrdinal());
    }
}
