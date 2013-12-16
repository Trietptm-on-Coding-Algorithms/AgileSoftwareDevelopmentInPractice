package de.kimminich.agile.samplesolutions.excercise6;

import de.kimminich.agile.excercises.excercise6.Hand;
import de.kimminich.agile.excercises.excercise6.HandCategory;

import java.util.*;

public class HandSampleImpl extends Hand {

    CardOccurrenceTuples cardOccurrences;

    public HandSampleImpl(int... cards) {
        this.cards = cards;
        checkNumberOfCards();

        cardOccurrences = new CardOccurrenceTuples(cards);
        checkImpossibleNumberOfSameCardValue();
        checkInvalidCardValues();
    }

    private void checkNumberOfCards() {
        if (cards.length > 5) {
            throw new IllegalArgumentException("Too many cards: " + cards.length);
        }
        if (cards.length < 5) {
            throw new IllegalArgumentException("Not enough cards: " + cards.length);
        }
    }

    private void checkImpossibleNumberOfSameCardValue() {
        if (cardOccurrences.cardOccurrences().contains(5)) {
            throw new IllegalArgumentException("Impossible number of same card value.");
        }
    }

    private void checkInvalidCardValues() {
        for (Integer card : cardOccurrences.cardTypes()) {
            if (card < 2 || card > ACE) {
                throw new IllegalArgumentException("Illegal card value: " + card);
            }
        }
    }

    @Override
    public HandCategory getHandCategory() {
        if (isFullHouse()) {
            return HandCategory.FullHouse;
        }
        if (isStraight()) {
            return HandCategory.Straight;
        }
        if (isFourOfAKind()) {
            return HandCategory.FourOfAKind;
        }
        if (isThreeOfAKind()) {
            return HandCategory.ThreeOfAKind;
        }
        if (isTwoPair()) {
            return HandCategory.TwoPair;
        }
        if (isOnePair()) {
            return HandCategory.OnePair;
        }
        return HandCategory.HighCard;
    }

    private boolean isFullHouse() {
        return isThreeOfAKind() && isOnePair();
    }

    private boolean isThreeOfAKind() {
        return hasCardNumberOfTimesInHand(3);
    }

    private boolean isOnePair() {
        return hasCardNumberOfTimesInHand(2);
    }

    private boolean isFourOfAKind() {
        return hasCardNumberOfTimesInHand(4);
    }

    private boolean hasCardNumberOfTimesInHand(int occurrences) {
        for (Integer occurrence : cardOccurrences.cardOccurrences()) {
            if (occurrence == occurrences) {
                return true;
            }
        }
        return false;
    }

    private boolean isTwoPair() {
        Boolean onePair = null;
        for (Integer occurrence : cardOccurrences.cardOccurrences()) {
            if (onePair == null) {
                if (occurrence == 2) {
                    onePair = true;
                    continue;
                }
            }
            if (occurrence == 2) {
                return true;
            }
        }
        return false;
    }

    private boolean isStraight() {
        Arrays.sort(cards);
        for (int i = 1; i < cards.length; i++) {
            if (!isCardSuccessorOfPreviousCard(i)) {
                return isStraightStartingWithAce();
            }
        }
        return true;
    }

    private boolean isCardSuccessorOfPreviousCard(int i) {
        return cards[i] - cards[i - 1] == 1;
    }


    private boolean isStraightStartingWithAce() {
        return Arrays.equals(cards, new int[]{2, 3, 4, 5, ACE});
    }

    private static class CardOccurrenceTuples {

        private Map<Integer, Integer> tuples = new HashMap<>();

        private CardOccurrenceTuples(int[] cards) {
            for (int card : cards) {
                addCard(card);
            }
        }

        private void addCard(int card) {
            if (tuples.get(card) == null) {
                tuples.put(card, 1);
            } else {
                tuples.put(card, tuples.get(card) + 1);
            }
        }

        private Set<Integer> cardTypes() {
            return tuples.keySet();
        }

        private Collection<Integer> cardOccurrences() {
            return tuples.values();
        }

    }
}
