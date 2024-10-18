package com.sportsradar.service;

import com.sportsradar.exception.FailedToFinishMatchException;
import com.sportsradar.exception.FailedToStartMatchException;
import com.sportsradar.exception.FailedToUpdateMatchScoreException;
import com.sportsradar.model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class LiveScoreBoardServiceTest {

    public static final String BRAZIL = "BRAZIL";
    public static final String ARGENTINA = "ARGENTINA";
    private LiveScoreBoardService liveScoreBoardService;

    @BeforeEach
    public void setUp() {
        liveScoreBoardService = new LiveScoreBoardServiceImpl();
    }

    @Test
    public void testStartMatchSuccess() throws FailedToStartMatchException {
        liveScoreBoardService.startMatch(BRAZIL, ARGENTINA);

        Set<Match> summary = liveScoreBoardService.getSummary();
        assertEquals(1, summary.size());

        Match match = summary.iterator().next();
        assertEquals(BRAZIL, match.getHomeTeam());
        assertEquals(ARGENTINA, match.getAwayTeam());
        assertEquals(0, match.getHomeTeamScore());
        assertEquals(0, match.getAwayTeamScore());
        assertFalse(match.getIsFinished());
    }

    @Test
    public void testStartMatchFailure_DuplicateMatch() throws FailedToStartMatchException {
        liveScoreBoardService.startMatch(BRAZIL, ARGENTINA);

        assertThrows(FailedToStartMatchException.class, () -> {
            liveScoreBoardService.startMatch(BRAZIL, ARGENTINA);  // Trying to start the same match again
        });
    }

    @Test
    public void testUpdateMatchScoreSuccess() throws FailedToStartMatchException, FailedToUpdateMatchScoreException {
        liveScoreBoardService.startMatch(BRAZIL, ARGENTINA);

        liveScoreBoardService.updateMatchScore(BRAZIL, ARGENTINA, 3, 2);

        Set<Match> summary = liveScoreBoardService.getSummary();
        Match match = summary.iterator().next();
        assertEquals(3, match.getHomeTeamScore());
        assertEquals(2, match.getAwayTeamScore());
    }

    @Test
    public void testUpdateMatchScoreFailure_NoMatchFound() {
        assertThrows(FailedToUpdateMatchScoreException.class, () -> {
            liveScoreBoardService.updateMatchScore(BRAZIL, ARGENTINA, 3, 2);  // No match started yet
        });
    }

    @Test
    public void testUpdateMatchScoreFailure_MatchFinished() throws FailedToStartMatchException, FailedToFinishMatchException {
        liveScoreBoardService.startMatch(BRAZIL, ARGENTINA);
        liveScoreBoardService.finishMatch(BRAZIL, ARGENTINA);

        assertThrows(FailedToUpdateMatchScoreException.class, () -> {
            liveScoreBoardService.updateMatchScore(BRAZIL, ARGENTINA, 1, 1);  // Trying to update after finishing
        });
    }

}
