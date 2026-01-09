package decoration.io;

import java.io.IOException;
import java.io.OutputStream;

public class JSONStream extends OutputStream {
    protected OutputStream outputStream;
    public JSONStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
    }
    @Override
    public void write(byte[] b) throws IOException {
        String content = new String(b);
        content = "{\r\n\tdata:\"" + content + "\"\r\n}";
        outputStream.write(content.getBytes());
    }
}
/*
{
    "xx": xxx,
    "sfjsklf": xxx
}

 */