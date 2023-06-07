package edu.ufp.inf.Projeto.Main;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.ufp.inf.Projeto.Classes.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        String fileUsers = "./data/fileUsers.txt";
        String fileNodes_PoIs = "./data/fileNodes_PoIs.txt";
        String fileWays = "./data/fileWays.txt";
        String fileTags = "./data/fileTags.txt";


        loadUsersFile(fileUsers);
        loadNodes_PoIsFile(fileNodes_PoIs);
        loadWaysFile(fileWays);
        loadTagsFile(fileTags);

        //createAdminUser("Jacinta");
        //createBasicUser("Marcos");

        AdminUser.getMyST_adminUser().get("Bessa").addTagNode(1, "Amenity:bar");
        AdminUser.getMyST_adminUser().get("Bessa").addTagWay(1, "Amenity:bar");

        System.out.println("\n" + "------------------------------------------------" + "\n");

        AdminUser.getMyST_adminUser().get("Miguelito").go("Estádio", "Café", false, false);
        BasicUser.getMyST_basicUser().get("Jacinta").go("Estádio", "Café", false, false);
        AdminUser.getMyST_adminUser().get("Joao").go("Estádio", "Café", false, false);
        AdminUser.getMyST_adminUser().get("Bessa").go("Estádio", "Café", false, false);
        AdminUser.getMyST_adminUser().get("Nuno").go("Shopping", "Estádio", false, true);
        AdminUser.getMyST_adminUser().get("Diana").go("Estádio", "Café", false, false);

        sleep(1000);
        AdminUser.getMyST_adminUser().get("Diana").go("Café", "Faculdade", false, false);
        AdminUser.getMyST_adminUser().get("Nuno").go("Estádio", "Restaurante", true, false);
        sleep(1000);

        System.out.println("\n" + "------------------------------------------------" + "\n");
        AdminUser.getMyST_adminUser().get("Diana").now();
        System.out.println("\n" + "------------------------------------------------" + "\n");

        AdminUser.getMyST_adminUser().get("Nuno").not_visited(new Date().getTime()-10000, new Date().getTime() + 10000);
        AdminUser.getMyST_adminUser().get("Nuno").visited(new Date().getTime()-10000, new Date().getTime() + 10000);
        AdminUser.getMyST_adminUser().get("Nuno").PoI_not_visited(new Date().getTime()-10000, new Date().getTime() + 10000);
        AdminUser.getMyST_adminUser().get("Nuno").visit_PoI("Faculdade", new Date().getTime()-10000 , new Date().getTime() + 10000);
        AdminUser.getMyST_adminUser().get("Nuno").top5Users(new Date().getTime()-10000, new Date().getTime() + 10000);
        AdminUser.getMyST_adminUser().get("Nuno").top5Visited(new Date().getTime()-10000, new Date().getTime() + 10000);

        System.out.println("\n" + "------------------------------------------------" + "\n");

        AdminUser.getMyST_adminUser().get("Diana").searchTag("Amenity:bar");

        System.out.println("\n" + "------------------------------------------------" + "\n");

        AdminUser.getMyST_adminUser().get("Nuno").addTrafficSign("Passadeira de Paranhos", 30.24582094747255, -16.261324216190015);
        AdminUser.getMyST_adminUser().get("Nuno").addTrafficSign("Passadeira de Penafiel", 60.24582094747255, -56.261324216190015);
        AdminUser.getMyST_adminUser().get("Bessa").addTrafficSign("Stop", 81.24582094747255, -16.261324216190015);
        AdminUser.getMyST_adminUser().get("Diogo").addTrafficSign("Cedência de passagem", 88.24582094747255, -26.261324216190015);
        AdminUser.getMyST_adminUser().get("Diogo").addTrafficSign("Sentido Proibido", 15.24582094747255, -100.261324216190015);
        AdminUser.getMyST_adminUser().get("Bessa").addTrafficSign("Sentido único", 50.24582094747255, -80.261324216190015);
        AdminUser.getMyST_adminUser().get("Miguelito").mostNextofCoordinate(25.24582094747255, -20.261324216190015, 10000);
        AdminUser.getMyST_adminUser().get("Bessa").mostNextofCoordinate(81.14582094747255, -15.161324216190015, 5000);
        BasicUser.getMyST_basicUser().get("Tiago").mostNextofCoordinate(70.14582094747255, -8.161324216190015, 3000);

        System.out.println("\n" + "------------------------------------------------" + "\n");

        //AdminUser.getMyST_adminUser().get("Nuno").deleteNodes(1);
        //AdminUser.getMyST_adminUser().get("Nuno").deleteWays(1);
        AdminUser.getMyST_adminUser().get("Bessa").searchNodes(1);
        AdminUser.getMyST_adminUser().get("Bessa").searchWays(1);
        AdminUser.getMyST_adminUser().get("Miguelito").editWays(1,"Funchal");
        AdminUser.getMyST_adminUser().get("Nuno").createWays("Açores",22,15,true,false,true);

        System.out.println("\n" + "------------------------------------------------" + "\n");

        System.out.println("GRAFO PRINCIPAL com distancia em KM " + Grafo.getMyDigraph());
        System.out.println("Sub grafo BIKE com tempo em horas  " + Grafo.getMySubDigraphBike_Time());
        System.out.println("Sub grafo CAR com tempo em horas  " + Grafo.getMySubDigraphCar_Time());
        System.out.println("Sub grafo WALK com tempo em horas  " + Grafo.getMySubDigraphWalk_Time());
        System.out.println("Sub grafo BIKE distancia em km  " + Grafo.getMySubDigraphBike_Dist());
        System.out.println("Sub grafo CAR distancia em km  " + Grafo.getMySubDigraphCar_Dist());
        System.out.println("Sub grafo WALK distancia em km  " + Grafo.getMySubDigraphWalk_Dist());

        exportUsers(fileUsers);
        exportNodes_PoIs(fileNodes_PoIs);
        exportWays(fileWays);
    }

    public static void createAdminUser(String name_user) {
        if (AdminUser.getMyST_adminUser().contains(name_user)) {
            System.out.println("User com o nome " + name_user + " já criado!");
        } else {
            AdminUser adminUser = new AdminUser(name_user);
            AdminUser.getMyST_adminUser().put(name_user, adminUser);
        }
    }

    public static void createBasicUser(String name_user) {
        if (BasicUser.getMyST_basicUser().contains(name_user)) {
            System.out.println("User com o nome " + name_user + " já criado!");
        } else {
            BasicUser basicuser = new BasicUser(name_user);
            BasicUser.getMyST_basicUser().put(name_user, basicuser);
        }
    }

    public static void exportUsers(String fileUsers) {
        Out myOut = new Out(fileUsers);
        for (String user : AdminUser.getMyST_adminUser().keys()) {
            myOut.println(AdminUser.getMyST_adminUser().get(user).getName() + ",admin");
        }
        for (String user : BasicUser.getMyST_basicUser().keys()) {
            myOut.println(BasicUser.getMyST_basicUser().get(user).getName_basicUser() + ",basic");

        }
    }

    public static void exportNodes_PoIs(String fileNodes_PoIs) {
        Out myOut = new Out(fileNodes_PoIs);

        for (Integer i : Nodes.getMyRedBlackBST_Nodes().keys()) {
            if (Nodes.getMyRedBlackBST_Nodes().get(i) instanceof PoI) {
                String name_PoI = ((PoI) Nodes.getMyRedBlackBST_Nodes().get(i)).getName();
                myOut.println(Nodes.getMyRedBlackBST_Nodes().get(i).getUsername() + "," +
                        Nodes.getMyRedBlackBST_Nodes().get(i).getLatitude() + "," +
                        Nodes.getMyRedBlackBST_Nodes().get(i).getLongitude() + "," + name_PoI);
            } else {
                myOut.println(Nodes.getMyRedBlackBST_Nodes().get(i).getUsername() + "," +
                        Nodes.getMyRedBlackBST_Nodes().get(i).getLatitude() + "," +
                        Nodes.getMyRedBlackBST_Nodes().get(i).getLongitude());
            }
        }
    }

    public static void exportWays(String fileWays) {
        Out myOut = new Out(fileWays);
        for (Integer i : Ways.getMyRedBlackBST_Ways().keys()) {
            myOut.println(Ways.getMyRedBlackBST_Ways().get(i).getUsername() + ","
                    + Ways.getMyRedBlackBST_Ways().get(i).getNameWay() + "," +
                    Ways.getMyRedBlackBST_Ways().get(i).getID_NodeOrg() + "," +
                    Ways.getMyRedBlackBST_Ways().get(i).getID_NodeDest() + "," +
                    Ways.getMyRedBlackBST_Ways().get(i).isTipoViaBicicleta() + "," +
                    Ways.getMyRedBlackBST_Ways().get(i).isTipoViaCarro() + "," +
                    Ways.getMyRedBlackBST_Ways().get(i).isTipoViaPe());
        }
    }

    public static void loadUsersFile(String fileUsers) {
        File myfile = new File(fileUsers);
        if (!myfile.canExecute()) {
            return;
        }
        In in = new In(fileUsers);
        String name_user;
        String BasicOrAdmin;
        while (!in.isEmpty()) {
            String[] buffer = in.readLine().split(",");
            name_user = buffer[0];
            BasicOrAdmin = buffer[1];
            if (Objects.equals(buffer[1], "basic")) {
                if (BasicUser.getMyST_basicUser().contains(name_user)) {
                    System.out.println("User com o nome " + name_user + " já criado!");
                } else {
                    BasicUser basicUser = new BasicUser(name_user);
                    BasicUser.getMyST_basicUser().put(name_user, basicUser);
                    //System.out.println(BasicUser.getMyST_basicUser().get(name_user));
                }
            } else {
                if (AdminUser.getMyST_adminUser().contains(name_user)) {
                    System.out.println("User com o nome " + name_user + " já criado!");
                } else {
                    AdminUser adminUser = new AdminUser(name_user);
                    AdminUser.getMyST_adminUser().put(name_user, adminUser);
                    //System.out.println(AdminUser.getMyST_adminUser().get(name_user));
                }
            }
        }

    }

    public static void loadNodes_PoIsFile(String fileNodes_PoIs) {
        File myfile = new File(fileNodes_PoIs);
        if (!myfile.canExecute()) {
            return;
        }
        In in = new In(fileNodes_PoIs);
        String name_user;
        String name_PoI;
        double latitude;
        double longitude;
        while (!in.isEmpty()) {
            String[] buffer = in.readLine().split(",");
            name_user = buffer[0];
            latitude = Double.parseDouble(buffer[1]);
            longitude = Double.parseDouble(buffer[2]);
            if (buffer.length == 4) {
                name_PoI = buffer[3];
                if (AdminUser.getMyST_adminUser().get(name_user) == null) {
                    System.out.println("Node não criado, pois não existe nenhum user com o nome " + name_user);
                } else {
                    AdminUser.getMyST_adminUser().get(name_user).createPoI(name_PoI, latitude, longitude);
                }
            } else {
                if (AdminUser.getMyST_adminUser().get(name_user) == null) {
                    System.out.println("Node não criado, pois não existe nenhum user com o nome " + name_user);
                } else {
                    AdminUser.getMyST_adminUser().get(name_user).createNodes(latitude, longitude);
                }
            }
        }
    }

    public static void loadWaysFile(String fileWays) {
        File myfile = new File(fileWays);
        if (!myfile.canExecute()) {
            return;
        }
        In in = new In(fileWays);
        String name_user;
        String nameWay;
        long ID_NodeOrg;
        long ID_NodeDest;
        boolean tipoViaBicicleta;
        boolean tipoViaCarro;
        boolean tipoViaPe;
        while (!in.isEmpty()) {
            String[] buffer = in.readLine().split(",");
            name_user = buffer[0];
            nameWay = buffer[1];
            ID_NodeOrg = Long.parseLong(buffer[2]);
            ID_NodeDest = Long.parseLong(buffer[3]);
            tipoViaBicicleta = Boolean.parseBoolean(buffer[4]);
            tipoViaCarro = Boolean.parseBoolean(buffer[5]);
            tipoViaPe = Boolean.parseBoolean(buffer[6]);
            if (AdminUser.getMyST_adminUser().get(name_user) == null) {
                System.out.println("Way não criada, pois não existe nenhum user com o nome " + name_user);
            } else {
                AdminUser.getMyST_adminUser().get(name_user).createWays(nameWay, ID_NodeOrg, ID_NodeDest, tipoViaBicicleta, tipoViaCarro, tipoViaPe);
            }
        }
    }

    public static void loadTagsFile(String fileTags) {
        File myfile = new File(fileTags);
        if (!myfile.canExecute()) {
            return;
        }
        In in = new In(fileTags);
        while (!in.isEmpty()) {
            String[] buffer = in.readLine().split(",");
            Tags.getMyHashMap().put(buffer[0], new ArrayList<>(Arrays.asList(buffer)));
            Tags.getMyHashMap().get(buffer[0]).remove(0);
        }
    }
}


