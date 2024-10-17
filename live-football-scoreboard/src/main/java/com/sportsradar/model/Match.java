package com.sportsradar.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Match {
    @EqualsAndHashCode.Include
    private String homeTeam;
    private Integer homeTeamScore;
    @EqualsAndHashCode.Include
    private String awayTeam;
    private Integer awayTeamScore;
    private Instant matchStart;
    private Boolean isFinished;

    @Override
    public String toString() {
        return
                homeTeam + " " + homeTeamScore + " - " + awayTeam + " " + awayTeamScore;
    }
}
