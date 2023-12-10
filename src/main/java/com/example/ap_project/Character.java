package com.example.ap_project;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Character extends CharacterAbstractClass{
    //implemented singleton design pattern.

    private static Character character=null;
    private static Image myimage ;
    private static ImageView myimageview;

    private Character (){
        this.myimage = new Image("Character.png");
    }

    public static Character getInstance(){
        if(character==null){
            character=new Character();
        }
        return character;
    }
    public static Image getMyimage() {
        return myimage;
    }
}


