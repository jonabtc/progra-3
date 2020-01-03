package com.example.brutalcorpse.gpacalculadora;

/**
 * Created by brutalcorpse on 23/01/17.
 */



public class Cursos {

    private String nombre, nota, creditos;
    public static int creditosTotal = 0;
    public static int  sumaNotas = 0;
    public static String gpa = "0";

    public Cursos(String nombre, String creditos, String nota) {
        this.nombre = nombre;
        this.creditos = creditos;
        this.nota = nota;
       aumentarVariables(creditos, nota);

    }
    public void aumentarVariables(String credit, String not){
        int a = Integer.parseInt(credit);

        creditosTotal += a;
        if(not.equalsIgnoreCase("A"))
            sumaNotas +=  a*4;
        if(not.equalsIgnoreCase("B"))
            sumaNotas +=  a*3;
        if(not.equalsIgnoreCase("C"))
            sumaNotas +=  a*2;
        if(not.equalsIgnoreCase("D"))
            sumaNotas +=  a*1;
        setGPA();
        /*
        System.out.println("================================");
        System.out.println(sumaNotas);
        System.out.println(creditosTotal);
        System.out.println(getGPA());*/
    }
    public static String getGPA(){

        return gpa;
    }
    public static void eliminandoCursos(Cursos curso){
        String not = curso.getNota();
        int a = Integer.parseInt(curso.getCreditos());

        creditosTotal -= a;
        if(not.equalsIgnoreCase("A"))
            sumaNotas -=  a*4;
        if(not.equalsIgnoreCase("B"))
            sumaNotas -=  a*3;
        if(not.equalsIgnoreCase("C"))
            sumaNotas -=  a*2;
        if(not.equalsIgnoreCase("D"))
            sumaNotas -=  a*1;
        setGPA();
    }
    public static void setGPA(){
        float a = 0.00f;
        a = (float) sumaNotas/ (float) creditosTotal;

        gpa = Float.toString(a);
    }
    public static void restart(){
        creditosTotal = 0;
        sumaNotas = 0;
        gpa = "0";
    }
    //<editor-fold desc="GET Y SET">
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCreditos() {
        return creditos;
    }

    public void setCreditos(String creditos) {
        this.creditos = creditos;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
    //</editor-fold>
}
