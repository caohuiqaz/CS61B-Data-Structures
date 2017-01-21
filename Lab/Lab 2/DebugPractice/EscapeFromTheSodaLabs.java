public class EscapeFromTheSodaLabs extends Trial {

    /**
     * General Ackbar has discovered treasures in the second floor labs. However, a nefarious entity, Leorge Gucas
     * has "deployed the algorithm" which is set to destroy the labs. To save Soda, collect all the treasures in as few
     * hops as possible. You may set only one breakpoint.
     */
    public static void main(String[] args) {
        /**
         * To get there in 5 hops, start at the same breakpoint. Simply, step out as soon as you
         * hit the first statement in trap() and double click at the end to avoid double hop
         * count in superTrap().
         *
         */
        Traveler cal = new Traveler("General Ackbar", 5, 8);
        treasureChest(cal);
        trap(cal);              // Simply step over this for 5 hops, no need to step into
        treasureChest(cal);
        superTrap(cal);         // The 'cheeky' way to cheat the problem is to evaluate expression
                                // on this step and then step over it. This counts the treasureChest()
                                // call twice and achieves the same effect with minimum hops.
        cal.endJourney();
    }

    public static void trap(Traveler t) {
        /**
         * The reason hops are counted twice here is due to the hops increment in nothingHere().
         * After we step over treasureChest, there is a sizeable time difference before we hit
         * step out. This is recorded. Also note, the step out goes over the rest of the program
         * before exiting. Therefore, it will go over the 10 loops listed. However, the time gap
         * is only sufficient for a hop to be registered in the first iteration.
         */

//        Use this for bonus #2
        if (t.isStopped()) {
            t.hop();
        }

        treasureChest(t);
        for (int i = 0; i < 10; i++) {
            nothingHere(t);
        }
    }

    public static void superTrap(Traveler t) {
        treasureChest(t);
        trap(t);
        for (int i = 0; i < 10; i++) {
            nothingHere(t);
        }
    }
}