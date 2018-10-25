import java.util.*;

/**
 *  Name:     Driver
 *  Purpose:  Main class that combines all the ElectionManager classes
 *
 *  Imports:
 *    - Distance File
 *    - Candidates File
 *    - Polling place Files
 *  Exports:
 *    - None
 *
 *  Author:  Julian Heng (19473701)
 **/

public class Driver
{
    // Regex String constants
    // Obtained from:
    //  https://stackoverflow.com/questions/6542996/how-to-split-csv-whose-columns-may-contain
    //  https://stackoverflow.com/a/49670696
    public static final
        String SPLIT_REGEX = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    public static final
        String WHITESPACE_REGEX = "\\s+";

    /**
     *  Name:     main
     *  Purpose:  The starting point of the program
     *  Imports:
     *    - args : A string array
     *  Exports:
     *    - none
     **/

    public static void main(String[] args)
    {
        // Check if there's enough arguments
        if (args.length < 3 ||
            args[0].equals("--help") ||
            args[0].equals("-h"))
        {
            printHelp();
        }
        else
        {
            // Load menu, try catch for any unhandled exception
            // before the menu starts
            try
            {
                menu(args);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("Exiting...");
            }
        }
    }

    /**
     *  Name:     menu
     *  Purpose:  The interface the user will be interacting with
     *  Imports:
     *    - files : A string array representing the filenames
     *  Exports:
     *    - none
     **/

    public static void menu(String[] files)
    {
        Menu menu;
        boolean exit;
        DSAGraph<Location,Trip> locations;
        DSALinkedList<Nominee> nomineesList;
        DSALinkedList<HousePreference> preferenceList;
        DSALinkedList<HousePreference> tempPrefList;
        Iterator<HousePreference> iter;
        long timeStart, timeEnd, duration;

        // Making menu
        menu = new Menu(6);
        menu.addOption("Please select an option:");
        menu.addOption("    1. List Nominees");
        menu.addOption("    2. Nominee Search");
        menu.addOption("    3. List by Margin");
        menu.addOption("    4. Itinerary by Margin");
        menu.addOption("    5. Exit");
        exit = false;

        // Parsing files
        locations = ElectionManagerInit.processDistances(files[0]);
        nomineesList = ElectionManagerInit.processNominees(files[1]);
        preferenceList = new DSALinkedList<HousePreference>();

        timeStart = System.nanoTime();

        for (int i = 2; i < files.length; i++)
        {
            tempPrefList = ElectionManagerInit.processPreference(
                                files[i], nomineesList);

            iter = tempPrefList.iterator();
            while (iter.hasNext())
            {
                preferenceList.insertLast(iter.next());
            }
        }

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;
        System.out.printf(
            "Processing preference file took %sms\n",
            Commons.toMiliseconds(duration)
        );

        // Main program loop
        while (! exit)
        {
            try
            {
                switch (menu.getUserInput())
                {
                    case 1:
                        ElectionManager.listNominees(nomineesList);
                        break;
                    case 2:
                        ElectionManager.searchNominees(nomineesList);
                        break;
                    case 3:
                        ElectionManager.listPartyMargin(preferenceList);
                        break;
                    case 4:
                        ElectionManager.createItinerary(
                            preferenceList,
                            locations
                        );
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        exit = true;
                        break;
                }
            }
            catch (Exception e)
            {
                System.out.println(
                    "Exception caught: " + e.getMessage()
                );
            }
        }
    }

    /**
     *  Name:     printHelp
     *  Purpose:  Print the help message
     *  Imports:
     *    - none
     *  Exports:
     *    - none
     **/

    public static void printHelp()
    {
        String msg[] = {
            "Usage: java Driver [Distance file] [House candidates file]" +
            "[House preference file]",
            "",
            "Example distance file:",
            "    from_State,from_Division,from_Latitude,from_Longitude," +
            "to_State,to_Division,to_Latitude,to_Longitude,distance_metres," +
            "time_hours_minutes,trans_type",
            "    WA,Perth Airport,-31.936,115.964,NT,Darwin Airport," +
            "-12.4088317,130.8726632,4016000,44:00:00,car",
            "",
            "Example house candidate file:",
            "    StateAb,DivisionID,DivisionNm,PartyAb,PartyNm," +
            "CandidateID,Surname,GivenNm,Elected,HistoricElected",
            "    NSW,151,Warringah,LP,Liberal,28624,ABBOTT,Tony,Y,Y",
            "",
            "Example house preference file:",
            "     StateAb,DivisionID,DivisionNm,PollingPlaceID,PollingPlace," +
            "CandidateID,Surname,GivenNm,BallotPosition,Elected," +
            "HistoricElected,PartyAb,PartyNm,OrdinaryVotes,Swing",
            "    WA,235,Brand,7472,Baldivis,28420,SCOTT,Philip,1,N,N,RUA," +
            "Rise Up Australia Party,176,4.28",
            ""
        };

        for (int i = 0; i < msg.length; i++)
        {
            System.out.println(msg[i]);
        }
    }
}
