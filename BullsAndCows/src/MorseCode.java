import java.io.File;
import java.io.FileNotFoundException; 
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @description 摩尔斯电码转换器
 * @author TLE-11
 * @create 2025/11/4
 **/
public class MorseCode {
    /*
     * 0 1 2 3 4
     * 5 6 7 8 9
     * A B C D E F
     * G H I J K L
     * M N O P Q R
     * S T U V W X
     * Y Z
     */
    private static final String[] symbols = new String[]{
            "-----", ".----", "..---", "...--", "....-",
            ".....", "-....", "--...", "---..", "----.",
            ".-", "-...", "-.-.", "-..", ".", "..-.",
            "--.", "....", "..", ".---", "-.-", ".-..",
            "--", "-.", "---", ".--.", "--.-", ".-.",
            "...", "-", "..-", "...-", ".--", "-..-",
            "-.--", "--.."};

    /* 快速查询表 */
    private static final Map<Character, String> CHAR_TO_MORSE = new HashMap<>();
    private static final Map<String, Character> MORSE_TO_CHAR = new HashMap<>();

    static {
        char[] chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        for (int i = 0; i < chars.length; i++) {
            CHAR_TO_MORSE.put(chars[i], symbols[i]);
            MORSE_TO_CHAR.put(symbols[i], chars[i]);
        }
    }

    /**
     * 编码函数，用于将文字转换为摩尔斯电码
     * @param input 输入文字
     * @return 生成的摩尔斯电码
     */
    public static String letter2morse(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        // 标记是否是第一个单词
        boolean firstWord = true;
        // 按空白切分单词
        for (String word : input.trim().toUpperCase().split("\\s+")) {
            if (!firstWord) {
                // 非首单词前加 7 个空格（单词间隔）
                sb.append("       ");
            }
            // 标记已处理过首单词
            firstWord = false;
            // 标记是否是该单词的第一个字母
            boolean firstLetter = true;
            // 遍历当前单词的每个字符
            for (char c : word.toCharArray()) {
                if (!firstLetter) {
                    // 非首字母前加 3 个空格（字母间隔）
                    sb.append("   ");
                }
                // 标记已处理过首字母
                firstLetter = false;
                // 查表得到摩尔斯符号，未知字符忽略
                sb.append(CHAR_TO_MORSE.getOrDefault(c, ""));
            }
        }
        // 返回最终拼好的摩尔斯字符串
        return sb.toString();
    }

    /**
     * 解码函数，用于将摩尔斯电码转换为文字
     * @param input 输入摩尔斯电码
     * @return 生成的文字
     */
    public static String morse2letter(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        // 按“单个空格”切分，连续空格会产生空串
        String[] tokens = input.split(" ");
        StringBuilder sb = new StringBuilder();
        // 连续空格计数
        int spaceCnt = 0;
        // 遍历切分后的每一段
        for (String tok : tokens) {
            // 空串代表遇到了连续空格
            if (tok.isEmpty()) {
                // 累计空格数量
                spaceCnt++;
            } else {
                /* 先把之前累计的空格还原成真实空格 */
                if (spaceCnt > 0) {
                    // 奇偶规则：n 个空格 → (n+1)/2 个真实空格
                    int realSpaces = (spaceCnt + 1) / 2;
                    for (int i = 0; i < realSpaces; i++) {
                        sb.append(' ');
                    }
                    // 空格数清零
                    spaceCnt = 0;
                }
                /* 解码当前 morse 单元 */
                // 查表得到对应字符
                Character ch = MORSE_TO_CHAR.get(tok);
                if (ch != null) {
                    sb.append(ch);
                }
            }
        }
        /* 处理行尾可能剩余的空格 */
        if (spaceCnt > 0) {
            int realSpaces = (spaceCnt + 1) / 2;
            for (int i = 0; i < realSpaces; i++) {
                sb.append(' ');
            }
        }
        // 去掉首尾多余空格
        return sb.toString().trim();
    }

    /**
     * 主函数
     * @param args 命令行参数
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File morse = new File("morse.txt");
        Scanner scannerMorse = new Scanner(morse);
        while (scannerMorse.hasNextLine()) {
            String line = scannerMorse.nextLine();
            System.out.println(morse2letter(line));
        }
        scannerMorse.close();

        File text = new File("text.txt");
        Scanner scannerText = new Scanner(text);
        while (scannerText.hasNextLine()) {
            String line = scannerText.nextLine();
            System.out.println(letter2morse(line));
        }
        scannerText.close();
    }
}
