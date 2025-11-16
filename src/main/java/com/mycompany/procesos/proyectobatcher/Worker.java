/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.procesos.proyectobatcher;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

/**
 *
 * @author W10-Portable
 */
public class Worker {
    private static int loadWork = 2000;
    
    public static void main(String[] args) {
        if(args.length < 4){
            System.out.println("No se han pasado los argumentos requeridos");
            System.exit(1);
        } else {
            String id = args[0];
            long duration_ms = Long.parseLong(args[1]);
            int cpu_cores = Integer.parseInt(args[2]);
            String mem_mb = args[3];
            
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
                long elapsed = 0;
                
                while(elapsed <= duration_ms){
                    double progress = Math.min(1.0, (double) elapsed / duration_ms);
                    
                    bw.write("{\"jobId\":\"" + id + "\",\"progress\":" + progress + "}");
                    bw.newLine();
                    bw.flush();
                    
                    String fakeData = "dataTrash...";
                    for(int i = 0; i < loadWork; i++){
                        fakeData = fakeData.replace('d', 'D');
                    }
                    
                    Thread.sleep(500);
                    
                    elapsed += 500;
                }
                System.exit(0);
            } catch (Exception e){
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }
    }
}
