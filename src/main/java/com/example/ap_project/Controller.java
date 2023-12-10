package com.example.ap_project;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
//
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
import java.io.File;

public class Controller{
    private static Stage stage;
    private static Scene scene;
    private static Parent rootMain;
    private static Parent rootPause;
    private static Parent rootHome;
    private static Parent rootGameOver;
    private static Timeline timeline;
    private static boolean onPlatform=true;
    private static int start;
    private static int end;
    //SCORE
    private static int score=0;
    public static int getScore() {
        return 0;
    }
    public static void setScore(int newScore) {
        score = newScore;
    }
    private static int cherryCount=0;
    public static int getCherryCount() {
        return 0;
    }
    public static void setCherryCount(int newCherryCount) {
        cherryCount = newCherryCount;
    }
    private static Image characterImage;
    private static ImageView characterImageView;
    public static ImageView getCharater(){
        return characterImageView;
    }
    @FXML
    private static Button mainButton = new Button();
    private static Group G ;
    private static Rectangle rect;
    private static Character c ;
    @FXML
    private static Pane mainPane;
    @FXML
    private static Line currentLine;
    private static boolean mousePressed = false;
    @FXML
    Character character=Character.getInstance();
    private static AtomicBoolean spacePressed = new AtomicBoolean(false);
    private static Label scoreLabel;
    private static Label cherryLabel;
    private  static boolean dead=false;
    private static Cherry cherry;
    private static Image cherryImage;
    private static ImageView cherryImageView;

