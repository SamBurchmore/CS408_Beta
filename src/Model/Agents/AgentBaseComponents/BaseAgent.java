package Model.Agents.AgentBaseComponents;

import Model.Agents.AgentConcreteComponents.*;
import Model.Agents.AgentInterfaces.*;
import Model.Agents.AgentStructs.AgentType;
import Model.Environment.Location;
import Model.Environment.Environment;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
public abstract class BaseAgent implements Agent {

    private Location location;
    private Color agentColor;
    private Reaction reaction = null;
    private Vision vision = null;
    private Attributes attributes;
    private Scores scores = null;
    private UUID agentID;

    public BaseAgent(Location location_, Color agentColor_, Reaction reaction_, Vision vision_, Attributes attributes_, Scores scores_) {
        this.location = location_;
        this.agentColor = agentColor_;
        this.reaction = reaction_;
        this.vision = vision_;
        this.attributes = attributes_;
        this.scores = scores_;
        this.agentID = UUID.randomUUID();
    }

    public BaseAgent(Location location_, Agent parent_a, Agent parent_b) {
        this.location = location_;
        this.agentColor = parent_a.getColor();
        if (parent_a.getAttributes().getType().equals(AgentType.PREY)) {
            this.reaction = new PreyReaction(new PredatorMotivations());
            //System.out.println("new predator");
        }
        else {
            this.reaction = new PreyReaction(new PreyMotivations());
            //System.out.println("new prey");
        }
        this.vision = new BasicVision();
        this.attributes = new BasicAttributes(parent_a.getAttributes(), parent_b.getAttributes());
        this.scores = new BasicScores(parent_a.getScores().getMAX_HUNGER(), parent_a.getScores().getMAX_HEALTH(), 0, parent_a.getScores().getMAX_HUNGER(), parent_a.getScores().getMAX_HEALTH(), parent_a.getScores().getMAX_AGE(), parent_a.getScores().getCreationDelay());
        //System.out.println(this.getAttributes().getVision());
        this.agentID = UUID.randomUUID();
    }

    @Override
    public Environment run(Environment environment_) {
        return environment_;
    }

    @Override
    public Environment move(Location newLocation, Environment environment_) {
        Location oldLocation = this.location;
        this.setLocation(newLocation);
        environment_.setTileAgent(newLocation, this);
        environment_.setTileAgent(oldLocation, null);
        return environment_;
    }

    @Override
    public Environment create(Location parentBLocation, Environment environment_) {
        ArrayList<Location> childLocations = environment_.emptyAdjacent(this.location);
        if (childLocations.size() > 0) {
            Collections.shuffle(childLocations);
            Location childLocation = childLocations.get(0);
            Agent child = this.combine(environment_.getTile(parentBLocation).getOccupant(), childLocation);
            environment_.setTileAgent(childLocation, child);
//            if (child.getAttributes().getType().equals(AgentType.PREDATOR)) {
//                System.out.println(child.getType() + " | Speed: " + child.getAttributes().getSpeed() + ", Size: " + child.getAttributes().getSize() + ", Vision: " + child.getAttributes().getVision());
//            }
        }
        return environment_;
    }

    @Override
    public void liveDay() {
        this.getScores().setHunger((this.getScores().getHunger() - this.getAttributes().getSize() / 3));
        this.getScores().setAge(this.getScores().getAge()+1);
        this.getScores().setCreationCounter((this.getScores().getCreationCounter()-1));
        if (this.getScores().getHunger() >= this.getScores().getMAX_HUNGER() / 2) {
            this.getScores().setHealth(this.getScores().getHealth() + this.getScores().getMAX_HEALTH() / 2);
        }
        if (this.getScores().getHunger() <= this.getScores().getMAX_HUNGER() / 2) {
            this.getScores().setHealth(this.getScores().getHealth() - this.getScores().getMAX_HEALTH() / 5);
        }
    }

    @Override
    public boolean isDead() {
        return this.getScores().getHealth() <= 0 || this.getScores().getAge() >= this.getScores().getMAX_AGE();
    }

    @Override
    public Color getColor() {
        return this.agentColor;
    }

    @Override
    public void setColor(Color color_) {
        this.agentColor = color_;
    }

    @Override
    public Attributes getAttributes() {
        return this.attributes;
    }

    @Override
    public void setAttributes(Attributes attributes_) {
        this.attributes = attributes_;
    }

    @Override
    public void setLocation(Location location_) {
        this.location = location_;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public Reaction getReaction() {
        return this.reaction;
    }

    @Override
    public void setReaction(Reaction reaction_) {
        this.reaction = reaction_;
    }

    @Override
    public Vision getVision() {
        return this.vision;
    }

    @Override
    public void setVision(Vision vision_) {
        this.vision = vision_;
    }

    @Override
    public Scores getScores() {
        return this.scores;
    }

    @Override
    public void setScores(Scores scores_) {
        this.scores = scores_;
    }

    @Override
    public UUID getID() {
        return this.agentID;
    }

}