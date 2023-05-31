public class Main {
    public static void main(String[] args) {
        MinesweeperModel model = new MinesweeperModel();
        MinesweeperView view = new MinesweeperView(model);
        MinesweeperController controller = new MinesweeperController(model, view);
        controller.startNewGame();
    }
}
