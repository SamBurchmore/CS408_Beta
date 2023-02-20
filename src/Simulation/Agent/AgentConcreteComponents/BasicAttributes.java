package Simulation.Agent.AgentConcreteComponents;

import Simulation.Agent.AgentInterfaces.Attributes;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

/** Makes up the agents attributes. Attributes are set values that don't change during the agent's life, except if
 * the agent mutates. Attributes define the agents qualities and behavior as well as their color and name.
 * @author Sam Burchmore
 * @version 1.0a
 * @since 1.0a
 */
public class BasicAttributes implements Attributes, Serializable {

    // The weight used by the Simulation class when it populates the environment
    private double spawningWeight;
    // The string that identifies the agent in the diagnostics view
    private String name;
    // The agents unique identifier, an agent will breed with and not eat agents with the same code
    private int code;
    // The percentage value for how likely the attributes are to mutate
    private int mutationChance;
    // The agents initial color
    private Color seedColor;

    // How far the agent can see and move in one turn
    private int range;
    // Used to calculate all the agents calculated stats
    private int size;
    // How many adjacent squares the agent will try to have children in, also used to calculate the creation cost.
    private int creationSize;

    // How much energy the agent can store = size*10
    private int energyCapacity;
    // How much energy the agent will lose per tile moved = size^0.7
    private int energyLostPerTile;
    // How much energy the agent can take from the environment in one turn = (size^0.7)*2
    private int eatAmount;
    // How long the agent can exist for = 25 + size^1.1
    private int lifespan;
    // How old the agent needs to be before it can breed = lifespan / 5
    private int creationAge;
    // How much energy the agent loses per child had, and how much energy each if its children will start with = (energyCapacity / 2) / creationSize
    private int creationCost;
    // How many steps need to occur before an agent can breed again = size^0.5
    private int creationDelay;
    // The color generated by the agents stats and seed color
    private Color mutatingColor = Color.white;

    // The random instance used for decisions within the class
    final private Random random = new Random();

    /**
     * Constructs a BasicAttributes object using the input parameters.
     * <p>
     * The constructor simply takes the input parameters and assigns them
     * to the new BasicAttributes instance. Then it calls the calculateAttributes()
     * method to calculate the rest of the attributes. If the attributes mutationMagnitude
     * is greater than zero, it will generate its mutating color from its seed color.
     * @param spawningWeight the weight used for this agent when populating the environment
     * @param name the string that identifies the agent in the diagnostics view
     * @param code the agents unique identifier, an agent will breed with and not eat agents with the same code
     * @param seedColor the agents initial color
     * @param mutationChance the percentage value for how likely the attributes are to mutate
     * @param range how far the agent can see and move in one turn
     * @param size used to calculate all the agents calculated stats
     * @param creationSize how many adjacent squares the agent will try to have children in, also used to calculate the creation cost.
     */
    public BasicAttributes(double spawningWeight, String name, int code, Color seedColor, int mutationChance, int range, int size, int creationSize) {
        this.spawningWeight = spawningWeight;
        this.code = code;
        this.name = name;
        this.mutationChance = mutationChance;
        this.seedColor = seedColor;

        this.range = range;
        this.size = size;
        this.creationSize = creationSize;

        calculateAttributes();

        mutateAttributesColor(
                getSize() / 100.0,
                getCreationSize() / 8.0,
                getRange() / 5.0,
                125);
    }

