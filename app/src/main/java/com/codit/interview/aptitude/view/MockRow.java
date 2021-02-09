package com.codit.interview.aptitude.view;

public class MockRow
{

    String mockTitle;
    int score;
    boolean isFinished;

    public boolean isAd() {
        return isAd;
    }

    public void setAd(boolean ad) {
        isAd = ad;
    }

    boolean isAd;

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    boolean isLocked;

    public boolean isLocked() {
        return isLocked;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public MockRow(int score, String mockTitle, boolean isFinished, boolean isLocked) {
        this.score = score;
        this.mockTitle = mockTitle;
        this.isFinished=isFinished;
        this.isLocked=isLocked;
    }

    public MockRow()
    {

    }

    public String getMockTitle() {
        return mockTitle;
    }

    public void setMockTitle(String mockTitle) {
        this.mockTitle = mockTitle;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
