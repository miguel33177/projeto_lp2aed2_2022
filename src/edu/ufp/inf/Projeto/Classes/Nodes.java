package edu.ufp.inf.Projeto.Classes;

import edu.princeton.cs.algs4.RedBlackBST;

import java.util.ArrayList;
import java.util.Date;

public class Nodes extends Object {
    private static final RedBlackBST<Integer, Nodes> myRedBlackBST_Nodes = new RedBlackBST<>();

    private final Integer ID;
    protected static Integer staticId = 0;
    private final ArrayList<String> tags_Nodes = new ArrayList<>();
    private double Latitude;
    private double Longitude;

    public Nodes(String username, Integer userID, Integer version, long timestamp, double latitude, double longitude) {
        super(username, userID, version, timestamp);
        Nodes.staticId++;
        this.ID = staticId;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.timestamp= getTimestamp();
    }

    public static RedBlackBST<Integer, Nodes> getMyRedBlackBST_Nodes() {
        return myRedBlackBST_Nodes;
    }

    public ArrayList<String> getTags() {
        return tags_Nodes;
    }

    public void setLatitude(double Latitude) {
        this.Latitude = Latitude;
    }

    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }

    public Integer getID() {
        return ID;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    @Override
    public String toString() {
        return "Nodes{" +
                "ID=" + ID +
                ", Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                ", tags=" + tags_Nodes +
                ", timestamp= " + timestamp +
                '}';

    }

    public static void main(String[] args) {
        Nodes node1 = new Nodes("Bessa", 1, 1, new Date().getTime(), 45444, 44541);
        Nodes node2 = new Nodes("gfd", 2, 2, new Date().getTime(), 445441, 45451451);
        getMyRedBlackBST_Nodes().put(node1.getID(), node1);
        getMyRedBlackBST_Nodes().put(node2.getID(), node2);
        System.out.println(getMyRedBlackBST_Nodes().get(1));
        System.out.println(getMyRedBlackBST_Nodes().get(2));
        System.out.println(node1.getTimestamp());
        System.out.println(node2.getTimestamp());
    }
}

