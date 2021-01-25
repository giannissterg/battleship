package gr.ste.presentation;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

class Grid extends Pane {

    int rows;
    int columns;

    double width;
    double height;

    Cell[][] cells;

    public Grid( int columns, int rows, double width, double height) {

        this.columns = columns;
        this.rows = rows;
        this.width = width;
        this.height = height;

        cells = new Cell[rows][columns];

    }

    /**
     * Add cell to array and to the UI.
     */
    public void add(Cell cell, int column, int row) {

        cells[row][column] = cell;

        double w = width / columns;
        double h = height / rows;
        double x = w * column;
        double y = h * row;

        cell.setLayoutX(x);
        cell.setLayoutY(y);
        cell.setPrefWidth(w);
        cell.setPrefHeight(h);

        getChildren().add(cell);

    }

    public Cell getCell(int column, int row) {
        return cells[row][column];
    }

    /**
     * Unhighlight all cells
     */
    public void unhighlight() {
        for( int row=0; row < rows; row++) {
            for( int col=0; col < columns; col++) {
                cells[row][col].unhighlight();
            }
        }
    }
}

class Cell extends StackPane {

    int column;
    int row;

    public Cell(int column, int row) {

        this.column = column;
        this.row = row;

        getStyleClass().add("cell");

//          Label label = new Label(this.toString());
//
//          getChildren().add(label);

        setOpacity(0.9);
    }

    public void highlight() {
        // ensure the style is only once in the style list
        getStyleClass().remove("cell-highlight");

        // add style
        getStyleClass().add("cell-highlight");
    }

    public void unhighlight() {
        getStyleClass().remove("cell-highlight");
    }

    public void hoverHighlight() {
        // ensure the style is only once in the style list
        getStyleClass().remove("cell-hover-highlight");

        // add style
        getStyleClass().add("cell-hover-highlight");
    }

    public void hoverUnhighlight() {
        getStyleClass().remove("cell-hover-highlight");
    }

    public String toString() {
        return this.column + "/" + this.row;
    }
}