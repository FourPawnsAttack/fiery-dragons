package dragonix.fierydragons1.cards;

import javafx.scene.paint.Color;

/**
 * Defines the enum for Creature Names with associated colors.
 * This enum represents different creatures within the game, each with a unique name and color.
 */
public enum CreatureName {

    // The different creatures in the game, each with a unique name and color.
    DRAGON_PIRATE("Dragon Pirate", null),
    SPIDER("Spider", Color.THISTLE),
    BABY_DRAGON("Baby Dragon", Color.LIGHTPINK),
    SALAMANDER("Salamander", Color.LIGHTCYAN),
    BAT("Bat", Color.LIGHTGREEN),
    MOVE_BACK("Move Back", null);

    private final String name;
    private final Color color;
//    private final Image image;

    /**
     * Constructor for the CreatureName enum.
     *
     * @param name  The display name of the creature.
     * @param color The color associated with the creature, used for UI representation.
     */
    CreatureName(String name, Color color) {
        this.name = name;
        this.color = color;
//        this.image = image;
    }

    /**
     * Retrieves the name of the creature.
     *
     * @return The name of the creature.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the color associated with the creature.
     *
     * @return The color of the creature, if defined; otherwise, null.
     */
    public Color getColor() {
        return color;
    }

//    public Image getImage() {
//        return image;
//    }
}

