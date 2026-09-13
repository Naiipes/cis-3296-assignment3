import com.opencsv.bean.CsvToBeanBuilder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

// handles reading and writing hackathon_teams.csv so Main doesn't have to
// deal with file stuff directly
public class TeamRepository {

    private final String filePath;

    public TeamRepository(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    // Reads all teams from the CSV file (same as the old getTeams() in Main)
    public List<Team> loadTeams() {
        try (Reader in = Files.newBufferedReader(Path.of(filePath))) {
            return new CsvToBeanBuilder<Team>(in).withType(Team.class).build().parse();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load team data from " + filePath, e);
        }
    }

    // Writes the teams back out to the CSV file, overwriting whatever was there.
    // Only saves university/team_name/initial_score/growth_rate 
    public void saveTeams(List<Team> teams) {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(filePath))) {
            writer.write("university,team_name,initial_score,growth_rate");
            writer.newLine();
            for (Team t : teams) {
                writer.write(String.join(",",
                        escape(t.getUniversity()),
                        escape(t.getTeam_name()),
                        String.valueOf(t.getInitial_score()),
                        String.valueOf(t.getGrowth_rate())));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save team data to " + filePath, e);
        }
    }

    // Wraps a field in quotes if it has a comma/quote/newline in it, so the CSV
    // doesn't break (e.g. a university name like "City U, London")
    private String escape(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
