import com.opencsv.bean.CsvBindByName;

public class Team {
    @CsvBindByName(column = "university")
        private String university;
    @CsvBindByName(column = "team_name")
        private String team_name;
    @CsvBindByName(column = "initial_score")
        private int initial_score;
    @CsvBindByName(column = "growth_rate")
        private double growth_rate;

    private double cumulative_score;
    private int rank;
    private String qualStatus;

    public Team() {}

    // Getters
    public String getUniversity() { return university; }
    public String getTeam_name() { return team_name; }
    public int getInitial_score() { return initial_score; }
    public double getGrowth_rate() { return growth_rate; }
    public double getCumulative_score() { return cumulative_score; }
    public int getRank() { return rank; }
    public String getQualStatus() { return qualStatus; }

    // Setters
    public void setUniversity(String university) { this.university = university; }
    public void setTeam_name(String team_name) { this.team_name = team_name; }
    public void setInitial_score(int initial_score) { this.initial_score = initial_score; }
    public void setGrowth_rate(double growth_rate) { this.growth_rate = growth_rate; }
    public void setCumulative_score(double cumulative_score) {
        this.cumulative_score = cumulative_score;}
    public void setRank(int rank) { this.rank = rank; }
    public void setQualStatus(String qualStatus) { this.qualStatus = qualStatus; }

    public String print() {
        return "" +
                university + " " +
                team_name + " " +
                initial_score + " " +
                growth_rate + " " +
                qualStatus;
    }
}