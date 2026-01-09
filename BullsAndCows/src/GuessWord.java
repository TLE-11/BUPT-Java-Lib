import java.util.Scanner;
import java.util.Random;

public class GuessWord {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        String[] words = {"banana", "telecommunication", "university", "program", "beijing", "posts"};

        char choice;
        do {
            String targetWord = words[random.nextInt(words.length)];
            char[] guessChars = new char[targetWord.length()];
            //给猜测数组赋值
            for (int i = 0; i < targetWord.length(); ++ i) {
                guessChars[i] = '*';
            }
            int missCount = 0;
            while (true) {
                //打印相关信息
                System.out.print("(Guess Enter a letter in word: ");
                for (char c : guessChars) {
                    System.out.print(c);
                }
                System.out.print(" > ");
                //读取用户输入的猜测字母
                char guess = scanner.next().charAt(0);
                //检测字母是否在单词中
                boolean isHit = false;
                //检测字母是否已经出现过
                boolean isAppeared = false;
                for (int i = 0; i < targetWord.length(); ++ i) {
                    //判断字母是否出现过
                    if (guessChars[i] == guess) {
                        System.out.println(guess + " is already in the word");
                        isAppeared = true;
                        break;
                    }
                    //判断字母是否在单词中
                    if (targetWord.charAt(i) == guess) {
                        guessChars[i] = guess;
                        isHit = true;
                    }
                }
                //如果字母不在单词中
                if (!isHit && !isAppeared) {
                    missCount ++;
                    System.out.println(guess + " is not in the word.");
                } else {
                    //判断是否已经全猜中了
                    boolean isAllGuessed = true;
                    for (char c : guessChars) {
                        if (c == '*') {
                            isAllGuessed = false;
                            break;
                        }
                    }
                    //如果全猜中了
                    if (isAllGuessed) {
                        System.out.println("The word is " + targetWord + ". You missed " + missCount + " times" + (missCount != 1 ? "s" : ""));
                        break;
                    }
                }
            }
            //询问用户是否要玩下一局
            System.out.println("Do you want to play again? (y or n >)") ;
            choice = scanner.next().charAt(0);
        } while (choice == 'y' || choice == 'Y');
        scanner.close();
        System.out.println("Game Over!");
    }
}
