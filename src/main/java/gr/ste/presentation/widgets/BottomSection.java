package gr.ste.presentation.widgets;

import gr.ste.presentation.view_models.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BottomSection extends VBox {
    public final TextField xCoordinateTextField;
    public final TextField yCoordinateTextField;
    public final Label invalidMoveLabel;
    public final Button fireButton;

    public BottomSection() {
        xCoordinateTextField = new TextField();
        yCoordinateTextField = new TextField();
        invalidMoveLabel = new Label();
        fireButton = new Button("Fire");

        fireButton.setOnMouseEntered(this::hover);
        fireButton.setOnMouseExited(this::unhover);

        invalidMoveLabel.setFont(Font.font("Blackadder ITC", FontWeight.NORMAL, 26));

        xCoordinateTextField.setPromptText("X coordinate");
        yCoordinateTextField.setPromptText("Y coordinate");
        Insets margin = new Insets(16.0);
        HBox.setMargin(xCoordinateTextField, margin);
        HBox.setMargin(yCoordinateTextField, margin);
        setOpaqueInsets(margin);

        CornerRadii radius = new CornerRadii(64.0);
        Background textFieldBackground = new Background(new BackgroundFill(Color.TRANSPARENT, radius, Insets.EMPTY));
        Border textFieldBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, radius, BorderWidths.DEFAULT));
        xCoordinateTextField.setBackground(textFieldBackground);
        xCoordinateTextField.setBorder(textFieldBorder);

        yCoordinateTextField.setBackground(textFieldBackground);
        yCoordinateTextField.setBorder(textFieldBorder);

        Background buttonBackground = new Background(new BackgroundFill(Color.TRANSPARENT, radius, Insets.EMPTY));
        fireButton.setBackground(buttonBackground);
        fireButton.setBorder(textFieldBorder);
        fireButton.setTextFill(Color.BLACK);

        HBox line = new HBox(xCoordinateTextField, yCoordinateTextField, fireButton);
        line.setAlignment(Pos.CENTER);

        getChildren().addAll(invalidMoveLabel, line);
        setPrefSize(100.0, 200.0);
        setAlignment(Pos.CENTER);
    }

    public BottomSection(GameState gameState) {
        xCoordinateTextField = new TextField();
        yCoordinateTextField = new TextField();
        invalidMoveLabel = new Label();
        fireButton = new Button("Fire");
        fireButton.setOnMouseEntered(this::hover);
        fireButton.setOnMouseExited(this::unhover);

        invalidMoveLabel.setTextFill(Color.RED);
        invalidMoveLabel.setFont(Font.font("Blackadder ITC", FontWeight.NORMAL, 26));

        Insets margin = new Insets(8.0);
        HBox.setMargin(xCoordinateTextField, margin);
        HBox.setMargin(yCoordinateTextField, margin);
        setOpaqueInsets(margin);

        CornerRadii radius = new CornerRadii(64.0);
        Background textFieldBackground = new Background(new BackgroundFill(Color.TRANSPARENT, radius, Insets.EMPTY));
        Border textFieldBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, radius, BorderWidths.DEFAULT));
        xCoordinateTextField.setBackground(textFieldBackground);
        xCoordinateTextField.setBorder(textFieldBorder);

        yCoordinateTextField.setBackground(textFieldBackground);
        yCoordinateTextField.setBorder(textFieldBorder);

        Background buttonBackground = new Background(new BackgroundFill(Color.TRANSPARENT, radius, Insets.EMPTY));
        fireButton.setBackground(buttonBackground);
        fireButton.setBorder(textFieldBorder);
        fireButton.setTextFill(Color.BLACK);

        HBox line = new HBox(xCoordinateTextField, yCoordinateTextField, fireButton);
        line.setAlignment(Pos.CENTER);

        getChildren().addAll(invalidMoveLabel, line);
        setPrefSize(100.0, 200.0);
        setAlignment(Pos.CENTER);

        xCoordinateTextField.textProperty().bindBidirectional(gameState.xTargetCoordinate);
        yCoordinateTextField.textProperty().bindBidirectional(gameState.yTargetCoordinate);

        invalidMoveLabel.textProperty().bind(gameState.invalidMove);
        invalidMoveLabel.visibleProperty().bind(gameState.showInvalidMoveLabel.not());
        fireButton.disableProperty().bind(gameState.showInvalidMoveLabel.not().or(gameState.hasStartedGame.not()));
    }

    public void update(GameState gameState) {
        xCoordinateTextField.textProperty().bindBidirectional(gameState.xTargetCoordinate);
        yCoordinateTextField.textProperty().bindBidirectional(gameState.yTargetCoordinate);

        invalidMoveLabel.textProperty().bind(gameState.invalidMove);
        invalidMoveLabel.visibleProperty().bind(gameState.showInvalidMoveLabel.not());
        fireButton.disableProperty().bind(gameState.showInvalidMoveLabel.not().or(gameState.hasStartedGame.not()));
    }

    private void hover(MouseEvent mouseEvent) {
        CornerRadii radius = new CornerRadii(64.0);
        Background buttonBackground = new Background(new BackgroundFill(Color.WHEAT, radius, Insets.EMPTY));
        fireButton.setBackground(buttonBackground);
    }

    private void unhover(MouseEvent mouseEvent) {
        CornerRadii radius = new CornerRadii(64.0);
        Background buttonBackground = new Background(new BackgroundFill(Color.TRANSPARENT, radius, Insets.EMPTY));
        fireButton.setBackground(buttonBackground);
    }
}
