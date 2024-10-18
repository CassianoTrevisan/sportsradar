package com.sportsradar.service;

import com.sportsradar.exception.FailedToFinishMatchException;
import com.sportsradar.exception.FailedToStartMatchException;
import com.sportsradar.exception.FailedToUpdateMatchScoreException;
import com.sportsradar.model.Match;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;

import static com.sportsradar.datastore.DataStore.*;

@Slf4j
public class LiveScoreBoardServiceImpl implements LiveScoreBoardService {

    public static final String A_MATCH_LIKE_THIS_ALREADY_EXISTS = "A match like this already exists!";

    @Override
    public void startMatch(String homeTeam, String awayTeam) throws FailedToStartMatchException {
       log.info("Starting a new match.");
        if(isMatchInProgress(homeTeam, awayTeam)){
            log.error(A_MATCH_LIKE_THIS_ALREADY_EXISTS);
            throw new FailedToStartMatchException(A_MATCH_LIKE_THIS_ALREADY_EXISTS);
        }
        addMatch(new Match(homeTeam, awayTeam, Instant.now()));
        log.info("Match added successfully!");
    }

    @Override
    public void updateMatchScore(String homeTeam, String awayTeam, Integer homeTeamScore, Integer awayTeamScore) throws FailedToUpdateMatchScoreException {

    }

    @Override
    public void finishMatch(String homeTeam, String awayTeam) throws FailedToFinishMatchException {

    }

    @Override
    public List<Match> getSummary() {
        return getActiveMatchesOrderedBySumOfGoalsAndStartTime();
    }
}
