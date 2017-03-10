import java.util.HashMap;

public class Warehouse {
    /**
     * The representation of the floor of this Warehouse, with the number of fascia stored in each location.
     */
    private HashMap<String, int[][][]> floor = new HashMap<>();


    /**
     * Constructs a new Warehouse, with each level containing 30 fascia.
     */
    public Warehouse() {
        int[][][] zoneA = new int[2][3][4];
        int[][][] zoneB = new int[2][3][4];
        for (int aisle = 0; aisle < 2; aisle++) {
            for (int rack = 0; rack < 3; rack++) {
                for (int level = 0; level < 4; level++) {
                    zoneA[aisle][rack][level] = 30;
                    zoneB[aisle][rack][level] = 30;
                }
            }
        }
        this.floor.put("A", zoneA);
        this.floor.put("B", zoneB);
    }


    /**
     * @param location A specific location in Warehouse represented as a list of ints
     *                 containing 4 pieces of information: the
     *                 zone (an integer in the range [0..1] where 0 is zone A and 1 is zone B),
     *                 the aisle number (an integer
     *                 in the range [0..1]), the rack number (an integer in the range ([0..2]),
     *                 and the level on the rack (an integer in the range [0..3]).
     * @return The number of fascia at location.
     */
    public int numberFascia(int[] location) {
        String zone = "";
        if (location[0] == 0) {
            zone = "A";
        } else if (location[0] == 1) {
            zone = "B";
        }
        int aisle = location[1];
        int rack = location[2];
        int level = location[3];
        return this.floor.get(zone)[aisle][rack][level];
    }

}
