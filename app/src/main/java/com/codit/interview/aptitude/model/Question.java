package com.codit.interview.aptitude.model;

/**
 * Created by Sreejith on 28-Feb-16.
 */
public class Question
{
    public int getQno() {
        return qno;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption1() {
        return option1;
    }

    public String getQue() {
        return que;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public String getAnswer() {
        return answer;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getAttempted() {
        return attempted;
    }

    public void setQno(int qno) {
        this.qno = qno;
    }

    public void setQue(String que) {
        this.que = que;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setAttempted(String attempted) {
        this.attempted = attempted;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }



    int qno;
    String que;
    public String option1;
    public String option2;
    public String option3;
    public String option4;
    public String answer;
    String explanation;
    String attempted;
    String notes;

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    String fav;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String time;

}
