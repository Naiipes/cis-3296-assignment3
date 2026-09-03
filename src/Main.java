import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter round number: ");
        Scanner kb = new Scanner(System.in);
        int round = kb.nextInt();
        while (round > 0) {
            calculateGrowth(round);
            System.out.println("Enter round number or -1 to exit: ");
            round = kb.nextInt();
            if (round == -1)
                break;
        }
    }

    public static void calculateGrowth(int D) {
        ArrayList<Double> scores = new ArrayList<>();
        double score = 10;
        double R = 1.2;
        for (int i = 1; i <= D; i++) {
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
            System.out.println("Qualified\n");
        } else {
            System.out.println("Not Qualified\n");
        }
        System.out.println("Sum: " + sum);
    }

    // Testing line for merging branches.
}