package com.sportsradar.service;

import com.sportsradar.datastore.DataStore;
import com.sportsradar.exception.FailedToFinishMatchException;
import com.sportsradar.exception.FailedToStartMatchException;
import com.sportsradar.exception.FailedToUpdateMatchScoreException;
import com.sportsradar.model.Match;
import com.sportsradar.model.Score;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.sportsradar.datastore.DataStore.addMatch;
import static com.sportsradar.datastore.DataStore.getMatches;
import static com.sportsradar.datastore.DataStore.isMatchInProgress;
import static com.sportsradar.datastore.DataStore.updateMatch;

@Slf4j
public class LiveScoreBoardServiceImpl implements LiveScoreBoardService {

    public static final String A_MATCH_LIKE_THIS_ALREADY_EXISTS = "A match like this already exists!";
    public static final String THIS_MATCH_DOESN_T_EXIST = "This match doesn't exist.";
    public static final String INVALID_INPUT_FOR_MATCH_START = "Invalid input for Match Start!";

    private static int applyAsInt(Match match) {
        return DataStore.getMatchScore(match).getHomeTeamScore() + DataStore.getMatchScore(match).getAwayTeamScore();
    }

    @Override
    public void startMatch(String homeTeam, String awayTeam) throws FailedToStartMatchException {
        startMatch(homeTeam, awayTeam, Instant.now());
    }

    @Override
    public void startMatch(String homeTeam, String awayTeam, Instant startTime) throws FailedToStartMatchException {
        log.info("Starting a new match.");
        validateMatchInput(homeTeam, awayTeam, startTime);
        if (isMatchInProgress(homeTeam, awayTeam)) {
            log.error(A_MATCH_LIKE_THIS_ALREADY_EXISTS);
            throw new FailedToStartMatchException(A_MATCH_LIKE_THIS_ALREADY_EXISTS);
        }
        addMatch(new Match(homeTeam, awayTeam, startTime));
        log.info("Match added successfully!");
    }

    private void validateMatchInput(String homeTeam, String awayTeam, Instant startTime) throws FailedToStartMatchException {
        log.info("Validation in progress. Home Team - {}, Away Team - {}, Start - {}", homeTeam, awayTeam, startTime);
        if(StringUtils.isEmpty(homeTeam) || StringUtils.isEmpty(awayTeam) || null == startTime){
            throw new FailedToStartMatchException(INVALID_INPUT_FOR_MATCH_START);
        }
    }

    public Score getMatchScore(Match match) {
        return DataStore.getMatchScore(match);
    }

    @Override
    public void updateMatchScore(String homeTeam, String awayTeam, Integer homeTeamScore, Integer awayTeamScore) throws FailedToUpdateMatchScoreException {
        log.info("Updating a match.");
        if (!isMatchInProgress(homeTeam, awayTeam)) {
            log.error(THIS_MATCH_DOESN_T_EXIST);
            throw new FailedToUpdateMatchScoreException(THIS_MATCH_DOESN_T_EXIST);
        }
        updateMatch(homeTeam, awayTeam, homeTeamScore, awayTeamScore);
    }

    @Override
    public void finishMatch(String homeTeam, String awayTeam) throws FailedToFinishMatchException {
        log.info("Finishing a match.");
        if (!isMatchInProgress(homeTeam, awayTeam)) {
            log.error(THIS_MATCH_DOESN_T_EXIST);
            throw new FailedToFinishMatchException(THIS_MATCH_DOESN_T_EXIST);
        }
        DataStore.finishMatch(homeTeam, awayTeam);
    }

    @Override
    public List<Match> getSummary() {
        return getMatches().stream().filter(match -> !match.getIsFinished())
                .sorted(Comparator.comparingInt(LiveScoreBoardServiceImpl::applyAsInt).reversed()
                        .thenComparing(Match::getMatchStart, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}
