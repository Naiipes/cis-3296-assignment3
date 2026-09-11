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

    private boolean isQualified;
    private double cumulative_score;

    public Team() {}

    public String getUniversity() { return university; }
    public String getTeam_name() { return team_name; }
    public int getInitial_score() { return initial_score; }
    public double getGrowth_rate() { return growth_rate; }
    public void setIsQualified(boolean isQualified) {
        this.isQualified = isQualified;
    }
    public boolean getIsQualified() { return isQualified; }
    public double getCumulative_score() { return cumulative_score; }
    public void setCumulative_score(double cumulative_score) {
        this.cumulative_score = cumulative_score;
    }

    public String print() {
        return "" +
                university + " " +
                team_name + " " +
                initial_score + " " +
                growth_rate + " " +
                isQualified;
    }

    public String leaderboardPrint()
    {
        return "" +
                university + " " +
                team_name + " " +
                cumulative_score + " " +
                isQualified;
    }
}