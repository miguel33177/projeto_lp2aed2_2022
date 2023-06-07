package edu.ufp.inf.Projeto.Classes;

import edu.princeton.cs.algs4.RedBlackBST;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PoI extends Nodes{
    private static final RedBlackBST<String,PoI> myRedBlackBST_PoI = new RedBlackBST<>();
    private static HashMap <PoI, ArrayList<RegistosPoI>> myHashMapPoI = new HashMap<>();
    String name_PoI;

    public PoI(String username, Integer userID,String name_PoI, Integer version, long timestamp, double latitude, double longitude) {
        super(username, userID, version, timestamp, latitude, longitude);
        this.name_PoI=name_PoI;

    }

    public static HashMap<PoI, ArrayList<RegistosPoI>> getMyHashMapPoI() {
        return myHashMapPoI;
    }

    public static RedBlackBST<String, PoI> getMyRedBlackBST_PoI() {
        return myRedBlackBST_PoI;
    }

    public String getName() {
        return name_PoI;
    }

    public void setName(String name) {
        this.name_PoI = name;
    }

    @Override
    public String toString() {
        return "PoI{" +
                "ID=" + this.getID() +
                " ,name_PoI='" + name_PoI + '\'' +
                " ,Latitude= " + getLatitude() +
                " ,Longitude= " + getLongitude() +
                " ,timestamp= " + timestamp +
                '}';
    }

    public static void main(String[] args) {
        Nodes nodes1 = new PoI("bessa",1,"Shopping",1,new Date().getTime(),234342,324243);
        Nodes.getMyRedBlackBST_Nodes().put(nodes1.getID(),nodes1);
        System.out.println(Nodes.getMyRedBlackBST_Nodes().get(nodes1.getID()));
    }
}
