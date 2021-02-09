package com.codit.interview.aptitude.model;

/**
 * Created by Sreejith on 05-Jul-17.
 */

public class BugReport {

    public String topic,correctAnswer,correctExp;
    public int qno;

    public BugReport(String topic, int qno,String correctAnswer, String correctExp  ) {
        this.topic = topic;
        this.correctAnswer = correctAnswer;
        this.correctExp = correctExp;
        this.qno = qno;
    }
}
