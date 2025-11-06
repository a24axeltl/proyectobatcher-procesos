/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.procesos.proyectobatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author W10-Portable
 */
public class Main {
    public static void main(String[] args) throws IOException {
        int cpuCores = 1;
        int memoryMB = 256;
        
        State state = new State();
        
        File jobsDir = new File("jobs");
        String[] jobsFiles = jobsDir.list();
        for(String jobFile : jobsFiles){
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            Job jobYAML = objectMapper.readValue(new File("jobs/" + jobFile), Job.class);
            state.getStateNEW().add(jobYAML);
        }
        
        state.setStateNEW(priorityOrder(state));
        showJobsStates(state);
        
        for(Job jobNEW : state.getStateNEW()){            
            String[] memoryJob = jobNEW.getResources().getMemory().split(" ");
            int cpuCoresValue = jobNEW.getResources().getCpu_cores();
            int memoryValue = Integer.parseInt(memoryJob[0]);
            if(cpuCores >= cpuCoresValue && memoryMB >= memoryValue){
                state.getStateREADY().add(jobNEW);
                cpuCores = cpuCores - cpuCoresValue;
                memoryMB = memoryMB - memoryValue;
            } else {
                state.getStateWAITING().add(jobNEW);
            }
        }
        state.getStateNEW().clear();
        
        showJobsStates(state);
    }
    
    private static ArrayList<Job> priorityOrder(State state){
        ArrayList<Job> listJob = new ArrayList<>();
        for(Job jobNEW : state.getStateNEW()){
            int priority = jobNEW.getPriority();
            if(priority >= 1){
                listJob.addFirst(jobNEW);
            } else if(priority == 4){
                listJob.addLast(jobNEW);
            } else {
                listJob.add(jobNEW);
            }
        }
        return listJob;
    }
    
    private static void showJobsStates(State state){
        System.out.println("State Jobs:");
        System.out.println("NEW: " + state.getStateNEW());
        System.out.println("READY: " + state.getStateREADY());
        System.out.println("WAITING: " +state.getStateWAITING());
        System.out.println();
    }
}
