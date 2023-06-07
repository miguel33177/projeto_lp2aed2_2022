package edu.ufp.inf.Projeto.Classes;

import edu.princeton.cs.algs4.*;

import java.util.*;
import java.util.Date;

import static java.lang.Thread.sleep;

public class AdminUser {
    private static final ST<String, AdminUser> myST_adminUser = new ST<>();
    private static HashMap<String, ArrayList<PoI>> myHashMap;
    String name;
    protected static Integer staticID_User = 0;
    private final Integer adminID;


    public AdminUser(String name) {
        staticID_User++;
        this.name = name;
        this.adminID = staticID_User;
        this.myHashMap = new HashMap<>();
    }

    public HashMap<String, ArrayList<PoI>> getMyHashMap() {
        return myHashMap;
    }

    public static ST<String, AdminUser> getMyST_adminUser() {
        return myST_adminUser;
    }

    public String getName() {
        return name;
    }

    public Integer getID() {
        return adminID;
    }

    @Override
    public String toString() {
        return "AdminUser{" +
                "myHashMap=" + myHashMap +
                ", name='" + name + '\'' +
                ", adminID=" + adminID +
                '}';
    }

    /**
     * Funçao para criar um node, inicialmente tem uma condiçao para que a primeira vez que crie um node o crie normalmente. A partir da segunda vez para criar o node
     * verifica primeiro se já não há fileNodes_PoIs.txt com essa latitude e longitude, de modo a garantir que todos os fileNodes_PoIs.txt sao unicos.
     *
     * @param Latitude  Latitude do do ponto de interesse a inserir pelo admin user
     * @param Longitude Longitude do do ponto de interesse a inserir pelo admin user
     */
    public void createNodes(double Latitude, double Longitude) {
        if (Nodes.getMyRedBlackBST_Nodes().size() >= 1) {
            int aux = 0;
            Nodes myNode = new Nodes(this.getName(), this.getID(), 1, new Date().getTime(), Latitude, Longitude);
            if ((Latitude >= -90 && Latitude <= 90) && (Longitude >= -180 && Longitude <= 180)) {
                for (int i = 0; i <= Nodes.getMyRedBlackBST_Nodes().size(); i++) {
                    if ((Nodes.getMyRedBlackBST_Nodes().get(i) != null && Nodes.getMyRedBlackBST_Nodes().get(i).getLatitude() == Latitude) && (Nodes.getMyRedBlackBST_Nodes().get(i) != null && Nodes.getMyRedBlackBST_Nodes().get(i).getLongitude() == Longitude)) {
                        aux = 1;
                        break;
                    } else {
                        aux = 2;
                    }
                }
                if (aux == 1) {
                    System.out.println("Latitude/Longitude já em uso. Node não criado!");
                } else {
                    Nodes.getMyRedBlackBST_Nodes().put(myNode.getID(), myNode);
                }
            } else {
                System.out.println("Valores de Latitude/Longitude não válidos");
            }

        } else {
            Nodes myNode = new Nodes(this.getName(), this.getID(), 1, new Date().getTime(), Latitude, Longitude);
            if ((Latitude >= -90 && Latitude <= 90) && (Longitude >= -180 && Longitude <= 180)) {
                Nodes.getMyRedBlackBST_Nodes().put(myNode.getID(), myNode);
            } else {
                System.out.println("Valores de Latitude/Longitude não válidos");
            }
        }
    }

    /**
     * Funçao para criar ways, inicialmente começa logo por verificar se os fileNodes_PoIs.txt escolhidos para a way existem, caso existam entao cria a way. De seguida, cria um grafo
     * e guardamos a way com o respetivo peso tambem calculado pela funcao haversine. Depois sao criados 3 sub grafos, um para cada tipo de via.
     *
     * @param nameWay          nome da way
     * @param ID_NodeOrg       id do node origem
     * @param ID_NodeDest      id do node destino
     * @param tipoViaBicicleta tipo de via se é bicicleta
     * @param tipoViaCarro     tipo de via se é carro
     * @param tipoViaPe        tipo de via se é a pé
     */

