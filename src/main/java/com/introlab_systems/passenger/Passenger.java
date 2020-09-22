package com.introlab_systems.passenger;

import com.introlab_systems.building.Building;
import com.introlab_systems.building.Floor;
import com.introlab_systems.elevator.Elevator;

import static com.introlab_systems.utils.Utils.generateAmountExcept;

public class Passenger {
    private Floor currentFloor;
    private int destinationFloorNumber;
    private String direction;
    private Elevator elevator;

    public Passenger(Floor currentFloor) {
        this.currentFloor = currentFloor;
    }

    public String getDirection() {
        return direction;
    }

    public void setCurrentFloor(Floor currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getDestinationFloorNumber() {
        return destinationFloorNumber;
    }

    public void generateDestinationFloor() {
        int floorsAmount = Building.getFloorsAmount();
        this.destinationFloorNumber = generateAmountExcept(floorsAmount, currentFloor.getFloorNumber());
    }

    public void setDirection() {
        if (currentFloor.getFloorNumber() < destinationFloorNumber) {
            direction = "up";
        } else {
            direction = "down";
        }
    }

    public void loadToElevator() {
        if (currentFloor.isElevatorHere() && currentFloor.isElevatorFree()) {
            elevator = currentFloor.getElevator();
            if (direction.equals(elevator.getCurrentDirection())) {
                elevator.getPassengersCabin().add(this);
                currentFloor.getPassengers().remove(this);
            }
        }
    }

    public void unloadFromElevator() {
        boolean isRequiredFloor =
                elevator.getCurrentFloor().getFloorNumber() == destinationFloorNumber;
        if (isRequiredFloor) {
            setCurrentFloor(elevator.getCurrentFloor());
            currentFloor.getPassengers().add(this);
            elevator.getPassengersCabin().remove(this);
            elevator = null;
        }
    }

    @Override
    public String toString() {
        return destinationFloorNumber + "";
    }
}
