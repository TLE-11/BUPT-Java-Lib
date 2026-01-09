public class ChainClass {
    public static void main(String[] args) {
        int[][] nums = new int[9][10];
        int[][] start = new int[][] {{0, 0}};
        int[][] end = new int[][] {{1, 2}};
        int[][] directions = new int[][] {{1, 2}, {2, 1}, {-1, 2}, {-2, 1}, {-1, -2}, {-2, -1}, {1, -2}, {2, -1}};

        int ans = 0;
        ans = bfs(nums, start, end, directions);

        System.out.println(ans);
    }

    public static int bfs(int[][] nums, int[][] start, int[][] end, int[][] directions) {
        int ans = 0;


        return ans;
    }
}
