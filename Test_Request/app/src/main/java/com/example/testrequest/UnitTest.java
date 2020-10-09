

package com.example.testrequest;

        import java.util.List;


public class UnitTest {

    private long cacheID;
    int guid;
    String genus;
    String species;
    String cultivar;
    String common;

    public List<Table> getSpecimens() {
        return specimens;
    }

    public void setSpecimens(List<Table> specimens) {
        this.specimens = specimens;
    }

    List<Table> specimens;

    public int getGuid() {
        return guid;
    }

    public void setGuid(int guid) {
        this.guid = guid;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getCultivar() {
        return cultivar;
    }

    public void setCultivar(String cultivar) {
        this.cultivar = cultivar;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }

    public String toString() {
        return genus + " " + species + " " + cultivar + " " + common;
    }

    public long getCacheID() {
        return cacheID;
    }

    public void setCacheID(long cacheID) {
        this.cacheID = cacheID;
    }
}