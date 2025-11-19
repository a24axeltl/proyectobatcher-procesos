/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import computerManagment.Computer;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jobData.Job;
import statesManagment.State;

/**
 *
 * @author W10-Portable
 */
public class PSPBatcher {
    private static final int cpuCoresMax = 4;
    private static final int memoryMbMax = 2048;
    
    public static void main(String[] args) throws IOException, InterruptedException {
        Computer computer = new Computer(cpuCoresMax, memoryMbMax);
        State state = new State();
        
        admisionsJobsToNewState(state);
        showJobsStates(state, computer);
        
        if(!state.getStateNEW().isEmpty()){
            admisionsNewJobsToReadyState(state, computer);
            
            showJobsStates(state, computer);
            runningProcessOfJobs(state, computer);
            
            while (!state.getStateWAITING().isEmpty() || !state.getStateRUNNING().isEmpty() || !state.getStateREADY().isEmpty()) {
                runningProcessOfJobs(state, computer);
            }
        }
    }
    
    private static void runningProcessOfJobs(State state, Computer computer) throws IOException, InterruptedException{
        List<Process> processJobs = new ArrayList<>();
        List<BufferedReader> readersJobs = new ArrayList<>();
        admisionsReadyJobsToRunningState(processJobs, readersJobs, state);

        showJobsStates(state, computer);

        readingSons(readersJobs);
        waitingSons(processJobs, state, computer);

        showJobsStates(state, computer);
    }
    
    private static void admisionsJobsToNewState(State state) throws IOException{
        File jobsDir = new File("jobs");
        String[] jobsFiles = jobsDir.list();
        if(jobsFiles != null){
            Arrays.sort(jobsFiles);
            for (String jobFile : jobsFiles) {
                ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                Job jobYAML = objectMapper.readValue(new File("jobs/" + jobFile), Job.class);
                if(jobYAML.getId() != null && jobYAML.getName() != null && jobYAML.getResources().getMemory() != null){
                    if (!jobYAML.getId().isEmpty() && !jobYAML.getName().isEmpty() && (jobYAML.getPriority() >= 0 && jobYAML.getPriority() <= 4) && jobYAML.getCpuValue() > 0 && jobYAML.getMemoryValue() > 0 && jobYAML.getWorkload().getDuration_ms() > 0) {
                        state.getStateNEW().add(jobYAML);
                    } else {
                        System.out.println("El fichero con ID (" + jobYAML.getId() + ") no cumple con los requisitos de validacion");
                    }
                } else {
                    System.out.println("El fichero con ID (" + jobYAML.getId() + ") no cumple con los requisitos de validacion [NULL]");
                }
            }
        } else {
            System.out.println("No se han detectado jobs para procesar. Cerrando el programa");
            System.exit(0);
        }   
    }
    
    private static void admisionsNewJobsToReadyState(State state, Computer computer){
        List<Job> stateNEW_Copy = new ArrayList<>(state.getStateNEW());
        for (Job jobNEW : stateNEW_Copy) {
            int cpuCoresValue = jobNEW.getCpuValue();
            int memoryValue = jobNEW.getMemoryValue();
            if(cpuCoresValue <= cpuCoresMax && memoryValue <= memoryValue){
                if (computer.getCpuCores() >= cpuCoresValue && computer.getMemoryMB() >= memoryValue) {
                    state.getStateREADY().add(jobNEW);
                    state.getStateNEW().remove(jobNEW);
                    reserveResources(jobNEW, computer);
                } else {
                    state.getStateWAITING().add(jobNEW);
                    state.getStateNEW().remove(jobNEW);
                }
            } else {
                System.out.println("El job " + jobNEW.getId() + " necesita mas recursos para ser ejecutados (Sera retirado)");
                state.getStateNEW().remove(jobNEW);
            }
        }
    }
    
