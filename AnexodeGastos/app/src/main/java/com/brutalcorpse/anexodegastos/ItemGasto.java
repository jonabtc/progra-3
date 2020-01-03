package com.brutalcorpse.anexodegastos;


public class ItemGasto {
    static  double VIVIENDA=0, ALIMENTACION=0, VESTIMENTA=0, SALUD=0, EDUCACION=0;
    static  double VIVIENDA_MAX=3650.00, ALIMENTACION_MAX=3650.0, VESTIMENTA_MAX=3650.0, SALUD_MAX=3650.0, EDUCACION_MAX=3650.0;
    private String fecha, tipo;
    private double valor;

    public ItemGasto(String fecha, String tipo, double valor) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.valor = valor;
        setValorTipo(tipo, valor);
    }

    private void setValorTipo(String tipo, double valor) {

        if(tipo.equals("Vivienda"))
            VIVIENDA+=valor;

        if(tipo.equals("Alimentación"))
            ALIMENTACION+=valor;

        if(tipo.equals("Vestimenta"))
            VESTIMENTA+=valor;

        if(tipo.equals("Salud"))
            SALUD+=valor;

        if(tipo.equals("Educación"))
            EDUCACION+=valor;
    }

    public static void reset(){
        VIVIENDA=0;
        ALIMENTACION=0;
        VESTIMENTA=0;
        SALUD=0;
        EDUCACION=0;
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

    public static void setViviendaMax(double viviendaMax) {
        if(viviendaMax>=100.0)
            VIVIENDA_MAX = viviendaMax;
        else
            VIVIENDA_MAX = 3650.0;
    }

    public static void setAlimentacionMax(double alimentacionMax) {
        if(alimentacionMax>=100.0)
        ALIMENTACION_MAX = alimentacionMax;
        else
            ALIMENTACION_MAX = 3650.0;
    }

    public static void setVestimentaMax(double vestimentaMax) {
        if(vestimentaMax>=100.0)
        VESTIMENTA_MAX = vestimentaMax;
        else
            VESTIMENTA_MAX = 3650.0;
    }

    public static void setSaludMax(double saludMax) {
        if(saludMax>=100.0)
        SALUD_MAX = saludMax;
        else
            SALUD_MAX = 3650.0;
    }

    public static void setEducacionMax(double educacionMax) {
        if(educacionMax>=100.0)
        EDUCACION_MAX = educacionMax;
        else
            EDUCACION_MAX = 3650.0;
    }
    public static void eliminando(String t, Double d){
        if(t.equals("Vivienda"))
            VIVIENDA -= d;
        if(t.equals("Educación"))
            EDUCACION -= d;
        if(t.equals("Salud"))
            SALUD -= d;
        if(t.equals("Vestimenta"))
            VESTIMENTA -= d;
        if(t.equals("Alimentacióm"))
            ALIMENTACION -= d;
    }

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


    @Override
    public String toString() {
        return tipo;
    }

}
