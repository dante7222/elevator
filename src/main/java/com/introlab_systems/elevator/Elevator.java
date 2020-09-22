package com.introlab_systems.elevator;

import com.introlab_systems.building.Floor;
import com.introlab_systems.passenger.Passenger;

import java.util.List;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;


public class Elevator {
    private static final int MAX_PASSENGERS_AMOUNT = 5;
    private Queue<Passenger> passengersCabin;
    private final List<Floor> floors;
    private NavigableSet<Integer> destinations;
    private String currentDirection = "up";
    private Floor currentFloor;

    public Elevator(List<Floor> floors) {
        this.floors = floors;
        currentFloor = floors.get(0);
        passengersCabin = new ConcurrentLinkedDeque<>();
        destinations = new TreeSet<>();
    }

    public Floor getCurrentFloor() {
        return currentFloor;
    }

    public NavigableSet<Integer> getDestinations() {
        return destinations;
    }

    public String getCurrentDirection() {
        return currentDirection;
    }

    public Queue<Passenger> getPassengersCabin() {
        return passengersCabin;
    }

    /**
     * Starting point for elevator
     * {@code Thread.sleep(1500} is used only for
     * poor output :)
     *
     * @throws InterruptedException
     *
     * Elevator at first floor, and it's empty
     * calls {@link #loadEmptyElevator()}
     * after that he will follow passenger's destination,
     * unload them / load new one.
     *
     * <p>If not full - will {@link #loadEnRoutePassengers(int)}
     */
    public void start() throws InterruptedException {
        System.out.println("Empty elevator \n");
        System.out.printf(
                "%-3s|      <%-16s>         |%-12s%n%n",
                currentFloor.getFloorNumber(), passengersCabin, currentFloor.getPassengers()
        );
        loadEmptyElevator();

        while (!destinations.isEmpty()) {
            Thread.sleep(1500);

            if (currentDirection.equals("up")) {

                if (!destinations.isEmpty()) {
                    loadEnRoutePassengers(destinations.first());
                }
                moveToFloor(destinations.first());
                destinations.pollFirst();

            } else if (currentDirection.equals("down")) {

                if (!destinations.isEmpty()) {
                    loadEnRoutePassengers(destinations.last());
                }
                moveToFloor(destinations.last());
                destinations.pollLast();
            }
            unloadPassengers();

            Thread.sleep(1500);

            loadPassengers();
            leaveFloor();
            calculateDestination();
        }
    }

    /**
     * Loads elevator when it's empty.
     * If floor doesn't have passengers elevator checks next floor
     *
     * <p>When floor with passengers found - makes all necessary stuff.
     */
    public void loadEmptyElevator() {
        while (passengersCabin.isEmpty()) {
            if (currentFloor.getPassengers().isEmpty()) {
                moveRelativelyToCurrentFloor();
            } else {
                System.out.println(currentFloor.getPassengers());
                biggestDestinationOnFloor();
                loadPassengers();
                leaveFloor();
                calculateDestination();
            }
        }
    }

    /**
     * If {@link #isElevatorFree()} tries to
     * find {@link #findFloorWithSameDirectionPassengers(int)}
     * if it's find such floors - moving to the floor, load passengers
     * and recalculate elevator's destinations.
     * @param destination -  elevator's closest destination floor
     */
    public void loadEnRoutePassengers(int destination) {
        if (isElevatorFree()) {
            List<Floor> floorWithEnRoutePassengers = findFloorWithSameDirectionPassengers(destination);

            if (!floorWithEnRoutePassengers.isEmpty()) {
                moveToFloor(floorWithEnRoutePassengers.get(0).getFloorNumber());
                loadPassengers();
                calculateDestination();
            }
        }
    }

    /**
     * This method tries to find floors which have passengers with elevator's
     * current direction, which between elevator's position and closest destinations
     *
     * @param destination - elevator's closest destination floor
     * @return - floors which between elevator's current floor and closest destinations
     */
    public List<Floor> findFloorWithSameDirectionPassengers(int destination) {
        return floors.stream()
                .filter(floor -> {
                    int targetFloorNumber = floor.getFloorNumber();
                    if (currentDirection.equals("up")) {
                        if (targetFloorNumber < destination && targetFloorNumber > currentFloor.getFloorNumber()) {
                            return (destination - targetFloorNumber) > 1;
                        }
                    } else {
                        if (targetFloorNumber > destination && targetFloorNumber < currentFloor.getFloorNumber()) {
                            return (targetFloorNumber - destination > 1);
                        }
                    }
                    return false;
                })
                .filter(floor -> {
                    if (!floor.getPassengers().isEmpty()) {
                        return floor.getPassengers()
                                .stream().anyMatch(passenger -> passenger.getDirection().equals(currentDirection));
                    } else {
                        return false;
                    }
                }).collect(Collectors.toList());
    }