    private void saveCherryCountToFile() {
        try {
            FileWriter fileWriter = new FileWriter("cherryCount.txt");
            fileWriter.write(Integer.toString(cherryCount));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int loadScoreFromFile() {
        try {
            File file = new File("cherryCount.txt");
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String scoreString = br.readLine();
                br.close();
                br.close();
                return Integer.parseInt(scoreString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0; // Return default score if file not found or error occurs
    }

    @FXML
    private void handleMousePress() {
//        playSound();
//        System.out.println("hello");
        mousePressed=true;
        new AnimationTimer(){
            @Override
            public void handle(long l) {
                if(mousePressed){
                    increaseLength(currentLine);
//                    System.out.println("Hello");
                }
            }
        }.start();
    }

    @FXML
    private void exit() throws IOException {
//        stopSound();
        mousePressed=false;
        rotateLine(currentLine);
        checkLength();
    }

    private void endGame(){
        dead=true;
        score=0;
        System.out.println("You Failed");
        saveCherryCountToFile();
//
        Timeline new_timeLine = new Timeline(new KeyFrame(Duration.millis(2000) , event -> {
            if(!isFlipped) {
                moveCharacter();
            }
        }));

        new_timeLine.play();
        Timeline new_timeLine2 = new Timeline(new KeyFrame(Duration.millis(500) , event -> {

        }));

        new_timeLine2.setOnFinished(event -> {
            try {
                switchToGameOver();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        new_timeLine.setOnFinished(event -> {
            fall(dead);
            new_timeLine2.play();
        });
    }

    private void checkLength() throws IOException {
//        System.out.println(start);
//        System.out.println(currentLine.getEndX());
//        System.out.println(end);
        moveCharacter();
        if(currentLine.getEndX()<end && currentLine.getEndX()>start){
            System.out.println("You Passed");
            Timeline new_timeLine1 = new Timeline(new KeyFrame(Duration.millis(2500) , event -> {
                moveCharacter();
            }));
            new_timeLine1.setOnFinished(event -> {
                if(isFlipped){
                    endGame();
                }
                else{
                    moveBack();
                };
            });
            new_timeLine1.play();
            score+=1;
            scoreLabel.setText("Score: "+score);
            cherryLabel.setText("Cherry Count: "+cherryCount);
        }
        else{
            endGame();
        }
    }

    private void increaseCherryCount(){
        cherryCount++;
        cherryLabel.setText("Cherry Count: "+cherryCount);
    }
    private void moveCharacter(){
        onPlatform=false;
        TranslateTransition translate = new TranslateTransition(Duration.seconds(2));
        translate.setNode(characterImageView);
        translate.setByX(+(currentLine.getEndX()-currentLine.getStartX()));
        translate.play();
        characterImageView.boundsInParentProperty().addListener((obs, init_pos,final_pos) -> {
            if (((Pane)rootMain).getChildren().contains(cherryImageView)){
                if (final_pos.intersects(cherryImageView.getBoundsInParent())){
                    increaseCherryCount();
                    ((Pane)rootMain).getChildren().remove(cherryImageView);


                }

            }
            });
        translate.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                onPlatform=true;
            }
        });
    }

    private void fall(boolean ferting){
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(characterImageView);
        translate.setByY(+250);
        translate.play();
//        switchToMainMenu();
    }

//    public void characterFlipper(){
//        characterImageView.setScaleY(-1);
//        characterImageView.setY(251);
//    }

    private boolean isFlipped = false; // Add a boolean flag to track the character's flipped state

    public void characterFlipper() {
        if(onPlatform){
            return;
        }

        if (isFlipped) {
            characterImageView.setScaleY(1);
            characterImageView.setY(251 - 40);
        } else {
            characterImageView.setScaleY(-1);
            characterImageView.setY(251);
        }
        isFlipped = !isFlipped;
    }

    public void moveBack(){
        ((Pane)rootMain).getChildren().remove(rect);
        ((Pane)rootMain).getChildren().remove(currentLine);
        ((Pane)rootMain).getChildren().remove(characterImageView);
        rect=createRectangle();
        ((Pane)rootMain).getChildren().add(rect);
        Character character = Character.getInstance();
        characterImage = Character.getMyimage() ;
        characterImageView = new ImageView(characterImage);
        characterImageView.setFitHeight(40);
        characterImageView.setFitWidth(40);
        characterImageView.setX(98-40);
        characterImageView.setY(251-40);
        rect.setOnMousePressed(event1->{
            handleMousePress();
        });
        rect.setOnMouseReleased(event1->{
            try {
                exit();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        currentLine = new Line(98, 251, 98, 251); // Change coordinates as needed
        currentLine.setStrokeWidth(3);
        ((Pane) rootMain).getChildren().add(currentLine);

        ((Pane) rootMain).getChildren().add(characterImageView) ;
        Random rand = new Random();
        int cher= rand.nextInt(0,2);
        if(cher==1) {
            cherry = Cherry.getInstance();
            cherryImage = Cherry.getMyimage();
            cherryImageView = new ImageView(cherryImage);
            int cherryX = rand.nextInt(98, (int)rect.getX());
//            System.out.println("cherry x : "+cherryX);
            cherryImageView.setFitHeight(20);
            cherryImageView.setFitWidth(20);
            cherryImageView.setX(cherryX);
            cherryImageView.setY(265);
            ((Pane) rootMain).getChildren().add(cherryImageView);
        }
    }

    private Rectangle createRectangle(){
        Random rand=new Random();
        int x=rand.nextInt(200)+200;
        int width=rand.nextInt(150)+50;
//        System.out.println(x);
//        System.out.println(width);
        start=x;
        end=start+width;
//        System.out.println(start);
//        System.out.println(end);
        Rectangle rect = new Rectangle(x,251,width,300);
//        mainPane.getChildren().add(rect);
        return rect;
    }

    private void increaseLength(Line currentLine){
        double newEndY = currentLine.getEndY() - 1; // Adjust length as needed
        currentLine.setEndY(newEndY);
    }

    private void rotateLine(Line currentLine){
        currentLine.setEndX((-currentLine.getEndY()+currentLine.getStartY())+98);
        currentLine.setEndY(251);
    }


    @FXML
    public void switchToGameScene(ActionEvent event) throws IOException {
        G = new Group() ;
        rootMain = new FXMLLoader(getClass().getResource("main.fxml")).load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scoreLabel = new Label("Score: "+score);
        cherryCount=loadScoreFromFile();
        cherryLabel=new Label("Cherries: "+cherryCount);
        cherryLabel.setText("Cherry Count: "+cherryCount);
        Font font = new Font("Times New Roman",35);
        currentLine = new Line(98, 251, 98, 251); // Change coordinates as needed
        currentLine.setStrokeWidth(3);
        ((Pane) rootMain).getChildren().add(currentLine);
        rect = createRectangle();

        scoreLabel.setFont(font);
        cherryLabel.setFont(font);
        ((Pane) rootMain).getChildren().add(rect);
        mainButton.toFront();
        ((Pane) rootMain).getChildren().add(scoreLabel);
        ((Pane) rootMain).getChildren().add(cherryLabel);
        cherryLabel.setLayoutY(30);
        Character character = Character.getInstance();
        characterImage = Character.getMyimage();
        characterImageView = new ImageView(characterImage);
        characterImageView.setFitHeight(40);
        characterImageView.setFitWidth(40);
        characterImageView.setX(98-40);
        characterImageView.setY(251-40);
        ((Pane) rootMain).getChildren().add(characterImageView);
        mainButton.toFront();
        rect.setOnMousePressed(event1->{
            handleMousePress();
        });
        rect.setOnMouseReleased(event1->{
            try {
                exit();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Random rand = new Random();
        int cher= rand.nextInt(0,2);
        if(cher==1) {
            cherry = Cherry.getInstance();
            cherryImage = Cherry.getMyimage();
            cherryImageView = new ImageView(cherryImage);
            int cherryX = rand.nextInt(98, (int)rect.getX());
//            System.out.println("cherry x : "+cherryX);
            cherryImageView.setFitHeight(20);
            cherryImageView.setFitWidth(20);
            cherryImageView.setX(cherryX);
            cherryImageView.setY(265);
            ((Pane) rootMain).getChildren().add(cherryImageView);
        }


        Scene scene = new Scene(rootMain);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToPauseScene(ActionEvent event) throws IOException{
        rootPause= FXMLLoader.load(getClass().getResource("pauseScreen.fxml"));
        ((Pane)rootMain).getChildren().removeAll();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene3=new Scene(rootPause);
        stage.setScene(scene3);
        stage.show();
    }

    @FXML
    public void switchToMainMenu(ActionEvent event) throws IOException{
        rootHome= FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene3=new Scene(rootHome);
        stage.setScene(scene3);
        stage.show();
    }


    @FXML
    public void switchToGameOver() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Game-Over.fxml"));
        rootGameOver = loader.load();

        // Assuming you have a reference to the main stage
        Stage mainStage = stage;

        Scene scene = new Scene(rootGameOver);
        mainStage.setScene(scene);
        mainStage.show();
    }

    @FXML
    public void revive(ActionEvent event) throws IOException{
        if(cherryCount>=10) {
            cherryCount-=10;
            saveCherryCountToFile();
            switchToGameScene(event);
        }
    }

    @FXML
    public void exitGame(){
        System.exit(0);
    }

    @FXML
    public void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE) {
            characterFlipper(); // Call your character flipping method here
        }
    }
}