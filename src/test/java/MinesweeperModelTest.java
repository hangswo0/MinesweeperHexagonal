import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MinesweeperModelTest {

    private MinesweeperModel model;


    @BeforeAll
    public void setUp() throws Exception {
        model = new MinesweeperModel();
        model.startGame(4, 10);
    }

    @Test
    void testGetCell() {
        MinesweeperCell cell = model.getCell(4, 4);
        MinesweeperCell cell1 = model.getCell(0, 1);
        assertNotNull(cell);
        assertNull(cell1);
    }

    @Test
    public void testSetFlag() {
        model.setFlag(2, 6);
        assertTrue(model.getCell(2, 6).flagged());
        model.setFlag(4, 3);
        assertTrue(model.getCell(4, 3).flagged());
    }

    @Test
    public void testRemoveFlag() {
        model.setFlag(2, 6);
        model.removeFlag(2, 6);
        assertTrue(model.getCell(2, 6).notFlagged());
        model.setFlag(4, 3);
        model.removeFlag(4, 3);
        assertTrue(model.getCell(4, 3).notFlagged());
    }

    @Test
    public void testGetSideCount() {
        assertEquals(7, model.getSideCount());
    }
}