    /**
     * Constructs a BasicAttributes object using two BasicAttributes objects.
     * <p>
     * The constructor takes two BasicAttributes objects and uses their fields to\
     * construct a new instance. mutationMagnitude, name, code, seedColor and spawningWeight
     * are all taken from the first input. seedColor, mutatingColor, size, creationSize, and range
     * are randomly taken from either input with an equal chance for either to be taken.
     */
    public BasicAttributes(Attributes attributesA, Attributes attributesB) {
        this.mutationChance = attributesA.getMutationChance();
        this.code =  attributesA.getCode();
        this.name = attributesA.getName();
        this.spawningWeight = attributesA.getSpawningWeight();

        this.seedColor = (Color) getChoice(attributesA.getSeedColor(), attributesB.getSeedColor());
        this.mutatingColor = (Color) getChoice(attributesA.getMutatingColor(), attributesB.getMutatingColor());
        this.size = (int) getChoice(attributesA.getSize(), attributesB.getSize());
        this.creationSize = (int) getChoice(attributesA.getCreationSize(), attributesB.getCreationSize());
        this.range = (int) getChoice(attributesA.getRange(), attributesB.getRange());
    }

    /**
     * Recalculates the mutating color.
     * <p>
     * When an agent mutates, its new color is generated by adding the constant multiplied
     * with the percentage difference, of one of its 3 mutating stats to a corresponding
     * chanel of the seed color. The mutating attributes correspond with the following RGB
     * channels: size-red, creationSize-green, range-blue. The result of the addition is
     * wrapped around (0-255)
     * @param a the percent change of the size attribute
     * @param b the percent change of the creationSize attribute
     * @param c the percent change of the range attribute
     */
    @Override
    public void mutateAttributesColor(double a, double b, double c, int constant) {
        int x2 = overflow255(getSeedColor().getRed(), ((int) (a * constant)));
        int y2 = overflow255(getSeedColor().getGreen(), ((int) (b * constant)));
        int z2 = overflow255(getSeedColor().getBlue(), ((int) (c * constant)));
        setMutatingColor(new Color(x2, y2, z2));
    }

    /**
     * Recalculates the seed color.
     * <p>
     * Sets the seedColor to a new color by taking its RGB channels and adding a
     * randomly generated number between 0 and the magnitude parameter. Values are
     * wrapped around (0-255)
     * @param magnitude the upper limit (exclusive) that each chanel will mutate by
     */
    @Override
    public void mutateSeedColor(int magnitude) {
        setMutatingColor(
                new Color(
                        overflow255(seedColor.getRed(), random.nextInt(magnitude)),
                        overflow255(seedColor.getGreen(), random.nextInt(magnitude)),
                        overflow255(seedColor.getBlue(), random.nextInt(magnitude))
                ));
    }

    /**
     * Calculates the calculated attributes.
     * <p>
     * Calculates each calculated attribute using the size and creationSize attributes.
     * This needs to happen after an agent has mutated. This is handled in the
     * Simulation.AgentLogic class which is why we can't have this in the constructor.
     */
    @Override
    public void calculateAttributes() {
        energyCapacity = size*10;
        energyLostPerTile = (int) Math.round(Math.pow(size, 0.70));
        eatAmount = (int) Math.round(Math.pow(size, 0.5)) * 2;
        lifespan = 25 + (int) Math.round(Math.pow(size, 1.1));
        creationAge = lifespan / 5;
        creationCost = (energyCapacity / 2) / creationSize;
        creationDelay = (int) Math.round(Math.pow(size, 0.5));
    }

    /**
     * Returns one of the agents 2 colors.
     * <p>
     * If the agents mutationMagnitude exceeds 0 then mutatingColor is returned,
     * else seedColor is returned.
     */
    @Override
    public Color getColor() {
        if (getMutationChance() > 0) {
            return getMutatingColor();
        }
        else {
            return getSeedColor();
        }
    }

    /**
     * Adds 2 values and wraps them around 0-255.
     * <p>
     * @param a the first number in the equation
     * @param b the second number in the equation
     */
    private static int overflow255(int a, int b) {
        if (a + b > 255) {
            return a + b - 255;
        }
        else if (a + b < 0) {
            return 255 - a + b;
        }
        return a + b;
    }

