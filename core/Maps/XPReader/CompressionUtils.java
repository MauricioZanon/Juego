package XPReader;

/**
 * Created by bison on 02-01-2016.
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class CompressionUtils {

    public static byte[] gzipDecodeByteArray(byte[] data) {
        GZIPInputStream gzipInputStream = null;
        try {
            gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(data));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            byte[] buffer = new byte[1024];
            while (gzipInputStream.available() > 0) {
                int count = gzipInputStream.read(buffer, 0, 1024);
                if(count > 0) {
                    outputStream.write(buffer, 0, count);
                }
            }
            outputStream.close();
            gzipInputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
