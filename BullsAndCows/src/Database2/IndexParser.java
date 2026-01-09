package Database2;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static Database2.Models.Book;

public final class IndexParser {
    // 复用你 SetUpDB 的限制：只认小写后缀
    private static final String[] ALLOWED = {"pdf", "mobi", "epub", "azw3", "html", "txt"};

    private IndexParser() {}

    private static boolean endsWithAllowedLowercaseExt(String filename) {
        for (String k : ALLOWED) {
            if (filename.endsWith("." + k)) return true;
        }
        return false;
    }

    private static String getKind(String filename) {
        int i = filename.lastIndexOf('.');
        return (i >= 0 && i < filename.length() - 1) ? filename.substring(i + 1) : "";
    }

    private static List<Book> parseIndex(Path indexPath, Charset cs) throws IOException {
        List<Book> books = new ArrayList<>(20000);
        String currentDir = "";

        try (BufferedReader br = Files.newBufferedReader(indexPath, cs)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // 目录行：./xxx:
                if (line.startsWith("./") && line.endsWith(":")) {
                    currentDir = line.substring(0, line.length() - 1);
                    continue;
                }

                if (!endsWithAllowedLowercaseExt(line)) continue;

                String kind = getKind(line);
                String fullName;

                if (line.startsWith("./")) {
                    fullName = line;
                } else if (!currentDir.isEmpty()) {
                    fullName = currentDir + "/" + line;
                } else {
                    fullName = line;
                }

                books.add(new Book(fullName, kind));
            }
        }
        return books;
    }

    public static List<Book> parseIndexWithFallback(Path indexPath) throws IOException {
        try {
            return parseIndex(indexPath, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            return parseIndex(indexPath, Charset.forName("GBK"));
        }
    }
}
