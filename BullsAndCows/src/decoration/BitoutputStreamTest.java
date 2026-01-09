package decoration;

import java.io.*;

/**
 * @description 比特输出流
 * @author TLE-11
 * @create 2025/12/9
 **/
public class BitoutputStreamTest {
    public static void main(String[] args) throws IOException {
        File temp = File.createTempFile("test", ".bin");
        temp.deleteOnExit();

        BitOutputStream bos = new BitOutputStream(temp);

        for (int i = 0; i < 10; i++) {
            bos.writeBit('0');
            bos.writeBit('1');
        }

        bos.writeBit("01010101010101011");
        bos.close();   // 自动补 0 输出剩余比特

        // UUUUX
        try (FileInputStream in = new FileInputStream(temp)) {
            byte[] data = new byte[5];
            int len = in.read(data);
            System.out.println(new String(data, 0, len));
        }
    }
}