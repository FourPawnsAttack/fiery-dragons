package dragonix.fierydragons1.cards;
import dragonix.fierydragons1.Player;
import javafx.scene.shape.Circle;
import java.util.Map;

/**
 * The Cave class represents a cave in the game, with properties such as its position,
 * the creature occupying it, and the player occupying it.
 */
public class Cave {
    private final Circle caveArc;
    private final Integer attachedPosition;
    private CreatureName caveCreature;
    private transient Player occupiedPlayer;

    /**
     * Returns the cave's occupied status.
     *
     * @return the cave's occupied status
     */
    public boolean getOccupied() {
        return isOccupied;
    }

    /**
     * Sets the cave's occupied status.
     *
     * @param occupied the status to set
     */
    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    private boolean isOccupied;

    /**
     * Constructs a Cave with the specified arc, position, and creature.
     * The cave is marked as occupied upon creation.
     *
     * @param caveArc         the graphical representation of the cave
     * @param attachedPosition the position attached to the cave
     * @param caveCreature     the creature occupying the cave
     */
    public Cave(Circle caveArc, Integer attachedPosition, CreatureName caveCreature){
        this.caveArc = caveArc;
        this.attachedPosition = attachedPosition;
        this.caveCreature = caveCreature;
        this.isOccupied = true;
    }

//    public static Cave fromMap(Map<String, Object> caveData, Map<String, Circle> caveArcs) {
//        String creatureNameStr = (String) caveData.get("caveCreature");
//        CreatureName creatureName = CreatureName.valueOf(creatureNameStr);
//        Double tempAttachedPosition = (Double) caveData.get("attachedPosition");
//        Integer attachedPosition = tempAttachedPosition.intValue();
//        String caveArcId = (String) caveData.get("caveArcId");
//
//        Circle caveArc = caveArcs.get(caveArcId);
//
//        return new Cave(caveArc, attachedPosition, creatureName);
//    }

    /**
     * Returns the player occupying the cave.
     *
     * @return the player occupying the cave
     */
    public Player getOccupiedPlayer() {
        return occupiedPlayer;
    }

    /**
     * Sets the player occupying the cave.
     *
     * @param occupiedPlayer the player to set as occupying the cave
     */
    public void setOccupiedPlayer(Player occupiedPlayer) {
        this.occupiedPlayer = occupiedPlayer;
    }

    /**
     * Returns the position attached to the cave.
     *
     * @return the attached position
     */
    public Integer getAttachedPosition() {
        return attachedPosition;
    }

    /**
     * Returns the creature occupying the cave.
     *
     * @return the creature occupying the cave
     */
    public CreatureName getCaveCreature() {
        return caveCreature;
    }

    /**
     * Sets the creature occupying the cave.
     *
     * @param creatureName the creature to set as occupying the cave
     */
    public void setCaveCreature(CreatureName creatureName) {
        this.caveCreature = creatureName;
    }

    /**
     * Returns the graphical representation of the cave.
     *
     * @return the cave arc
     */
    public Circle getCaveArc() {
        return caveArc;
    }

    /**
     * Returns a Cave object created from the specified map of cave data.
     *
     * @return Cave
     */
    public static Cave fromMap(Map<String, Object> caveData, Map<String, Circle> caveArcs) {
        String creatureNameStr = (String) caveData.get("caveCreature");
        CreatureName creatureName = CreatureName.valueOf(creatureNameStr);
        double tempAttachedPosition = (Double) caveData.get("attachedPosition"); // Use double instead of Double
        int attachedPosition = (int) tempAttachedPosition; // Cast to int directly
        String caveArcId = "Cave" + creatureName.getName().replaceAll("\\s+","").toUpperCase(); // Set ID with "Cave" prefix

        Circle caveArc = caveArcs.get(caveArcId);

        if (caveArc == null) {
            // Create a new Circle with the appropriate ID
            caveArc = new Circle();
            caveArc.setId(caveArcId);
            // Set the position based on the caveArcId
            switch (caveArcId) {
                case "CaveSALAMANDER":
                    caveArc.setLayoutX(89.0);
                    caveArc.setLayoutY(444.0);
                    break;
                case "CaveBABYDRAGON":
                    caveArc.setLayoutX(448.0);
                    caveArc.setLayoutY(781.0);
                    break;
                case "CaveSPIDER":
                    caveArc.setLayoutX(467.0);
                    caveArc.setLayoutY(74.0);
                    break;
                case "CaveBAT":
                    caveArc.setLayoutX(799.0);
                    caveArc.setLayoutY(442.0);
                    break;
                // Add cases for other caveArcIds if needed
                default:
                    // Handle the case when caveArcId doesn't match any predefined positions
                    break;
            }
        }

        return new Cave(caveArc, attachedPosition, creatureName);
    }

}
