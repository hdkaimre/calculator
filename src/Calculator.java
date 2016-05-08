import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

public class Calculator extends Application implements ActionListener {
    //get rates

    TextField eurField = new TextField();
    TextField otherMoney = new TextField();

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
               primaryStage.setTitle("Calculator");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        File ratesFile = new File("C:\\Users\\HansDaniel\\oop\\calculator\\src\\rates.txt");
        ArrayList<String> list = new ArrayList<>();
        Scanner scanner = new Scanner(ratesFile);
        while (scanner.hasNextLine()){
            list.add(scanner.nextLine());
        }
        double usdRate = Double.parseDouble(list.get(0));
        double rubRate = Double.parseDouble(list.get(1));
        double chfRate = Double.parseDouble(list.get(2));

        scanner.close();


        Text scenetitle = new Text("Eurokalkulaator");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label eurLabel = new Label("EUR:");
        grid.add(eurLabel, 0, 1);
        grid.add(eurField, 1, 1);

        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "USD",
                        "RUB",
                        "CHF"
                );


        final ComboBox comboBox = new ComboBox(options);
        comboBox.setValue("USD");

        grid.add(comboBox, 0, 2);
        grid.add(otherMoney, 1, 2);

        Button button = new Button("Clear");
        grid.add(button, 1, 3);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> {
                    eurField.setText("");
                    otherMoney.setText("");
                });

        eurField.addEventHandler(KeyEvent.ANY,
                e -> {
                    String money = eurField.getText();
                    if(eurToOther(money,1.0).equals("nope")){
                        otherMoney.setText("");
                    }
                    else if(comboBox.getValue().equals("USD")){
                        otherMoney.setText(eurToOther(money,usdRate));
                    }
                    else if(comboBox.getValue().equals("RUB")){
                        otherMoney.setText(eurToOther(money,rubRate));
                    }
                    else if(comboBox.getValue().equals("CHF")){
                        otherMoney.setText(eurToOther(money,chfRate));
                    }
                });

        otherMoney.addEventHandler(KeyEvent.ANY,
                e -> {
                    String money = otherMoney.getText();
                    if(otherToEur(money,1.0).equals("nope")){
                        eurField.setText("Ei suuda infot töödelda!");
                    }
                    else if(comboBox.getValue().equals("USD")){
                        eurField.setText(otherToEur(money,usdRate));
                    }
                    else if(comboBox.getValue().equals("RUB")){
                        eurField.setText(otherToEur(money,rubRate));
                    }
                    else if(comboBox.getValue().equals("CHF")){
                        eurField.setText(otherToEur(money,chfRate));
                    }
                });

        Group root = new Group();
        root.getChildren().add(grid);

        Canvas canvas = new Canvas(300, 300);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc);

        root.getChildren().add(canvas);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static String eurToOther(String eur, double rate) {
        try {
            BigDecimal doubleEur = BigDecimal.valueOf(Double.parseDouble(eur));
            return String.valueOf(doubleEur.multiply(BigDecimal.valueOf(rate)));
        }
        catch (Exception e){
            return "nope";
        }
    }


    public static String otherToEur(String other, double rate) {
        try {
            double doubleOther = Double.parseDouble(other);
            return String.valueOf(doubleOther / rate);
        }
        catch (Exception e){
            return "nope";
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(120, 220, 140, 240);
        gc.fillOval(160, 220, 30, 30);
        gc.strokeRoundRect(210, 220, 30, 30, 10, 10);
    }
}