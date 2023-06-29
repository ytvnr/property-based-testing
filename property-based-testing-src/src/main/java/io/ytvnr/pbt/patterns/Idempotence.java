package io.ytvnr.pbt.patterns;

import java.util.List;

/**
 * @author Yann TAVERNIER (yann.tavernier at graviteesource.com)
 * @author GraviteeSource Team
 */
public class Idempotence {

    public static List<String> sort(List<String> strings) {
        return strings.stream().sorted().toList();
    }
}
