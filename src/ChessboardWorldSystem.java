public class ChessboardWorldSystem {
    private CellBlock[][] board;
    private int length = 25;
    private int width = 25;
    private int second;

    public ChessboardWorldSystem() {
        board = new CellBlock[length][width];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = new CellBlock(0, false); // 初始化每个 CellBlock为年龄为0的死亡细胞
            }
        }
        this.second = 0;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public void SetCellBlock(int x, int y){
        board[x][y].setAlive(true);
        board[x][y].setAge(0);
    }

    public void ReplaceCellBlock(int x, int y){
        board[x][y].setAlive(false);
        board[x][y].setAge(0);
    }

    public CellBlock getCellBlock(int x, int y) {
        return board[x][y];
    }

    public void stepOneSecond() {
        this.second++;
    }

}
