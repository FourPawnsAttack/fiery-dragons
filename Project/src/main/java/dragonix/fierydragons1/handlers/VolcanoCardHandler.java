package dragonix.fierydragons1.handlers;

import dragonix.fierydragons1.cards.CreatureName;
import javafx.scene.shape.Rectangle;
import java.util.*;

/**
 * The VolcanoCardHandler class is responsible for assigning creatures to volcano cards.
 * It initializes sequences of creatures and assigns them to volcano cards.
 */
public class VolcanoCardHandler {
    private List<List<CreatureName>> sequences;
    private HashMap<Integer, CreatureName> volcanoCardsHashMap = new HashMap<>();

    /**
     * Constructor for VolcanoCardHandler. Calls the method to initialize sequences of creatures.
     */
    public VolcanoCardHandler() {
        initializeSequences();
    }

    /**
     * Initializes the sequences of creatures using CreatureName enums.
     */
    private void initializeSequences() {
        sequences = new ArrayList<>();
        CreatureName[][] volcanoSequence = new CreatureName[][] {
                {CreatureName.BABY_DRAGON, CreatureName.BAT, CreatureName.SPIDER},
                {CreatureName.SALAMANDER, CreatureName.SPIDER, CreatureName.BAT},
                {CreatureName.SPIDER, CreatureName.SALAMANDER, CreatureName.BABY_DRAGON},
                {CreatureName.BAT, CreatureName.SPIDER, CreatureName.BABY_DRAGON},
                {CreatureName.SPIDER, CreatureName.BAT, CreatureName.SALAMANDER},
                {CreatureName.BABY_DRAGON, CreatureName.SALAMANDER, CreatureName.BAT},
                {CreatureName.BAT, CreatureName.BABY_DRAGON, CreatureName.SALAMANDER},
                {CreatureName.SALAMANDER, CreatureName.BABY_DRAGON, CreatureName.SPIDER}
        };
        for (CreatureName[] sequence : volcanoSequence) {
            sequences.add(Arrays.asList(sequence));
        }
    }

    /**
     * Assigns creatures to volcano cards and fills the hashmap with their assigned indices.
     *
     * @param volcanoRectangles A list of rectangles representing volcano cards.
     */
    public void assignCreaturesToCards(List<Rectangle> volcanoRectangles) {
        Collections.shuffle(sequences);
        List<CreatureName> flattenedList = sequences.stream()
                .flatMap(List::stream)
                .toList();
        for (int i = 0; i < volcanoRectangles.size(); i++) {
            CreatureName creature = flattenedList.get(i);
            Rectangle currentRectangle = volcanoRectangles.get(i);
            // Set the fill color of the rectangle
            currentRectangle.setFill(creature.getColor());

            volcanoCardsHashMap.put(i, creature); // Fill the hashmap
        }
    }

    /**
     * Retrieves the hashmap containing volcano card mappings.
     *
     * @return A hashmap with index-creature pairs.
     */
    public HashMap<Integer, CreatureName> getVolcanoCardsHashMap() {
        return volcanoCardsHashMap;
    }
}
