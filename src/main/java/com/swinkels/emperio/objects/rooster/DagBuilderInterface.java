package com.swinkels.emperio.objects.rooster;

import java.util.ArrayList;
import java.util.Date;

public interface DagBuilderInterface {
    public void setId(int id) ;

    public void setDagNummer(int dagNummer);

    public void setDag(int dag);

    public void setOpeningsTijd(Date openingsTijd);

    public void setSluitingsTijd(Date sluitingsTijd) ;

    public void setAfspraken(ArrayList<Afspraak> afspraken);
    public Dag build();
}
