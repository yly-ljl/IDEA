import java.util.ArrayList;
import java.util.Random;
public class ChessboardWorldSystem3 {
    private final Object[][] board;
    private Object[][] copyBoard;
    private final int length;
    private final int width;
    private int second;
    private final ArrayList<DanDelifeon3> DanDelifeons;

    public ChessboardWorldSystem3(int length, int width) {
        this.length = length;
        this.width = width;
        board = new Object[this.length][this.width];
        //设置一个复制棋盘，帮助达到每次判定所有启命英同时进行的效果
        copyBoard = new Object[this.length][this.width];
        this.second = 0;
        DanDelifeons = new ArrayList<>();
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public void setCellBlock(int x, int y, int age, Object[][] board) {
        board[x][y] = new CellBlock3(age);//放置一个年龄为0的存活细胞
    }

    public void setDandelifeon(int x, int y, ChessboardWorldSystem3 chessboard) {
        DanDelifeon3 danDelifeon = new DanDelifeon3(x,y, chessboard);
        board[x][y] = danDelifeon; // 在指定位置放置启命英
        DanDelifeons.add(danDelifeon); //将放置的启命英添加到集合中
    }

    public void replaceCellBlock(int x, int y, Object[][] board){
        board[x][y]=null; //删除细胞方块
    }

    //得到棋盘中的某一格
    public Object getLattice(int x, int y, Object[][] board){
        return board[x][y];
    }

    //得到DanDelifeons集合中的元素个数
    public int getDanDelifeonsSize(){
        return DanDelifeons.size();
    }

    public Object[][] getBoard() {
        return this.board;
    }

    public CellBlock3 getCellBlock(int x, int y, Object[][] board) {

        return (CellBlock3) board[x][y];
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public Object[][] getCopyBoard() {
        return copyBoard;
    }

    public void setCopyBoard(Object[][] copyBoard) {
        this.copyBoard = copyBoard;
    }

    //判断两个启命英的工作范围是否存在重叠
    public void ifOverlaps(){
        boolean overlaps = false;
        for (int i = 0; i < DanDelifeons.size() - 1; i++) {
            for (int j = i + 1; j < DanDelifeons.size(); j++) {
                if (!(Math.abs(DanDelifeons.get(i).getDan_x() - DanDelifeons.get(j).getDan_x()) >= 25 || Math.abs(DanDelifeons.get(i).getDan_y() - DanDelifeons.get(j).getDan_y()) >= 25)) {
                    overlaps = true;
                    break;
                }
            }
            if (overlaps) {
            break;
            }
        }
        if (overlaps) {
            throw new RuntimeException("DanDelifeons cannot overlap!");
        }
    }

    //判断启命英的工作范围是否会超出边界
    public void ifDanDelifeonsOutOfBounds(){
        boolean flag = false;
        for (DanDelifeon3 danDelifeon : DanDelifeons) {
            if (danDelifeon.getDan_x() + 12 > length || danDelifeon.getDan_y() + 12 > width || danDelifeon.getDan_x() - 12 < 0 || danDelifeon.getDan_y() - 12 < 0) {
                flag = true;
                break;
            }
        }
        if (flag) {
            throw new ArrayIndexOutOfBoundsException("The range of DanDelifeon is out Of bounds!");
        }
    }

    //判断随机添加细胞时是否将细胞添加到启命英的位置
    public boolean ifCellAndDanDelifeonsOverlaps(int x, int y){
        boolean flag = false;
        for (DanDelifeon3 danDelifeon : DanDelifeons) {
            if (x == danDelifeon.getDan_x() && y == danDelifeon.getDan_y()) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public void stepOneSecond(int numOfGames) {
        //随机放置细胞方块
        randomSetCellBlock();

        //记录游戏进行的次数
        int gameNums = 0;
        //记录每局游戏进行的判定轮数,对第一次判定的特殊情况做处理
        int cnt_label = 1;
        int sumOfMana = 0;
        while (gameNums <= numOfGames) {
            copy();
            int cnt = cnt_label;
            boolean isGameOver = false;
            //将DanDelifeon集合中的所有元素循环进行判定
            for (DanDelifeon3 danDelifeon : DanDelifeons) {
                danDelifeon.lifeGameCheck1(danDelifeon.getDan_x(), danDelifeon.getDan_y());
            }
            this.second++;
            //将原棋盘复制给复制棋盘，开始下一轮的判定
            copy();
            for (DanDelifeon3 danDelifeon : DanDelifeons) {
                danDelifeon.lifeGameCheck2(danDelifeon.getDan_x(), danDelifeon.getDan_y());
            }
            this.second++;
            copy();
            for (DanDelifeon3 danDelifeon : DanDelifeons) {
                danDelifeon.lifeGameCheck3(danDelifeon.getDan_x(), danDelifeon.getDan_y());
            }
            this.second++;
            copy();
            int label = 0;
            for (int i = 0; i < DanDelifeons.size(); i++, label++) {
                DanDelifeon3 danDelifeon = DanDelifeons.get(i);
                boolean flag = danDelifeon.lifeGameCheck4(danDelifeon.getDan_x(), danDelifeon.getDan_y(), cnt, label);

                //一旦IsGameOver变为true,那么它就不再改变
                //直到label == DanDelifeons.size()-1,即对最后一个启命英进行判定后，再将游戏范围内所有细胞变为死亡细胞
                if (!isGameOver){
                    isGameOver = flag;
                }
            }
            this.second++;

            if (isGameOver){
                this.second++;
                //如果一局游戏结束了，将cnt_label变为1来记录下一局游戏判定的轮数
                cnt_label = 1;
                gameNums++;

                randomSetCellBlock();
            }else {
                //若游戏未结束，则判定轮数加1
                cnt_label++;
            }

        }
        for (DanDelifeon3 danDelifeon : DanDelifeons) {
            sumOfMana += danDelifeon.getAccumulatedMana();
        }
        System.out.println("总产魔量:" + sumOfMana);
        System.out.println("总用时:" + this.second);
    }

    //将原棋盘复制给复制棋盘
    private void copy(){
        for (int i = 0; i < this.length; i++) {
            if (this.width >= 0) System.arraycopy(this.board[i], 0, this.copyBoard[i], 0, this.width);
        }
    }

    //将复制棋盘中的某一格与原棋盘中对应的格子交换
    public void changeOneCellBlock(int x, int y){
        Object obj = this.copyBoard[x][y];
        this.copyBoard[x][y] = this.board[x][y];
        this.board[x][y] = obj;
    }

    private void randomSetCellBlock(){
        Random rand = new Random();
        for (int i = 0; i < this.length; i++) {
            for (int j = 0; j < this.width; j++) {
                if (!ifCellAndDanDelifeonsOverlaps(i, j) && rand.nextBoolean()) {
                    this.setCellBlock(i,j, 0, getBoard());
                }
            }
        }
    }

}

