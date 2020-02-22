package com.swinkels.emperio.objects.instellingen;

import java.util.HashMap;

public interface InstellingenFacadeInterface {
    public HashMap<String, String> getInplanSettingsDTO() ;
    public HashMap<String, String> toDto() ;

    void saveAfspraakKleuren(String kleurKlasse1, double maximumPrijsVanKlasse1, String kleurKlasse2, double maximumPrijsVanKlasse2, String kleurKlasse3);
}
