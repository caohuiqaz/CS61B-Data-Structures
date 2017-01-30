package creatures;
import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.HugLifeUtils;
import java.awt.Color;
import java.util.Map;
import java.util.List;

public class Clorus extends Creature
{
	/** Amount of energy used per move. */
    private static final double ENERGY_TO_MOVE = 0.03;

    /** Amount of energy used per stay tick. */
    private static final double ENERGY_TO_STAY = 0.01;

    /** Fraction of energy to retain when replicating. */
    private static final double ENERGY_REPLICATE_MODIFIER = 0.5;

    /** Fraction of energy transferred to offspring. */
    private static final double ENERGY_TO_OFFSPRING_MODIFIER = 0.5;

	/** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;

	public Clorus(double e) 
	{
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
    }
	
	public Color color() {
        return color(r, g, b);
    }

    public void attack(Creature c) {
        energy = energy + c.energy();
    }

    public void move() {
        energy = energy - ENERGY_TO_MOVE;
    }

    public void stay() {
        energy = energy - ENERGY_TO_STAY;
    }

    public Creature replicate() {
        double offspringEnergy = energy * ENERGY_TO_OFFSPRING_MODIFIER;
        energy = energy * ENERGY_REPLICATE_MODIFIER;
        return new Clorus(offspringEnergy);
    }
	
	public Action chooseAction(Map<Direction, Occupant> neighbors) {

        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plip = getNeighborsOfType(neighbors, "plip");

        if (empties.size() == 0) {
            return new Action(Action.ActionType.STAY);
        }

        else if (plip.size() > 0) {
            return new Action(Action.ActionType.ATTACK,
                    HugLifeUtils.randomEntry(plip));
        }

        else if (energy >= 1) {
            return new Action(Action.ActionType.REPLICATE,
                    HugLifeUtils.randomEntry(empties));
        }

        return new Action(Action.ActionType.MOVE,
                HugLifeUtils.randomEntry(empties));
    }

}