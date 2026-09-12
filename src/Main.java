import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.opencsv.bean.CsvToBeanBuilder;

public class Main {
    public static void main(String[] args) {
        List<Team> teams = getTeams();

        Scanner kb = new Scanner(System.in);

        printTeams(teams);
        System.out.println("Enter number of rounds: ");
        if (kb.hasNextInt()) {
            int D = kb.nextInt();
            while((D < 4 || D > 10)) {
                System.out.println("Invalid number. Please enter a round between 4 and 10: ");
                D = kb.nextInt();
            }
            for(Team t : teams)
            {
                teamScore(t, D);
            }
            updateLeaderboard(teams);
            printLeaderboard(teams);
        } else {
            System.out.println("Invalid input.");
            return;
        }
        System.out.println("Finish");
    }

    public static List<Team> getTeams() {
        List<Team> teams;
        try (Reader in = Files.newBufferedReader(Path.of("hackathon_teams.csv"))) {
            teams = new CsvToBeanBuilder<Team>(in).withType(Team.class).build().parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return teams;
    }

    public static void teamScore(Team t, int D) {
        if (D <= 0) {
            return;
        }
        ArrayList<Double> scores = new ArrayList<>();
        double score = t.getInitial_score();
        scores.add(score);
        double R = t.getGrowth_rate();
        for (int i = 1; i < D; i++) {
            score *= R;
            scores.add(score);
        }
        double sum = 0;
        boolean isQualified = false;
        for (int i = 0; i < scores.size(); i++) {
            sum += scores.get(i);
            if (sum >= 80 && i < 4) {
                isQualified = true;
            }
        }
        if (isQualified) {
            t.setQualStatus("QUALIFIED");
        } else {
            t.setQualStatus("NOT QUALIFIED");
        }
        t.setCumulative_score(sum);
    }

    public static List<Team> updateLeaderboard(List<Team> teams)
    {
        teams.sort((t1, t2) -> Double.compare(t2.getCumulative_score(), t1.getCumulative_score()));

        int rank = 1;
        for(int i = 0; i < teams.size(); i++)
        {
            if(i > 0 && teams.get(i).getCumulative_score() ==  teams.get(i - 1).getCumulative_score())
            {
                teams.get(i).setRank(teams.get(i - 1).getRank());
            }
            else
            {
                teams.get(i).setRank(rank);
            }
            rank++;
        }
        return teams;
    }

    public static void printLeaderboard(List<Team> teams)
    {
        System.out.printf("%-14s %-32s %-24s %-22s %-20s\n",
            "Rank", "University", "Team Name", "Score", "Status");
        System.out.println("-------------------------------------------------------" + 
            "----------------------------------------------------");

        for(Team t : teams)
        {
            System.out.printf("%-5s %-40s %-23s %-20.5f %-20s\n",
                " " + t.getRank(),
                t.getUniversity(),
                t.getTeam_name(),
                t.getCumulative_score(),
                t.getQualStatus());
        }
        System.out.println("-------------------------------------------------------" + 
            "----------------------------------------------------");
    }

    public static void printTeams(List<Team> teams)
    {
        System.out.printf("%-43s %-20s %-20s %-10s\n",
            "        University", "Team Name", "Initial Score", "Growth Rate");
        System.out.println("-----------------------------------------------------" + 
            "--------------------------------------------");  
        for(Team t : teams)
        {
            System.out.printf("%-40s %-28s %-17d %-10.5f\n",
                t.getUniversity(),
                t.getTeam_name(),
                t.getInitial_score(),
                t.getGrowth_rate());
        }
        System.out.println("-----------------------------------------------------" + 
            "--------------------------------------------");    
    }
}