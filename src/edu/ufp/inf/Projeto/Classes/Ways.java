package edu.ufp.inf.Projeto.Classes;

import edu.princeton.cs.algs4.RedBlackBST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Ways extends Object {
    private static RedBlackBST<Integer, Ways> myRedBlackBST_Ways = new RedBlackBST<>();
    String nameWay;
    protected static Integer staticID = 0;
    private final Integer ID;
    private final Nodes[] arrayNodes = new Nodes[2];
    private final ArrayList<String> tags_Ways = new ArrayList<>();
    private Long ID_NodeOrg;
    private Long ID_NodeDest;
    private final boolean tipoViaCarro;
    private final boolean tipoViaBicicleta;
    private final boolean tipoViaPe;


    public static RedBlackBST<Integer, Ways> getMyRedBlackBST_Ways() {
        return myRedBlackBST_Ways;
    }

    public Ways(String username, Integer userID, String nameWay, Integer version, long timestamp, boolean tipoViaBicicleta, boolean tipoViaCarro, boolean tipoViaPe) {
        super(username, userID, version, timestamp);
        staticID++;
        this.ID = staticID;
        this.nameWay = nameWay;
        this.ID_NodeOrg = null;
        this.ID_NodeDest = null;
        super.setVersion(version);
        this.tipoViaBicicleta = tipoViaBicicleta;
        this.tipoViaCarro = tipoViaCarro;
        this.tipoViaPe = tipoViaPe;

    }

    public static void setMyRedBlackBST_Ways(RedBlackBST<Integer, Ways> myRedBlackBST_Ways) {
        Ways.myRedBlackBST_Ways = myRedBlackBST_Ways;
    }

    public ArrayList<String> getTags_Ways() {
        return tags_Ways;
    }

    public boolean isTipoViaCarro() {
        return tipoViaCarro;
    }

    public boolean isTipoViaBicicleta() {
        return tipoViaBicicleta;
    }

    public boolean isTipoViaPe() {
        return tipoViaPe;
    }

    public Integer getID() {
        return ID;
    }

    public String getNameWay() {
        return nameWay;
    }

    public void setNameWay(String nameWay) {
        this.nameWay = nameWay;
    }

    public Nodes[] getArrayNodes() {
        return arrayNodes;
    }

    public void setArrayNodes(Nodes a, Nodes b) {
        this.arrayNodes[0] = a;
        this.arrayNodes[1] = b;
    }

    public long getID_NodeOrg() {
        return ID_NodeOrg;
    }

    public void setID_NodeOrg(long ID_NodeOrg) {
        this.ID_NodeOrg = ID_NodeOrg;
    }

    public long getID_NodeDest() {
        return ID_NodeDest;
    }

    public void setID_NodeDest(long ID_NodeDest) {
        this.ID_NodeDest = ID_NodeDest;
    }

    @Override
    public String toString() {
        return "Ways{" +
                "nameWay='" + nameWay + '\'' +
                ", ID=" + ID +
                ", arrayNodes=" + Arrays.toString(arrayNodes) +
                ", ID_NodeOrg=" + ID_NodeOrg +
                ", ID_NodeDest=" + ID_NodeDest +
                ", tags=" + tags_Ways +
                ", tipoViaCarro=" + tipoViaCarro +
                ", tipoViaBicicleta=" + tipoViaBicicleta +
                ", tipoViaPe=" + tipoViaPe +
                '}';
    }

    public static void main(String[] args) {
        Ways ways1 = new Ways("bessa", 1, "rua ze de agrelo", 1, new Date().getTime(),true,false,true);
        System.out.println(ways1);
        System.out.println(ways1.getTimestamp());
    }
}
