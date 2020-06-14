package sample.llenarTabla;

public class CPU
{
    private int cpu;
    private boolean disponible;

    public CPU(int cpu,boolean disponible){
        this.cpu = cpu;
        this.disponible = disponible;
    }

    public int getCpu() { return cpu; }
    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}