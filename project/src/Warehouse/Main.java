package Warehouse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {

    public static void main(String[] pars) {

        // Set up various groups of workers.
        HashMap<String, Picker> pickers = new HashMap<String, Picker>();
        HashMap<String, Sequencer> sequencers = new HashMap<String, Sequencer>();
        HashMap<String, Loader> loaders = new HashMap<String, Loader>();

        // Read translation table and convert to usable format.
        HashMap<String, String[]> mapSKU = new HashMap<>();
        try {
            Scanner scanner = new Scanner(new File("CSC207/Project/group_0384/project/translation.csv"));
            scanner.next();
            scanner.next();
            scanner.next();
            while (scanner.hasNext()) {
                String[] nextType = scanner.next().split(",");
                String[] frontColourModel = {nextType[0], nextType[1], "Front"};
                String[] backColourModel = {nextType[0], nextType[1], "Back"};
                mapSKU.put(nextType[2], frontColourModel);
                mapSKU.put(nextType[3], backColourModel);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read traversal table and convert to usable format.
        HashMap<String, String[]> locationSKUs = new HashMap<>();
        try {
            Scanner scanner = new Scanner(new File("CSC207/Project/group_0384/project/traversal_table.csv"));
            while (scanner.hasNext()) {
                String[] nextPlace = scanner.next().split(",");
                String[] location = {nextPlace[0], nextPlace[1], nextPlace[2], nextPlace[3]};
                locationSKUs.put(nextPlace[4], location);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set up Warehouse.
        Warehouse warehouse = new Warehouse();

        // Set up data structures to represent the order in which things are done in the warehouse.
        ArrayList<Order> orders = new ArrayList<Order>();
        LinkedList<PickingRequest> pickRequests = new LinkedList<PickingRequest>();
        LinkedList<PickingRequest> sequenceRequests = new LinkedList<PickingRequest>();
        LinkedList<PickingRequest> loadRequests = new LinkedList<PickingRequest>();


        // Read from event log and run simulation.
        try {
            Scanner scanner = new Scanner(new File("CSC207/Project/group_0384/project/event_log.txt"));

            while (scanner.hasNext()) {
                String[] event = scanner.next().split(" ");
                // If the event is an incoming order.
                if (event[0].equals("Order")) {
                    String[] nextOrder = {event[1], event[2]};
                    orders.add(Order(nextOrder));
                    if (orders.size() == 4) {
                        pickRequests.add(PickingRequest(orders));
                        orders.clear();
                    }
                }
                // If the event involves a picker.
                else if (event[0].equals("Picker")) {
                    String pickerName = event[1];
                    // If picker is not currently registered.
                    if (!pickers.containsKey(pickerName)) {
                        pickers.put(pickerName, Picker(pickerName));
                    }
                    // If picker is ready.
                    else if (event[2].equals("ready")) {
                        pickers.get(pickerName).start(pickRequests.poll());
                    }
                    // If picker picked something.
                    else if (event[2].equals("pick")) {
                        pickers.get(pickerName).pick(Fascia(event[3], mapSKU.get(event[3])));
                        warehouse.remove(locationSKUs.get(event[3]));
                    }
                    // If picker is sent to marshalling.
                    else if (event[2].equals("to")) {
                        sequenceRequests.add(pickers.get(pickerName).unload());
                    }
                }
                // If the event involves a sequencer.
                else if (event[0].equals("Sequencer")) {
                }
                // If the event involves a loader.
                else if (event[0].equals("Loader")) {
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
