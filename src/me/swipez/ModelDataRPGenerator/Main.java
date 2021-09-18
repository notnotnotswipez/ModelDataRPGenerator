package me.swipez.ModelDataRPGenerator;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Main {

    public static File outputFolder;
    public static File inputFolder;

    public final static Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        FileHandler fileHandler = new FileHandler("status.log");

        LOGGER.addHandler(fileHandler);
        File mainDirectory = new File(System.getProperty("user.dir"));
        outputFolder = new File(mainDirectory.getPath()+"/output");
        inputFolder = new File(mainDirectory.getPath()+"/input");

        initializeFolders();
        InputReader.readInput();
        pressEnterKeyToContinue();
    }

    private static void initializeFolders(){
        File mainDirectory = new File(System.getProperty("user.dir"));
        outputFolder = new File(mainDirectory.getPath()+"/output");
        inputFolder = new File(mainDirectory.getPath()+"/input");

        if (!outputFolder.exists()){
            outputFolder.mkdir();
        }
        if (!inputFolder.exists()){
            inputFolder.mkdir();
        }
    }

    private static void pressEnterKeyToContinue()
    {
        System.out.println("Press Enter key to continue...");
        Scanner s = new Scanner(System.in);
        s.nextLine();
    }
}
