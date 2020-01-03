package com.brutalcorpse.calculadoradegastos;


public class ItemGasto {
    private String fecha, tipo;
    private double valor;

    public ItemGasto(String fecha, String tipo, double valor) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.valor = valor;
    }

    //<editor-fold desc="GETTER">
    public String getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }
    //</editor-fold>

    //<editor-fold desc="SETTER">
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
    //</editor-fold>
}
