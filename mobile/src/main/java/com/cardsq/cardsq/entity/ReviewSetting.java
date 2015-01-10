package com.cardsq.cardsq.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Review Setting is used to manage how users review the cards
 */
public class ReviewSetting {

    private static List<ReviewStage> reviewStages = new ArrayList<ReviewStage>();

    private static final ReviewStage LEVEL1 = new ReviewStage("Level 1", 1, new Date(2000));
    private static final ReviewStage LEVEL2 = new ReviewStage("Level 2", 2, new Date(4000));
    private static final ReviewStage LEVEL3 = new ReviewStage("Level 3", 3, new Date(6000));



    public ReviewSetting(){
        setupAsDefault();
    }


    public  void setupAsDefault(){
        reviewStages.clear();
        reviewStages.add(LEVEL1);
        reviewStages.add(LEVEL2);
        reviewStages.add(LEVEL3);
    }

    public List<ReviewStage> getReviewSetting(){
        return this.reviewStages;
    }

    public ReviewStage getReviewLevel(int level){
        if(level < 0 || level > this.reviewStages.size()){
            System.out.println("The input level doesn't exist");
        }
        return this.reviewStages.get(level-1);
    }

    public static List<ReviewStage> getReviewStages(){
        return reviewStages;
    }
}
