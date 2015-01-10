package com.cardsq.cardsq.entity;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lzhu on 1/10/15.
 */
public class ReviewAlgorithm {

    public List<Card> updateCurrentQBasedOnUserReviewFeedback(List<Card> currentCardList){
        List<Card> cardListAfterReview  = new ArrayList<Card>();
        for(Card card: cardListAfterReview){
            if(card.isKnown()){
                ReviewStage nextReviewStage = card.setToNextReviewStage();
                card.setNextReviewTime(new Date(Long.getLong(new Date().toString())
                        + Long.getLong(nextReviewStage.getDurationBeforeNextStage().toString())));
            }else{
                card.setNextReviewTime(new Date(Long.getLong(new Date().toString())
                        + Long.getLong(card.getReviewStage().getDurationBeforeNextStage().toString())));
            }
        }

        return cardListAfterReview;
    }

    private void insertToQ(Card card, List<Card> cardList){
        if(cardList.size()==0){
            cardList.add(card);
        }else{
            boolean inserted =false;
            for(int i=0; i<cardList.size(); i++){
                //insert before the first Card that has later review time
                if(card.getNextReviewTime().compareTo(cardList.get(i).getNextReviewTime())==-1){
                    cardList.add(i-1, card);
                    inserted = true;
                    break;
                }
            }
            if(!inserted){
                cardList.add(card);
            }
        }
    }


}