    public void moveRelativelyToCurrentFloor() {
        int currentFloorNumber = currentFloor.getFloorNumber();
        int lastFloorNumber = floors.size();

        if (currentFloorNumber >= 1 && currentFloorNumber < lastFloorNumber) {
            moveToFloor(currentFloorNumber + 1);

        } else if (currentFloorNumber <= lastFloorNumber && currentFloorNumber != 1) {
            moveToFloor(currentFloorNumber - 1);
        }
    }

    private void reverseElevatorDirection() {
        if (currentDirection.equals("up")) {
            currentDirection = "down";
        } else {
            currentDirection = "up";
        }
    }

    /**
     * Finds count passengers with elevator direction,
     * then finds count of passengers with opposite.
     * Change elevator direction accordingly to biggest
     * amount of passenger's direction of the floor.
     */
    public void biggestDestinationOnFloor() {
        final long countOfDirection = currentFloor.getPassengers().stream()
                .filter(passenger -> passenger.getDirection().equals(currentDirection))
                .count();

        final long countOfReverse = currentFloor.getPassengers().size() - countOfDirection;

        if (countOfDirection < countOfReverse) {
            reverseElevatorDirection();
        }
    }

    /**
     * Load  process delegated to passenger
     *
     * <p>Before loading process sets currentFloor elevator
     * from null to this(cause elevator is here now)
     * Checks is elevator free {@link #isElevatorFree()}
     * and allow passenger to load, he knows if his direction = elevator's direction.
     *
     * <p>{@code .filter}  for not load same passenger again,
     * because passenger decides to load only by current destination
     * of elevator (up/down)
     */
    public void loadPassengers() {
        System.out.println("Before loading \n");
        System.out.printf(
                "%-3s|      <%-16s>         |%-12s%n%n",
                currentFloor.getFloorNumber(), passengersCabin, currentFloor.getPassengers()
        );

        currentFloor.setElevator(this);
        Queue<Passenger> floorPassengers = currentFloor.getPassengers();

        if (isElevatorFree()) {
            if (!floorPassengers.isEmpty()) {
                floorPassengers.stream()
                        .filter(passenger -> passenger.getDestinationFloorNumber() != currentFloor.getFloorNumber()).
                        forEach(Passenger::loadToElevator);

                System.out.println("After loading \n");
                System.out.printf(
                        "%-3s|      <%-16s>         |%-12s%n%n",
                        currentFloor.getFloorNumber(), passengersCabin, currentFloor.getPassengers()
                );
            }
        }
    }

    /**
     * Unload process delegated to passenger
     */
    public void unloadPassengers() {
        passengersCabin.forEach(Passenger::unloadFromElevator);

        System.out.println("After unloading \n");
        System.out.printf(
                "%-3s|      <%-16s>         |%-12s%n%n",
                currentFloor.getFloorNumber(), passengersCabin, currentFloor.getPassengers()
        );
    }

    /**
     * This method created to avoid situation when
     * elevator unload passengers and after recalculation
     * of their destinations - takes them again.
     *
     * <p>Simply iterates through {@link #currentFloor} passengers
     *  generates destination and sets direction
     */
    public void leaveFloor() {
        currentFloor.setElevator(null);
        currentFloor.getPassengers().stream()
                .filter(passenger -> passenger.getDestinationFloorNumber() == currentFloor.getFloorNumber())
                .forEach(passenger -> {
                    passenger.generateDestinationFloor();
                    passenger.setDirection();
                });
    }

    public void moveToFloor(int floorNumber) {
        System.out.println("Going to Floor: " + floorNumber + "\n");

        if (currentFloor.getFloorNumber() < floorNumber) {
            moveUpTo(floorNumber);
        } else if (currentFloor.getFloorNumber() > floorNumber) {
            moveDownTo(floorNumber);
        }
    }

    private void moveUpTo(int floorNumber) {
        currentDirection = "up";
        if (floorNumber <= floors.size()) {
            for (int i = currentFloor.getFloorNumber(); i < floorNumber; i++) {
                currentFloor = floors.get(i);
            }
        }
    }

    private void moveDownTo(int floorNumber) {
        currentDirection = "down";
        if (floorNumber > 0) {
            for (int i = currentFloor.getFloorNumber() - 1; i >= floorNumber; i--) {
                currentFloor = floors.get(i - 1);
            }
        }
    }

    /**
     * Iterates through passengers currently in elevator and
     * fills {@link #destinations} fir passenger's destination floor.
     */
    public void calculateDestination() {
        passengersCabin.forEach(passenger -> destinations.add(passenger.getDestinationFloorNumber()));
    }

    public boolean isElevatorFree() {
        return passengersCabin.size() != MAX_PASSENGERS_AMOUNT;
    }
}
