package aima.gui.fx.views;

import aima.core.environment.eightpuzzle.EightPuzzleBoard;
import aima.core.util.datastructure.XYLocation;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * Controller class which provides functionality for using a stack pane as a
 * state view for the Eight Puzzle problem.
 *
 * @author Ruediger Lunde
 */
public class EightPuzzleViewCtrl {

    private Button[] tileBtns = new Button[9];
    private EightPuzzleBoard board;

    /**
     * Adds a grid pane to the provided pane and creates a controller class
     * instance which is responsible for Eight Puzzle tile positioning on the grid.
     */
    public EightPuzzleViewCtrl(StackPane viewRoot) {
        GridPane gridPane = new GridPane();
        viewRoot.getChildren().add(gridPane);
        viewRoot.setAlignment(Pos.CENTER);
        gridPane.maxWidthProperty().bind(Bindings.min(viewRoot.widthProperty(), viewRoot.heightProperty()).subtract(20));
        gridPane.maxHeightProperty().bind(Bindings.min(viewRoot.widthProperty(), viewRoot.heightProperty()).subtract(10));
        RowConstraints c1 = new RowConstraints();
        c1.setPercentHeight(100.0 / 3);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(100.0 / 3);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        for (int i = 0; i < 3; i++) {
            gridPane.getRowConstraints().add(c1);
            gridPane.getColumnConstraints().add(c2);
        }
        for (int i = 0; i < 9; i++) {
            Button btn = new Button();
            btn.setOnAction(this::handleButtonEvent);
            btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            btn.setMinSize(10, 10);
            btn.widthProperty().addListener((obs, o, n) -> btn.setFont(Font.font(n.intValue() / 2.5)));
            tileBtns[i] = btn;
            gridPane.add(btn, i % 3, i / 3);
        }
    }

    public void initialize(EightPuzzleBoard board) {
        this.board = board;
        update();
    }

    /** Updates the view. */
    public void update() {
        for (int i = 0; i < 9; i++ ) {
            int tileNo = board.getValueAt(new XYLocation(i % 3, i / 3));
            tileBtns[i].setText(tileNo == 0 ? "" : Integer.toString(tileNo));
            tileBtns[i].setDisable(tileNo == 0);
        }
    }

    public void handleButtonEvent(ActionEvent ae) {
        for (int i = 0; i < 9; i++) {
            if (ae.getSource() == tileBtns[i]) {
                XYLocation locGap = board.getLocationOf(0);
                if (locGap.getX() == i % 3) {
                    if (locGap.getY() == i / 3 - 1)
                        board.moveGapDown();
                    else if (locGap.getY() == i / 3 + 1)
                        board.moveGapUp();
                } else if (locGap.getY() == i / 3) {
                    if (locGap.getX() == i % 3 - 1)
                        board.moveGapRight();
                    else if (locGap.getX() == i % 3 + 1)
                        board.moveGapLeft();
                }
            }
        }
        update();
    }
}
