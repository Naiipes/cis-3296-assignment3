import com.opencsv.bean.CsvToBeanBuilder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        List<Team> teams = getTeams();
        List<Team> leaderboard = new ArrayList<>();

        Scanner kb = new Scanner(System.in);

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
                leaderboard = updateLeaderboard(leaderboard, t);
            }
            printLeaderboard(leaderboard);
            //System.out.println(teamScore(teams.get(2), D));
            //System.out.println(teams.get(2).print());
        } else {
            System.out.println("Invalid input.");
            return;
        }
        System.out.println("Finish");
//        System.out.println("Enter round number: ");
//        Scanner kb = new Scanner(System.in);
//        if (kb.hasNextInt()) {
//            int round = kb.nextInt();
//            while (round > 0) {
//                calculateGrowth(round);
//                System.out.println("Enter round number or -1 to exit: ");
//                round = kb.nextInt();
//                if (round == -1)
//                    break;
//            }
//        } else {
//            System.out.println("Not a number\n");
//        }
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
        for (int i = 0; i < D; i++) {
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
            t.setIsQualified(true);
        } else {
            t.setIsQualified(false);
        }
        t.setCumulative_score(sum);
    }

    public static List<Team> updateLeaderboard(List<Team> teams, Team t)
    {
        int i = 0;
        while(i < teams.size() && teams.get(i).getCumulative_score() > t.getCumulative_score())
        {
            i++;
        }
        teams.add(i, t);
        return teams;
    }

    public static void printLeaderboard(List<Team> teams)
    {
        System.out.println("Leaderboard:");
        for(int i = 0; i < teams.size(); i++)
        {
            System.out.println(teams.get(i).leaderboardPrint());
        }
    }
}