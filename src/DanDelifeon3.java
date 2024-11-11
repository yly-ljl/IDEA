public class DanDelifeon3 {
    private final int Dan_x;
    private final int Dan_y;
    private final ChessboardWorldSystem3 chessboard;
    private int accumulatedMana;

    public DanDelifeon3(int Dan_x, int Dan_y, ChessboardWorldSystem3 chessboard) {
        this.Dan_x = Dan_x;
        this.Dan_y = Dan_y;
        this.chessboard = chessboard;
        this.accumulatedMana = 0;
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

    public void lifeGameCheck1(int Dan_x, int Dan_y) {

        for (int dx = -12; dx <= 12 ; dx++){
            for (int dy = -12; dy <= 12; dy++) {
                boolean flag = isCellBlock3(Dan_x,Dan_y,dx,dy);
                if (flag) {
                    int count = countAliveNeighbors(Dan_x + dx,Dan_y + dy);
                    if (count < 2 || count > 4){
                        //使这一位置由细胞方块变成null，细胞死亡
                        chessboard.replaceCellBlock(Dan_x + dx,Dan_y + dy, chessboard.getCopyBoard());//生命过少或过剩的情况
                        chessboard.changeOneCellBlock(Dan_x + dx,Dan_y + dy);
                    }
                }

            }
        }
    }

    public void lifeGameCheck2(int Dan_x, int Dan_y) {
        for (int dx = -12; dx <= 12 ; dx++){
            for (int dy = -12; dy <= 12; dy++) {
                boolean flag = isCellBlock3(Dan_x,Dan_y,dx,dy);
                if (flag) {
                    int count = countAliveNeighbors(Dan_x + dx,Dan_y + dy);
                    if (2 <= count && count <= 3){
                        chessboard.getCellBlock(Dan_x + dx,Dan_y + dy, chessboard.getCopyBoard()).ageIncrease();
                        //每次对复制棋盘上的细胞进行操作后，将复制棋盘上的这个细胞与原棋盘上的交换，复制棋盘上的该细胞保持未处理时的状态
                        chessboard.changeOneCellBlock(Dan_x + dx,Dan_y + dy);
                        //复制棋盘上的下一个细胞进行操作时，不会受先前细胞处理的影响，达到同时处理的效果。
                    }

                }

            }
        }
    }


    public void lifeGameCheck3(int Dan_x, int Dan_y) {
        for (int dx = -12; dx <= 12 ; dx++){
            for (int dy = -12; dy <= 12; dy++) {

                boolean flag = chessboard.getLattice(Dan_x + dx, Dan_y + dy, chessboard.getCopyBoard()) == null;
                if (flag) {
                    int count = countAliveNeighbors(Dan_x + dx,Dan_y + dy);
                    if (count == 3){
//                        CellBlock3 cell = new CellBlock3(getMaxAgeOfNeighbors(Dan_x + dx,Dan_y + dy));
                        //将要重生的位置由原来的Object变为CellBlock3，年龄取周围存活细胞年龄的最大值
                        chessboard.setCellBlock(Dan_x + dx, Dan_y + dy,getMaxAgeOfNeighbors(Dan_x+dx, Dan_y+dy),chessboard.getCopyBoard());//细胞繁殖
                        chessboard.changeOneCellBlock(Dan_x + dx,Dan_y + dy);
                    }
                }
            }
        }
    }

    //引入变量cnt来记录该局游戏中所经过的判定轮数
    public boolean lifeGameCheck4(int Dan_x, int Dan_y, int cnt, int label) {
        boolean isGameOver = false;
        if (countAliveNeighbors(Dan_x, Dan_y) > 0){
            this.accumulatedMana += computeAccumulatedMana();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) {
                        continue;
                    }
                    chessboard.replaceCellBlock(Dan_x + dx, Dan_y + dy,chessboard.getCopyBoard());
                    chessboard.changeOneCellBlock(Dan_x + dx, Dan_y + dy);
                }
            }
            //除非已经判定过至少一轮，否则该局游戏不会结束
            if (cnt > 1){
                isGameOver = true;
                this.accumulatedMana += computeAccumulatedMana();
                //label用来判定启命英集合中的所有启命英是否都经过了该判定。防止先判定的启命英造成了replaceAllCells而使之后启命英的判定无效
                if (label == chessboard.getDanDelifeonsSize() - 1){
                    replaceAllCells();
                }
            }
        }
        return isGameOver;
    }

    private boolean isCellBlock3(int x, int y, int dx, int dy) {
        //检查该格是否是CellBlock,以及检查是否越界
        //此处判定同时给lifeGameCheck方法和private方法调用，故条件较多。
        return (x + dx >= 0) && (y + dy >= 0) && (x + dx < chessboard.getLength()) && (y + dy < chessboard.getWidth()) && chessboard.getLattice(x + dx, y + dy, chessboard.getCopyBoard()) instanceof CellBlock3 && (dx != 0 || dy != 0);
    }

    //计算周围存活的细胞数
    private int countAliveNeighbors(int x, int y){
        int count = 0;
        for (int dx = -1; dx <= 1; dx++){
            for (int dy = -1; dy <= 1; dy++){
                if (isCellBlock3(x,y,dx,dy)){
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
                if (!isCellBlock3(x,y,dx,dy)){
                    continue;
                }
                max = Math.max(max,chessboard.getCellBlock(x + dx,y + dy, chessboard.getCopyBoard()).getAge());
            }
        }
        return max;
    }

    //删除游戏范围内的所有细胞
    private void replaceAllCells(){
        for (int i = 0; i < chessboard.getLength(); i++){
            for (int j = 0; j < chessboard.getWidth(); j++){
                if (!(chessboard.getLattice(i,j, chessboard.getBoard()) instanceof CellBlock3)) {
                    continue;
                }
                chessboard.replaceCellBlock(i, j, chessboard.getBoard());
            }
        }
    }

    //计算周围存活细胞的总年龄
    private int getSumAgeOfNeighbors(int x, int y){
        int sum = 0;
        for (int dx = -1; dx <= 1; dx++){
            for (int dy = -1; dy <= 1; dy++){
                if (!isCellBlock3(x, y,dx,dy)){
                    continue;
                }
                sum += Math.min(chessboard.getCellBlock(x + dx, y + dy, chessboard.getCopyBoard()).getAge(), 100); //年龄大于100的细胞与年龄等于100的细胞产生的魔力量相同

            }
        }
        return sum;
    }

    //计算所产生的魔力量
    public int computeAccumulatedMana(){
        return getSumAgeOfNeighbors(Dan_x, Dan_y) * 60;
    }
}
