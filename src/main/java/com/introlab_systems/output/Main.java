package com.introlab_systems.output;

import com.introlab_systems.building.Building;
import com.introlab_systems.elevator.Elevator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Building building = new Building(5, 20);

        building.getFloors().forEach(floor -> floor.getPassengers()
                .forEach(passenger -> {
                    passenger.generateDestinationFloor();
                    passenger.setDirection();
                }));

        Elevator elevator = building.getElevator();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Elevator will start work until he have destinations");
        System.out.println("after that type something and he will continue");
        System.out.println("type something");

        while (scanner.hasNextLine()) {
            System.out.println("type something");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("stop")) {
                break;
            }
            elevator.start();
        }
    }
}
