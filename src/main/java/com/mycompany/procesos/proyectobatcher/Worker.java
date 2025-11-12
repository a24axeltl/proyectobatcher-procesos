/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.procesos.proyectobatcher;

/**
 *
 * @author W10-Portable
 */
public class Worker {
    public static void main(String[] args){
        String id = args[0];
        int duration_ms = Integer.parseInt(args[1]);
        int cpu_cores = Integer.parseInt(args[2]);
        String mem_mb = args[3];
        
        System.out.println("Datos Job: " + id + " | " + duration_ms + " | " + cpu_cores + " | " + mem_mb);
    }
}
