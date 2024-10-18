package com.sportsradar.datastore;

import com.sportsradar.model.Match;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public final class DataStore {
    private static final Map<Match, Boolean> matches;

    static {
        matches = new HashMap<>();
    }

    public static void addMatch(Match match) {
        matches.put(match, false);
    }

    public static boolean isMatchInProgress(String homeTeam, String awayTeam) {
        Match m = new Match(homeTeam, awayTeam, Instant.now());
        return matches.containsKey(m) && !matches.get(m);
    }

    public static List<Match> getActiveMatchesOrderedBySumOfGoalsAndStartTime() {
        List<Match> matchList = new ArrayList<>(matches.keySet());
        return matchList.stream().filter(match -> !match.getIsFinished())
                .sorted(Comparator.comparing(Match::getMatchStart, Comparator.reverseOrder())
                        .thenComparing(m -> m.getHomeTeamScore() + m.getAwayTeamScore()))
                .collect(Collectors.toList());
    }

}
