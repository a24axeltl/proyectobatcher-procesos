/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.procesos.proyectobatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author W10-Portable
 */
public class Main {
    private static int cpuCores = 4;
    private static int memoryMB = 2048;
    
    public static void main(String[] args) throws IOException, InterruptedException {
        State state = new State();
        File jobsDir = new File("jobs");
        
        admisionsJobsToNewState(jobsDir,state);
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
        
        List<Process> processJobs = new ArrayList<>();
        for(Job jobREADY : state.getStateREADY()){
            if(state.getStateRUNNING().isEmpty()){
                processCreation(jobREADY,processJobs);
                state.getStateRUNNING().addFirst(jobREADY);
            } else {
                processCreation(jobREADY,processJobs);
                state.getStateRUNNING().add(jobREADY);
            }  
        }
        state.getStateREADY().clear();
        
        for(int i = 0; i < processJobs.size(); i++){
            Process processJob = processJobs.get(i);
            Job job = state.getStateRUNNING().get(i);
            int exitCode = processJob.waitFor();
            if(exitCode == 0){
                state.getStateDONE().add(job);
                freeResources(job);
                
            } else {
                state.getStateFAILED().add(job);
                freeResources(job);
            }
            System.out.println("[HIJO " + processJob.pid() + "]PROCESO TERMINADO CON SALIDA: " + exitCode);
            
            i++;
        }
        state.getStateRUNNING().clear();
        showJobsStates(state);
    }
    
    private static void admisionsJobsToNewState(File jobsDir, State state) throws IOException{
        String[] jobsFiles = jobsDir.list();
        for(String jobFile : jobsFiles){
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            Job jobYAML = objectMapper.readValue(new File("jobs/" + jobFile), Job.class);
            state.getStateNEW().add(jobYAML);
        }
    }
    
    private static void processCreation(Job jobREADY,List<Process> processJobs) throws IOException{
        String cp = System.getProperty("java.class.path");
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", cp, "com.mycompany.procesos.proyectobatcher.Worker",
                jobREADY.getId(),
                String.valueOf(jobREADY.getWorkload().getDuration_ms()),
                String.valueOf(jobREADY.getResources().getCpu_cores()),
                jobREADY.getResources().getMemory());
        pb.inheritIO();

        Process processJob = pb.start();
        processJobs.add(processJob);
    }
    
    private static void freeResources(Job job){
        cpuCores = cpuCores + job.getResources().getCpu_cores();
        String[] memoryJob = job.getResources().getMemory().split(" ");
        int memoryValue = Integer.parseInt(memoryJob[0]);
        memoryMB = memoryMB + memoryValue;
    }
    
    private static void showJobsStates(State state){
        System.out.println("State Jobs:");
        System.out.println("NEW: " + state.getStateNEW());
        System.out.println("READY: " + state.getStateREADY());
        System.out.println("WAITING: " +state.getStateWAITING());
        System.out.println("---------------------------------------");
        System.out.println("RUNNING: " +state.getStateRUNNING());
        System.out.println("DONE: " +state.getStateDONE());
        System.out.println("FAILED: " +state.getStateFAILED());
        System.out.println();
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
}
