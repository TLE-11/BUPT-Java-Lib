package Database2;

public final class Models {
    private Models() {}

    public static class Book {
        public int id;
        public String name;
        public String kind;

        public Book() {}
        public Book(int id, String name, String kind) {
            this.id = id;
            this.name = name;
            this.kind = kind;
        }
        public Book(String name, String kind) {
            this.name = name;
            this.kind = kind;
        }
    }

    public static class LogEntry {
        public int id;
        public String time;
        public String action;
        public String detail;

        public LogEntry(int id, String time, String action, String detail) {
            this.id = id;
            this.time = time;
            this.action = action;
            this.detail = detail;
        }
    }
}
