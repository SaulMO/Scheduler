package sample.llenarTabla;

public class Proceso
{
    private int idp;
    private int duracion;
    private int llegada;

    public Proceso(int idp,int duracion,int llegada){
        this.idp = idp;
        this.duracion = duracion;
        this.llegada = llegada;
    }

    public int getIdp() { return idp; }
    public int getDuracion() { return duracion; }
    public int getLlegada() { return llegada; }

    public void setIdp(int idp) { this.idp = idp; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    public void setLlegada(int llegada) { this.llegada = llegada; }
}