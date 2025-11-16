/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.procesos.proyectobatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
        
        List<Job> stateNEW_Copy = new ArrayList<>(state.getStateNEW());
        for (Job jobNEW : stateNEW_Copy) {
            String[] memoryJob = jobNEW.getResources().getMemory().split(" ");
            int cpuCoresValue = jobNEW.getResources().getCpu_cores();
            int memoryValue = Integer.parseInt(memoryJob[0]);

            if (cpuCores >= cpuCoresValue && memoryMB >= memoryValue) {
                state.getStateREADY().add(jobNEW);
                state.getStateNEW().remove(jobNEW);
                cpuCores = cpuCores - cpuCoresValue;
                memoryMB = memoryMB - memoryValue;
            } else {
                state.getStateWAITING().add(jobNEW);
                state.getStateNEW().remove(jobNEW);
            }
        }
        showJobsStates(state);
        
        while(!state.getStateWAITING().isEmpty() || !state.getStateRUNNING().isEmpty()){
            checkWaitingJobs(state);
            
            List<Process> processJobs = new ArrayList<>();
            List<BufferedReader> readersJobs = new ArrayList<>();

            List<Job> stateREADY_Copy = new ArrayList<>(state.getStateREADY());
            for (Job jobREADY : stateREADY_Copy) {
                if (state.getStateRUNNING().isEmpty()) {
                    processCreation(jobREADY, processJobs, readersJobs);
                    state.getStateRUNNING().add(0, jobREADY);
                    state.getStateREADY().remove(jobREADY);
                } else {
                    processCreation(jobREADY, processJobs, readersJobs);
                    state.getStateRUNNING().add(jobREADY);
                    state.getStateREADY().remove(jobREADY);
                }
            }
            showJobsStates(state);

            for (BufferedReader lectorJob : readersJobs) {
                String line;
                while ((line = lectorJob.readLine()) != null) {
                    System.out.println(line);
                }
            }

            for (int i = 0; i < processJobs.size(); i++) {
                Process processJob = processJobs.get(i);
                Job job = state.getStateRUNNING().get(i);
                int exitCode = processJob.waitFor();
                if (exitCode == 0) {
                    state.getStateDONE().add(job);
                    freeResources(job);

                } else {
                    state.getStateFAILED().add(job);
                    freeResources(job);
                }
                System.out.println("[HIJO " + processJob.pid() + "]PROCESO TERMINADO CON SALIDA: " + exitCode);
            }
            state.getStateRUNNING().clear();
            showJobsStates(state);
        }
    }
    
    private static void admisionsJobsToNewState(File jobsDir, State state) throws IOException{
        String[] jobsFiles = jobsDir.list();
        if(jobsFiles != null){
            Arrays.sort(jobsFiles);
            for (String jobFile : jobsFiles) {
                ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                Job jobYAML = objectMapper.readValue(new File("jobs/" + jobFile), Job.class);
                state.getStateNEW().add(jobYAML);
            }
        } else {
            System.out.println("NO se han detectado jobs para procesar. Cerrando el programa");
            System.exit(0);
        }
        
    }
    
    private static void freeResources(Job job){
        cpuCores = cpuCores + job.getResources().getCpu_cores();
        String[] memoryJob = job.getResources().getMemory().split(" ");
        int memoryValue = Integer.parseInt(memoryJob[0]);
        memoryMB = memoryMB + memoryValue;
    }
    
    private static void checkWaitingJobs(State state){
        List<Job> stateWAITY_Copy = new ArrayList<>(state.getStateWAITING());
        for (Job jobWAITING : stateWAITY_Copy) {
            String[] memoryJob = jobWAITING.getResources().getMemory().split(" ");
            int cpuCoresValue = jobWAITING.getResources().getCpu_cores();
            int memoryValue = Integer.parseInt(memoryJob[0]);

            if (cpuCores >= cpuCoresValue && memoryMB >= memoryValue) {
                state.getStateREADY().add(jobWAITING);
                state.getStateWAITING().remove(jobWAITING);
                cpuCores = cpuCores - cpuCoresValue;
                memoryMB = memoryMB - memoryValue;
            }
        }
    }
    
    private static void processCreation(Job jobREADY,List<Process> processJobs, List<BufferedReader> readersJobs) throws IOException{
        String cp = System.getProperty("java.class.path");
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", cp, "com.mycompany.procesos.proyectobatcher.Worker",
                jobREADY.getId(),
                String.valueOf(jobREADY.getWorkload().getDuration_ms()),
                String.valueOf(jobREADY.getResources().getCpu_cores()),
                jobREADY.getResources().getMemory());

        Process processJob = pb.start();
        processJobs.add(processJob);
        readersJobs.add(new BufferedReader(new InputStreamReader(processJob.getInputStream())));
    }
    
    private static void showJobsStates(State state){
        System.out.println("=========================================================================");
        System.out.println("State Jobs:");
        System.out.println("CPU: " + cpuCores + " | Memory: " + memoryMB);
        System.out.println();
        System.out.println("NEW("+state.getStateNEW().size()+"): " + state.getStateNEW());
        System.out.println("READY("+state.getStateREADY().size()+"): " + state.getStateREADY());
        System.out.println("WAITING("+state.getStateWAITING().size()+"):" + state.getStateWAITING());
        System.out.println("---------------------------------------");
        System.out.println("RUNNING("+state.getStateRUNNING().size()+"): " +state.getStateRUNNING());
        System.out.println("DONE("+state.getStateDONE().size()+"): " +state.getStateDONE());
        System.out.println("FAILED("+state.getStateFAILED()+": " +state.getStateFAILED());
        System.out.println("=========================================================================");
    } 
}
