package com.example.ap_project;
import org.junit.Test ;

import static org.testng.AssertJUnit.*;

public class JUnitTesting {

    @Test
    public void testScore(){
        //check that the initial score is zero or not.
        assertEquals(0,Controller.getScore());
    }

    @Test
    public void testIfCharacterNULL(){
        //test is the main character is not null.
        assertNotNull(Controller.getCharater());
    }
}
