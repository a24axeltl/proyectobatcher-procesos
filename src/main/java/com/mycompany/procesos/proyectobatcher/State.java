/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.procesos.proyectobatcher;

import java.util.ArrayList;

/**
 *
 * @author W10-Portable
 */
public class State {
    private ArrayList<Job> stateNEW;
    private ArrayList<Job> stateREADY;
    private ArrayList<Job> stateWAITING;
    
    public State(){
        stateNEW = new ArrayList<>();
        stateREADY = new ArrayList<>();
        stateWAITING = new ArrayList<>();
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
}
