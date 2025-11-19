/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package statesManagment;

import java.util.ArrayList;
import jobData.Job;

/**
 *
 * @author W10-Portable
 */
public class State {
    private ArrayList<Job> stateNEW;
    private ArrayList<Job> stateREADY;
    private ArrayList<Job> stateWAITING;
    private ArrayList<Job> stateRUNNING;
    private ArrayList<Job> stateDONE;
    private ArrayList<Job> stateFAILED;
    
    public State(){
        stateNEW = new ArrayList<>();
        stateREADY = new ArrayList<>();
        stateWAITING = new ArrayList<>();
        stateRUNNING = new ArrayList<>();
        stateDONE = new ArrayList<>();
        stateFAILED = new ArrayList<>();
    }

    /**
     * @return the stateNEW
     */
    public ArrayList<Job> getStateNEW() {
        return stateNEW;
    }

    /**
     * @param stateNEW the stateNEW to set
     */
    public void setStateNEW(ArrayList<Job> stateNEW) {
        this.stateNEW = stateNEW;
    }

    /**
     * @return the stateREADY
     */
    public ArrayList<Job> getStateREADY() {
        return stateREADY;
    }

    /**
     * @param stateREADY the stateREADY to set
     */
    public void setStateREADY(ArrayList<Job> stateREADY) {
        this.stateREADY = stateREADY;
    }

    /**
     * @return the stateWAITING
     */
    public ArrayList<Job> getStateWAITING() {
        return stateWAITING;
    }

    /**
     * @param stateWAITING the stateWAITING to set
     */
    public void setStateWAITING(ArrayList<Job> stateWAITING) {
        this.stateWAITING = stateWAITING;
    }

    /**
     * @return the stateDONE
     */
    public ArrayList<Job> getStateDONE() {
        return stateDONE;
    }

    /**
     * @param stateDONE the stateDONE to set
     */
    public void setStateDONE(ArrayList<Job> stateDONE) {
        this.stateDONE = stateDONE;
    }

    /**
     * @return the stateFAILED
     */
    public ArrayList<Job> getStateFAILED() {
        return stateFAILED;
    }

    /**
     * @param stateFAILED the stateFAILED to set
     */
    public void setStateFAILED(ArrayList<Job> stateFAILED) {
        this.stateFAILED = stateFAILED;
    }

    /**
     * @return the stateRUNNING
     */
    public ArrayList<Job> getStateRUNNING() {
        return stateRUNNING;
    }

    /**
     * @param stateRUNNING the stateRUNNING to set
     */
    public void setStateRUNNING(ArrayList<Job> stateRUNNING) {
        this.stateRUNNING = stateRUNNING;
    }
}
