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
        DSALinkedList<String> distFiles, nomineesFiles, prefFiles;

        DSAGraph<Location,Trip> locations;
        DSALinkedList<Nominee> nomineesList;
        DSALinkedList<HousePreference> preferenceList;

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
                distFiles = new DSALinkedList<String>();
                nomineesFiles = new DSALinkedList<String>();
                prefFiles = new DSALinkedList<String>();

                locations = new DSAGraph<Location,Trip>();
                nomineesList = new DSALinkedList<Nominee>();
                preferenceList = new DSALinkedList<HousePreference>();

                parseArgs(args, distFiles, nomineesFiles, prefFiles);

                prepareLists(
                    distFiles, nomineesFiles, prefFiles,
                    locations, nomineesList, preferenceList
                );

                menu(locations, nomineesList, preferenceList);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Exiting...");
            }
        }
    }

    /**
     *  Name:     parseArgs
     *  Purpose:  Setting the correct filename to the appropriate list
     *  Imports:
     *    - args          : The arguments passed to the program
     *    - distFiles     : A list for the filenames representing distances
     *    - nomineesFiles : A list for the filenames representing nominees
     *    - prefFiels     : A list for the filenames representing preferences
     *  Exports:
     *    - none
     **/

    public static void parseArgs(
        String[] args,
        DSALinkedList<String> distFiles,
        DSALinkedList<String> nomineesFiles,
        DSALinkedList<String> prefFiles)
    {
        int i = 0;
        while (i < args.length)
        {
            if (args[i].equals("-d") ||
                args[i].equals("--distance"))
            {
                distFiles.insertLast(args[++i]);
            }
            else if (args[i].equals("-c") ||
                     args[i].equals("--candidate"))
            {
                nomineesFiles.insertLast(args[++i]);
            }
            else
            {
                prefFiles.insertLast(args[i]);
            }

            i++;
        }
    }

    /**
     *  Name:     prepareLists
     *  Purpose:  Parsing files and processing them
     *  Imports:
     *    - distFiles      : A list for the filenames representing distances
     *    - nomineesFiles  : A list for the filenames representing nominees
     *    - prefFiels      : A list for the filenames representing preferences
     *    - locations      : A graph represeting all the locations
     *    - nomineesList   : A list representing all the nominees objects
     *    - preferenceList : A list representing all the preference objects
     *  Exports:
     *    - none
     **/

    public static void prepareLists(
        DSALinkedList<String> distFiles,
        DSALinkedList<String> nomineesFiles,
        DSALinkedList<String> prefFiles,
        DSAGraph<Location,Trip> locations,
        DSALinkedList<Nominee> nomineesList,
        DSALinkedList<HousePreference> preferenceList)
    {
        DSALinkedList<Nominee> tempNomineesList;
        DSALinkedList<HousePreference> tempPrefList;
        Iterator<Nominee> iterNominee;
        Iterator<HousePreference> iterPref;
        Iterator<String> iterFile;

        long timeStart, timeEnd, duration;
        long timeStartFunc, timeEndFunc, durationFunc;

        timeStart = System.nanoTime();
        timeStartFunc = System.nanoTime();

        // Parsing files
        iterFile = distFiles.iterator();
        while (iterFile.hasNext())
        {
            locations = ElectionManagerInit.processDistances(
                            iterFile.next(),
                            locations
                        );
        }

        timeEndFunc = System.nanoTime();
        durationFunc = timeEndFunc - timeStartFunc;
        Commons.header(
            String.format(
                "Processing distances took %sms",
                Commons.toMiliseconds(durationFunc)
            )
        );

        timeStartFunc = System.nanoTime();
        iterFile = nomineesFiles.iterator();
        while (iterFile.hasNext())
        {
            tempNomineesList = ElectionManagerInit.processNominees(
                                    iterFile.next()
                                );

            iterNominee = tempNomineesList.iterator();
            while (iterNominee.hasNext())
            {
                nomineesList.insertLast(iterNominee.next());
            }
        }

        timeEndFunc = System.nanoTime();
        durationFunc = timeEndFunc - timeStartFunc;
        Commons.header(
            String.format(
                "Processing nominees took %sms",
                Commons.toMiliseconds(durationFunc)
            )
        );

        timeStartFunc = System.nanoTime();
        iterFile = prefFiles.iterator();
        while (iterFile.hasNext())
        {
            tempPrefList = ElectionManagerInit.processPreference(
                                iterFile.next(), nomineesList
                            );

            iterPref = tempPrefList.iterator();
            while (iterPref.hasNext())
            {
                preferenceList.insertLast(iterPref.next());
            }
        }

        timeEndFunc = System.nanoTime();
        durationFunc = timeEndFunc - timeStartFunc;
        Commons.header(
            String.format(
                "Processing preferences took %sms",
                Commons.toMiliseconds(durationFunc)
            )
        );

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;
        Commons.header(
            String.format(
                "Processing all files took %sms",
                Commons.toMiliseconds(duration)
            )
        );
    }

    /**
     *  Name:     menu
     *  Purpose:  The interface the user will be interacting with
     *  Imports:
     *    - locations      : A graph represeting all the locations
     *    - nomineesList   : A list representing all the nominees objects
     *    - preferenceList : A list representing all the preference objects
     *  Exports:
     *    - none
     **/

    public static void menu(
        DSAGraph<Location,Trip> locations,
        DSALinkedList<Nominee> nomineesList,
        DSALinkedList<HousePreference> preferenceList)
    {
        Menu menu;
        boolean exit;

        // Making menu
        menu = new Menu(6);
        menu.addOption("Please select an option:");
        menu.addOption("    1. List Nominees");
        menu.addOption("    2. Nominee Search");
        menu.addOption("    3. List by Margin");
        menu.addOption("    4. Itinerary by Margin");
        menu.addOption("    5. Exit");
        exit = false;

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
            "Usage: java Driver -d [Distance file] -c [House candidates file]" +
            " [House preference file]",
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
