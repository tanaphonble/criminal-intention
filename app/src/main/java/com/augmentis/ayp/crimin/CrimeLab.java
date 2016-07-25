package com.augmentis.ayp.crimin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Tanaphon on 7/18/2016.
 */
public class CrimeLab {
    List<Crime> crimeList;

    private static CrimeLab instance;

    public static CrimeLab getInstance() {
        if (instance == null) {
            instance = new CrimeLab();
        }
        return instance;
    }

    private CrimeLab() {
        crimeList = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);
            crimeList.add(crime);
        }
    }

    public int getCrimePositionById(UUID uuid) {
        int position = 0;
        for (Crime crime : crimeList) {
            if (crime.getId().equals(uuid)) return position;
        }
        position++;
        return -1;
    }

    public Crime getCrimeById(UUID uuid) {
        for (Crime crime : crimeList) {
            if (crime.getId().equals(uuid)) {
                return crime;
            }
        }
        return null;
    }

    public List<Crime> getCrimes() {
        return this.crimeList;
    }

//    public static void main(String[] args){
//        List<Crime> crimeList = new CrimeLab().getCrimes();
//        for(Crime crime : crimeList ) System.out.println(crime.toString());
//    }
}
