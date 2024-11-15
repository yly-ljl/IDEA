public class Dandelifeon {
    private final int Dan_x;
    private final int Dan_y;
    private final ChessboardWorldSystem chessboard;
    private int accumulatedMana;
    private Object[][] workPlace;

    public Dandelifeon(int Dan_x, int Dan_y, ChessboardWorldSystem chessboard) {
        this.Dan_x = Dan_x;
        this.Dan_y = Dan_y;
        this.chessboard = chessboard;
        this.accumulatedMana = 0;
        //Dandelifeon的工作范围是25*25
        this.workPlace = new Object[25][25];
    }

    public ChessboardWorldSystem getChessboard() {
        return chessboard;
    }

    public Object[][] getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(Object[][] workPlace) {
        this.workPlace = workPlace;
    }

    public int getDan_y() {
        return Dan_y;
    }

    public int getDan_x() {
        return Dan_x;
    }

    public int getAccumulatedMana(){
        return this.accumulatedMana;
    }

    public boolean lifeGameCheck(int label){
        boolean isGameOver = false;
        //根据本局游戏进行了多少秒来决定进行哪一个判定
        int whichJudge = (this.chessboard.getSecond() + 1) % 4;
        switch (whichJudge) {
            case 1,2,3:
                for (int dx = -12; dx <= 12; dx++) {
                    for (int dy = -12; dy <= 12; dy++) {
                        int count = countAliveNeighbors(this.Dan_x + dx, this.Dan_y + dy);
                        boolean flag = isCellBlock(this.Dan_x, this.Dan_y, dx, dy);
                        if (flag && whichJudge != 3) {
                            //生命过少或过剩的情况
                            if((count < 2 || count > 4) && whichJudge == 1){
                                this.workPlace[dx+12][dy+12] = null;
                            //细胞年龄加1的情况
                            } else if (count <= 3 && whichJudge == 2) {
                                CellBlock cell = (CellBlock) this.workPlace[dx+12][dy+12];
                                cell.ageIncrease();
                                this.workPlace[dx+12][dy+12] = cell;
                            }
                            //死细胞复生的情况
                        } else if (count == 3 && whichJudge == 3) {
                            CellBlock cell = new CellBlock(getMaxAgeOfNeighbors(this.Dan_x +dx, this.Dan_y +dy));
                            this.workPlace[dx+12][dy+12] = cell;
                        }
                        //交换来保持同时处理
                        changeOneCell(dx, dy);
                    }
                }
                copy();
                break;
            //判断是否结束游戏
            case 0:
                if (countAliveNeighbors(Dan_x, Dan_y) > 0) {
                    this.accumulatedMana += computeAccumulatedMana();
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (dx == 0 && dy == 0) {
                                continue;
                            }
                            chessboard.replaceCellBlock(Dan_x + dx, Dan_y + dy);
                        }
                    }
                }
                if (this.chessboard.getSecond() / 4 >= 1){
                    isGameOver = true;
                    this.accumulatedMana += computeAccumulatedMana();
                    //检查启命英集合中的所有启命英是否都经过了该判定4。防止先判定的启命英造成了replaceAllCells而使之后启命英的判定无效
                    if (label == chessboard.getDandelifeonsSize() - 1){
                        replaceAllCells();
                    }
                }
                break;
        }
        return isGameOver;
    }

    private boolean isCellBlock(int x, int y, int dx, int dy) {
        //检查该格是否是CellBlock,以及检查是否越界
        //此处判定同时给lifeGameCheck方法和private方法调用，故条件较多。
        return (x + dx >= 0) && (y + dy >= 0) && (x + dx < chessboard.getLength()) && (y + dy < chessboard.getWidth()) && chessboard.getLattice(x + dx, y + dy) instanceof CellBlock && (dx != 0 || dy != 0);
    }

    //将Dandelifeon的工作区域copy一份出来
    public void copy(){
        for (int dx = -12; dx <= 12; dx++) {
            for (int dy = -12; dy <= 12; dy++) {
                this.workPlace[dx+12][dy+12] = this.chessboard.getLattice(this.Dan_x + dx, this.Dan_y + dy);
            }
        }
    }

    private void changeOneCell(int dx, int dy){
        Object obj = this.workPlace[dx+12][dy+12];
        this.workPlace[dx+12][dy+12] = this.chessboard.getLattice(this.Dan_x+dx, this.Dan_y+dy);
        this.chessboard.setLattice(this.Dan_x+dx, this.Dan_y+dy, obj);
    }

    //计算周围存活的细胞数
    private int countAliveNeighbors(int x, int y){
        int count = 0;
        for (int dx = -1; dx <= 1; dx++){
            for (int dy = -1; dy <= 1; dy++){
                if (isCellBlock(x,y,dx,dy)){
                    count++;
                }
            }
        }
        return count;
    }

    //得到周围年龄最大的细胞的年龄
    private int getMaxAgeOfNeighbors(int x, int y){
        int max = 0;
        for (int dx = -1; dx <= 1; dx++){
            for (int dy = -1; dy <= 1; dy++){
                if (!isCellBlock(x,y,dx,dy)){
                    continue;
                }
                max = Math.max(max,chessboard.getCellBlock(x + dx,y + dy).getAge());
            }
        }
        return max;
    }

    //删除游戏范围内的所有细胞
    private void replaceAllCells(){
        for (int i = 0; i < chessboard.getLength(); i++){
            for (int j = 0; j < chessboard.getWidth(); j++){
                if (!(chessboard.getLattice(i,j) instanceof CellBlock)) {
                    continue;
                }
                chessboard.replaceCellBlock(i, j);
            }
        }
    }

    //计算周围存活细胞的总年龄
    private int getSumAgeOfNeighbors(){
        int sum = 0;
        for (int dx = -1; dx <= 1; dx++){
            for (int dy = -1; dy <= 1; dy++){
                if (!isCellBlock(this.Dan_x, this.Dan_y,dx,dy)){
                    continue;
                }
                sum += Math.min(chessboard.getCellBlock(this.Dan_x + dx, this.Dan_y + dy).getAge(), 100); //年龄大于100的细胞与年龄等于100的细胞产生的魔力量相同
            }
        }
        return sum;
    }

    //计算所产生的魔力量
    public int computeAccumulatedMana(){
        return getSumAgeOfNeighbors() * 60;
    }
}
