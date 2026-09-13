import java.io.BufferedWriter;
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

            // Interactive interface starts here
            // Test code only, maybe we can make a separate method to enter the interactive interface
            System.out.println("Would you like to view a specific Team's information? (Y/N)");
            String input = kb.next();
                if(input.equalsIgnoreCase("Y"))
                {
                    kb.nextLine(); // Consume newline from next()
                    System.out.println("Enter the Team Name or University: ");
                    String teamName = kb.nextLine();
                    viewTeamInfo(teams, teamName);
                }
                else
                {
                    System.out.println("Thank you for using the program.");
                }

            System.out.println("Would you like to add a new team? (Y/N)");
            input = kb.next();
            if(input.equalsIgnoreCase("Y"))
            {
                addTeam(teams, kb);
                printTeams(teams);
                // save so it's not lost next run
                saveTeams(teams, "hackathon_teams.csv");
                System.out.println("Changes saved to hackathon_teams.csv");
            }
            else
            {
                System.out.println("Thank you for using the program.");
            }
            System.out.println("Would you like to update a team's information? (Y/N)");
            input = kb.next();
            if(input.equalsIgnoreCase("Y"))
            {
                kb.nextLine(); // Consume newline from next();
                System.out.println("Enter the Team Name or University: ");
                String teamName = kb.nextLine();
                boolean found = false;
                for(Team t: teams)
                {
                    if(t.getTeam_name().equalsIgnoreCase(teamName) || t.getUniversity().equalsIgnoreCase(teamName))
                    {
                        found = true;
                        updateTeamInfo(t, kb);
                        printTeamInfo(t);
                    }
                }

                if (!found) {
                    System.out.println("No team or university found matching \"" + teamName + "\"\n");
                } else {
                    // save so it's not lost next run
                    saveTeams(teams, "hackathon_teams.csv");
                    System.out.println("Changes saved to hackathon_teams.csv");
                }
            }
            else
            {
                System.out.println("Thank you for using the program.");
                return;
            }
        }
        else {
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

    // writes the teams back out to the CSV file
    public static void saveTeams(List<Team> teams, String filePath) {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(filePath))) {
            writer.write("university,team_name,initial_score,growth_rate");
            writer.newLine();
            for (Team t : teams) {
                writer.write(String.join(",",
                        escapeCsv(t.getUniversity()),
                        escapeCsv(t.getTeam_name()),
                        String.valueOf(t.getInitial_score()),
                        String.valueOf(t.getGrowth_rate())));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save team data to " + filePath, e);
        }
    }

    // quotes the field if it has a comma in it so the CSV doesn't break
    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
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
        System.out.printf("%-45s %-22s %-20s %-10s\n",
            "          University", "Team Name", "Initial Score", "Growth Rate");
        System.out.println("-----------------------------------------------------" +
            "------------------------------------------------");

        int i = 1;
        for(Team t : teams)
        {
            System.out.printf("%-3s %-40s %-28s %-17d %-10.5f\n",
                i + ".",
                t.getUniversity(),
                t.getTeam_name(),
                t.getInitial_score(),
                t.getGrowth_rate());
            i++;
        }
        System.out.println("--------------------------------------------------------" +
            "---------------------------------------------");
    }

    public static void viewTeamInfo(List<Team> teams, String teamName)
    {
        boolean found = false;

        for(Team t: teams)
        {
            if(t.getTeam_name().equalsIgnoreCase(teamName) || t.getUniversity().equalsIgnoreCase(teamName))
            {
                found = true;
                printTeamInfo(t);
            }
        }

        if (!found) {
            System.out.println("No team or university found matching \"" + teamName + "\"");
        }
    }

    public static void printTeamInfo(Team t)
    {
        System.out.println(
            "Rank: " + t.getRank() + "\n" +
            "University: " + t.getUniversity() + "\n" +
            "Team Name: " + t.getTeam_name() + "\n" +
            "Initial Score: " + t.getInitial_score() + "\n" +
            "Growth Rate: " + t.getGrowth_rate() + "\n" +
            "Cumulative Score: " + t.getCumulative_score() + "\n" +
            "Status: " + t.getQualStatus()
        );
    }

    public static void addTeam(List<Team> teams, Scanner kb)
    {
        kb.nextLine();
        Team newTeam = new Team();
        System.out.println("Enter the University Name: ");
        newTeam.setUniversity(kb.nextLine());
        System.out.println("Enter the Team Name: ");
        newTeam.setTeam_name(kb.nextLine());
        System.out.println("Enter the Initial Score: ");
        while(!kb.hasNextInt())
        {
            System.out.println("Invalid input. Please enter an integer for the Initial Score: ");
            kb.next();
        }
        newTeam.setInitial_score(kb.nextInt());
        System.out.println("Enter the Growth Rate: ");
        while(!kb.hasNextDouble())
        {
            System.out.println("Invalid input. Please enter a decimal for the Growth Rate: ");
            kb.next();
        }
        newTeam.setGrowth_rate(kb.nextDouble());
        teams.add(newTeam);
        System.out.println("New team added successfully.");
    }

    public static void updateTeamInfo(Team t, Scanner kb)
    {
        boolean done = false;

        while(!done)
        {
            System.out.println("What would you like to update?" +
                "\nEnter the number corresponding to the field you want to update:" +
                "\n0. Exit Menu" +
                "\n1. University Name" +
                "\n2. Team Name" +
                "\n3. Initial Score" +
                "\n4. Growth Rate");
            while(!kb.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                kb.next();
            }
            int choice = kb.nextInt();
            kb.nextLine(); // Consumes the newline character
            switch(choice)
            {
                case 0:
                    done = true;
                    System.out.println("Exited update menu.");
                    break;
                case 1:
                    System.out.println("Enter the new University Name: ");
                    t.setUniversity(kb.nextLine());
                    break;
                case 2:
                    System.out.println("Enter the new Team Name: ");
                    t.setTeam_name(kb.nextLine());
                    break;
                case 3:
                    System.out.println("Enter the new Initial Score: ");
                    while(!kb.hasNextInt())
                    {
                        System.out.println("Invalid input. Please enter an integer for the Initial Score: ");
                        kb.next();
                    }
                    t.setInitial_score(kb.nextInt());
                    kb.nextLine();
                    break;
                case 4:
                    System.out.println("Enter the new Growth Rate: ");
                    while(!kb.hasNextDouble())
                    {
                        System.out.println("Invalid input. Please enter a decimal for the Growth Rate: ");
                        kb.next();
                    }
                    t.setGrowth_rate(kb.nextDouble());
                    kb.nextLine();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        System.out.println("Team information updated successfully.");
    }
}
