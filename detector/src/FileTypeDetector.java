import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileTypeDetector {
    private static Map<String, byte[]> types; /*
    первые несколько байт файла называются "магическими числами"
    они обозначают тип содержимого
    Для определения байтов берём несколько файлов одного типа, открываем в HEX-редакторе
    (в моём случае Be.HexEditor) и смотрим первые 3-4 байта каждого файла
    Они будут одинаковыми во всех файлах - это и есть магические числа
    */
    static {
        types = new HashMap<>();
        types.put("mp3", new byte[]{0x49, 0x44, 0x33});
        types.put("png", new byte[]{(byte) 0x89, 0x50, 0x4E});
        types.put("jpg", new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});
        types.put("gif", new byte[]{0x47, 0x49, 0x46});
        types.put("docx", new byte[]{0x50, 0x4B, 0x03});
        types.put("drawio", new byte[]{0x3c, 0x6d, 0x78});
        types.put("html", new byte[]{0x3c, 0x21, 0x44});
    }

    public static String detectFileType(String filePath) {
        /*
        Открываем файл и считываем первые 3 байта
         */
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            byte[] header = new byte[3];
            int bytesRead = inputStream.read(header);
            /*
            Сопоставляем байты с массивами байтов из наших заготовок (types) с помощью метода compareBytes()
            Если метод вернёт true, то программа вернёт строку с названием типа файла
            Если программа не смогла определить тип, вернётся "Тип не известен :("
             */
            if (bytesRead >= 3) {
                for (Map.Entry<String, byte[]> entry : types.entrySet()) {
                    if (compareBytes(header, entry.getValue())) {
                        return entry.getKey();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Тип не известен :(";
    }
    private static boolean compareBytes(byte[] bytes1, byte[] bytes2) {
        if (bytes1.length != bytes2.length) { /* если длина прочитанных из файла байтов не совпадёт с длиной
        массива байтов в types, вернётся false
        */
            return false;
        }
        for (int i = 0; i < bytes1.length; i++) { // если хоть какой-то байт не совпадает, вернётся false
            if (bytes1[i] != bytes2[i]) {
                return false;
            }
        }
        return true; // если всё хорошо, вернётся true
    }

    public static void main(String[] args) {
        System.out.println("Тип файла project.drawio: " + detectFileType("project.drawio"));
        System.out.println("Тип файла diagram.docx: " + detectFileType("diagram.docx"));
    }
}