package edu.ufp.inf.Projeto.Classes;

import edu.princeton.cs.algs4.RedBlackBST;

public class TrafficSigns {
    private static final RedBlackBST <String,TrafficSigns> myRedBlackBST_TrafficSigns = new RedBlackBST<>();
    String name;
    private double latitude;
    private double longitude;

    public static RedBlackBST<String, TrafficSigns> getMyRedBlackBST_TrafficSigns() {
        return myRedBlackBST_TrafficSigns;
    }
    public TrafficSigns(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "TrafficSigns{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public static void main(String[] args) {
        TrafficSigns myTrafficSigns = new TrafficSigns("Passadeira", 345345343,453345345);
        getMyRedBlackBST_TrafficSigns().put(myTrafficSigns.getName(),myTrafficSigns);
        System.out.println(getMyRedBlackBST_TrafficSigns().get("Passadeira"));

    }
}

