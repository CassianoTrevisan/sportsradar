package com.sportsradar.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Score {
    @Setter
    private Integer awayTeamScore;
    @Setter
    private Integer homeTeamScore;
    private Match match;

    public Score(Match match){
        this.awayTeamScore = 0;
        this.homeTeamScore = 0;
        this.match = match;
    }

    public Score(Match match, Integer homeTeamScore, Integer awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
        this.homeTeamScore = homeTeamScore;
        this.match = match;
    }
}
