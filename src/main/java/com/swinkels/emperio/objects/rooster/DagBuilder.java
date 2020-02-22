package com.swinkels.emperio.objects.rooster;

import java.util.ArrayList;
import java.util.Date;

public class DagBuilder implements DagBuilderInterface{
    private ArrayList<Afspraak> afspraken;
    private int dagNummer;
    private int id;
    private int dag;
    private Date openingsTijd;
    private Date sluitingsTijd;

    public void setId(int id) {
        this.id = id;
    }

    public void setDagNummer(int dagNummer){
        this.dagNummer = dagNummer;
    }

    public void setDag(int dag) {
        this.dag = dag;
    }

    public void setOpeningsTijd(Date openingsTijd) {
        this.openingsTijd = openingsTijd;
    }

    public void setSluitingsTijd(Date sluitingsTijd) {
        this.sluitingsTijd = sluitingsTijd;
    }

    public void setAfspraken(ArrayList<Afspraak> afspraken) {
        this.afspraken = afspraken;
    }
    public Dag build(){
        return new  Dag(afspraken, id, dagNummer, openingsTijd, sluitingsTijd);
    }
}
