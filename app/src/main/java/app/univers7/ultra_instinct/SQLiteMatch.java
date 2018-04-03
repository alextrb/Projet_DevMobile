package app.univers7.ultra_instinct;

/**
 * Created by Alex on 03/04/2018.
 */

public class SQLiteMatch {

    private long id;
    private String player1_name;
    private String player2_name;
    private int victories;
    private int loses;
    private int kos;
    private Double lat;
    private Double lng;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlayer1_name() {
        return player1_name;
    }

    public void setPlayer1_name(String player1_name) {
        this.player1_name = player1_name;
    }

    public String getPlayer2_name() {
        return player2_name;
    }

    public void setPlayer2_name(String player2_name) {
        this.player2_name = player2_name;
    }

    public int getVictories() {
        return victories;
    }

    public void setVictories(int victories) {
        this.victories = victories;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public int getKos() {
        return kos;
    }

    public void setKos(int kos) {
        this.kos = kos;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