    private static void admisionsReadyJobsToRunningState(List<Process> processJobs, List<BufferedReader> readersJobs, State state) throws IOException{
        List<Job> stateREADY_Copy = new ArrayList<>(state.getStateREADY());
        for (Job jobREADY : stateREADY_Copy) {
            if (state.getStateRUNNING().isEmpty()) {
                processCreation(jobREADY, processJobs, readersJobs);
                state.getStateRUNNING().add(0, jobREADY);
                
                long startTime = System.nanoTime();
                jobREADY.setStartTime(startTime);
                
                state.getStateREADY().remove(jobREADY);
            } else {
                processCreation(jobREADY, processJobs, readersJobs);
                state.getStateRUNNING().add(jobREADY);
                
                long startTime = System.nanoTime();
                jobREADY.setStartTime(startTime);
                
                state.getStateREADY().remove(jobREADY);
            }
        }
    }
    
    private static void processCreation(Job jobREADY,List<Process> processJobs, List<BufferedReader> readersJobs) throws IOException{
        String cp = System.getProperty("java.class.path");
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", cp, "main.WorkerBatcher",
                jobREADY.getId(),
                String.valueOf(jobREADY.getWorkload().getDuration_ms()),
                String.valueOf(jobREADY.getCpuValue()),
                jobREADY.getResources().getMemory());

        Process processJob = pb.start();
        processJobs.add(processJob);
        readersJobs.add(new BufferedReader(new InputStreamReader(processJob.getInputStream())));
    }
    
    private static void readingSons(List<BufferedReader> readersJobs) throws IOException{
        for (BufferedReader lectorJob : readersJobs) {
            String line;
            while ((line = lectorJob.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
    
    private static void waitingSons(List<Process> processJobs, State state, Computer computer) throws InterruptedException{
        for (int i = 0; i < processJobs.size(); i++) {
            Process processJob = processJobs.get(i);
            Job job = state.getStateRUNNING().get(i);
            int exitCode = processJob.waitFor();
            if (exitCode == 0) {
                state.getStateDONE().add(job);
                
                long endTime = System.nanoTime();
                job.setDurationTime(endTime - job.getStartTime());
                
                freeResources(job, computer);
                checkWaitingJobs(state, computer);

            } else {
                state.getStateFAILED().add(job);
                freeResources(job, computer);
                checkWaitingJobs(state, computer);
            }
            System.out.println("[HIJO " + processJob.pid() + "]PROCESO TERMINADO CON SALIDA: " + exitCode);
        }
        state.getStateRUNNING().clear();
        showJobsStates(state, computer);
    }
    
    private static void checkWaitingJobs(State state, Computer computer){
        List<Job> stateWAITY_Copy = new ArrayList<>(state.getStateWAITING());
        for (Job jobWAITING : stateWAITY_Copy) {
            int cpuCoresValue = jobWAITING.getCpuValue();
            int memoryValue = jobWAITING.getMemoryValue();

            if (computer.getCpuCores() >= cpuCoresValue && computer.getMemoryMB() >= memoryValue) {
                state.getStateREADY().add(jobWAITING);
                state.getStateWAITING().remove(jobWAITING);
                reserveResources(jobWAITING, computer);
            }
        }
    }
    
    private static void showJobsStates(State state, Computer computer) throws InterruptedException{
        System.out.println("=========================================================================");
        System.out.println("State Jobs:");
        System.out.println("CPU: " + computer.getCpuCores() + " | Memory: " + computer.getMemoryMB());
        System.out.println();
        System.out.println("NEW("+state.getStateNEW().size()+"): " + state.getStateNEW());
        System.out.println("READY("+state.getStateREADY().size()+"): " + state.getStateREADY());
        System.out.println("WAITING("+state.getStateWAITING().size()+"):" + state.getStateWAITING());
        System.out.println("---------------------------------------");
        System.out.println("RUNNING("+state.getStateRUNNING().size()+"): " + state.getStateRUNNING());
        System.out.println("DONE("+state.getStateDONE().size()+"): " + state.getStateDONE());
        System.out.println("FAILED("+state.getStateFAILED()+": " + state.getStateFAILED());
        System.out.println("=========================================================================");
        Thread.sleep(1800);
    }
    
    private static void freeResources(Job job, Computer computer){
        computer.setCpuCores(computer.getCpuCores() + job.getCpuValue());
        computer.setMemoryMB(computer.getMemoryMB() + job.getMemoryValue());
    }
    
    private static void reserveResources(Job job, Computer computer){
        computer.setCpuCores(computer.getCpuCores() - job.getCpuValue());
        computer.setMemoryMB(computer.getMemoryMB() - job.getMemoryValue());
    }
}
