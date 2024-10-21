package com.sportsradar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Match {
    @EqualsAndHashCode.Include
    private final String homeTeam;
    @EqualsAndHashCode.Include
    private final String awayTeam;
    private final Instant matchStart;
    @Setter
    private Boolean isFinished;

    public Match(String homeTeam, String awayTeam, Instant matchStart) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchStart = matchStart;
        this.isFinished = false;
    }

}
