package dev.voltstro.assessment2;

import org.springframework.util.StringUtils;

public class Utils {
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }
}
