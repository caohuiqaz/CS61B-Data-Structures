public abstract class Trial {

    public static void lava(Traveler t) {
        if (t.isStopped()) {
            t.hop();
            System.out.println("Ouch, that burns!");
            t.endJourney();
        }
    }

    public static void nothingHere(Traveler t) {
        if (t.isStopped()) {
           // t.hop();            // Comment this line for bonus #2
        }
    }

    public static void treasureChest(Traveler t) {
        if (t.isStopped()) {
            //t.hop();            // Comment this line for bonus #2
            t.collectTreasure();
        }
    }
}