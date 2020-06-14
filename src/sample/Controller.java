package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sample.llenarTabla.CPU;
import sample.llenarTabla.PCB;
import sample.llenarTabla.Proceso;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    @FXML TextField txtDuracion,txtLlegada;
    @FXML Button btnIngresarProceso,btnStartStop,btnContar;
    @FXML Label lblTiempo,lblCPU;
    @FXML TextArea txtAreaProcesos,txtAreaSalida,txtAreaMemoria;
    @FXML TableView<PCB> tblViewPCB;
    private int contSeg;
    private ArrayList<Proceso> procesos;
    private int contProceso,duracion,llegada;
    private ArrayList<PCB> pcb;
    private ArrayList<Integer> memoria;
    private ArrayList<Integer> salida;
    private CPU cpu;
    boolean pasoAMemoria;
    private int tiempoTotal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        procesos = new ArrayList<>();
        txtAreaProcesos.setEditable(false);
        btnIngresarProceso.setOnAction(evento);
        btnStartStop.setOnAction(evento);
        btnContar.setOnAction(evento);
        txtAreaProcesos.appendText("PID\t\tDuracion\t\tLlegada");
        contProceso = 1;
        contSeg = 0;
        btnContar.setDisable(true);
        pcb = new ArrayList<>();
        memoria = new ArrayList<>();
        salida = new ArrayList<>();
        cpu = new CPU(0,true);
        txtAreaMemoria.setEditable(false);
        txtAreaSalida.setEditable(false);
    }

    EventHandler<ActionEvent> evento = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() == btnIngresarProceso){
                if (verificarProceso()){
                    procesos.add(new Proceso(contProceso,duracion,llegada));
                    txtAreaProcesos.appendText("\nP"+procesos.get(contProceso-1).getIdp()+
                                                "\t\t"+procesos.get(contProceso-1).getDuracion()+
                                                "\t\t\t"+procesos.get(contProceso-1).getLlegada());
                    contProceso++;
                    txtDuracion.setText("");
                    txtLlegada.setText("");
                }
            }
            if (event.getSource() == btnStartStop){
                if (btnStartStop.getText().equals("Simular")){
                    if (!procesos.isEmpty()){
                        habilitarSimulacion();
                        contSeg = 0;
                        contProceso = 0;
                        realizarAlgoritmo(0,false);
                    }else
                        alertaProcesosVacios();
                }else{
                    deshabilitarSimulacion();
                }
            }
            if (event.getSource() == btnContar){
                contSeg++;
                lblTiempo.setText(contSeg+".00");
                realizarAlgoritmo(contSeg,true);
            }
        }
    };

    private void realizarAlgoritmo(int segundo,boolean recorrido){
        if (!recorrido){
            for(int i=contProceso;i<procesos.size();i++){
                tiempoTotal = tiempoTotal + procesos.get(i).getDuracion();
                if (procesos.get(i).getLlegada()==segundo){
                    if (cpu.isDisponible()){
                        pcb.add(new PCB(procesos.get(i).getIdp(),'X',"CPU",procesos.get(i).getDuracion()));
                        lblCPU.setText("P"+pcb.get(i).getNoIdentificador());
                        cpu.setCpu(i);
                        cpu.setDisponible(false);
                    }else{
                        pcb.add(new PCB(procesos.get(i).getIdp(),'W',"M",procesos.get(i).getDuracion()));
                        memoria.add(pcb.get(i).getNoIdentificador());
                    }
                    contProceso++;
                }
            }
            llenarTodo();
        }else{
            pcb.get(cpu.getCpu()).recorrerTiempo();
            if (pcb.get(cpu.getCpu()).getTiempo() == 0){
                pcb.get(cpu.getCpu()).setEstado('F');
                pcb.get(cpu.getCpu()).setUbicacion("S");
                salida.add(pcb.get(cpu.getCpu()).getNoIdentificador());
                pasoAMemoria = true;
                cpu.setDisponible(true);
            }
            if (contSeg == tiempoTotal){
                alertaFinalizado();
                btnContar.setDisable(true);
                lblCPU.setText("CPU :");
                txtAreaSalida.setText("");
                salida.add(contProceso);
                for (int i=0;i<salida.size();i++){
                    txtAreaSalida.appendText("    P"+salida.get(i)+"\n");
                }
                pcb.get(pcb.size()-1).setEstado('F');
                pcb.get(pcb.size()-1).setUbicacion("S");
                tblViewPCB.getItems().removeAll(pcb);
                tblViewPCB.getItems().addAll(pcb);

            }else{
                for (int i=(contProceso-1);i<procesos.size();i++){
                    if (procesos.get(i).getLlegada() == segundo){
                        if (cpu.isDisponible()){
                            pcb.add(new PCB(procesos.get(i).getIdp(),'X',"CPU",procesos.get(i).getDuracion()));
                            cpu.setDisponible(false);
                            cpu.setCpu(i);
                            pasoAMemoria = false;
                            lblCPU.setText("CPU : P"+(i+1));
                        }else{
                            pcb.add(new PCB(procesos.get(i).getIdp(),'W',"M",procesos.get(i).getDuracion()));
                            memoria.add(pcb.get(i).getNoIdentificador());
                        }
                        contProceso++;
                    }
                }
                System.out.println("se permite el ingreso de procesos desde memoria "+pasoAMemoria);
                if (pasoAMemoria){
                    System.out.println("Ingresare un proceso en memoria");
                    pcb.get(memoria.get(0)-1).setEstado('X');
                    pcb.get(memoria.get(0)-1).setUbicacion("CPU");
                    cpu.setDisponible(false);
                    cpu.setCpu((memoria.get(0))-1);
                    pasoAMemoria = false;
                    lblCPU.setText("CPU : P"+pcb.get(cpu.getCpu()).getNoIdentificador());
                    memoria.remove(0);
                }
                llenarTodo();
            }
        }
    }

    private void habilitarSimulacion(){
        btnContar.setDisable(false);
        btnIngresarProceso.setDisable(true);
        txtDuracion.setDisable(true);
        txtLlegada.setDisable(true);
        btnStartStop.setText("Parar");
    }
    private void deshabilitarSimulacion(){
        procesos.clear();
        txtAreaProcesos.setText("PID\t\tDuracion\t\tLlegada");
        btnStartStop.setText("Simular");
        btnContar.setDisable(true);
        txtDuracion.setDisable(false);
        txtLlegada.setDisable(false);
        btnIngresarProceso.setDisable(false);
        contProceso = 1;
        contSeg = 0;
        lblCPU.setText("CPU : ");
        lblTiempo.setText("0.00");
        txtAreaSalida.setText("");
        txtAreaMemoria.setText("");
        tblViewPCB.getItems().removeAll(pcb);
        pcb.clear();
        memoria.clear();
        salida.clear();
    }
    private boolean verificarProceso(){
        boolean bandera = true;
        try{
            duracion = Integer.parseInt(txtDuracion.getText());
            llegada = Integer.parseInt(txtLlegada.getText());
            bandera = true;
        }catch(NumberFormatException e){
            bandera = false;
            alertaA();
        }
        return bandera;
    }
    private void alertaProcesosVacios(){
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("ERROR");
        alerta.setContentText("No ha ingresado ningun proceso");
        alerta.showAndWait();
    }
    private void alertaA(){
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText("Datos Ingresados incorrectamente");
        alerta.setContentText("El proceso no se ingreso correctamente... Recuerda que el primer proceso entra en 0");
        alerta.showAndWait();
    }
    private void alertaFinalizado(){
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("FINALIZADO");
        alerta.setHeaderText("SIMULACIÓN FINALIZADA");
        alerta.setContentText("La Simulación finalizó con "+tiempoTotal+" quantum's transcurridos");
        alerta.showAndWait();
    }
    private void llenarTodo(){
        txtAreaMemoria.setText("");
        txtAreaSalida.setText("");
        tblViewPCB.getItems().removeAll(pcb);
        tblViewPCB.getItems().addAll(pcb);
        for (int i=0;i<memoria.size();i++){
            txtAreaMemoria.appendText("    P"+memoria.get(i)+"\n");
        }
        for (int i=0;i<salida.size();i++){
            txtAreaSalida.appendText("    P"+salida.get(i)+"\n");
        }
    }
}