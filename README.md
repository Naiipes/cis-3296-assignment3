# TUJ AI Hackathon — Team Leaderboard

This program tracks hackathon teams across a set number
of rounds, computes each team's cumulative score from its starting score
and per-round growth rate, determines whether the team qualifies, and prints a
ranked leaderboard. Team records can be viewed, added, and edited while the
program is running, and changes are written back to the data file.

## Team Members

| Member         | GitHub       |
|----------------|--------------|
| Otavio Seki    | @Naiipes     |
| Karl Fernandez | @kidevil1290 |
| Kody Wong      | @kodywong1   |

**Chosen starting version:**
Otavio

## Requirements

- **JDK 23**
- **Maven** — for dependency resolution and running from the command line
- **OpenCSV 5.9** — declared in `pom.xml`, downloaded automatically by Maven

## Running the program

### From the command line

```bash
mvn compile
mvn exec:java
```

### From an IDE

Clone the repository, open it as a Maven project, let the IDE import
dependencies, then run `src/Main.java`.

### Data file

The program reads and writes **`hackathon_teams.csv`**. Run from the
repository root, or the program will fail on startup when it can't find the file.

The file has four columns and one header row:

```csv
university,team_name,initial_score,growth_rate
Temple University Japan,TUJ AI,10,1.40
```

Only these four fields are stored. Rank, cumulative score, and qualification
status are only calculated at runtime.

## Code organization

```
Main.java   — scoring logic, leaderboard, menu, CSV read/write
Team.java   — data for one team
```

**`Team.java`** holds one team record. The four CSV fields
(`university`, `team_name`, `initial_score`, `growth_rate`) are mapped by
OpenCSV's `@CsvBindByName`. 
`cumulative_score`, `rank`, and `qualStatus` are
calculated at runtime.

**`Main.java`** is organized into four groups of methods:

## Features

On startup the program lists all teams, prompts for a round number between 4 and
10, computes scores, and prints the leaderboard. It then enters a menu loop:

1. **Menu UI** - allows user to select and navigate through different features to manage teams.
    Continues to loop until user chooses to exit.
2. **View leaderboard** — ranked leaderboard with score and qualification status
3. **View a team's information** — look up by team name or university with a message when nothing matches
4. **Add a new team** — prompts for each field, scores and ranks the new team immediately.
5. **Update a team's information** — opens another menu for editing any of the four fields,
   with rescoring and re-ranking after the edit.
6. **CSV Read and Write** - handles reading csv data and handles auto-save for data proctection in case
  of crashes, to ensure data is preserved for next run time.

## Checks Made

### PR #2 — Basic Leaderboard feature added + minor compile error handling on my end

The branch lowered `maven.compiler.source` and
`maven.compiler.target` from 23 to 22 so the project would compile on machines with older Java versions. 
It was later changed back to 23 after updating Java version.

Fixed an issue where the loop iterator was changed
from `i = 1` to `i = 0` while the initial score had already been added to the
list, producing `D + 1` scores for `D` rounds.

### PR #3 — Redesigned Leaderboard method and added `printTeams()`

Pulled the branch, built it, and compared leaderboard output against the data
file. Formatting and ranking were correct without any errors.
Approved without changes.

### PR #4 — Methods added view+print TeamInfo, addTeam, UpdateTeamInfo + minor organizing in Team.java

Review found three issues, all fixed before merge:

- Both lookup prompts used `Scanner.next()`, which reads a single token, so `Temple University Japan`
  was captured as `Temple` and never matched. Switched to `nextLine()`, with
  the leftover newline consumed first.
- A lookup that matched nothing printed no output at all. Added a found flag so both paths report the unmatched input.
- The update menu called `nextInt()` without a guard, throwing `InputMismatchException` on a letter. It now loops and
warns the user on wrong inputs.  

### PR #5 — Kody

User interface redesign warranted to provide user options to select through different features willingly without going through a series of prompts to enter them.

### PR #6 — Recalculate scores and ranks after adding or updating a team

PR #5 introduced a new bug: adding a
team left it with a cumulative score of `0.0`, rank `0`, and a null status,
while editing a team's initial score or growth rate saved the new value but
left the old score in place until the next run. Both menu paths now rescore and
re-rank before saving.



