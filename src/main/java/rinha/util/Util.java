package rinha.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {
    static public String getData() {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
        return agora.format(formatter);
    }
}
