class MinesweeperController {

    private MinesweeperModel model;
    private MinesweeperView view;

    MinesweeperController(MinesweeperModel model, MinesweeperView view) {
        this.model = model;
        this.view = view;
    }

    void setView(MinesweeperView view) {
        this.view = view;
    }

    void startNewGame() {
        int[] gameSettings = view.getGameSettings();
        try {
            model.startGame(gameSettings[0], gameSettings[1]);
        } catch (Exception e) {
            model.startGame();
        }
        view.init(gameSettings[0], gameSettings[1]);
    }

    void onLeftClick(int strIndex, int cellIndex) {
        model.openCell(strIndex, cellIndex);
        if (model.isWin()) {
            view.showWinMessage();
            startNewGame();
        } else if (model.isGameOver()) {
            view.showGameOverMessage();
            startNewGame();
        }
    }

    void onRightClick(int strIndex, int cellIndex) {
        if (model.getCell(strIndex, cellIndex).flagged())
            model.setFlag(strIndex, cellIndex);
        else
            model.removeFlag(strIndex, cellIndex);
    }
}