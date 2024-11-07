import java.util.Scanner;
import java.util.Random;
public class Test3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();
        System.out.println("请输入棋盘的大小:");
        System.out.print("length:");
        int length = sc.nextInt();
        System.out.print("width:");
        int width = sc.nextInt();

        ChessboardWorldSystem3 chessboardWorldSystem = new ChessboardWorldSystem3(length, width);

        System.out.println("请输入您要放置的启命英的个数:");
        int T = sc.nextInt();
        for (int i = 0; i < T; i++) {

            System.out.println("请设置第" + (i + 1) + "个启命英的位置");
            System.out.print("X:");
            int Dan_X = sc.nextInt();
            System.out.print("Y:");
            int Dan_Y = sc.nextInt();

            chessboardWorldSystem.setDandelifeon(Dan_X,Dan_Y,chessboardWorldSystem);

        }

        try {
            chessboardWorldSystem.ifDanDelifeonsOutOfBounds();
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


        System.out.println("请输入要进行的游戏次数:");
        int GamesNums = sc.nextInt();

        chessboardWorldSystem.stepOneSecond(GamesNums);
    }
}