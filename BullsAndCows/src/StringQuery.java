import java.util.HashSet;
import java.util.Set;

public class StringQuery {
    public static void main(String[] args) {
        String S = "abcabcbb";
        int ans = SelectString(S);
        System.out.println(ans);
    }

    public static int SelectString(String S) {
        Set<Character> occ = new HashSet<>();
        int ans = 0, r = -1;

        for (int i = 0; i < S.length(); ++ i) {
            if (i > 0) {
                occ.remove(S.charAt(i - 1));
            }
            while (r < S.length() - 1 && !occ.contains(S.charAt(r + 1))) {
                occ.add(S.charAt(r + 1));
                r ++;
            }
            ans = Math.max(ans, r - i + 1);
        }

        return ans;
    }
}
