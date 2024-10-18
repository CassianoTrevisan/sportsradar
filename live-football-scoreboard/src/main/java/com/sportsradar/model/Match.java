package com.sportsradar.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Match {
    @EqualsAndHashCode.Include
    private final String homeTeam;
    @Setter
    private Integer homeTeamScore;
    @EqualsAndHashCode.Include
    private final String awayTeam;
    @Setter
    private Integer awayTeamScore;
    private final Instant matchStart;
    private Boolean isFinished;

    public Match(String homeTeam, String awayTeam, Instant matchStart) {
        this.homeTeam = homeTeam;
        this.homeTeamScore = 0;
        this.awayTeam = awayTeam;
        this.awayTeamScore = 0;
        this.matchStart = matchStart;
        this.isFinished = false;
    }


    @Override
    public String toString() {
        return
                homeTeam + " " + homeTeamScore + " - " + awayTeam + " " + awayTeamScore;
    }
}
