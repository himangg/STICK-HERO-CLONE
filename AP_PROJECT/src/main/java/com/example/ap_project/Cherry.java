package com.example.ap_project;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Cherry implements CherryInterface{
    //implemented singleton design pattern because there can be atmost one cherry at any moment of time.

    private static Cherry cherry=null;
    private static Image myimage ;
    private static ImageView myimageview;

    private Cherry(){
        this.myimage = new Image("Cherry.png");
    }

    public static Cherry getInstance(){
        if(cherry==null) {
            cherry = new Cherry();
        }
        return cherry;
    }
    public static Image getMyimage() {
        return myimage;
    }
}
