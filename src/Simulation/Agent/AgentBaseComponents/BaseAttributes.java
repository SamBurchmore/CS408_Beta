package Simulation.Agent.AgentBaseComponents;

import Simulation.Agent.AgentInterfaces.Attributes;
import Simulation.Agent.AgentStructs.ColorModel;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

public abstract class BaseAttributes implements Attributes, Serializable {

    // The weight used by the Simulation class when it populates the environment
    private int spawningWeight;
    // The string that identifies the agent in the diagnostics view
    private String name;
    // The agents unique identifier, an agent will breed with and not eat agents with the same code
    private Integer ID;
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
    // How long the agent can exist for = 15 * size^0.75
    private int lifespan;
    // How old the agent needs to be before it can breed = lifespan / 5
    private int creationAge;
    // How much energy the agent loses per child had, and how much energy each if its children will start with = (energyCapacity / 2) / creationSize
    private int creationCost;
    // How many steps need to occur before an agent can breed again = size^0.5
    private int creationDelay;
    // The color generated by the agents stats and seed color
    private Color mutatingColor = Color.white;

    // An integer value representing this agents color model
    private ColorModel colorModel;
    // An integer value which defines the boundaries for how much this agents color will randomly change by.
    private int randomColorModelMagnitude = 5;

    // The random instance used for decisions within the class
    final private Random random = new Random();

    /**
     * Constructs a BaseAttributes object using the input parameters.
     * <p>
     * The constructor simply takes the input parameters and assigns them
     * to the new BaseAttributes instance. Then it calls the calculateAttributes()
     * method to calculate the rest of the attributes.
     * @param spawningWeight the weight used for this agent when populating the environment
     * @param name the string that identifies the agent in the diagnostics view
     * @param ID the agents unique identifier, an agent will breed with and not eat agents with the same code
     * @param seedColor the agents initial color
     * @param mutationChance the percentage value for how likely the attributes are to mutate
     * @param range how far the agent can see and move in one turn
     * @param size used to calculate all the agents calculated stats
     * @param creationSize how many adjacent squares the agent will try to have children in, also used to calculate the creation cost.
     */
    public BaseAttributes(int spawningWeight, String name, int ID, Color seedColor, ColorModel colorModel, int randomColorModelMagnitude, int mutationChance, int range, int size, int creationSize) {
        setSpawningWeight(spawningWeight);
        setID(ID);
        setName(name);
        setMutationChance(mutationChance);
        setSeedColor(seedColor);
        setColorModel(colorModel);
        setRandomColorModelMagnitude(randomColorModelMagnitude);
        setRange(range);
        setSize(size);
        setCreationSize(creationSize);
        calculateAttributes();
    }

    /**
     * Constructs a BaseAttributes object using two Attributes objects.
     * <p>
     * The constructor takes two Attributes objects and passes them to the
     * generateAttributes() method.
     */
    public BaseAttributes(Attributes attributesA, Attributes attributesB) {
        this.generateAttributes(attributesA, attributesB);
    }

    /**
     * Initialises this instance using the two BaseAttributes objects.
     * <p>
     * mutationMagnitude, name, code, seedColor, spawningWeight, colorModel, and randomColorModelMagnitude
     * are all taken from the first input. seedColor, mutatingColor, size, creationSize, and range are
     * randomly taken from either input with an equal chance for either to be taken.
     * @param attributesA the first set of Attributes
     * @param attributesB the second set of Attributes
     */
    @Override
    public void generateAttributes(Attributes attributesA, Attributes attributesB) {
        this.mutationChance = attributesA.getMutationChance();
        this.ID =  attributesA.getID();
        this.name = attributesA.getName();
        this.spawningWeight = attributesA.getSpawningWeight();
        this.colorModel = attributesA.getColorModel();
        this.randomColorModelMagnitude = attributesA.getRandomColorModelMagnitude();

        this.seedColor = (Color) getChoice(attributesA.getSeedColor(), attributesB.getSeedColor());
        this.mutatingColor = (Color) getChoice(attributesA.getMutatingColor(), attributesB.getMutatingColor());
        this.size = (int) getChoice(attributesA.getSize(), attributesB.getSize());
        this.creationSize = (int) getChoice(attributesA.getCreationSize(), attributesB.getCreationSize());
        this.range = (int) getChoice(attributesA.getRange(), attributesB.getRange());
        if (getColorModel().equals(ColorModel.RANDOM)) {
            mutateSeedColor();
        }
    }

