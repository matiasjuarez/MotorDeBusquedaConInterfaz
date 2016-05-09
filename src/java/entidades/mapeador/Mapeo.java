/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.mapeador;

/**
 *
 * @author Matías
 */
public class Mapeo implements Comparable<Mapeo>{
    private String URL;
    private int map;

    /**
     * @return the URL
     */
    public String getURL() {
        return URL;
    }

    /**
     * @param URL the URL to set
     */
    public void setURL(String URL) {
        this.URL = URL;
    }

    /**
     * @return the map
     */
    public int getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(int map) {
        this.map = map;
    }

    @Override
    public int compareTo(Mapeo o) {
        return this.getMap() - o.getMap();
    }
}
