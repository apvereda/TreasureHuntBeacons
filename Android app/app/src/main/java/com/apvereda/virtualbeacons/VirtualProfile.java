package com.apvereda.virtualbeacons;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class VirtualProfile {

    private static VirtualProfile myVirtualProfile;

    private Map<String, String> userHints = new HashMap<>();//list of the hints found by the user

    private String userName;//name of the current user
    private HashSet<String> foundTreasures = new HashSet<>();//list of all found treasures by the user

    double myTemperature = 20;


    public static VirtualProfile getVirtualProfileInstance() {
        if(myVirtualProfile == null){
            myVirtualProfile = new VirtualProfile();
        }
        return myVirtualProfile;
    }

    private VirtualProfile(){

    }

    public void updateUserHints(String key, String value){
        userHints.put(key, value);
    }

    public void updateUserTreasures(String treasure){
        foundTreasures.add(treasure);
    }

    public void restartGameVariables(){
        userHints = new HashMap<>();
        foundTreasures = new HashSet<>();
    }


    public Map<String, String> getUserHints() {
        return userHints;
    }

    public void setUserHints(Map<String, String> userHints) {
        this.userHints = userHints;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public HashSet<String> getFoundTreasures() {
        return foundTreasures;
    }

    public void setFoundTreasures(HashSet<String> foundTreasures) {
        this.foundTreasures = foundTreasures;
    }

    public double getTemperature() {

        return myTemperature;
    }

    public void setTemperature(double temperature) {
        myTemperature = temperature;
    }
}
