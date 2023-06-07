package edu.ufp.inf.Projeto.Classes;

import java.util.Date;

public class RegistosPoI {
    String name;
    private Date timestamp_PoI_chegada;
    private Date timestamp_PoI_ida;

    public RegistosPoI(String name) {
        this.name = name;
        this.timestamp_PoI_chegada = new Date();
        this.timestamp_PoI_ida = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date gettimestamp_PoI_chegada() {
        return timestamp_PoI_chegada;
    }

    public Date getTimestamp_PoI_ida() {
        return timestamp_PoI_ida;
    }

    public void setTimestamp_PoI_ida(Date timestamp_PoI_ida) {
        this.timestamp_PoI_ida = timestamp_PoI_ida;
    }

    @Override
    public String toString() {
        return "RegistosPoI{" +
                "name='" + name + '\'' +
                ", timestamp_PoI_chegada=" + timestamp_PoI_chegada +
                ", timestamp_PoI_ida=" + timestamp_PoI_ida +
                '}';
    }

    public static void main(String[] args) {
        RegistosPoI x = new RegistosPoI("Bruno");

    }
}