    /**
     * Recalculates the seed color.
     * <p>
     * Sets the seedColor to a new color by taking its RGB channels and adding a
     * randomly generated number between 0 and the magnitude parameter. Values are
     * wrapped around (0-255)
     */
    @Override
    public void mutateSeedColor() {
        setSeedColor(
                new Color(
                        overflow255(seedColor.getRed(), random.nextInt(randomColorModelMagnitude)),
                        overflow255(seedColor.getGreen(), random.nextInt(randomColorModelMagnitude)),
                        overflow255(seedColor.getBlue(), random.nextInt(randomColorModelMagnitude))
                ));
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
     * Returns the agents display color.
     * <p>
     * This is the default implementation and just returns the seed color.
     */
    @Override
    public Color getColor() {
        return getSeedColor();
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
        energyCapacity = size*5;
        energyLostPerTile = (int) Math.round(Math.pow(size, 0.65) - 1);
        eatAmount = (int) Math.round(Math.pow(size, 0.5)) * 2;
        lifespan = 15 * (int) Math.round(Math.pow(size, 0.75));
        creationAge = lifespan / 5;
        creationCost = (energyCapacity / 2) / creationSize;
        creationDelay = (int) Math.round(Math.pow(size, 0.5));
    }

    /**
     * Adds 2 values and wraps them around 0-255.
     * <p>
     * @param a the first number in the equation
     * @param b the second number in the equation
     */
    protected static int overflow255(int a, int b) {
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
    protected Object getChoice(Object objectA, Object objectB) {
        if (random.nextInt(2) > 0) {
            return objectA;
        }
        return objectB;
    }

    @Override
    public String toString() {
        return  "-spawningWeight=" + spawningWeight + "%" +
                ",\n-ID=" + ID +
                ",\n-name=" + name +
                ",\n-mutationChance=" + mutationChance + "%" +
                ",\n-seedColor=" + seedColor.toString() +
                ",\n-range=" + range +
                ",\n-size=" + size +
                ",\n-creationSize=" + creationSize +
                ",\n-energyCapacity=" + energyCapacity +
                ",\n-energyLostPerTile=" + energyLostPerTile +
                ",\n-eatAmount=" + eatAmount +
                ",\n-lifespan=" + lifespan +
                ",\n-creationAge=" + creationAge +
                ",\n-creationCost=" + creationCost +
                ",\n-creationDelay=" + creationDelay +
                ",\n-mutatingColor=" + mutatingColor +
                ",\n-colorModel=" + colorModel +
                ",\n-randomColorModelMagnitude=" + randomColorModelMagnitude +
                ".";
    }
    @Override
    public int getSpawningWeight() {
        return spawningWeight;
    }
    @Override
    public void setSpawningWeight(int spawningWeight) {
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
    public Integer getID() {
        return ID;
    }
    @Override
    public void setID(int code) {
        this.ID = code;
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
        this.range = Math.min(Math.max(range, 1), 6);
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
    public ColorModel getColorModel() {
        return colorModel;
    }
    @Override
    public void setColorModel(ColorModel colorModel) {
        this.colorModel = colorModel;
    }
    @Override
    public int getRandomColorModelMagnitude() {
        return randomColorModelMagnitude;
    }
    @Override
    public void setRandomColorModelMagnitude(int randomColorModelMagnitude) {
        this.randomColorModelMagnitude = randomColorModelMagnitude;
    }
    @Override
    public Random getRandom() {
        return random;
    }
}
