package com.sportsradar.datastore;

import com.sportsradar.exception.FailedToFinishMatchException;
import com.sportsradar.model.Match;
import com.sportsradar.model.Score;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class DataStore {
    private static final Map<Match, Score> matches;

    static {
        matches = new HashMap<>();
    }

    public static void addMatch(Match match) {
        matches.put(match, new Score(match));
    }

    public static Score getMatchScore(Match match) {
        return matches.get(match);
    }

    public static void updateMatch(String homeTeam, String awayTeam, Integer homeTeamScore, Integer awayTeamScore) {
        if (isMatchInProgress(homeTeam, awayTeam)) {
            Match keyMatch = Match.builder().homeTeam(homeTeam).awayTeam(awayTeam).build();
            matches.get(keyMatch).setAwayTeamScore(awayTeamScore);
            matches.get(keyMatch).setHomeTeamScore(homeTeamScore);
        }
    }

    public static boolean isMatchInProgress(String homeTeam, String awayTeam) {
        Match m = new Match(homeTeam, awayTeam, Instant.now());
        return matches.containsKey(m) && !matches.get(m).getMatch().getIsFinished();
    }

    public static Set<Match> getMatches() {
        return Collections.unmodifiableSet(matches.keySet());
    }


    public static void finishMatch(String homeTeam, String awayTeam) throws FailedToFinishMatchException {
        if (isMatchInProgress(homeTeam, awayTeam)) {
            Match keyMatch = Match.builder().homeTeam(homeTeam).awayTeam(awayTeam).build();
            matches.get(keyMatch).getMatch().setIsFinished(true);
        }
    }

    public static void clear() {
        matches.clear();
    }

}
