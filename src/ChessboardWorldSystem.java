import java.util.ArrayList;

public class ChessboardWorldSystem {
    private final Object[][] board;
    private final int length;
    private final int width;
    private int second;
    private final ArrayList<Dandelifeon> Dandelifeons;

    public ChessboardWorldSystem(int length, int width) {
        this.length = length;
        this.width = width;
        board = new Object[this.length][this.width];
        this.second = 0;
        Dandelifeons = new ArrayList<>();
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public void setCellBlock(int x, int y, int age) {
        this.board[x][y] = new CellBlock(age);//放置一个年龄为0的存活细胞
    }

    public void setDandelifeon(int x, int y) {
        Dandelifeon danDelifeon = new Dandelifeon(x,y, this);
        this.board[x][y] = danDelifeon; // 在指定位置放置启命英
        Dandelifeons.add(danDelifeon); //将放置的启命英添加到集合中
    }

    public void replaceCellBlock(int x, int y){
        board[x][y]=null; //删除细胞方块
    }

    //得到棋盘中的某一格
    public Object getLattice(int x, int y){
        return this.board[x][y];
    }

    //设置棋盘中的某一格
    public void setLattice(int x, int y, Object object){
        this.board[x][y] = object;
    }

    //得到DanDelifeons集合中的元素个数
    public int getDandelifeonsSize(){
        return Dandelifeons.size();
    }

    public Object[][] getBoard() {
        return this.board;
    }

    public CellBlock getCellBlock(int x, int y) {

        return (CellBlock) this.board[x][y];
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    //判断两个启命英的工作范围是否存在重叠
    public void ifOverlaps(){
        boolean overlaps = false;
        for (int i = 0; i < Dandelifeons.size() - 1; i++) {
            for (int j = i + 1; j < Dandelifeons.size(); j++) {
                if (!(Math.abs(Dandelifeons.get(i).getDan_x() - Dandelifeons.get(j).getDan_x()) >= 25 || Math.abs(Dandelifeons.get(i).getDan_y() - Dandelifeons.get(j).getDan_y()) >= 25)) {
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
    public void ifDandelifeonsOutOfBounds(){
        boolean flag = false;
        for (Dandelifeon danDelifeon : Dandelifeons) {
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
        for (Dandelifeon danDelifeon : Dandelifeons) {
            if (x == danDelifeon.getDan_x() && y == danDelifeon.getDan_y()) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public ArrayList<Dandelifeon> getDandelifeons() {
        return this.Dandelifeons;
    }

    public void printBoard(){
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[0].length; j++) {
                if (this.board[i][j] instanceof CellBlock) {
                    System.out.printf("%-5s", ((CellBlock) this.board[i][j]).getAge());
                }else if (this.board[i][j] instanceof Dandelifeon) {
                    System.out.printf("%-5s", "Dan");
                }else{
                    System.out.printf("%-5s", "[]");
                }
            }
            System.out.println();
        }
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.print("\n\n");
    }

    public boolean stepOneSecond(){
        boolean isGameOver = false;
        //label记录判定4中已经判定过的Dandelifeon
        int label = 0;
        for (Dandelifeon danDelifeon : Dandelifeons) {
            boolean flag = danDelifeon.lifeGameCheck(label);
            //若isGameOver已经为true，则不再改变
            if (!isGameOver) {
                isGameOver = flag;
            }
            label++;
        }
        this.second++;
        printBoard();
        if (isGameOver) {
            this.second++;
        }
        return isGameOver;
    }
}

