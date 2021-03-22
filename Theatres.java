package com.example.ekaappi;


import java.util.ArrayList;

public class Theatres {
    private static Theatres theatres = new Theatres();
    public static Theatres getInstance(){
        return theatres;
    }
    ArrayList<TheatreInfo> list;

    public Theatres(){
       list = new ArrayList<TheatreInfo>();
    }

    public void addElement(String place, int id){
        TheatreInfo newTheater = new TheatreInfo(place, id);
        list.add(newTheater);
        return;
    }
}