    public void createWays(String nameWay, long ID_NodeOrg, long ID_NodeDest, boolean tipoViaBicicleta, boolean tipoViaCarro, boolean tipoViaPe) {
        if (ID_NodeOrg == ID_NodeDest) {
            System.out.println("Node origem e node destino nao podem ser iguais!");
            return;
        }
        for (Integer i : Ways.getMyRedBlackBST_Ways().keys()) {
            if (Objects.equals(Ways.getMyRedBlackBST_Ways().get(i).getNameWay(), nameWay) && Ways.getMyRedBlackBST_Ways().get(i).getID_NodeOrg() == ID_NodeOrg &&
                    Ways.getMyRedBlackBST_Ways().get(i).getID_NodeDest() == ID_NodeDest) {
                System.out.println("Way não criada, pois já existe essa Way " + "( " + Ways.getMyRedBlackBST_Ways().get(i) + " )");
                return;
            }
        }
        if ((Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeDest) == null || Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeOrg) == null) ||
                (!tipoViaBicicleta && !tipoViaCarro && !tipoViaPe)) {
            System.out.println("Way não criada, verique se os nodes inseridos existem e se escolheu pelo menos um tipo de via para a way!");
        } else {
            Ways myWay = new Ways(this.getName(), this.getID(), nameWay, 1, new Date().getTime(), tipoViaBicicleta, tipoViaCarro, tipoViaPe);
            myWay.setID_NodeOrg(ID_NodeOrg);
            myWay.setID_NodeDest(ID_NodeDest);
            Ways.getMyRedBlackBST_Ways().put(myWay.getID(), myWay);
            myWay.setArrayNodes(Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeOrg), Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeDest));
            DirectedEdge myDirectedEdge = new DirectedEdge((int) ID_NodeOrg, (int) ID_NodeDest,
                    haversineDistanceTime(Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeOrg).getLatitude(),
                            Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeOrg).getLongitude(),
                            Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeDest).getLatitude(),
                            Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeDest).getLongitude()));
            Grafo.getMyDigraph().addEdge(myDirectedEdge);

            if (Ways.getMyRedBlackBST_Ways().get(myWay.getID()).isTipoViaBicicleta()) {
                DirectedEdge myDirectedEdgeBike_Time = new DirectedEdge((int) ID_NodeOrg, (int) ID_NodeDest,
                        haversineDistanceTime(Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeOrg).getLatitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeOrg).getLongitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeDest).getLatitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeDest).getLongitude() / 10)); //time em horas
                Grafo.getMySubDigraphBike_Time().addEdge(myDirectedEdgeBike_Time);

                DirectedEdge myDirectedEdgeBike_Dist = new DirectedEdge((int) ID_NodeOrg, (int) ID_NodeDest,
                        haversineDistanceTime(Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeOrg).getLatitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeOrg).getLongitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeDest).getLatitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeDest).getLongitude()));
                Grafo.getMySubDigraphBike_Dist().addEdge(myDirectedEdgeBike_Dist);

            }
            if (Ways.getMyRedBlackBST_Ways().get(myWay.getID()).isTipoViaCarro()) {
                DirectedEdge myDirectedEdgeCar = new DirectedEdge((int) ID_NodeOrg, (int) ID_NodeDest,
                        haversineDistanceTime(Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeOrg).getLatitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeOrg).getLongitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeDest).getLatitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeDest).getLongitude()) / 50);
                Grafo.getMySubDigraphCar_Time().addEdge(myDirectedEdgeCar);

                DirectedEdge myDirectedEdgeCar_Dist = new DirectedEdge((int) ID_NodeOrg, (int) ID_NodeDest,
                        haversineDistanceTime(Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeOrg).getLatitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeOrg).getLongitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeDest).getLatitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeDest).getLongitude()));
                Grafo.getMySubDigraphCar_Dist().addEdge(myDirectedEdgeCar_Dist);
            }

            if (Ways.getMyRedBlackBST_Ways().get(myWay.getID()).isTipoViaPe()) {
                DirectedEdge myDirectedEdgeWalk = new DirectedEdge((int) ID_NodeOrg, (int) ID_NodeDest,
                        haversineDistanceTime(Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeOrg).getLatitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeOrg).getLongitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeDest).getLatitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeDest).getLongitude()) / 5);
                Grafo.getMySubDigraphWalk_Time().addEdge(myDirectedEdgeWalk);

                DirectedEdge myDirectedEdgeWalk_Dist = new DirectedEdge((int) ID_NodeOrg, (int) ID_NodeDest,
                        haversineDistanceTime(Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeOrg).getLatitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeOrg).getLongitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeDest).getLatitude(),
                                Nodes.getMyRedBlackBST_Nodes().get((int) ID_NodeDest).getLongitude()));
                Grafo.getMySubDigraphWalk_Dist().addEdge(myDirectedEdgeWalk_Dist);

            }
        }
    }

    /**
     * Funçao para eliminar um node, e de seguida eliminar o node das ways em que estiver
     *
     * @param ID_NODE id do node a eliminar
     */
    public void deleteNodes(Integer ID_NODE) {
        Nodes.getMyRedBlackBST_Nodes().delete(ID_NODE);
        for (int i = 1; i <= Ways.getMyRedBlackBST_Ways().size(); i++) {
            if (Ways.getMyRedBlackBST_Ways().get(i) != null && Ways.getMyRedBlackBST_Ways().get(i).getID_NodeOrg() == ID_NODE) {
                Ways.getMyRedBlackBST_Ways().delete(i);
            }
            if (Ways.getMyRedBlackBST_Ways().get(i) != null && Ways.getMyRedBlackBST_Ways().get(i).getID_NodeDest() == ID_NODE) {
                Ways.getMyRedBlackBST_Ways().delete(i);
            }
        }
        refreshGraphs();
    }

    /**
     * Atualizar grafos depois da remocao da way/node
     *
     */
    private void refreshGraphs() {
        Grafo.setMyDigraph(new EdgeWeightedDigraph(Nodes.getMyRedBlackBST_Nodes().max() + 1));
        Grafo.setMySubDigraphBike_Dist(new EdgeWeightedDigraph(Nodes.getMyRedBlackBST_Nodes().max() + 1));
        Grafo.setMySubDigraphBike_Time(new EdgeWeightedDigraph(Nodes.getMyRedBlackBST_Nodes().max() + 1));
        Grafo.setMySubDigraphCar_Dist(new EdgeWeightedDigraph(Nodes.getMyRedBlackBST_Nodes().max() + 1));
        Grafo.setMySubDigraphCar_Time(new EdgeWeightedDigraph(Nodes.getMyRedBlackBST_Nodes().max() + 1));
        Grafo.setMySubDigraphWalk_Dist(new EdgeWeightedDigraph(Nodes.getMyRedBlackBST_Nodes().max() + 1));
        Grafo.setMySubDigraphWalk_Time(new EdgeWeightedDigraph(Nodes.getMyRedBlackBST_Nodes().max() + 1));

        for (Integer i : Ways.getMyRedBlackBST_Ways().keys()) {
            if (Nodes.getMyRedBlackBST_Nodes().get((int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeOrg()) != null &&
                    Nodes.getMyRedBlackBST_Nodes().get((int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeOrg()) != null &&
                    Nodes.getMyRedBlackBST_Nodes().get((int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeDest()) != null &&
                    Nodes.getMyRedBlackBST_Nodes().get((int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeDest()) != null) {

                double weight = haversineDistanceTime(Nodes.getMyRedBlackBST_Nodes().get((int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeOrg()).getLatitude(),
                        Nodes.getMyRedBlackBST_Nodes().get((int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeOrg()).getLongitude(),
                        Nodes.getMyRedBlackBST_Nodes().get((int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeDest()).getLatitude(),
                        Nodes.getMyRedBlackBST_Nodes().get((int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeDest()).getLongitude());

                DirectedEdge myDirectedEdge = new DirectedEdge((int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeOrg(), (int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeDest(), weight);
                Grafo.getMyDigraph().addEdge(myDirectedEdge);

                if (Ways.getMyRedBlackBST_Ways().get(i).isTipoViaPe()) {
                    DirectedEdge myDirectedEdgePe_Time = new DirectedEdge((int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeOrg(), (int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeDest(), weight / 5);
                    DirectedEdge myDirectedEdgePe_Dist = new DirectedEdge((int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeOrg(), (int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeDest(), weight);
                    Grafo.getMySubDigraphWalk_Time().addEdge(myDirectedEdgePe_Time);
                    Grafo.getMySubDigraphWalk_Dist().addEdge(myDirectedEdgePe_Dist);
                }
                if (Ways.getMyRedBlackBST_Ways().get(i).isTipoViaCarro()) {
                    DirectedEdge myDirectedEdgeCar_Time = new DirectedEdge((int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeOrg(), (int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeDest(), weight / 50);
                    DirectedEdge myDirectedEdgeCar_Dist = new DirectedEdge((int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeOrg(), (int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeDest(), weight);
                    Grafo.getMySubDigraphCar_Time().addEdge(myDirectedEdgeCar_Time);
                    Grafo.getMySubDigraphCar_Dist().addEdge(myDirectedEdgeCar_Dist);
                }
                if (Ways.getMyRedBlackBST_Ways().get(i).isTipoViaBicicleta()) {
                    DirectedEdge myDirectedEdgeBike_Time = new DirectedEdge((int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeOrg(), (int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeDest(), weight / 10);
                    DirectedEdge myDirectedEdgeBike_Dist = new DirectedEdge((int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeOrg(), (int) Ways.getMyRedBlackBST_Ways().get(i).getID_NodeDest(), weight);
                    Grafo.getMySubDigraphBike_Time().addEdge(myDirectedEdgeBike_Time);
                    Grafo.getMySubDigraphBike_Dist().addEdge(myDirectedEdgeBike_Dist);
                }
            }
        }

    }

    /**
     * Funcao para eliminar uma way
     *
     * @param ID_WAY id da way a eliminar
     */

    public void deleteWays(Integer ID_WAY) {
        Ways.getMyRedBlackBST_Ways().delete(ID_WAY);
        refreshGraphs();

    }

    /* public void editNodes(Integer ID_NODE, String name,double latitude,double longitude) {
     }*/

    /**
     * Funcao para editar nome da way, alem disso atualiza a versao da way
     *
     * @param ID_WAY  id da way a editar
     * @param nameWay nome novo da way
     */
    public void editWays(Integer ID_WAY, String nameWay) {
        Ways.getMyRedBlackBST_Ways().get(ID_WAY).setNameWay(nameWay);
        Ways.getMyRedBlackBST_Ways().get(ID_WAY).setVersion(Ways.getMyRedBlackBST_Ways().get(ID_WAY).getVersion() + 1);
    }

    /**
     * Funcao para procurar um determinado node caso exista, caso contrario imprime node nao encontrado.
     *
     * @param ID_NODE id do Node a procurar
     */
    public void searchNodes(Integer ID_NODE) {
        if (Nodes.getMyRedBlackBST_Nodes().get(ID_NODE) != null) {
            System.out.println("SEARCH NODES " + Nodes.getMyRedBlackBST_Nodes().get(ID_NODE));

        } else {
            System.out.println("Node não encontrado!");
        }
    }

    /**
     * Funcao para procurar uma determinada way caso exista, caso contrario imprime way nao encontrada.
     *
     * @param ID_WAY id da way a procurar
     */
    public void searchWays(Integer ID_WAY) {
        if (Ways.getMyRedBlackBST_Ways().get(ID_WAY) != null) {
            System.out.println("SEARCH WAYS " + Ways.getMyRedBlackBST_Ways().get(ID_WAY));
        } else {
            System.out.println("Way não encontrada!");
        }
    }

    /**
     * Funçao para criar um PoI, que é um node, inicialmente tem uma condiçao para que a primeira vez que crie um PoI o crie normalmente. A partir da segunda vez para criar o PoI
     * verifica primeiro se já não há fileNodes_PoIs.txt com essa latitude e longitude, de modo a garantir que todos os fileNodes_PoIs.txt sao unicos.
     *
     * @param name      nome do ponto de interesse a inserir pelo admin user
     * @param Latitude  Latitude do do ponto de interesse a inserir pelo admin user
     * @param Longitude Longitude do do ponto de interesse a inserir pelo admin user
     */

    public void createPoI(String name, double Latitude, double Longitude) {

        if (Nodes.getMyRedBlackBST_Nodes().size() >= 1) {
            int aux = 0;
            PoI nodePoI = new PoI(this.getName(), this.getID(), name, 1, new Date().getTime(), Latitude, Longitude);
            if ((Latitude >= -90 && Latitude <= 90) && (Longitude >= -180 && Longitude <= 180)) {
                for (int i = 0; i <= Nodes.getMyRedBlackBST_Nodes().size(); i++) {
                    if ((Nodes.getMyRedBlackBST_Nodes().get(i) != null && Nodes.getMyRedBlackBST_Nodes().get(i).getLatitude() == Latitude) && (Nodes.getMyRedBlackBST_Nodes().get(i) != null && Nodes.getMyRedBlackBST_Nodes().get(i).getLongitude() == Longitude)) {
                        aux = 1;
                        break;
                    } else {
                        aux = 2;
                    }
                }
                if (aux == 1) {
                    System.out.println("Latitude/Longitude já em uso. PoI não criado!");
                } else {
                    Nodes.getMyRedBlackBST_Nodes().put(nodePoI.getID(), nodePoI);
                    PoI.getMyRedBlackBST_PoI().put(name, nodePoI);
                }
            } else {
                System.out.println("Valores de Latitude/Longitude não válidos");
            }
        } else {
            PoI nodePoI = new PoI(this.getName(), this.getID(), name, 1, new Date().getTime(), Latitude, Longitude);
            if ((Latitude >= -90 && Latitude <= 90) && (Longitude >= -180 && Longitude <= 180)) {
                Nodes.getMyRedBlackBST_Nodes().put(nodePoI.getID(), nodePoI);
                PoI.getMyRedBlackBST_PoI().put(name, nodePoI);
            } else {
                System.out.println("Valores de Latitude/Longitude não válidos");
            }
        }
    }

    /**
     * Funçao para adicionar tags a um determinado node, guardar as tags num ArrayList desse node, e guardar num hash map os fileNodes_PoIs.txt usadas em cada tag.
     *
     * @param ID_NODE id do node a inserir a tag
     * @param tag     tag a inserir pelo adminUser
     */

    public void addTagNode(Integer ID_NODE, String tag) {
        StringBuilder key = new StringBuilder();
        StringBuilder val = new StringBuilder();
        int count = 0;
        for (int i = 0; i < tag.length(); i++) {
            if (tag.charAt(i) == ':') {
                count++;
            }
            if (count == 0) {
                key.append(tag.charAt(i));
            }
            if (count == 1) {
                if (tag.charAt(i) != ':') {
                    val.append(tag.charAt(i));
                }
            }
        }
        if (count != 1) {
            System.out.println("Formato inválido! (Formato correto String:String)");
        }
        if (AdminUser.getMyST_adminUser().get(name) != null) {
            if (Nodes.getMyRedBlackBST_Nodes().get(ID_NODE) != null) {
                if (Tags.getMyHashMap().containsKey(key.toString()) && Tags.getMyHashMap().get(key.toString()).contains(val.toString())) {
                    if (!Tags.getMyHashMapNodes().containsKey(key + ":" + val)) {
                        Nodes.getMyRedBlackBST_Nodes().get(ID_NODE).getTags().add(tag);
                        ArrayList<Nodes> x = new ArrayList<>();
                        x.add(Nodes.getMyRedBlackBST_Nodes().get(ID_NODE));
                        Tags.getMyHashMapNodes().put(key + ":" + val, x);
                    } else {
                        Tags.getMyHashMapNodes().get(key + ":" + val).add(Nodes.getMyRedBlackBST_Nodes().get(ID_NODE));
                    }
                } else {
                    System.out.println("Tag não encontrada!");
                }
            } else {
                System.out.println("Node não criado!");
            }
        } else {
            System.out.println("User nao encontrado!");
        }

    }

    /**
     * Funçao para adicionar tags a uma determinada way, guardar as tags num ArrayList dessa way, e guardar num hash map as ways usadas em cada tag.
     *
     * @param ID_WAY id da way a inserir a tag
     * @param tag    tag a inserir pelo adminUser
     */

    public void addTagWay(Integer ID_WAY, String tag) {
        StringBuilder key = new StringBuilder();
        StringBuilder val = new StringBuilder();

        int count = 0;
        for (int i = 0; i < tag.length(); i++) {
            if (tag.charAt(i) == ':') {
                count++;
            }
            if (count == 0) {
                key.append(tag.charAt(i));
            }
            if (count == 1) {
                if (tag.charAt(i) != ':') {
                    val.append(tag.charAt(i));
                }
            }
        }
        if (count != 1) {
            System.out.println("Formato inválido! (Formato correto String:String)");
        }
        if (Tags.getMyHashMap().containsKey(key.toString()) && Tags.getMyHashMap().get(key.toString()).contains(val.toString())) {
            if (!Tags.getMyHashMapWays().containsKey(key + ":" + val)) {
                if (Ways.getMyRedBlackBST_Ways().get(ID_WAY) != null) {
                    Ways.getMyRedBlackBST_Ways().get(ID_WAY).getTags_Ways().add(tag);
                    ArrayList<Ways> y = new ArrayList<>();
                    y.add(Ways.getMyRedBlackBST_Ways().get(ID_WAY));
                    Tags.getMyHashMapWays().put(key + ":" + val, y);
                } else {
                    System.out.println("Way nao encontrada!");
                }
            } else {
                Tags.getMyHashMapWays().get(key + ":" + val).add(Ways.getMyRedBlackBST_Ways().get(ID_WAY));
            }
        } else {
            System.out.println("Tag não encontrada!");
        }
    }

    /** Função para procurar uma tag e imprimir lista de nodes ou ways associadas a essa tag
     *
     * @param tag tag a procurar
     */
    public void searchTag(String tag) {
        if (Tags.getMyHashMapNodes().get(tag) != null) {
            System.out.println("Lista de Nodes com a tag " + tag);
            System.out.println(Tags.getMyHashMapNodes().get(tag));
        }
        if (Tags.getMyHashMapWays().get(tag) != null) {
            System.out.println("Lista de Ways com a tag " + tag);
            System.out.println(Tags.getMyHashMapWays().get(tag));
        }
    }

    /**
     * Funçao para criar o peso de uma way
     *
     * @param lat1 Latitude NodeOrigem
     * @param lon1 Longitude NodeOrigem
     * @param lat2 Latitude NodeDestino
     * @param lon2 Longitude NodeDestino
     */

    static double haversineDistanceTime(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }

    /** Funcao para preencher o HashMap <PoI, ArrayList<RegistosPoI>>, cada poi tera um arraylist com os users que passaram por esse poi
     *
     * @param poI_Dest PoI a inserir no arraylist de PoIs
     * @param x ArrayList<PoI> x
     */
    private void aux_func_dataGo(String poI_Dest, ArrayList<PoI> x) {
        x.add(PoI.getMyRedBlackBST_PoI().get(poI_Dest));
        getMyHashMap().put(this.getName(), x);

        RegistosPoI myRegistosPoI = new RegistosPoI(this.getName());

        if (PoI.getMyHashMapPoI().get(PoI.getMyRedBlackBST_PoI().get(poI_Dest)) == null) {
            ArrayList<RegistosPoI> y = new ArrayList<>();
            y.add(myRegistosPoI);
            PoI.getMyHashMapPoI().put(PoI.getMyRedBlackBST_PoI().get(poI_Dest), y);
        } else {
            ArrayList<RegistosPoI> y = PoI.getMyHashMapPoI().get(PoI.getMyRedBlackBST_PoI().get(poI_Dest));
            y.add(myRegistosPoI);
            PoI.getMyHashMapPoI().put(PoI.getMyRedBlackBST_PoI().get(poI_Dest), y);
        }
    }

    /**
     *
     * @param poI_Dest PoI a inserir no arraylist de PoIs
     */
    private void data_go(String poI_Dest) {
        if (!getMyHashMap().containsKey(this.getName())) {
            ArrayList<PoI> x = new ArrayList<>();
            aux_func_dataGo(poI_Dest, x);

        } else {
            ArrayList<PoI> x = getMyHashMap().get(this.getName());
            aux_func_dataGo(poI_Dest, x);
        }
    }

    /**
     * Funcao para ir de um PoI a outro com o transporte escolhido
     * @param poI_Org PoI origem
     * @param poI_Dest PoI Destino
     * @param transporteCarro caso seja true transporte escolhido é carro
     * @param transporteBicicleta caso seja true transporte escolhido é bicicleta
     */
    public void go(String poI_Org, String poI_Dest, boolean transporteCarro, boolean transporteBicicleta) {
        if (!PoI.getMyRedBlackBST_PoI().contains(poI_Org) || !PoI.getMyRedBlackBST_PoI().contains(poI_Dest)) {
            System.out.println("PoI nao encontrado!");
        } else {
            if (transporteCarro && transporteBicicleta) {
                System.out.println("Escolha apenas um transporte!");
            } else {
                if (!transporteCarro && !transporteBicicleta) {
                    Integer s = 1;
                    DijkstraSP myDijkstraSP_Time = new DijkstraSP(Grafo.getMySubDigraphWalk_Time(), s);
                    DijkstraSP myDijkstraSP_Dist = new DijkstraSP(Grafo.getMySubDigraphWalk_Dist(), s);
                    Integer id = PoI.getMyRedBlackBST_PoI().get(poI_Dest).getID();
                    if (myDijkstraSP_Time.hasPathTo(id)) {
                        distTo(poI_Dest, myDijkstraSP_Time, myDijkstraSP_Dist);
                        data_go(poI_Dest);
                        PoI lastElement = getMyHashMap().get(this.getName()).get(getMyHashMap().get(this.getName()).size() - 1);
                        ArrayList<RegistosPoI> x = PoI.getMyHashMapPoI().get(lastElement);
                        for (int i = 0; i < x.size(); i++) {
                            if (Objects.equals(x.get(i).getName(), this.getName())) {
                                if (x.get(i).getTimestamp_PoI_ida() == null) {
                                    x.get(i).setTimestamp_PoI_ida(new Date(new Date().getTime() + getRandomNumber(100000, 500000)));
                                }
                            }
                        }
                    } else {
                        System.out.println("Não há caminho de " + poI_Org + " para " + poI_Dest);
                    }
                } else if (transporteCarro) {
                    Integer s = PoI.getMyRedBlackBST_PoI().get(poI_Org).getID();
                    DijkstraSP myDijkstraSP_Time = new DijkstraSP(Grafo.getMySubDigraphCar_Time(), s);
                    DijkstraSP myDijkstraSP_Dist = new DijkstraSP(Grafo.getMySubDigraphCar_Dist(), s);
                    Integer id = PoI.getMyRedBlackBST_PoI().get(poI_Dest).getID();
                    if (myDijkstraSP_Time.hasPathTo(id)) {
                        distTo(poI_Dest, myDijkstraSP_Time, myDijkstraSP_Dist);
                        data_go(poI_Dest);
                        PoI lastElement = getMyHashMap().get(this.getName()).get(getMyHashMap().get(this.getName()).size() - 1);
                        ArrayList<RegistosPoI> x = PoI.getMyHashMapPoI().get(lastElement);
                        for (int i = 0; i < x.size(); i++) {
                            if (Objects.equals(x.get(i).getName(), this.getName())) {
                                if (x.get(i).getTimestamp_PoI_ida() == null) {
                                    x.get(i).setTimestamp_PoI_ida(new Date(new Date().getTime() + getRandomNumber(100000, 500000)));
                                }
                            }
                        }
                    } else {
                        System.out.println("Não há caminho de " + poI_Org + " para " + poI_Dest);
                    }
                } else {
                    Integer s = PoI.getMyRedBlackBST_PoI().get(poI_Org).getID();
                    DijkstraSP myDijkstraSP_Time = new DijkstraSP(Grafo.getMySubDigraphBike_Time(), s);
                    DijkstraSP myDijkstraSP_Dist = new DijkstraSP(Grafo.getMySubDigraphBike_Dist(), s);
                    Integer id = PoI.getMyRedBlackBST_PoI().get(poI_Dest).getID();
                    if (myDijkstraSP_Time.hasPathTo(id)) {
                        distTo(poI_Dest, myDijkstraSP_Time, myDijkstraSP_Dist);
                        data_go(poI_Dest);
                        PoI lastElement = getMyHashMap().get(this.getName()).get(getMyHashMap().get(this.getName()).size() - 1);
                        ArrayList<RegistosPoI> x = PoI.getMyHashMapPoI().get(lastElement);
                        for (int i = 0; i < x.size(); i++) {
                            if (Objects.equals(x.get(i).getName(), this.getName())) {
                                if (x.get(i).getTimestamp_PoI_ida() == null) {
                                    x.get(i).setTimestamp_PoI_ida(new Date(new Date().getTime() + getRandomNumber(100000, 500000)));
                                }
                            }
                        }
                    } else {
                        System.out.println("Não há caminho de " + poI_Org + " para " + poI_Dest);
                    }
                }
            }
        }
    }

    /** Calcula distancia em horas e km de um PoI origem a um PoI Destino e seguidamente imprime o caminho todo do PoI origem ao PoI destino
     *
     * @param poI_Dest PoI destino
     * @param myDijkstraSP_Time DijkstraSP com unidade metrica de tempo do sub grafo bike/carro/pe
     * @param myDijkstraSP_Dist DijkstraSP com unidade metrica de distancia do sub grafo bike/carro/pe
     */

    private void distTo(String poI_Dest, DijkstraSP myDijkstraSP_Time, DijkstraSP myDijkstraSP_Dist) {
        Integer id = PoI.getMyRedBlackBST_PoI().get(poI_Dest).getID();
        System.out.print(this.getName() + " | " + "to " + poI_Dest + " | ");
        System.out.print("Distancia (km) = " + myDijkstraSP_Dist.distTo(id) + " | Duração (h) = " + myDijkstraSP_Time.distTo(id) + " ");
        System.out.println(" | " + myDijkstraSP_Dist.pathTo(id));
    }

    /**
     * Todos os PoI não visitados/usados por um User num dado período
     * @param timestamp_chegada TimeStamp chegada de um dado periodo
     * @param timestamp_ida TimeStampo partida de um dado periodo
     */

    public void not_visited(long timestamp_chegada, long timestamp_ida) {
        System.out.println(this.getName() + " não visitou:");
        ArrayList<String> auxArrayList = new ArrayList<>();
        if (timestamp_chegada > timestamp_ida) {
            System.out.println("Timestamps inválidos! (timestamp_chegada < timestamp_ida)");
        } else {
            for (String poi : PoI.getMyRedBlackBST_PoI().keys()) {
                auxArrayList.add(PoI.getMyRedBlackBST_PoI().get(poi).getName());
            }
            if (getMyHashMap().get(this.getName()) == null) {
                System.out.println(auxArrayList);
            } else {
                for (int i = 0; i < getMyHashMap().get(this.getName()).size(); i++) {
                    PoI aux = getMyHashMap().get(this.getName()).get(i);
                    ArrayList<RegistosPoI> x = PoI.getMyHashMapPoI().get(aux);
                    for (int j = 0; j < x.size(); j++) {
                        if (Objects.equals(x.get(j).getName(), this.getName())) {
                            if (((x.get(j).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                                    && (x.get(j).getTimestamp_PoI_ida().getTime() < timestamp_ida))
                                    || ((x.get(j).gettimestamp_PoI_chegada().getTime() < timestamp_chegada)
                                    && (x.get(j).getTimestamp_PoI_ida().getTime() < timestamp_ida))
                                    || ((x.get(j).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                                    && (x.get(j).getTimestamp_PoI_ida().getTime() > timestamp_ida))
                                    || ((x.get(j).gettimestamp_PoI_chegada().getTime() < timestamp_chegada)
                                    && (x.get(j).getTimestamp_PoI_ida() == null))
                                    || ((x.get(j).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                                    && (x.get(j).getTimestamp_PoI_ida() == null))) {
                                if (getMyHashMap().get(this.getName()).contains(PoI.getMyRedBlackBST_PoI().get(aux.name_PoI))) {
                                    auxArrayList.remove(aux.name_PoI);
                                }
                            }
                        }
                    }
                }
                System.out.println(auxArrayList);
            }
        }

    }

    /** Todos os PoI visitados/usados por um User num dado período
     *
     * @param timestamp_chegada TimeStamp chegada de um dado periodo
     * @param timestamp_ida TimeStamp partida de um dado periodo
     */
    public void visited(long timestamp_chegada, long timestamp_ida) {
        System.out.println(this.getName() + " visitou:");
        ArrayList<String> auxArrayList = new ArrayList<>();
        if (timestamp_chegada > timestamp_ida) {
            System.out.println("Timestamps inválidos! (timestamp_chegada < timestamp_ida)");
        } else {
            if (getMyHashMap().get(this.getName()) == null) {
                System.out.println(auxArrayList);
            } else {
                for (int i = 0; i < getMyHashMap().get(this.getName()).size(); i++) {
                    PoI aux = getMyHashMap().get(this.getName()).get(i);
                    ArrayList<RegistosPoI> x = PoI.getMyHashMapPoI().get(aux);
                    for (int j = 0; j < x.size(); j++) {
                        if (Objects.equals(x.get(j).getName(), this.getName())) {
                            if (((x.get(j).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                                    && (x.get(j).getTimestamp_PoI_ida().getTime() < timestamp_ida))
                                    || ((x.get(j).gettimestamp_PoI_chegada().getTime() < timestamp_chegada)
                                    && (x.get(j).getTimestamp_PoI_ida().getTime() < timestamp_ida))
                                    || ((x.get(j).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                                    && (x.get(j).getTimestamp_PoI_ida().getTime() > timestamp_ida))
                                    || ((x.get(j).gettimestamp_PoI_chegada().getTime() < timestamp_chegada)
                                    && (x.get(j).getTimestamp_PoI_ida() == null))
                                    || ((x.get(j).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                                    && (x.get(j).getTimestamp_PoI_ida() == null))) {
                                if (getMyHashMap().get(this.getName()).contains(PoI.getMyRedBlackBST_PoI().get(aux.name_PoI))) {
                                    auxArrayList.add(aux.name_PoI);
                                }
                            }
                        }
                    }
                }
                System.out.println(auxArrayList);
            }
        }
    }

    /** Todos os User que já visitaram/usaram um determinado PoI
     *
     * @param poI determinado PoI visitado pelos users
     * @param timestamp_chegada TimeStamp chegada de um dado periodo
     * @param timestamp_ida TimeStamp partida de um dado periodo
     */
    public void visit_PoI(String poI, long timestamp_chegada, long timestamp_ida) {
        if (PoI.getMyRedBlackBST_PoI().get(poI) != null) {
            System.out.println(PoI.getMyRedBlackBST_PoI().get(poI).getName() + " foi visitado por:");
            if (timestamp_chegada > timestamp_ida) {
                System.out.println("Timestamps inválidos! (timestamp_chegada < timestamp_ida)");
            } else {
                ArrayList<String> auxArrayList = new ArrayList<>();
                ArrayList<RegistosPoI> x = PoI.getMyHashMapPoI().get(PoI.getMyRedBlackBST_PoI().get(poI));
                if (x == null) {
                    System.out.println(auxArrayList);
                } else {
                    for (int i = 0; i < x.size(); i++) {
                        if (((x.get(i).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                                && (x.get(i).getTimestamp_PoI_ida().getTime() < timestamp_ida))
                                || ((x.get(i).gettimestamp_PoI_chegada().getTime() < timestamp_chegada)
                                && (x.get(i).getTimestamp_PoI_ida().getTime() < timestamp_ida))
                                || ((x.get(i).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                                && (x.get(i).getTimestamp_PoI_ida().getTime() > timestamp_ida))
                                || ((x.get(i).gettimestamp_PoI_chegada().getTime() < timestamp_chegada)
                                && (x.get(i).getTimestamp_PoI_ida() == null))
                                || ((x.get(i).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                                && (x.get(i).getTimestamp_PoI_ida() == null))) {
                            auxArrayList.add(x.get(i).name);
                        }
                    }
                    System.out.println(auxArrayList);
                }

            }
        } else {
            System.out.println("POI NAO EXISTE");
        }


    }

    /** Todas os PoI que não foram visitados/usados por Users
     *
     * @param timestamp_chegada TimeStamp chegada de um dado periodo
     * @param timestamp_ida TimeStamp partida de um dado periodo
     */
    public void PoI_not_visited(long timestamp_chegada, long timestamp_ida) {
        System.out.println("PoI´s não visitados:");
        if (timestamp_chegada > timestamp_ida) {
            System.out.println("Timestamps inválidos! (timestamp_chegada < timestamp_ida)");
        } else {
            ArrayList<String> auxArrayList = new ArrayList<>();
            for (String poi : PoI.getMyRedBlackBST_PoI().keys()) {
                auxArrayList.add(PoI.getMyRedBlackBST_PoI().get(poi).getName());
            }
            for (Map.Entry mapElement : PoI.getMyHashMapPoI().entrySet()) {
                PoI key = (PoI) mapElement.getKey();
                for (int i = 0; i < PoI.getMyHashMapPoI().get(key).size(); i++) {
                    if (((PoI.getMyHashMapPoI().get(key).get(i).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                            && (PoI.getMyHashMapPoI().get(key).get(i).getTimestamp_PoI_ida().getTime() < timestamp_ida))
                            || ((PoI.getMyHashMapPoI().get(key).get(i).gettimestamp_PoI_chegada().getTime() < timestamp_chegada)
                            && (PoI.getMyHashMapPoI().get(key).get(i).getTimestamp_PoI_ida().getTime() < timestamp_ida))
                            || ((PoI.getMyHashMapPoI().get(key).get(i).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                            && (PoI.getMyHashMapPoI().get(key).get(i).getTimestamp_PoI_ida().getTime() > timestamp_ida))
                            || ((PoI.getMyHashMapPoI().get(key).get(i).gettimestamp_PoI_chegada().getTime() < timestamp_chegada)
                            && (PoI.getMyHashMapPoI().get(key).get(i).getTimestamp_PoI_ida() == null))
                            || ((PoI.getMyHashMapPoI().get(key).get(i).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                            && (PoI.getMyHashMapPoI().get(key).get(i).getTimestamp_PoI_ida() == null))) {
                        auxArrayList.remove(key.name_PoI);
                    }
                }
            }
            System.out.println(auxArrayList);
        }

    }

    /** Top-5 dos Users que visitaram/usaram o maior número de PoI num dado período
     *
     * @param timestamp_chegada TimeStamp chegada de um dado periodo
     * @param timestamp_ida TimeStamp partida de um dado periodo
     */
    public void top5Users(long timestamp_chegada, long timestamp_ida) {
        System.out.println("TOP 5 Users que visitaram maior número de PoI");
        ArrayList<String> top5Array = new ArrayList<>();
        HashMap<String, Integer> top5Hash = new HashMap<>();
        int size;
        if (timestamp_chegada > timestamp_ida) {
            System.out.println("Timestamps inválidos! (timestamp_chegada < timestamp_ida)");
        } else {
            for (String user : getMyST_adminUser().keys()) {
                String name = getMyST_adminUser().get(user).getName();
                if (getMyHashMap().get(name) == null) {
                    size = 0;
                } else {
                    size = getMyHashMap().get(name).size();
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < PoI.getMyHashMapPoI().get(getMyHashMap().get(name).get(i)).size(); j++) {
                            if (((PoI.getMyHashMapPoI().get(getMyHashMap().get(name).get(i)).get(j).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                                    && (PoI.getMyHashMapPoI().get(getMyHashMap().get(name).get(i)).get(j).getTimestamp_PoI_ida().getTime() < timestamp_ida))
                                    || ((PoI.getMyHashMapPoI().get(getMyHashMap().get(name).get(i)).get(j).gettimestamp_PoI_chegada().getTime() < timestamp_chegada)
                                    && (PoI.getMyHashMapPoI().get(getMyHashMap().get(name).get(i)).get(j).getTimestamp_PoI_ida().getTime() < timestamp_ida))
                                    || ((PoI.getMyHashMapPoI().get(getMyHashMap().get(name).get(i)).get(j).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                                    && (PoI.getMyHashMapPoI().get(getMyHashMap().get(name).get(i)).get(j).getTimestamp_PoI_ida().getTime() > timestamp_ida))
                                    || ((PoI.getMyHashMapPoI().get(getMyHashMap().get(name).get(i)).get(j).gettimestamp_PoI_chegada().getTime() < timestamp_chegada)
                                    && (PoI.getMyHashMapPoI().get(getMyHashMap().get(name).get(i)).get(j).getTimestamp_PoI_ida() == null))
                                    || ((PoI.getMyHashMapPoI().get(getMyHashMap().get(name).get(i)).get(j).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                                    && (PoI.getMyHashMapPoI().get(getMyHashMap().get(name).get(i)).get(j).getTimestamp_PoI_ida() == null))) {
                                top5Hash.put(name, size);
                            }
                        }
                    }
                }
            }
            int count = 0;
            HashMap<String, Integer> sorted_HashMap = sortByValueDescendingOrder(top5Hash);
            for (Map.Entry mapElement : sorted_HashMap.entrySet()) {
                if (count < 5) {
                    String key = (String) mapElement.getKey();
                    top5Array.add(key);
                    count++;
                }
            }
            System.out.println(top5Array);
        }
    }

    /** Top-5 dos PoIs que foram visitados/usados num dado período
     *
     * @param timestamp_chegada TimeStamp chegada de um dado periodo
     * @param timestamp_ida TimeStamp partida de um dado periodo
     */
    public void top5Visited(long timestamp_chegada, long timestamp_ida) {
        System.out.println("TOP 5 PoIs que foram visitados/usados");
        ArrayList<String> top5Array = new ArrayList<>();
        HashMap<String, Integer> top5Hash = new HashMap<>();
        int size;
        if (timestamp_chegada > timestamp_ida) {
            System.out.println("Timestamps inválidos! (timestamp_chegada < timestamp_ida)");
        } else {
            for (Map.Entry mapElement : PoI.getMyHashMapPoI().entrySet()) {
                PoI key = (PoI) mapElement.getKey();
                if (PoI.getMyHashMapPoI().get(key) == null) {
                    size = 0;
                } else {
                    size = PoI.getMyHashMapPoI().get(key).size();
                    for (int i = 0; i < size; i++) {
                        if (((PoI.getMyHashMapPoI().get(key).get(i).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                                && (PoI.getMyHashMapPoI().get(key).get(i).getTimestamp_PoI_ida().getTime() < timestamp_ida))
                                || ((PoI.getMyHashMapPoI().get(key).get(i).gettimestamp_PoI_chegada().getTime() < timestamp_chegada)
                                && (PoI.getMyHashMapPoI().get(key).get(i).getTimestamp_PoI_ida().getTime() < timestamp_ida))
                                || ((PoI.getMyHashMapPoI().get(key).get(i).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                                && (PoI.getMyHashMapPoI().get(key).get(i).getTimestamp_PoI_ida().getTime() > timestamp_ida))
                                || ((PoI.getMyHashMapPoI().get(key).get(i).gettimestamp_PoI_chegada().getTime() < timestamp_chegada)
                                && (PoI.getMyHashMapPoI().get(key).get(i).getTimestamp_PoI_ida() == null))
                                || ((PoI.getMyHashMapPoI().get(key).get(i).gettimestamp_PoI_chegada().getTime() > timestamp_chegada)
                                && (PoI.getMyHashMapPoI().get(key).get(i).getTimestamp_PoI_ida() == null))) {
                            top5Hash.put(key.name_PoI, size);
                        }
                    }
                }
            }
            int count = 0;
            HashMap<String, Integer> sorted_HashMap = sortByValueDescendingOrder(top5Hash);

            for (Map.Entry mapElement : sorted_HashMap.entrySet()) {
                if (count < 5) {
                    String key = (String) mapElement.getKey();
                    top5Array.add(key);
                    count++;
                }
            }

        }
        System.out.println(top5Array);
    }

    /** Sorting a Hashmap according to values, order Descending
     *
     * @param hm
     * @return
     */
    private static HashMap<String, Integer> sortByValueDescendingOrder(HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    /** Sorting a Hashmap according to values, order Ascending
     *
     * @param hm
     * @return
     */
    private static HashMap<String, Integer> sortByValueAscendingOrder(HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    /**
     * método now() para apresentar o estado de todos os PoI num dado instante
     */
    public void now() {
        for (Map.Entry mapElement : PoI.getMyHashMapPoI().entrySet()) {
            PoI key = (PoI) mapElement.getKey();
            System.out.print("\n" + key.name_PoI.toUpperCase() + " --> ");
            for (int i = 0; i < PoI.getMyHashMapPoI().get(key).size(); i++) {
                System.out.print("*** " + PoI.getMyHashMapPoI().get(key).get(i).getName() + " *** ");
                long tempo_utilizacao = PoI.getMyHashMapPoI().get(key).get(i).getTimestamp_PoI_ida().getTime() - PoI.getMyHashMapPoI().get(key).get(i).gettimestamp_PoI_chegada().getTime();
                System.out.print(get_tempo_utilizacao(tempo_utilizacao) + " | ");
            }
            System.out.print("\n");
        }
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    static String get_tempo_utilizacao(long tempo_utilizacao) {
        long minutes = (tempo_utilizacao / 1000) / 60;
        long seconds = (tempo_utilizacao / 1000) % 60;

        return ("Tempo de utilizacao " + minutes + " minutos e "
                + seconds + " segundos.");
    }

    /** Adicionar um sinal de transito
     *
     * @param trafficSign Sinal de transito a adicionar
     * @param latitude latitude do sinal de transito
     * @param longitude longitude do sinal de transito
     */
    public void addTrafficSign(String trafficSign, double latitude, double longitude) {
        if ((latitude >= -90 && latitude <= 90) && (longitude >= -180 && longitude <= 180)) {
            if (TrafficSigns.getMyRedBlackBST_TrafficSigns().size() >= 1) {
                for (String s : TrafficSigns.getMyRedBlackBST_TrafficSigns().keys()) {
                    if (TrafficSigns.getMyRedBlackBST_TrafficSigns().get(s).getLatitude() ==
                            latitude && TrafficSigns.getMyRedBlackBST_TrafficSigns().get(s).getLongitude() ==
                            longitude && Objects.equals(TrafficSigns.getMyRedBlackBST_TrafficSigns().get(s).getName(), trafficSign)) {
                        System.out.println("Já existe esse sinal de transito no local escolhido!");
                        return;
                    } else {
                        TrafficSigns myTrafficSign = new TrafficSigns(trafficSign, latitude, longitude);
                        TrafficSigns.getMyRedBlackBST_TrafficSigns().put(trafficSign, myTrafficSign);
                    }
                }
            } else {
                TrafficSigns myTrafficSign = new TrafficSigns(trafficSign, latitude, longitude);
                TrafficSigns.getMyRedBlackBST_TrafficSigns().put(trafficSign, myTrafficSign);
            }
        } else {
            System.out.println("Valores de Latitude/Longitude não válidos");
        }
    }

    /**Listar sinais de transito mais próximos de uma coordenada geográfica indicada na
     pesquisa .
     *
     * @param latitude latitude coordenada
     * @param longitude longitude coordenada
     * @param max_kms raio de kms a listar, pois o mais proximos pode ser relativo entao meti maximo de kms
     */
    public void mostNextofCoordinate(double latitude, double longitude, Integer max_kms) {
        ArrayList<String> myTrafficSigns = new ArrayList<>();
        HashMap<String, Integer> myHashMap_aux = new HashMap<>();
        if ((latitude >= -90 && latitude <= 90) && (longitude >= -180 && longitude <= 180)) {
            for (String i : TrafficSigns.getMyRedBlackBST_TrafficSigns().keys()) {
                int value = (int) haversineDistanceTime(latitude, latitude, TrafficSigns.getMyRedBlackBST_TrafficSigns().get(i).getLatitude(), TrafficSigns.getMyRedBlackBST_TrafficSigns().get(i).getLongitude());

                if (value <= max_kms) {
                    myHashMap_aux.put(TrafficSigns.getMyRedBlackBST_TrafficSigns().get(i).getName(), value);
                }
            }
            HashMap<String, Integer> sorted_HashMap = sortByValueAscendingOrder(myHashMap_aux);
            for (Map.Entry mapElement : sorted_HashMap.entrySet()) {
                String key = (String) mapElement.getKey();
                myTrafficSigns.add(key);
            }
        } else {
            System.out.println("Valores de Latitude/Longitude não válidos");
        }

        System.out.println("Considerando uma escala de até " +  max_kms + " -> Sinais mais próximos de " + latitude + " e " + longitude + " = " + myTrafficSigns);
    }


    public static void main(String[] args) {
        AdminUser adminUser1 = new AdminUser("Miguelito");
        AdminUser adminUser2 = new AdminUser("Bessa");
        getMyST_adminUser().put(adminUser1.getName(), adminUser1);
        getMyST_adminUser().put(adminUser2.getName(), adminUser2);
        System.out.println(getMyST_adminUser().get("Miguelito"));
        System.out.println(getMyST_adminUser().get("Bessa"));


    }
}
