package dragonix.fierydragons1.cards;

import java.util.*;

/**
 * The CardFactory class is responsible for creating and managing DragonCards.
 * It ensures that cards are created according to predefined rules and provides methods
 * for generating random DragonCards.
 */
public class CardFactory {
    private static final int MAX_OCCURRENCES = 3;
    private final Map<CreatureName, Integer> cardCounters = new HashMap<>();
    private final List<DragonCards> allCards = new ArrayList<>();

    /**
     * Constructs a CardFactory, initializing the card counters for each creature type
     * and preparing the list of all possible cards.
     */
    public CardFactory() {
        // Initialize the card counters for each creature type
        for (CreatureName creature : CreatureName.values()) {
            cardCounters.put(creature, 0);
        }
        // Prepare the list of all possible cards in advance
        initializeAllCards();
    }

    /**
     * Initializes the list of all possible DragonCards based on the creature types
     * and the maximum occurrences allowed for each type. Special rules are applied
     * for specific creature types.
     */
    private void initializeAllCards() {
        for (CreatureName creature : CreatureName.values()) {
            for (int quantity = 1; quantity <= MAX_OCCURRENCES; quantity++) {
                if(creature == CreatureName.DRAGON_PIRATE){
                    allCards.add(new DragonCards(creature, -quantity));
                }
                else if(creature == CreatureName.MOVE_BACK && quantity == 1){
                    allCards.add(new DragonCards(creature, 1));
                }
                else if(creature != CreatureName.MOVE_BACK){
                    allCards.add(new DragonCards(creature, quantity));
                }
            }
        }
        Collections.shuffle(allCards); // Shuffle the cards to provide random assignment
    }

    /**
     * Creates and returns a random DragonCard from the list of all possible cards.
     * If all cards have been created, an IllegalStateException is thrown.
     *
     * @return a random DragonCard
     * @throws IllegalStateException if all DragonCards have been created
     */
    public DragonCards createRandomDragonCard() {
        if (allCards.isEmpty()) {
            throw new IllegalStateException("All DragonCards have been created");
        }
        // Get and remove the first card from the shuffled list
        return allCards.removeFirst();
    }

    /**
     * Returns the number of different types of creatures.
     *
     * @return the number of different creature types
     */
    public int getCardTypes() {
        return CreatureName.values().length;
    }
}
