package com.cardsq.cardsq.entity;

import java.util.Date;
import java.util.UUID;

/**
 *  A card is an atomic unit. It maintains the knowledge a user wants to memorize and the learning status.
 *
 * Created by lzhu on 1/10/15.
 */
public class Card {

    private UUID id;
    private String title;
    private String explanation;
    private Date createdAt;
    private Date updatedAt;
    private UUID creatorId;
    private UUID currentOwnerId;
    private int reviewCounter;
    private Date lastReviewTime;
    private Date nextReviewTime;
    private boolean known;
    private int knownCounter;
    private ReviewStage reviewStage;

    public Card(){};

    public Card(String title, String explanation){
        this.id = UUID.randomUUID();
        this.title = title;
        this.explanation = explanation;
        this.createdAt = new Date();
        this.updatedAt = new Date();
//        this.reviewStage = this.creator.getReviewSetting().getReviewLevel(1);
    }

    //The morphia automatically fills int and boolean fields
    public Card(String title, String explanation, UUID creatorId, Date createdAt, UUID currentOwnerId){
        this.id = UUID.randomUUID();
        this.title = title;
        this.explanation = explanation;
        this.createdAt = createdAt;
        this.updatedAt = new Date();
        this.creatorId = creatorId;
        this.currentOwnerId = currentOwnerId;
    }





    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getCreator() {
        return creatorId;
    }

    public void setCreator(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public int getReviewCounter() {
        return reviewCounter;
    }

    public void setReviewCounter(int reviewCounter) {
        this.reviewCounter = reviewCounter;
    }

    public Date getLastReviewTime() {
        return lastReviewTime;
    }

    public void setLastReviewTime(Date lastReviewTime) {
        this.lastReviewTime = lastReviewTime;
    }

    public Date getNextReviewTime() {
        return nextReviewTime;
    }

    public void setNextReviewTime(Date nextReviewTime) {
        this.nextReviewTime = nextReviewTime;
    }

    public boolean isKnown() {
        return known;
    }

    public void setKnown(boolean known) {
        this.known = known;
    }

    public int getKnownCounter() {
        return knownCounter;
    }

    public void setKnownCounter(int knownCounter) {
        this.knownCounter = knownCounter;
    }

    public ReviewStage getReviewStage() {
        return reviewStage;
    }

    public void setReviewStage(ReviewStage reviewStage) {
        this.reviewStage = reviewStage;
    }


    public ReviewStage setToNextReviewStage(){
        int currentReviewLevel = reviewStage.getLevel();
        ReviewStage nextLevel = ReviewSetting.getReviewStages().get(currentReviewLevel); //list index starts from 0
        setReviewStage(nextLevel);
        return nextLevel;

    }
}
