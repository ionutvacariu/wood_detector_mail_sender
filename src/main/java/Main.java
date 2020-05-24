import java.io.File;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    private final static String path_to_image = "/Users/ionutvacariu/PycharmProjects/woodDetectorPython/darknetW/detectedPlates/img/";

    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = localDateTime.toLocalDate();

        String s = "../darknetW/detectedPlates/img/detectedPlatecutout_wood_withTime1590352936.128745conf.jpg";

        String[] split = s.split("/");
        String s1 = split[split.length - 1];
        String[] split1 = s1.split("\\.");

        int lastDot = s1.lastIndexOf(".");

        String file_name = s1.substring(0, lastDot);
        String file_extension = s1.substring(lastDot, s1.length());

        String large = file_name.concat("_large");
        String large_file_name = large.concat(file_extension);


        //return path_to_image + "/" + large_file_name;
        File f = new File(path_to_image + "/" + large_file_name);


        System.out.println(f.exists());
    }
}