    /**
     * Randomly returns one of the 2 input parameters.
     * <p>
     * Takes 2 objects ands randomly returns one, with an equal chance for both.
     * @param objectA the first object
     * @param objectB the second object
     */
    private Object getChoice(Object objectA, Object objectB) {
        if (random.nextInt(2) > 0) {
            return objectA;
        }
        return objectB;
    }

    @Override
    public double getSpawningWeight() {
        return spawningWeight;
    }
    @Override
    public void setSpawningWeight(double spawningWeight) {
        this.spawningWeight = spawningWeight;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public int getCode() {
        return code;
    }
    @Override
    public void setCode(int code) {
        this.code = code;
    }
    @Override
    public int getMutationChance() {
        return mutationChance;
    }
    @Override
    public void setMutationChance(int mutationChance) {
        this.mutationChance = mutationChance;
    }
    @Override
    public Color getSeedColor() {
        return seedColor;
    }
    @Override
    public void setSeedColor(Color seedColor) {
        this.seedColor = seedColor;
    }
    @Override
    public int getRange() {
        return range;
    }
    @Override
    public void setRange(int range) {
        this.range = Math.min(Math.max(size, 1), 6);
    }
    @Override
    public int getSize() {
        return size;
    }
    @Override
    public void setSize(int size) {
        this.size = Math.min(Math.max(size, 1), 101);
    }
    @Override
    public int getCreationSize() {
        return creationSize;
    }
    @Override
    public void setCreationSize(int creationSize) {
        this.creationSize = Math.min(Math.max(creationSize, 1), 9);
    }
    @Override
    public int getEnergyCapacity() {
        return energyCapacity;
    }
    @Override
    public void setEnergyCapacity(int energyCapacity) {
        this.energyCapacity = energyCapacity;
    }
    @Override
    public int getEnergyLostPerTile() {
        return energyLostPerTile;
    }
    @Override
    public void setEnergyLostPerTile(int energyLostPerTile) {
        this.energyLostPerTile = energyLostPerTile;
    }
    @Override
    public int getEatAmount() {
        return eatAmount;
    }
    @Override
    public void setEatAmount(int eatAmount) {
        this.eatAmount = eatAmount;
    }
    @Override
    public int getLifespan() {
        return lifespan;
    }
    @Override
    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }
    @Override
    public int getCreationAge() {
        return creationAge;
    }
    @Override
    public void setCreationAge(int creationAge) {
        this.creationAge = creationAge;
    }
    @Override
    public int getCreationCost() {
        return creationCost;
    }
    @Override
    public void setCreationCost(int creationCost) {
        this.creationCost = creationCost;
    }
    @Override
    public int getCreationDelay() {
        return creationDelay;
    }
    @Override
    public void setCreationDelay(int creationDelay) {
        this.creationDelay = creationDelay;
    }
    @Override
    public Color getMutatingColor() {
        return mutatingColor;
    }
    @Override
    public void setMutatingColor(Color mutatingColor) {
        this.mutatingColor = mutatingColor;
    }
    @Override
    public String toString() {
        return "BasicAttributes{" +
                "spawningWeight=" + spawningWeight +
                ", name='" + name + '\'' +
                ", code=" + code +
                ", mutationMagnitude=" + mutationChance +
                ", seedColor=" + seedColor +
                ", range=" + range +
                ", size=" + size +
                ", creationSize=" + creationSize +
                ", energyCapacity=" + energyCapacity +
                ", energyLostPerTurn=" + energyLostPerTile +
                ", eatAmount=" + eatAmount +
                ", lifespan=" + lifespan +
                ", creationAge=" + creationAge +
                ", creationCost=" + creationCost +
                ", creationDelay=" + creationDelay +
                ", mutatingColor=" + mutatingColor +
                ", random=" + random +
                '}';
    }
    @Override
    public Attributes copy() {
        return new BasicAttributes(
                getSpawningWeight(),
                getName(),
                getCode(),
                getSeedColor(),
                getMutationChance(),
                getRange(),
                getSize(),
                getCreationSize());
    }
}