package dragonix.fierydragons1.cards;

/**
 * The DragonCards class represents a card in the game, with properties such as the creature type
 * and the quantity associated with the card.
 */
public class DragonCards {
    private CreatureName creatureName;
    private int quantity;

    /**
     * Constructs a DragonCard with the specified creature type and quantity.
     *
     * @param creatureName the type of creature on the card
     * @param quantity the quantity associated with the card
     */
    public DragonCards(CreatureName creatureName, int quantity) {
        this.creatureName = creatureName;
        this.quantity = quantity;
    }

//    public DragonCards(CreatureName creatureName) {
//        this.creatureName = creatureName;
//    }

    /**
     * Returns the creature type on the card.
     *
     * @return the creature type on the card
     */
    public CreatureName getCreatureName() {
        return creatureName;
    }

    /**
     * Returns the quantity associated with the card.
     *
     * @return the quantity associated with the card
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns a string representation of the DragonCard, including the creature type and quantity.
     *
     * @return a string representation of the DragonCard
     */
    @Override
    public String toString() {
        return "DragonCards{" +
                "creatureName=" + creatureName +
                ", quantity=" + quantity +
                '}';
    }
}
