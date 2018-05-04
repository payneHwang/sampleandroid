package com.pep.dubsdk.model;

public class Sentence {
    public int sentenceId;
    public String sentenceEN;
    public String sentenceCN;
    public int fromSeconds;
    public int toSeconds;

    @Override
    public String toString() {
        return "Sentence{" +
                "sentenceId=" + sentenceId +
                ", sentenceEN='" + sentenceEN + '\'' +
                ", sentenceCN='" + sentenceCN + '\'' +
                ", fromSeconds=" + fromSeconds +
                ", toSeconds=" + toSeconds +
                '}';
    }
}
