package edu.ufp.inf.Projeto.Classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class Tags {
    private static final HashMap<String, ArrayList<String>> myHashMap = new HashMap<>();
    private static final HashMap<String, ArrayList<Nodes>> myHashMapNodes = new HashMap<>();
    private static final HashMap<String, ArrayList<Ways>> myHashMapWays = new HashMap<>();

    public static HashMap<String, ArrayList<String>> getMyHashMap() {
        return myHashMap;
    }

    public static HashMap<String, ArrayList<Nodes>> getMyHashMapNodes() {
        return myHashMapNodes;
    }

    public static HashMap<String, ArrayList<Ways>> getMyHashMapWays() {
        return myHashMapWays;
    }

    public static void main(String[] args) {
        getMyHashMap().put("Amenity", new ArrayList<>(Arrays.asList("bar", "biergarten", "cafe", "fast_food", "food_court", "ice_cream", "school", "car_wash", "atm", "dentist", "veterinary", "casino", "nightclub", "stripclub", "prison", "bench", "telephone", "toilets", "recycling", "clock", "sports_center", "internet_cafe", "drinking_water", "police", "theatre", "planetarium")));
        getMyHashMap().put("Barrier", new ArrayList<>(Arrays.asList("hedge", "kerb", "wall", "handrail", "cable_barrier", "fence", "rope", "swing_gate", "stile", "lift_gate")));
        getMyHashMap().put("Building", new ArrayList<>(Arrays.asList("house", "office", "retail", "industrial", "commercial", "chapel", "cathedral", "religious", "temple", "synagogue", "church", "university")));
        System.out.println(getMyHashMap());

        Ways ways1 = new Ways("bessa", 1, "rua ze de agrelo", 1, new Date().getTime(),true,false,true);
        ArrayList<Ways> y = new ArrayList<>();
        y.add(ways1);
        Tags.getMyHashMapWays().put(myHashMap.get("Amenity").get(0), y);
        System.out.println(getMyHashMapWays());

        Nodes node1 = new Nodes("Bessa", 1, 1, new Date().getTime(), 45444, 44541);
        ArrayList<Nodes> x = new ArrayList<>();
        x.add(node1);
        Tags.getMyHashMapNodes().put(myHashMap.get("Barrier").get(2),x);
        System.out.println(getMyHashMapNodes());


    }
}
