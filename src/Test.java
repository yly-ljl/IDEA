import java.util.Scanner;
import java.util.Random;
public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //手动设置初始参数
//        System.out.println("请输入棋盘的大小:");
//        System.out.print("length:");
//        int length = sc.nextInt();
//        System.out.print("width:");
//        int width = sc.nextInt();
//
//        ChessboardWorldSystem chessboardWorldSystem = new ChessboardWorldSystem(length, width);
//
//        System.out.println("请输入您要放置的启命英的个数:");
//        int T = sc.nextInt();
//        for (int i = 0; i < T; i++) {
//
//            System.out.println("请设置第" + (i + 1) + "个启命英的位置");
//            System.out.print("X:");
//            int Dan_X = sc.nextInt();
//            System.out.print("Y:");
//            int Dan_Y = sc.nextInt();
//
//            chessboardWorldSystem.setDandelifeon(Dan_X,Dan_Y);
//        }
//
//        System.out.println("请输入要进行的游戏次数:");
//        int gamesNums = sc.nextInt();
//
//        自动设置初始参数
        ChessboardWorldSystem chessboardWorldSystem = new ChessboardWorldSystem(30,30);
        chessboardWorldSystem.setDandelifeon(13,13);
        int gamesNums = 5;

        try {
            chessboardWorldSystem.ifDandelifeonsOutOfBounds();
        }catch (Exception e){
            System.out.println("异常信息:" + e.getMessage());
            return;
        }

        try {
            chessboardWorldSystem.ifOverlaps();
        }catch (Exception e){
            System.out.println("异常信息:" + e.getMessage());
            return;
        }


        int gameCount = 0;//记录游戏实际进行的次数
        int cnt = 0;//记录一局游戏进行的轮数
        randomSetCellBlock(chessboardWorldSystem);
        System.out.println("----------------第" + 1 + "局游戏:-----------------");
        while (gameCount < gamesNums) {
            if (chessboardWorldSystem.stepOneSecond()){
                gameCount++;
                randomSetCellBlock(chessboardWorldSystem);
                if (gameCount < gamesNums) {
                    System.out.println("-------------------第" + (gameCount + 1) + "局游戏:--------------------");
                }
            }
        }
        int sumOfMana = 0;
        int sumOfSecond = chessboardWorldSystem.getSecond();
        for (int i = 0; i < chessboardWorldSystem.getDandelifeonsSize(); i++){
            sumOfMana += chessboardWorldSystem.getDandelifeons().get(i).getAccumulatedMana();
        }

        System.out.println("Game Over!");
        System.out.println("总产魔量:" + sumOfMana);
        System.out.println("总用时:" + sumOfSecond);
    }
    //随机放置细胞
    public static void randomSetCellBlock(ChessboardWorldSystem chessboardWorldSystem){
        Random rand = new Random();
        for (int i = 0; i < chessboardWorldSystem.getLength(); i++) {
            for (int j = 0; j < chessboardWorldSystem.getWidth(); j++) {
                if (!chessboardWorldSystem.ifCellAndDanDelifeonsOverlaps(i, j) && rand.nextBoolean()) {
                    chessboardWorldSystem.setCellBlock(i,j, 0);
                }
            }
        }
        //对workPlace要进行第一次初始化
        for (int i = 0; i < chessboardWorldSystem.getDandelifeonsSize(); i++){
            chessboardWorldSystem.getDandelifeons().get(i).copy();
        }
    }
}