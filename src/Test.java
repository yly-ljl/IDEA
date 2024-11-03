import java.util.Random;

public class Test {
    public static void main(String[] args) {
        int sumOfMana = 0;
        int sumOfSecond = 0;
        int cnt = 0;
        Random random = new Random();

        while (cnt < 10) {
            DanDelifeon danDelifeon = new DanDelifeon();
            System.out.println("第" + (cnt + 1) + "次游戏");

            // 随机初始化细胞
            for (int i = 0; i < 25; i++) {
                for (int j = 0; j < 25; j++) {
                    if (random.nextBoolean()) { // 随机决定是否激活细胞
                        danDelifeon.getBoard().SetCellBlock(i, j);
                    }
                }
            }

            int mana = danDelifeon.lifeGameCheck();
            sumOfMana += mana;
            sumOfSecond += danDelifeon.getBoard().getSecond();

            // 输出每次游戏的魔力和时间
            System.out.println("游戏结束，魔力:" + mana);
            System.out.println("经过时间:" + danDelifeon.getBoard().getSecond() + "秒");
            System.out.println("=================================");
            cnt++;
        }

        // 输出总魔力和总时间
        System.out.println();
        System.out.println("总产魔量:" + sumOfMana);
        System.out.println("总用时:" + sumOfSecond + "秒");
    }
}
