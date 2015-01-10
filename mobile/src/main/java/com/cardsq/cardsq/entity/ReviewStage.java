package com.cardsq.cardsq.entity;

import java.util.Date;
import java.util.UUID;

/**
 * Created by lzhu on 1/10/15.
 */

public class ReviewStage{


    private UUID id;
    private String description;
    private int level;
    private Date durationBeforeNextStage;





    public ReviewStage(String description, int level, Date durationBeforeNextStage){
        this.id = UUID.randomUUID();
        this.description = description;
        this.level = level;
        this.durationBeforeNextStage = durationBeforeNextStage;
    }

    public int getLevel(){
        return level;
    }

    public Date getDurationBeforeNextStage(){
        return durationBeforeNextStage;
    }
}
