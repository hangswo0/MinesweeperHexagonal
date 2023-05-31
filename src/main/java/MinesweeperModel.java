import java.util.ArrayList;
import java.util.List;

class MinesweeperModel {
    private MinesweeperView view;
    private MinesweeperController controller;

    private int side; //количество блоков в одной стороне шестиугольника
    private boolean firstStep;
    private boolean gameOver;
    private MinesweeperCell[][] cells;
    private int mineCount;

    private static final int MIN_SIDE_SIZE = 4;
    private static final int MAX_SIDE_SIZE = 9;

    private static final int MIN_MINE_COUNT = 7;

    void MinesweeperModel(MinesweeperView view, MinesweeperController controller) {
        this.view = view;
        this.controller = controller;
    }
    void startGame(int side, int mineCount) {
        int cellsCount = 1;
        int s = 1;
        while (s < side) {
            cellsCount += 6 * s;
            s++;
        }
        if (side >= MIN_SIDE_SIZE && side <= MAX_SIDE_SIZE)
            this.side = side;
        else
            side = MIN_SIDE_SIZE;
        if (mineCount >= MIN_MINE_COUNT && mineCount < cellsCount)
            this.mineCount = mineCount;
        else
            mineCount = MIN_MINE_COUNT;
        this.firstStep = true;
        this.gameOver = false;
        this.cells = new MinesweeperCell[side * 2 - 1][side * 2 - 1];
        for (int strIndUp = side - 1; strIndUp >= 0; strIndUp--)
            for (int cellInd = (side - 1) - strIndUp; cellInd < side * 2 - 1; cellInd++) {
                cells[strIndUp][cellInd] = new MinesweeperCell();
                cells[strIndUp][cellInd].setEmpty();
            }
        for (int strIndDown = side; strIndDown < side * 2 - 1; strIndDown++)
            for (int cellInd = (strIndDown % side) + 1; cellInd < side * 2 - 1; cellInd++) {
                cells[strIndDown][cellInd] = new MinesweeperCell();
                cells[strIndDown][cellInd].setEmpty();
            }
    }

    void startGame() {
        this.firstStep = true;
        this.gameOver = false;
        this.cells = new MinesweeperCell[MIN_SIDE_SIZE * 2 - 1][MIN_SIDE_SIZE * 2 - 1];
        int startIndCellUp = 0;
        for (int strIndUp = MIN_SIDE_SIZE - 1; strIndUp >= 0; strIndUp--)
            for (int cellInd = (MIN_SIDE_SIZE - 1) - strIndUp; cellInd < MIN_SIDE_SIZE * 2 - 1; cellInd++) {
                cells[strIndUp][cellInd] = new MinesweeperCell();
                cells[strIndUp][cellInd].setEmpty();
            }
        for (int strIndDown = MIN_SIDE_SIZE; strIndDown < MIN_SIDE_SIZE * 2 - 1; strIndDown++)
            for (int cellInd = (strIndDown % MIN_SIDE_SIZE) + 1; cellInd < MIN_SIDE_SIZE * 2 - 1; cellInd++) {
                cells[strIndDown][cellInd] = new MinesweeperCell();
                cells[strIndDown][cellInd].setEmpty();
            }
    }

    MinesweeperCell getCell(int strIndex, int cellIndex) {
        if (strIndex < 0 || strIndex > side * 2 - 2 || cellIndex < 0 || cellIndex > side * 2 - 2 || cells[strIndex][cellIndex] == null)
            return null;
        else
            return cells[strIndex][cellIndex];
    }

    void setFlag(int strInd, int cellInd) {
        if (cells[strInd][cellInd].closed() && cells[strInd][cellInd].notFlagged())
            cells[strInd][cellInd].setFlag();
    }

    void removeFlag(int strInd, int cellInd) {
        if (cells[strInd][cellInd].closed() && cells[strInd][cellInd].flagged())
            cells[strInd][cellInd].removeFlag();
    }

    boolean isWin() {
        int startIndCellUp = 0;
        while (startIndCellUp < side) {
            for (int strIndexUp = side - 1; strIndexUp >= 0; strIndexUp--)
                for (int cellIndex = startIndCellUp; cellIndex < side * 2 - 1; cellIndex++) {
                    MinesweeperCell cell = cells[strIndexUp][cellIndex];
                    if (cell.closed() || (cell.notFlagged() || cell.mined()))
                        return false;
                }
            startIndCellUp++;
        }
        int startIndCellDown = 1;
        while (startIndCellDown < side) {
            for (int strIndexDown = side; strIndexDown < side * 2 - 1; strIndexDown++)
                for (int cellIndex = startIndCellDown; cellIndex < side * 2 - 1; cellIndex++) {
                    MinesweeperCell cell = cells[strIndexDown][cellIndex];
                    if (cell.closed() || (cell.notFlagged() && cell.mined()))
                        return false;
                }
            startIndCellDown++;
        }
        return true;
    }

    boolean isGameOver() {
        return gameOver;
    }

    void openCell(int strIndex, int cellIndex) {
        MinesweeperCell cell = getCell(strIndex, cellIndex);
        if (cell.empty())
            return;
        cell.open();
        if (cell.mined()) {
            gameOver = true;
            return;
        }
        if (firstStep) {
            firstStep = false;
            mineGenerator();
        }
        int mineAround = countMinesAround(strIndex, cellIndex);
        if (mineAround == 0) {
            List<MinesweeperCell> neighbours = getNeighbours(strIndex, cellIndex);
            for (MinesweeperCell n : neighbours) {
                if (n.closed()) { //открываем соседние клетки
                    for (int i = 0; i < side * 2 - 1; i++)
                        for (int j = 0; j < side * 2 - 1; j++)
                            if (cells[i][j] == n)
                                openCell(i, j);
                }
            }
        }

    }

    void mineGenerator() {
        int mines = 0;
        while (mines < mineCount) {
            int si = (int) (Math.random() * (side * 2 - 1)); //случайный индекс строки
            int ci = (int) (Math.random() * (side * 2 - 1)); //случайный индекс ячейски в строке
            if (cells[si][ci] != null && cells[si][ci].empty()) {
                cells[si][ci].setMined();
                mines++;
            }
        }
    }

    List<MinesweeperCell> getNeighbours(int strIndex, int cellIndex) {
        List<MinesweeperCell> neighbours = new ArrayList<>();
       if (cells[strIndex][cellIndex + 1] != null)
           neighbours.add(cells[strIndex][cellIndex + 1]);
        if (cells[strIndex][cellIndex - 1] != null)
            neighbours.add(cells[strIndex][cellIndex - 1]);
        if (cells[strIndex - 1][cellIndex] != null)
            neighbours.add(cells[strIndex - 1][cellIndex]);
        if (cells[strIndex - 1][cellIndex + 1] != null)
            neighbours.add(cells[strIndex - 1][cellIndex + 1]);
        if (cells[strIndex + 1][cellIndex] != null)
            neighbours.add(cells[strIndex][cellIndex]);
        if (cells[strIndex + 1][cellIndex + 1] != null)
            neighbours.add(cells[strIndex][cellIndex + 1]);
        return neighbours;
    }

    int countMinesAround(int strIndex, int cellIndex) {
        List<MinesweeperCell> neighbours = getNeighbours(strIndex, cellIndex);
        int countBombNear = 0;
        for (MinesweeperCell n : neighbours) {
            if (n.mined())
                countBombNear++;
        }
        return countBombNear;
    }

    //этот метод нужен для вьюхи
    int getSideCount() { //возвращает число всех строк (и столбцов) поля
        return side * 2 - 1;
    }
}