package edu.ufp.inf.Projeto.Classes;

import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.ST;

import java.util.*;

public class BasicUser {
    private static final ST<String, BasicUser> myST_basicUser = new ST<>();
    private HashMap<String, ArrayList<PoI>> myHashMap;
    protected static Integer staticID_BasicUser = 0;
    String name_basicUser;
    private final Integer basicID;


    public BasicUser(String name_basicUser) {
        AdminUser.staticID_User++;
        this.name_basicUser = name_basicUser;
        this.basicID = AdminUser.staticID_User;
        this.myHashMap = new HashMap<>();
    }

    public static ST<String, BasicUser> getMyST_basicUser() {
        return myST_basicUser;
    }

    public String getName_basicUser() {
        return name_basicUser;
    }

    public HashMap<String, ArrayList<PoI>> getMyHashMap() {
        return myHashMap;
    }

    public Integer getID() {
        return basicID;
    }

    @Override
    public String toString() {
        return "BasicUser{" +
                "myHashMap=" + myHashMap +
                ", name_basicUser='" + name_basicUser + '\'' +
                ", basicID=" + basicID +
                '}';
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
    /** Funcao para preencher o HashMap <PoI, ArrayList<RegistosPoI>>, cada poi tera um arraylist com os users que passaram por esse poi
     *
     * @param poI_Dest PoI a inserir no arraylist de PoIs
     * @param x ArrayList<PoI> x
     */
    private void aux_func_dataGo(String poI_Dest, ArrayList<PoI> x) {
        x.add(PoI.getMyRedBlackBST_PoI().get(poI_Dest));
        getMyHashMap().put(this.getName_basicUser(), x);

        RegistosPoI myRegistosPoI = new RegistosPoI(this.getName_basicUser());

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
        if (!getMyHashMap().containsKey(this.getName_basicUser())) {
            ArrayList<PoI> x = new ArrayList<>();
            aux_func_dataGo(poI_Dest, x);

        } else {
            ArrayList<PoI> x = getMyHashMap().get(this.getName_basicUser());
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
                        PoI lastElement = getMyHashMap().get(this.getName_basicUser()).get(getMyHashMap().get(this.getName_basicUser()).size() - 1);
                        ArrayList<RegistosPoI> x = PoI.getMyHashMapPoI().get(lastElement);
                        for (int i = 0; i < x.size(); i++) {
                            if (Objects.equals(x.get(i).getName(), this.getName_basicUser())) {
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
                        PoI lastElement = getMyHashMap().get(this.getName_basicUser()).get(getMyHashMap().get(this.getName_basicUser()).size() - 1);
                        ArrayList<RegistosPoI> x = PoI.getMyHashMapPoI().get(lastElement);
                        for (int i = 0; i < x.size(); i++) {
                            if (Objects.equals(x.get(i).getName(), this.getName_basicUser())) {
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
                        PoI lastElement = getMyHashMap().get(this.getName_basicUser()).get(getMyHashMap().get(this.getName_basicUser()).size() - 1);
                        ArrayList<RegistosPoI> x = PoI.getMyHashMapPoI().get(lastElement);
                        for (int i = 0; i < x.size(); i++) {
                            if (Objects.equals(x.get(i).getName(), this.getName_basicUser())) {
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
        System.out.print(this.getName_basicUser() + " | " + "to " + poI_Dest + " | ");
        System.out.print("Distancia (km) = " + myDijkstraSP_Dist.distTo(id) + " | Duração (h) = " + myDijkstraSP_Time.distTo(id) + " ");
        System.out.println(" | " + myDijkstraSP_Dist.pathTo(id));
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    /**
     * Todos os PoI não visitados/usados por um User num dado período
     * @param timestamp_chegada TimeStamp chegada de um dado periodo
     * @param timestamp_ida TimeStampo partida de um dado periodo
     */
    public void not_visited(long timestamp_chegada, long timestamp_ida) {
        System.out.println(this.getName_basicUser() + " não visitou:");
        ArrayList<String> auxArrayList = new ArrayList<>();
        if (timestamp_chegada > timestamp_ida) {
            System.out.println("Timestamps inválidos! (timestamp_chegada < timestamp_ida)");
        } else {
            for (String poi : PoI.getMyRedBlackBST_PoI().keys()) {
                auxArrayList.add(PoI.getMyRedBlackBST_PoI().get(poi).getName());
            }
            if (getMyHashMap().get(this.getName_basicUser()) == null) {
                System.out.println(auxArrayList);
            } else {
                for (int i = 0; i < getMyHashMap().get(this.getName_basicUser()).size(); i++) {
                    PoI aux = getMyHashMap().get(this.getName_basicUser()).get(i);
                    ArrayList<RegistosPoI> x = PoI.getMyHashMapPoI().get(aux);
                    for (int j = 0; j < x.size(); j++) {
                        if (Objects.equals(x.get(j).getName(), this.getName_basicUser())) {
                            if ((x.get(j).gettimestamp_PoI_chegada().compareTo(new Date(timestamp_chegada)) > 0)
                                    && (x.get(j).getTimestamp_PoI_ida().compareTo(new Date(timestamp_ida)) < 0)
                                    || (x.get(j).gettimestamp_PoI_chegada().compareTo(new Date(timestamp_chegada)) == 0)
                                    || (x.get(j).getTimestamp_PoI_ida().compareTo(new Date(timestamp_ida)) == 0)
                                    || (x.get(j).gettimestamp_PoI_chegada().compareTo(new Date(timestamp_chegada)) > 0)) {
                                if (getMyHashMap().get(this.getName_basicUser()).contains(PoI.getMyRedBlackBST_PoI().get(aux.name_PoI))) {
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
        System.out.println(this.getName_basicUser() + " visitou:");
        ArrayList<String> auxArrayList = new ArrayList<>();
        if (timestamp_chegada > timestamp_ida) {
            System.out.println("Timestamps inválidos! (timestamp_chegada < timestamp_ida)");
        } else {
            if (getMyHashMap().get(this.getName_basicUser()) == null) {
                System.out.println(auxArrayList);
            } else {
                for (int i = 0; i < getMyHashMap().get(this.getName_basicUser()).size(); i++) {
                    PoI aux = getMyHashMap().get(this.getName_basicUser()).get(i);
                    ArrayList<RegistosPoI> x = PoI.getMyHashMapPoI().get(aux);
                    for (int j = 0; j < x.size(); j++) {
                        if (Objects.equals(x.get(j).getName(), this.getName_basicUser())) {
                            if ((x.get(j).gettimestamp_PoI_chegada().compareTo(new Date(timestamp_chegada)) >= 0)
                                    && (x.get(j).getTimestamp_PoI_ida().compareTo(new Date(timestamp_ida)) <= 0)
                                    || (x.get(j).gettimestamp_PoI_chegada().compareTo(new Date(timestamp_chegada)) > 0)
                                    || (x.get(j).getTimestamp_PoI_ida().compareTo(new Date(timestamp_ida)) == 0)
                                    || (x.get(j).gettimestamp_PoI_chegada().compareTo(new Date(timestamp_chegada)) > 0)) {
                                if (getMyHashMap().get(this.getName_basicUser()).contains(PoI.getMyRedBlackBST_PoI().get(aux.name_PoI))) {
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

        System.out.println("Considerando uma escala de até " + max_kms + " -> Sinais mais próximos de " + latitude + " e " + longitude + " = " + myTrafficSigns);
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


    public static void main(String[] args) {
        BasicUser basicUser1 = new BasicUser("Miguel");
        BasicUser basicUser2 = new BasicUser("Diogo");
        getMyST_basicUser().put(basicUser1.getName_basicUser(), basicUser1);
        getMyST_basicUser().put(basicUser2.getName_basicUser(), basicUser2);
        System.out.println(getMyST_basicUser().get("Miguel"));
        System.out.println(getMyST_basicUser().get("Diogo"));

    }
}
