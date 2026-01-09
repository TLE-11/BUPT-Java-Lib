package CountKeyWords;

import java.io.File;
import java.util.*;

public class CountKeywords {
    public static void main(String[] args) throws Exception {
        // 实际文件绝对路径
        String filename = "/Users/Apple/IdeaProjects/Java_Class/2025.9.23/BullsAndCows/src/CountKeyWords/Ch21Q03_TestFile.java";
        File file = new File(filename);

        if (file.exists()) {
            countKeywords(file);
        } else {
            System.out.println("File " + filename + " does not exist");
        }
    }

    public static void countKeywords(File file) throws Exception {
        // 关键字数组
        String[] keywordString = {
                "abstract", "assert", "boolean", "break",
                "byte", "case", "catch", "char", "class",
                "const", "extends", "for", "final", "finally",
                "float", "goto", "if", "implements", "import",
                "instanceof", "int", "interface", "long",
                "native", "new", "package", "private", "protected",
                "public", "return", "short", "static", "strictfp",
                "super", "switch", "synchronized", "this", "throw",
                "throws", "transient", "try", "void", "volatile",
                "while", "true", "false", "null"
        };

        Set<String> keywordSet = new HashSet<>(Arrays.asList(keywordString));

        // 使用 TreeMap 按字母顺序存储关键字及其出现次数
        Map<String, Integer> keywordMap = new TreeMap<>();

        // 读取整个文件内容
        Scanner input = new Scanner(file);
        StringBuilder fileContent = new StringBuilder();
        while(input.hasNextLine()){
            fileContent.append(input.nextLine()).append("\n");
        }
        input.close();

        String text = fileContent.toString();

        // 过滤注释和字符串的解析逻辑
        StringBuilder validCode = new StringBuilder();
        boolean inString = false;       // 是否在字符串中
        boolean inLineComment = false;  // 是否在单行注释中
        boolean inBlockComment = false; // 是否在多行注释中

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // 向前查看一个字符以检测注释符号
            char next = (i < text.length() - 1) ? text.charAt(i + 1) : '\0';

            // 处理跳过状态 (如果在注释或字符串内，忽略当前字符)
            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false; // 单行注释在换行时结束
                }
                continue; // 跳过此字符
            }
            if (inBlockComment) {
                if (c == '*' && next == '/') {
                    inBlockComment = false; // 多行注释在 */ 处结束
                    i++; // 消耗掉 '/'
                }
                continue; // 跳过此字符
            }
            if (inString) {
                if (c == '\\') {
                    i++; // 跳过字符串内的转义字符
                } else if (c == '"') {
                    inString = false; // 字符串在遇到下一个双引号时结束
                }
                continue; // 跳过此字符
            }

            // 检查状态的开始 (是否开始进入注释或字符串)
            if (c == '"') {
                inString = true;
                continue;
            }
            if (c == '/' && next == '/') {
                inLineComment = true;
                i ++; // 消耗掉第二个 '/'
                continue;
            }
            if (c == '/' && next == '*') {
                inBlockComment = true;
                i ++; // 消耗掉 '*'
                continue;
            }

            // 累积有效代码
            // 将非标识符字符（如 '(', ';', '+'）替换为空格
            // 这样 Scanner 就可以区分粘连的单词，例如把 "for(int" 拆分为 "for int"
            if (Character.isJavaIdentifierPart(c)) {
                validCode.append(c);
            } else {
                validCode.append(" ");
            }
        }

        // 统计关键字
        Scanner tokenizer = new Scanner(validCode.toString());
        int totalKeywords = 0;

        while (tokenizer.hasNext()) {
            String word = tokenizer.next();
            if (keywordSet.contains(word)) {
                keywordMap.put(word, keywordMap.getOrDefault(word, 0) + 1);
                totalKeywords++;
            }
        }
        tokenizer.close();

        // 输出结果
        for (Map.Entry<String, Integer> entry : keywordMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("\n全部关键字应为: " + totalKeywords + " 个");
    }
}