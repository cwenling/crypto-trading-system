package spark.ui;

import java.util.Scanner;

public class UiUtil {

    private final Scanner sc;

    public UiUtil() {
        this.sc = new Scanner(System.in);
    }

    public String readInput() {
        return this.sc.nextLine();
    }

    public void close() {
        this.sc.close();
    }

    public void print(String output) {
        System.out.println(output);
    }

}
