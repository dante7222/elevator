package com.introlab_systems.building;

import com.introlab_systems.passenger.Passenger;
import com.introlab_systems.utils.Utils;
import com.introlab_systems.elevator.Elevator;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Floor {
    private final int FLOOR_NUMBER;
    private Queue<Passenger> passengers;
    private Elevator elevator;

    public Floor(int FLOOR_NUMBER) {
        this.FLOOR_NUMBER = FLOOR_NUMBER;
        passengers = new ConcurrentLinkedDeque<>();
    }

    public int getFloorNumber() {
        return FLOOR_NUMBER;
    }

    public Queue<Passenger> getPassengers() {
        return passengers;
    }

    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
    }

    public boolean isElevatorHere() {
        return elevator != null;
    }

    public boolean isElevatorFree() {
        return elevator.isElevatorFree();
    }

    public void generatePassengersOnFloor() {
        int passengersAmount = Utils.generateAmount(0, 10);

        for (int i = 0; i < passengersAmount; i++) {
            passengers.add(new Passenger(this));
        }
    }

    public Elevator getElevator() {
        return elevator;
    }
}
