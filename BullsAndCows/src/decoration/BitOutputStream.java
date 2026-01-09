package decoration;

import java.io.*;

/**
 * @description 位输出流，用于将比特写入文件
 * @author TLE-11
 * @create 2025/12/9
 **/
public class BitOutputStream extends FilterOutputStream {
    private int buffer;
    private int bitCount;

    public BitOutputStream(File file) throws FileNotFoundException {
        super(new FileOutputStream(file));
        buffer = 0;
        bitCount = 0;
    }

    public void writeBit(char bit) throws IOException {
        if (bit != '0' && bit != '1') {
            throw new IllegalArgumentException();
        }
        buffer = (buffer << 1) | (bit - '0');
        if (++ bitCount == 8) {
            out.write(buffer);
            buffer = bitCount = 0;
        }
    }

    public void writeBit(String bits) throws IOException {
        for (int i = 0; i < bits.length(); i++) {
            writeBit(bits.charAt(i));
        }
    }

    /**
     * 重写write方法，支持写入字节
     */
    @Override
    public void write(int b) throws IOException {
        // 将字节拆分为8个比特写入
        for (int i = 7; i >= 0; i--) {
            writeBit(((b >> i) & 1) == 1 ? '1' : '0');
        }
    }

    /**
     * 重写write方法，支持写入字节数组
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            write(b[off + i] & 0xFF);
        }
    }

    /**
     * 将缓冲区的内容写入输出流（如果缓冲区非空）
     */
    @Override
    public void flush() throws IOException {
        if (bitCount > 0) {
            // 如果缓冲区未满，左移填充0
            buffer = buffer << (8 - bitCount);
            out.write(buffer);
            buffer = 0;
            bitCount = 0;
        }
        super.flush();
    }



    @Override
    public void close() throws IOException {
        if (bitCount > 0) {
            buffer <<= (8 - bitCount);
            out.write(buffer);
            buffer = bitCount = 0;
        }
        super.close();
    }

}