package edu.ufp.inf.Projeto.Classes;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;

public class Grafo {
    private static  EdgeWeightedDigraph myDigraph = new EdgeWeightedDigraph(Nodes.getMyRedBlackBST_Nodes().max()+1);
    private static  EdgeWeightedDigraph mySubDigraphCar_Time = new EdgeWeightedDigraph(Nodes.getMyRedBlackBST_Nodes().max()+1);
    private static  EdgeWeightedDigraph mySubDigraphBike_Time = new EdgeWeightedDigraph(Nodes.getMyRedBlackBST_Nodes().max()+1);
    private static  EdgeWeightedDigraph mySubDigraphWalk_Time = new EdgeWeightedDigraph(Nodes.getMyRedBlackBST_Nodes().max()+1);
    private static  EdgeWeightedDigraph mySubDigraphCar_Dist = new EdgeWeightedDigraph(Nodes.getMyRedBlackBST_Nodes().max()+1);
    private static  EdgeWeightedDigraph mySubDigraphBike_Dist = new EdgeWeightedDigraph(Nodes.getMyRedBlackBST_Nodes().max()+1);
    private static  EdgeWeightedDigraph mySubDigraphWalk_Dist = new EdgeWeightedDigraph(Nodes.getMyRedBlackBST_Nodes().max()+1);

    public static void setMyDigraph(EdgeWeightedDigraph myDigraph) {
        Grafo.myDigraph = myDigraph;
    }

    public static void setMySubDigraphCar_Time(EdgeWeightedDigraph mySubDigraphCar_Time) {
        Grafo.mySubDigraphCar_Time = mySubDigraphCar_Time;
    }

    public static void setMySubDigraphBike_Time(EdgeWeightedDigraph mySubDigraphBike_Time) {
        Grafo.mySubDigraphBike_Time = mySubDigraphBike_Time;
    }

    public static void setMySubDigraphWalk_Time(EdgeWeightedDigraph mySubDigraphWalk_Time) {
        Grafo.mySubDigraphWalk_Time = mySubDigraphWalk_Time;
    }

    public static void setMySubDigraphCar_Dist(EdgeWeightedDigraph mySubDigraphCar_Dist) {
        Grafo.mySubDigraphCar_Dist = mySubDigraphCar_Dist;
    }

    public static void setMySubDigraphBike_Dist(EdgeWeightedDigraph mySubDigraphBike_Dist) {
        Grafo.mySubDigraphBike_Dist = mySubDigraphBike_Dist;
    }

    public static void setMySubDigraphWalk_Dist(EdgeWeightedDigraph mySubDigraphWalk_Dist) {
        Grafo.mySubDigraphWalk_Dist = mySubDigraphWalk_Dist;
    }

    public static EdgeWeightedDigraph getMyDigraph() {
        return myDigraph;
    }

    public static EdgeWeightedDigraph getMySubDigraphCar_Time() {
        return mySubDigraphCar_Time;
    }

    public static EdgeWeightedDigraph getMySubDigraphBike_Time() {
        return mySubDigraphBike_Time;
    }

    public static EdgeWeightedDigraph getMySubDigraphWalk_Time() {
        return mySubDigraphWalk_Time;
    }
    public static EdgeWeightedDigraph getMySubDigraphCar_Dist() {
        return mySubDigraphCar_Dist;
    }

    public static EdgeWeightedDigraph getMySubDigraphBike_Dist() {
        return mySubDigraphBike_Dist;
    }

    public static EdgeWeightedDigraph getMySubDigraphWalk_Dist() {
        return mySubDigraphWalk_Dist;
    }

    public static void main(String[] args) {
        DirectedEdge myDirectedEdge1= new DirectedEdge(1,2,1.0);
        DirectedEdge myDirectedEdge2= new DirectedEdge(1,0,3.0);
        DirectedEdge myDirectedEdge3= new DirectedEdge(2,3,4.0);
        myDigraph.addEdge(myDirectedEdge1);
        myDigraph.addEdge(myDirectedEdge2);
        myDigraph.addEdge(myDirectedEdge3);
        System.out.println(myDigraph);
    }
}
