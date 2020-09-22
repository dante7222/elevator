package com.introlab_systems.output;

import com.introlab_systems.building.Floor;
import com.introlab_systems.passenger.Passenger;

import java.util.*;

/**
 * Currently unused, it was created for capture
 * elevator's each step and then draw it, but
 * due to lack of time - incomplete.
 */
public class Result {
    private static final Result result = new Result();
    private static List<Step> steps = new ArrayList<>();

    public static Result getResult() {
        return result;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public static class Step {
        private Queue<Passenger> elevatorPassengers;
        private String currentDirection;
        private Floor currentFloor;
        private Set<Integer> destinations;

        public Step(Queue<Passenger> lifePassengers, String currentDirection,
                    Floor currentFloor, Set<Integer> destinations) {
            this.elevatorPassengers = lifePassengers;
            this.currentDirection = currentDirection;
            this.currentFloor = currentFloor;
            this.destinations = destinations;
        }

        public Queue<Passenger> getElevatorPassengers() {
            return elevatorPassengers;
        }

        public void setElevatorPassengers(Queue<Passenger> elevatorPassengers) {
            this.elevatorPassengers = elevatorPassengers;
        }

        public String getCurrentDirection() {
            return currentDirection;
        }

        public void setCurrentDirection(String currentDirection) {
            this.currentDirection = currentDirection;
        }

        public Floor getCurrentFloor() {
            return currentFloor;
        }

        public void setCurrentFloor(Floor currentFloor) {
            this.currentFloor = currentFloor;
        }
    }
}
