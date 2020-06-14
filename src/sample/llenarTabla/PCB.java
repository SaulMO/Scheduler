package sample.llenarTabla;

public class PCB
{
    private int noIdentificador;
    private char estado;
    private String ubicacion;
    private int tiempo;

    public PCB(int noIdentificador, char estado, String ubicacion, int tiempo){
        this.noIdentificador = noIdentificador;
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.tiempo = tiempo;
    }

    public int getNoIdentificador(){return noIdentificador;}
    public char getEstado(){return estado;}
    public String getUbicacion(){return ubicacion;}
    public int getTiempo(){return tiempo;}

    public void setEstado(char estado){this.estado = estado;}
    public void setUbicacion(String ubicacion){this.ubicacion = ubicacion;}
    public void recorrerTiempo(){tiempo = tiempo-1;}
}