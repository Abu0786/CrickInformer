package com.crick.apis.services.impl;

import com.crick.apis.entities.Match;
import com.crick.apis.repositories.MatchRepo;
import com.crick.apis.services.CricketService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class CricketServiceImpl implements CricketService {


    private MatchRepo matchRepo;

    public CricketServiceImpl(MatchRepo matchRepo) {
        this.matchRepo = matchRepo;
    }

    @Override
    public List<Match> getLiveMatchScores() {
        List<Match> matches = new ArrayList<>();
        try {
            String url = "https://www.cricbuzz.com/cricket-match/live-scores";
            Document document = Jsoup.connect(url).get();
            Elements liveScoreElements = document.select("div.cb-mtch-lst.cb-tms-itm");
            for (Element match : liveScoreElements) {
                HashMap<String, String> liveMatchInfo = new LinkedHashMap<>();
                String teamsHeading = match.select("h3.cb-lv-scr-mtch-hdr").select("a").text();
                String matchNumberVenue = match.select("span").text();
                Elements matchBatTeamInfo = match.select("div.cb-hmscg-bat-txt");
                String battingTeam = matchBatTeamInfo.select("div.cb-hmscg-tm-nm").text();
                String score = matchBatTeamInfo.select("div.cb-hmscg-tm-nm+div").text();
                Elements bowlTeamInfo = match.select("div.cb-hmscg-bwl-txt");
                String bowlTeam = bowlTeamInfo.select("div.cb-hmscg-tm-nm").text();
                String bowlTeamScore = bowlTeamInfo.select("div.cb-hmscg-tm-nm+div").text();
                String textLive = match.select("div.cb-text-live").text();
                String textComplete = match.select("div.cb-text-complete").text();
                //getting match link
                String matchLink = match.select("a.cb-lv-scrs-well.cb-lv-scrs-well-live").attr("href").toString();

                Match match1 = new Match();
                match1.setTeamHeading(teamsHeading);
                match1.setMatchNumberVenue(matchNumberVenue);
                match1.setBattingTeam(battingTeam);
                match1.setBattingTeamScore(score);
                match1.setBowlTeam(bowlTeam);
                match1.setBowlTeamScore(bowlTeamScore);
                match1.setLiveText(textLive);
                match1.setMatchLink(matchLink);
                match1.setTextComplete(textComplete);
                match1.setMatchStatus();


                matches.add(match1);

//                update the match in database


                updateMatch(match1);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matches;
    }

    private void updateMatch(Match match1) {

        Match match = this.matchRepo.findByTeamHeading(match1.getTeamHeading()).orElse(null);
        if (match == null) {
            this.matchRepo.save(match1);
        } else {

            match1.setMatchId(match.getMatchId());
            this.matchRepo.save(match1);
        }

    }

    @Override
    public List<List<String>> getIccRanking() {
        List<List<String>> ranking = new ArrayList<>();
        String rankingUrl = "https://www.cricbuzz.com/cricket-stats/icc-rankings/men/batting";

        try {
            // Fetch the HTML document from the URL
            Document document = Jsoup.connect(rankingUrl).get();

            // Select the elements that contain the ranking information
            Elements rows = document.select("div.cb-col.cb-col-100.cb-font-14.cb-lst-itm.text-center");

            // Iterate through each player row and extract the ranking data
            for (Element row : rows) {
                // Ensure the row has at least 4 child elements

                    List<String> playerData = new ArrayList<>();

                    // Extract the player's position (1st child element)
                    String position = row.child(0).text();

                    // Extract the player's name (2nd child element)
                    String playerName = row.child(1).text();

                    // Extract the country name (3rd child element)
                    String country = row.child(2).text();



                    // Add extracted data to the list
                    playerData.add(position);
                    playerData.add(playerName);
                    playerData.add(country);

                    ranking.add(playerData);

            }

            // Print the rankings (for debugging purposes)
            System.out.println(ranking);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ranking;
    }


    @Override
    public List<Match> getAllMatches() {
        return this.matchRepo.findAll();
    }
}
