package com.sportsradar.service;

import com.sportsradar.datastore.DataStore;
import com.sportsradar.exception.FailedToFinishMatchException;
import com.sportsradar.exception.FailedToStartMatchException;
import com.sportsradar.exception.FailedToUpdateMatchScoreException;
import com.sportsradar.model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static com.sportsradar.service.LiveScoreBoardServiceImpl.INVALID_INPUT_FOR_MATCH_START;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class LiveScoreBoardServiceTest {

    public static final String BRAZIL = "BRAZIL";
    public static final String ARGENTINA = "ARGENTINA";
    private static final String ITALY = "ITALY";
    private static final String GERMANY = "GERMANY";
    private static final String MEXICO = "MEXICO";
    private static final String SPAIN = "SPAIN";
    private static final String URUGUAY = "URUGUAY";
    private static final String AUSTRALIA = "AUSTRALIA";
    private static final String CANADA = "CANADA";
    private static final String FRANCE = "FRANCE";

    private LiveScoreBoardService liveScoreBoardService;

    @BeforeEach
    public void setUp() {
        DataStore.clear();
        liveScoreBoardService = new LiveScoreBoardServiceImpl();
    }

    @Test
    public void testStartMatchSuccess() throws FailedToStartMatchException {
        liveScoreBoardService.startMatch(BRAZIL, ARGENTINA);

        List<Match> summary = liveScoreBoardService.getSummary();
        assertEquals(1, summary.size());

        Match match = summary.get(0);
        assertEquals(BRAZIL, match.getHomeTeam());
        assertEquals(ARGENTINA, match.getAwayTeam());
        assertEquals(0, liveScoreBoardService.getMatchScore(match).getHomeTeamScore());
        assertEquals(0, liveScoreBoardService.getMatchScore(match).getAwayTeamScore());
        assertFalse(match.getIsFinished());
    }

    @Test
    public void testStartMatchInvalidInput() {
        //Home team is empty
        Exception exception = assertThrows(FailedToStartMatchException.class, () -> {
            liveScoreBoardService.startMatch("", "Argentina", Instant.now());
        });
        assertEquals(INVALID_INPUT_FOR_MATCH_START, exception.getMessage());

        //Away team is empty
        exception = assertThrows(FailedToStartMatchException.class, () -> {
            liveScoreBoardService.startMatch("Brazil", "", Instant.now());
        });
        assertEquals(INVALID_INPUT_FOR_MATCH_START, exception.getMessage());

        //Home team is null
        exception = assertThrows(FailedToStartMatchException.class, () -> {
            liveScoreBoardService.startMatch(null, "Argentina", Instant.now());
        });
        assertEquals(INVALID_INPUT_FOR_MATCH_START, exception.getMessage());

        //Away team is null
        exception = assertThrows(FailedToStartMatchException.class, () -> {
            liveScoreBoardService.startMatch("Brazil", null, Instant.now());
        });
        assertEquals(INVALID_INPUT_FOR_MATCH_START, exception.getMessage());

        //Start time is null
        exception = assertThrows(FailedToStartMatchException.class, () -> {
            liveScoreBoardService.startMatch("Brazil", "Argentina", null);
        });
        assertEquals(INVALID_INPUT_FOR_MATCH_START, exception.getMessage());
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

        Match match = new Match(BRAZIL, ARGENTINA, null);
        assertEquals(3, liveScoreBoardService.getMatchScore(match).getHomeTeamScore());
        assertEquals(2, liveScoreBoardService.getMatchScore(match).getAwayTeamScore());
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

    @Test
    public void testSummaryOrder() throws FailedToStartMatchException, FailedToUpdateMatchScoreException {
        liveScoreBoardService.startMatch(MEXICO, CANADA, Instant.parse("2023-10-20T10:00:00Z"));
        liveScoreBoardService.startMatch(SPAIN, BRAZIL, Instant.parse("2023-10-20T11:00:00Z"));
        liveScoreBoardService.startMatch(GERMANY, FRANCE, Instant.parse("2023-10-20T12:00:00Z"));
        liveScoreBoardService.startMatch(URUGUAY, ITALY, Instant.parse("2023-10-20T13:00:00Z"));
        liveScoreBoardService.startMatch(ARGENTINA, AUSTRALIA, Instant.parse("2023-10-20T15:00:00Z"));

        liveScoreBoardService.updateMatchScore(MEXICO, CANADA, 0, 5);
        liveScoreBoardService.updateMatchScore(SPAIN, BRAZIL, 2,10);
        liveScoreBoardService.updateMatchScore(GERMANY, FRANCE, 2,2);
        liveScoreBoardService.updateMatchScore(URUGUAY, ITALY, 6, 6);
        liveScoreBoardService.updateMatchScore(ARGENTINA, AUSTRALIA, 3, 1);

        List<Match> summary = liveScoreBoardService.getSummary();
        assertEquals(5, summary.size());

        summary.forEach(match -> {
            System.out.println(match.getHomeTeam() + " - " + match.getAwayTeam() + " - " +
                    liveScoreBoardService.getMatchScore(match).getHomeTeamScore() + " - " +
                    liveScoreBoardService.getMatchScore(match).getAwayTeamScore());
        });
        // Assert the correct order based on sum of scores and start time
        Match match1 = summary.get(0);
        assertEquals(URUGUAY, match1.getHomeTeam());
        assertEquals(ITALY, match1.getAwayTeam());
        assertEquals(6, liveScoreBoardService.getMatchScore(match1).getHomeTeamScore());
        assertEquals(6, liveScoreBoardService.getMatchScore(match1).getAwayTeamScore());

        Match match2 = summary.get(1);
        assertEquals(SPAIN, match2.getHomeTeam());
        assertEquals(BRAZIL, match2.getAwayTeam());
        assertEquals(2, liveScoreBoardService.getMatchScore(match2).getHomeTeamScore());
        assertEquals(10, liveScoreBoardService.getMatchScore(match2).getAwayTeamScore());

        Match match3 = summary.get(2);
        assertEquals(MEXICO, match3.getHomeTeam());
        assertEquals(CANADA, match3.getAwayTeam());
        assertEquals(0, liveScoreBoardService.getMatchScore(match3).getHomeTeamScore());
        assertEquals(5, liveScoreBoardService.getMatchScore(match3).getAwayTeamScore());

        Match match4 = summary.get(3);
        assertEquals(ARGENTINA, match4.getHomeTeam());
        assertEquals(AUSTRALIA, match4.getAwayTeam());
        assertEquals(3, liveScoreBoardService.getMatchScore(match4).getHomeTeamScore());
        assertEquals(1, liveScoreBoardService.getMatchScore(match4).getAwayTeamScore());

        Match match5 = summary.get(4);
        assertEquals(GERMANY, match5.getHomeTeam());
        assertEquals(FRANCE, match5.getAwayTeam());
        assertEquals(2, liveScoreBoardService.getMatchScore(match5).getHomeTeamScore());
        assertEquals(2, liveScoreBoardService.getMatchScore(match5).getAwayTeamScore());

        //a second update to this match, for the sake of stressing the logic
        liveScoreBoardService.updateMatchScore(MEXICO, CANADA, 8, 5);

        summary = liveScoreBoardService.getSummary();

        Match matchUpdated = summary.get(0);
        assertEquals(MEXICO, matchUpdated.getHomeTeam());
        assertEquals(CANADA, matchUpdated.getAwayTeam());
        assertEquals(8, liveScoreBoardService.getMatchScore(matchUpdated).getHomeTeamScore());
        assertEquals(5, liveScoreBoardService.getMatchScore(matchUpdated).getAwayTeamScore());

    }

    @Test
    public void testFinishMatch() throws FailedToStartMatchException, FailedToFinishMatchException {
        LiveScoreBoardService liveScoreBoardService = new LiveScoreBoardServiceImpl();
        liveScoreBoardService.startMatch(BRAZIL, ARGENTINA, Instant.parse("2023-10-20T10:00:00Z"));
        liveScoreBoardService.finishMatch(BRAZIL, ARGENTINA);

        Set<Match> matches = DataStore.getMatches();
        Match match = matches.stream()
                .filter(m -> m.getHomeTeam().equals(BRAZIL) && m.getAwayTeam().equals(ARGENTINA))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Match not found"));


        assertTrue(match.getIsFinished(), "Match should be marked as finished");

        assertThrows(FailedToFinishMatchException.class, () -> liveScoreBoardService.finishMatch(BRAZIL, ARGENTINA), "Finishing an already finished match should throw an exception");
    }

}
