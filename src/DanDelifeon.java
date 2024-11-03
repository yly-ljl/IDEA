public class DanDelifeon {
    private int Dan_x = 12;
    private int Dan_y = 12;
    private ChessboardWorldSystem chessboard;
    private int accumulatedMana;

    public DanDelifeon() {
        this.chessboard = new ChessboardWorldSystem();
//        SetOriginalCells(this.chessboard); //创建时初始化所有细胞
        this.accumulatedMana = 0;
    }


    public ChessboardWorldSystem getBoard() {
        return chessboard;
    }

    public void setBoard(ChessboardWorldSystem chessboard) {
        this.chessboard = chessboard;
    }

    //进行生命游戏判定
    public int lifeGameCheck(){
        boolean IsGameOver = false;

        for (int x = 0; x < 25; x++) {
            for (int y = 0; y < 25; y++) {
                if (x == Dan_x && y == Dan_y) {
                    continue;
                }
                chessboard.stepOneSecond(); //时间过去一秒，判定进行一次

                //对细胞的判定
                int count = CountAliveNeighbors(x, y);
                boolean flag = chessboard.getCellBlock(x,y).getIsAlive();
                if (flag){
                    if (count < 2 || count > 4){
                        chessboard.getCellBlock(x,y).Die(); //生命过少和过剩
                    } else if (count <= 3) {
                        chessboard.getCellBlock(x,y).AgeIncrease();
                    }
                } else if (count == 3) {
                    chessboard.getCellBlock(x,y).Rebirth(GetMaxAgeOfNeighbors(x, y)); //细胞繁殖
                }

                //对是否结束游戏的判定
                IsGameOver = CountAliveNeighbors(Dan_x, Dan_y) > 0;
                if (IsGameOver){
                    //若是第一次判定
                    if (chessboard.getSecond() == 1){
                        this.accumulatedMana += getAccumulatedMana();
                        IsGameOver = false; //若是第一次判定，则不会导致游戏结束

                        //将启命英周围的细胞为判定死亡
                        for (int dx = -1; dx <= 1; dx++) {
                            for (int dy = -1; dy <= 1; dy++) {
                                if (dx == 0 && dy == 0) {
                                    continue;
                                }
                                chessboard.ReplaceCellBlock(Dan_x + dx, Dan_y + dy);
                            }
                        }
                        //若不是第一次判定且游戏结束，则计算魔力总值并清除所有细胞方块
                    }else {
                        this.accumulatedMana += getAccumulatedMana();
                        ReplaceAllCells();
                        break;
                    }
                }
            }
            if (IsGameOver){
                break;
            }

        }

        return this.accumulatedMana;
    }

    //计算周围存活的细胞数
    private int CountAliveNeighbors(int x, int y){
        int count = 0;
        for (int dx = -1; dx <= 1; dx++){
            for (int dy = -1; dy <= 1; dy++){
                //超出范围或达到启命英所在位置的坐标的情况跳过
                if ((dx == 0 && dy == 0) || (x + dx < 0) || (y + dy < 0) || (x + dx >= 25) || (y + dy >= 25) || ((x + dx == Dan_x) && (y + dy == Dan_y))){
                    continue;
                }
                if (chessboard.getCellBlock(x + dx,y + dy).getIsAlive()){
                    count++;
                }
            }
        }
        return count;
    }

    //得到周围年龄最大的细胞的年龄
    private int GetMaxAgeOfNeighbors(int x, int y){
        int max = 0;
        for (int dx = -1; dx <= 1; dx++){
            for (int dy = -1; dy <= 1; dy++){
                //超出范围或达到启命英所在位置的坐标或细胞死亡的情况跳过
                if ((dx == 0 && dy == 0) || (x + dx < 0) || (y + dy < 0) || (x + dx >= 25) || (y + dy >= 25) || ((x + dx == Dan_x) && (y + dy == Dan_y)) || !(chessboard.getCellBlock(x + dx,y + dy).getIsAlive())){
                    continue;
                }
                max = Math.max(max,chessboard.getCellBlock(x + dx,y + dy).getAge());
            }
        }
        return max;
    }

    //初始化所有细胞为年龄为0的细胞
    private void SetOriginalCells(ChessboardWorldSystem chessboard){
        for (int i = 0; i < 25; i++){
            for (int j = 0; j < 25; j++){
                if (i == Dan_x && j == Dan_y) {
                    continue;
                }
                chessboard.SetCellBlock(i,j);
            }
        }
    }

    //删除游戏范围内的所有细胞
    private void ReplaceAllCells(){
        for (int i = 0; i < 25; i++){
            for (int j = 0; j < 25; j++){
                if (i == Dan_x && j == Dan_y) {
                    continue;
                }
                chessboard.ReplaceCellBlock(i, j);
            }
        }
    }

    //计算周围存活细胞的总年龄
    private int GetSumAgeOfNeighbors(int x, int y){
        int sum = 0;
        for (int dx = -1; dx <= 1; dx++){
            for (int dy = -1; dy <= 1; dy++){
                //超出范围或达到启命英所在位置的坐标或细胞死亡的情况跳过
                if ((dx == 0 && dy == 0) || (x + dx < 0) || (y + dy < 0) || (x + dx >= 25) || (y + dy >= 25) || ((x + dx == Dan_x) && (y + dy == Dan_y)) || !(chessboard.getCellBlock(x + dx,y + dy).getIsAlive())){
                    continue;
                }
                if (chessboard.getCellBlock(x + dx,y + dy).getIsAlive()){
                    if (chessboard.getCellBlock(x + dx,y + dy).getAge() > 100){
                        sum += 100; //年龄大于100的细胞与年龄等于100的细胞产生的魔力量相同
                    }else {
                        sum += chessboard.getCellBlock(x + dx,y + dy).getAge();
                    }
                }
            }
        }
        return sum;
    }

    //计算所产生的魔力量
    public int getAccumulatedMana(){
        return GetSumAgeOfNeighbors(Dan_x, Dan_y) * 60;
    }
}
