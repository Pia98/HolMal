package com.holmal.app.holmal.utils;

import com.holmal.app.holmal.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.util.Log;

/**
 * hier stehen alle methoden, um referencen zwischen firebase objekten herzustellen
 *
 * ACHTUNG: diese klasse soll keine eigenen attribute haben, sie dient lediglich dazu, dass all
 * diese Methoden gesammelt an einer Stelle stehen und global aufrufbar sind
 */
public class ReferencesHandling {

    /**
     *
     * @param personIDList ist eine Liste mit Ids von Mitgliedern eines Haushalts
     * @param personHash ist eine HasMap mit allen Personen und deren Ids
     * @return Diese Methode gibt eine ArrayList mit allen Personen Objekten in einem Haushalt zurück
     */
    public ArrayList<Person> getAllMembersOfOneHousehold(List<String> personIDList, HashMap<String, Person> personHash){
        ArrayList<Person> personenImHaushalt = new ArrayList<>();
        for(int i = 0; i < personIDList.size(); i++){
            Log.i("ReferenceHandling", "get all members of an household personId: " + personIDList.get(i));
            Log.i("ReferenceHandling", "show me what in personHash is: " + personHash.toString());
            if(personHash.containsKey(personIDList.get(i))){
                Log.i("ReferenceHandling", "found this person: " + personIDList.get(i));
                personenImHaushalt.add(personHash.get(personIDList.get(i)));
            }
        }
        return personenImHaushalt;
    }
}
