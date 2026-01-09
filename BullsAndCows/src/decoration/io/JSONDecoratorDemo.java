package decoration.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JSONDecoratorDemo {
    public static void main(String[] args) throws IOException {
        File file = new File("./json.txt");
        file.createNewFile();
        OutputStream oStream = new FileOutputStream(file);
        JSONStream js = new JSONStream(oStream);
        String content = "I love Commodore 64";
        js.write(content.getBytes());
        js.close();
        oStream.close();
    }
}