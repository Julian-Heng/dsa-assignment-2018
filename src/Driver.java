import java.util.*;

/**
 *  Name:    Election Manager
 *  Purpose: Manage election by viewing stats and calculations
 *  Imports:
 *    - Distance File
 *    - Candidates File
 *    - Polling place Files
 *  Exports:
 *    - None
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
        locations = processDistances(files[0]);
        nomineesList = processNominees(files[1]);
        preferenceList = new DSALinkedList<HousePreference>();

        timeStart = System.nanoTime();

        for (int i = 2; i < files.length; i++)
        {
            tempPrefList = processPreference(files[i], nomineesList);

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
            toMiliseconds(duration)
        );

        // Main program loop
        while (! exit)
        {
            try
            {
                switch (menu.getUserInput())
                {
                    case 1:
                        listNominees(nomineesList);
                        break;
                    case 2:
                        searchNominees(nomineesList);
                        break;
                    case 3:
                        listPartyMargin(preferenceList);
                        break;
                    case 4:
                        createItinerary(preferenceList, locations);
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
     *  Name:     processDistances
     *  Purpose:  Parse the distance file into a graph
     *  Imports:
     *    - file : A string for the filename
     *  Exports:
     *    - locationGraph : A Graph containing all the locations
     **/

    public static DSAGraph<Location,Trip> processDistances(
        String file)
    {
        DSALinkedList<String> list;
        DSAGraph<Location,Trip> locationGraph;
        Iterator<String> iter;
        String line;
        Location startLocation, endLocation;

        long timeStart, timeEnd, funcDuration;

        String spinner[] = {"\\", "|", "/", "-"};
        int count = 1;

        Trip tripInfo;

        int distance;
        int duration;
        String transportType;

        String[] split;
        String[] timeSplit;

        timeStart = System.nanoTime();

        // Read the file
        list = FileIO.readText(file);
        iter = list.iterator();

        locationGraph = new DSAGraph<Location,Trip>();

        // Loop through the file
        while (iter.hasNext())
        {
            // Print status
            line = iter.next();
            System.out.printf(
                "\rProcessing %s... [%d/%d] %s",
                file,
                count,
                list.getCount(),
                spinner[count++ % spinner.length]
            );

            // Split csv and create starting and ending locations
            split = line.split(SPLIT_REGEX);

            startLocation = new Location(split[0], split[1],
                                         split[2], split[3]);
            endLocation = new Location(split[4], split[5],
                                       split[6], split[7]);

            try
            {
                // Getting distance
                if (split[8].equals("NONE"))
                {
                    distance = 0;
                }
                else
                {
                    distance = Integer.parseInt(split[8]);
                }

                // Getting time
                if (split[9].contains(":"))
                {
                    timeSplit = split[9].split(":");
                    duration = Integer.parseInt(timeSplit[0]) * 3600 +
                               Integer.parseInt(timeSplit[1]) * 60 +
                               Integer.parseInt(timeSplit[2]);
                }
                else if (split[9].equals("NONE"))
                {
                    duration = 0;
                }
                else
                {
                    duration = Integer.parseInt(split[9]);
                }

                // Getting transport
                transportType = split[10];

                // Creating a tripInfo Object as the edge value
                tripInfo = new Trip(transportType, distance, duration);

                // Add starting and ending locations to the graph
                locationGraph.addVertex(
                    startLocation.getDivision(),
                    startLocation
                );

                locationGraph.addVertex(
                    endLocation.getDivision(),
                    endLocation
                );

                // Connect them
                locationGraph.addEdge(
                    startLocation.getDivision(),
                    endLocation.getDivision(),
                    tripInfo.getDuration(),
                    tripInfo
                );
            }
            catch (NumberFormatException e)
            {
                // Catch any invalid string integers
                throw new IllegalArgumentException(
                    "Invalid Distance File: " + e.getMessage()
                );
            }
            catch (Exception e)
            {
                // Catch any other exception for formatting
                throw new IllegalArgumentException(
                    "Error: " + e.getMessage()
                );
            }
        }

        // Time statistics
        timeEnd = System.nanoTime();
        funcDuration = timeEnd - timeStart;

        System.out.printf("\u0008%sms\n", toMiliseconds(funcDuration));

        return locationGraph;
    }

    /**
     *  Name:     processNominees
     *  Purpose:  Parse the house candidates file to Nominee Objects
     *  Imports:
     *    - file : A string for the filename
     *  Exports:
     *    - nomineeList : A linked list for the list of nominees
     **/

    public static DSALinkedList<Nominee> processNominees(String file)
    {
        DSALinkedList<String> list;
        DSALinkedList<Nominee> nomineeList;
        Iterator<String> iter;
        DSALinkedList<String> invalidEntries;
        String line;

        long timeStart, timeEnd, duration;

        String spinner[] = {"\\", "|", "/", "-"};
        int count = 1;

        timeStart = System.nanoTime();

        // Read the file
        list = FileIO.readText(file);
        iter = list.iterator();

        nomineeList = new DSALinkedList<Nominee>();
        invalidEntries = new DSALinkedList<String>();
        line = "";

        // Loop through the file
        if (iter.hasNext())
        {
            while (iter.hasNext())
            {
                // Print status
                System.out.printf(
                    "\rProcessing %s... [%d/%d] %s",
                    file,
                    count,
                    list.getCount(),
                    spinner[count++ % spinner.length]
                );

                // Create a new nominee object and insert last
                try
                {
                    line = iter.next();
                    nomineeList.insertLast(new Nominee(line));
                }
                catch (IllegalArgumentException e)
                {
                    // For any invalid entries, insert to a seperate list
                    invalidEntries.insertLast(line);
                }
            }
        }
        else
        {
            // Throw exception file is empty
            throw new IllegalArgumentException(
                "House candidate file is invalid"
            );
        }

        // Print time statistics
        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        System.out.printf("\u0008%sms\n", toMiliseconds(duration));

        // Print invalid entries
        if (invalidEntries.getCount() != 0)
        {
            iter = invalidEntries.iterator();
            while (iter.hasNext())
            {
                System.out.printf(
                    "Invalid entry in %s: %s, ignoring...\n",
                    file, iter.next()
                );
            }
        }

        return nomineeList;
    }

    /**
     *  Name:     processPreference
     *  Purpose:  Parse the preference file to HousePreference Objects
     *  Imports:
     *    - file        : A string for the filename
     *    - nomineeList : A linked list containing the list of Nominees
     *                    to refer to
     *  Exports:
     *    - preferenceList : A list of preferences
     **/

    public static DSALinkedList<HousePreference> processPreference(
        String file, DSALinkedList<Nominee>nomineeList)
    {
        DSALinkedList<String> list;
        DSALinkedList<String> inList;
        DSALinkedList<HousePreference> preferenceList;
        DSALinkedList<Nominee> tempNomineeList;
        Iterator<String> iter;
        Iterator<String> iter2;
        Iterator<Nominee> iterNominee;
        String line;

        Nominee tempNominee;
        HousePreference tempHousePref;
        String divisionId, divisionName;

        DSALinkedList<String> divisionIdList;

        long timeStart, timeEnd, duration;

        String[] split;

        String spinner[] = {"\\", "|", "/", "-"};
        int count = 1;

        timeStart = System.nanoTime();

        // Read the file
        list = FileIO.readText(file);
        iter = list.iterator();

        divisionIdList = new DSALinkedList<String>();

        // Get unique Division IDs from the preference files
        while (iter.hasNext())
        {
            line = iter.next();
            split = line.split(SPLIT_REGEX);
            if (! split[1].isEmpty() &&
                ! split[1].equals("DivisionID") &&
                isUnique(divisionIdList, split[1]))
            {
                divisionIdList.insertLast(split[1]);
            }
        }

        preferenceList = new DSALinkedList<HousePreference>();
        iter = divisionIdList.iterator();

        // For every unique Division IDs, loop through the preference file
        // and calculate total votes for the Nominees in that Division
        if (iter.hasNext())
        {
            while (iter.hasNext())
            {
                line = iter.next();

                // Get the list of nominees from unique Division ID
                tempNomineeList = getNomineeFromDivisionId(nomineeList, line);

                // Get the list of preferences from unique Division ID
                inList = getPreferenceFromDivisionId(list, line);

                // Get Division Name and ID
                split = inList.peekFirst().split(SPLIT_REGEX);

                divisionName = split[2];
                divisionId = split[1];

                tempHousePref = new HousePreference(divisionName, divisionId);

                iter2 = inList.iterator();

                // Loop preference file
                while (iter2.hasNext())
                {
                    // Print status
                    System.out.printf(
                        "\rProcessing %s... [%d/%d] %s",
                        file,
                        count,
                        list.getCount(),
                        spinner[count++ % spinner.length]
                    );

                    split = iter2.next().split(SPLIT_REGEX);

                    try
                    {
                        // Checking for informal votes
                        if (! split[6].equals("Informal") &&
                            ! split[7].equals("Informal"))
                        {
                            // Update the nominee's votes
                            tempNominee = getNomineeFromCandidateId(
                                tempNomineeList, split[5]
                            );

                            tempNominee.addNumVotes(split[13]);
                        }
                        else
                        {
                            tempHousePref.addNumInformalVotes(split[13]);
                        }
                    }
                    // Formatting exceptions
                    catch (NumberFormatException e)
                    {
                        throw new IllegalArgumentException(
                            "Invalid Number of Votes: " + split[13]
                        );
                    }
                    catch (ArrayIndexOutOfBoundsException e)
                    {
                        // Checks if there's enough information
                        throw new IllegalArgumentException(
                            "Invalid House Preference File"
                        );
                    }
                }

                // For all nominees in Division, add to the
                // house preference object
                iterNominee = tempNomineeList.iterator();
                while (iterNominee.hasNext())
                {
                    tempHousePref.addNomineeToList(iterNominee.next());
                }

                // Add house preference for that unique division to the
                // overall preference list
                tempHousePref.updateTotalVotes();
                preferenceList.insertLast(tempHousePref);
            }
        }
        else
        {
            // Throw exception file is empty
            throw new IllegalArgumentException(
                "House preference file is invalid"
            );
        }

        // Print time statistics
        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        System.out.printf("\u0008%sms\n", toMiliseconds(duration));

        return preferenceList;
    }

    /**
     *  Name:     getNomineeFromState
     *  Purpose:  Extract Nominees from a state search
     *  Imports:
     *    - nomineeList : A list of nominees objects
     *    - inState     : A string for the state to search
     *  Exports:
     *    - returnList : A list of nominess with the corresponding state
     **/

    public static DSALinkedList<Nominee> getNomineeFromState(
        DSALinkedList<Nominee> nomineeList, String inState)
    {
        DSALinkedList<Nominee> returnList;
        Iterator<Nominee> iter;
        Nominee inList;

        returnList = new DSALinkedList<Nominee>();
        iter = nomineeList.iterator();

        // For all Nominees in list
        while (iter.hasNext())
        {
            inList = iter.next();

            // If Nominee is from search state, add to list
            if (inList.getState().equals(inState))
            {
                returnList.insertLast(inList);
            }
        }

        return returnList;
    }

    /**
     *  Name:     getNomineeFromDivisionId
     *  Purpose:  Extract Nominees from a division search
     *  Imports:
     *    - nomineeList  : A list of nominees objects
     *    - inDivisionId : A string for the division to search
     *  Exports:
     *    - returnList : A list of nominees with the corresponding division ID
     **/

    public static DSALinkedList<Nominee> getNomineeFromDivisionId(
        DSALinkedList<Nominee> nomineeList, String inDivisionId)
    {
        DSALinkedList<Nominee> returnList;
        Iterator<Nominee> iter;
        Nominee inList;
        returnList = new DSALinkedList<Nominee>();

        iter = nomineeList.iterator();

        // For all Nominees in list
        while (iter.hasNext())
        {
            inList = iter.next();

            // If Nominee is from division, add to list
            if (compareIntString(inList.getIdDivision(), inDivisionId))
            {
                returnList.insertLast(inList);
            }
        }

        return returnList;
    }

    /**
     *  Name:     getNomineeFromCandidateId
     *  Purpose:  Extract Nominee from candidate id search
     *  Imports:
     *    - nomineeList   : A list of nominees objects
     *    - inCandidateId : A string for the candidate id to search
     *  Exports:
     *    - result : The nominee with the corresponding candidate id
     **/

    public static Nominee getNomineeFromCandidateId(
        DSALinkedList<Nominee> nomineeList, String inCandidateId)
    {
        Nominee inList = null;
        Nominee result = null;
        Iterator<Nominee> iter = nomineeList.iterator();
        boolean isFound = false;

        // For all Nominees in list
        while (! isFound && iter.hasNext())
        {
            inList = iter.next();

            // If nominee has the Candidate ID
            if (compareIntString(inList.getIdCandidate(), inCandidateId))
            {
                result = inList;
                isFound = true;
            }
        }

        return result;
    }

    /**
     *  Name:     getPreferenceFromDivisionId
     *  Purpose:  Return a list of entries in the preference file
     *            containing the Division ID
     *  Imports:
     *    - list         : A string list containing the preference file
     *    - inDivisionId : A string for the division ID to search
     *  Exports:
     *    - returnList : A String list extracting from the prefernce file
     **/

    public static DSALinkedList<String> getPreferenceFromDivisionId(
        DSALinkedList<String> list, String inDivisionId)
    {
        DSALinkedList<String> returnList;
        Iterator<String> iter;
        String inList;
        String[] split;
        returnList = new DSALinkedList<String>();

        // For all entries in preference file
        iter = list.iterator();
        while (iter.hasNext())
        {
            inList = iter.next();
            split = inList.split(SPLIT_REGEX);

            // If Division ID matches, add to return list
            if (! split[1].isEmpty() && split[1].equals(inDivisionId))
            {
                returnList.insertLast(inList);
            }
        }

        return returnList;
    }

    /**
     *  Name:     listNominees
     *  Purpose:  First option of the menu, displaying sorted Nominees
     *            with filters
     *  Imports:
     *    - nomineeList : A list of all Nominees
     *  Exports:
     *    - none
     **/

    public static void listNominees(DSALinkedList<Nominee> nomineeList)
    {
        // Header for the output file
        String header[] = {
            "StateAb", "DivisionID", "DivisionNm", "PartyAb", "PartyNm",
            "CandidateID", "Surname", "GivenNm", "Elected", "HistoricElected"
        };

        Menu filterMenu;
        Menu orderMenu;
        Iterator<Nominee> iter;
        Nominee inList;
        Nominee inResult;
        Nominee searchResult[];
        Object sorted[];
        long timeStart, timeEnd, duration;

        iter = nomineeList.iterator();

        // Array of booleans for determining filter options
        boolean filterOptions[] = {false, false, false};

        // Array to contain filter messages
        String filterMsg[] = {
            "Input state filter: ",
            "Input party filter: ",
            "Input division filter: "
        };

        String filters[] = new String[3];

        String userInput;
        String[] split;
        int optionSwitch;

        String sortLine = "";

        int numMatches = 0;
        int numNominee = 0;
        int i = 0;

        DSAMaxHeap heap;

        String[] fileContents;

        // Creating menus
        filterMenu = new Menu(4);
        orderMenu = new Menu(5);

        filterMenu.addOption("Select filter type (eg: 1 2 3):");
        filterMenu.addOption("    1. State");
        filterMenu.addOption("    2. Party");
        filterMenu.addOption("    3. Division");

        orderMenu.addOption("Select order type (eg: 1 2 3):");
        orderMenu.addOption("    1. Surname");
        orderMenu.addOption("    2. State");
        orderMenu.addOption("    3. Party");
        orderMenu.addOption("    4. Divisions");

        filterMenu.printMenu();
        userInput = Input.string();
        split = userInput.split(WHITESPACE_REGEX);

        // Get filter options
        if (! userInput.isEmpty())
        {
            processFilterOptions(filterOptions, split);
        }

        // Process filter options
        for (i = 0; i < filters.length; i++)
        {
            filters[i] = filterOptions[i] ? getFilter(filterMsg[i]) : ".*";
        }

        // Get order options
        orderMenu.printMenu();
        userInput = Input.string();
        split = userInput.split(WHITESPACE_REGEX);

        timeStart = System.nanoTime();

        // Get the number of matches for creating array
        // Array because of sorting an array is easier than
        // sorting a linked list
        while (iter.hasNext())
        {
            numNominee++;
            if (searchNominee1(iter.next(), filters))
            {
                numMatches++;
            }
        }

        searchResult = new Nominee[numMatches];
        sorted = new Nominee[numMatches];
        heap = new DSAMaxHeap(numMatches);
        iter = null;
        inList = null;
        iter = nomineeList.iterator();
        i = 0;

        // Get the matched nominees and add them to the array
        while (iter.hasNext())
        {
            inList = iter.next();
            if (searchNominee1(inList, filters))
            {
                searchResult[i] = inList;
                i++;
            }
        }

        // If order options were selected
        if (! userInput.isEmpty())
        {
            // For each search result, generate the sort line
            // and add to the heap as the priority
            for (int j = 0; j < searchResult.length; j++)
            {
                inResult = searchResult[j];
                sortLine = generateSortLine(inResult, split);
                heap.add(sortLine, inResult);
            }

            // Sort and convert back to an array
            heap.heapSort();
            sorted = heap.toObjArray();

            for (int j = 0; j < numMatches; j++)
            {
                searchResult[j] = (Nominee)sorted[j];
            }
        }

        // Convert the array to a csv format
        fileContents = new String[numMatches];

        for (i = 0; i < numMatches; i++)
        {
            fileContents[i] = searchResult[i].toString();
        }

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        // Print table and prompt for saving the results as a file
        printCsvTable(fileContents, header);
        System.out.printf("\n%d/%d matches\n", numMatches, numNominee);
        System.out.printf("Took %sms\n\n", toMiliseconds(duration));

        saveCsvToFile(fileContents, "nominees_list.csv");
    }

    /**
     *  Name:     searchNominees
     *  Purpose:  Second option in the menu, displays nominees that match
     *            a user's search field
     *  Imports:
     *    - nomineeList : A list of nominees to search through
     *  Exports:
     *    - none
     **/

    public static void searchNominees(DSALinkedList<Nominee> nomineeList)
    {
        // Header for the output file
        String header[] = {
            "StateAb", "DivisionID", "DivisionNm", "PartyAb", "PartyNm",
            "CandidateID", "Surname", "GivenNm", "Elected", "HistoricElected"
        };

        Menu filterMenu;
        Iterator<Nominee> iter;
        Nominee inList;
        Nominee searchResult[];
        long timeStart, timeEnd, duration;

        iter = nomineeList.iterator();

        // Array of booleans for filter options
        boolean filterOptions[] = {false, false};

        // Array of strings for filter messages
        String filterMsg[] = {
            "Input state filter: ",
            "Input party filter: "
        };

        String filters[] = new String[2];

        String userInput;
        String[] split;

        String nameFilter = "";

        int numMatches = 0;
        int numNominee = 0;
        int i = 0;

        String fileContents[];

        // Create menu
        filterMenu = new Menu(3);

        filterMenu.addOption("Select filter type (eg: 1 2 3):");
        filterMenu.addOption("    1. State");
        filterMenu.addOption("    2. Party");

        // Get the search term
        nameFilter = Input.string("Input surname search term: ");

        // Get any filter options
        filterMenu.printMenu();
        userInput = Input.string();

        timeStart = System.nanoTime();
        split = userInput.split(WHITESPACE_REGEX);

        // If there are filters, process them into arrays
        if (! userInput.isEmpty())
        {
            processFilterOptions(filterOptions, split);
        }

        // For all selected filter options, convert them to regex
        for (i = 0; i < filters.length; i++)
        {
            filters[i] = filterOptions[i] ? getFilter(filterMsg[i]) : ".*";
        }

        // Count the number of matches
        while (iter.hasNext())
        {
            if (searchNominee2(iter.next(), nameFilter, filters))
            {
                numMatches++;
            }
        }

        searchResult = new Nominee[numMatches];
        iter = null;
        inList = null;
        iter = nomineeList.iterator();
        i = 0;

        // For each nominee in nominee list
        while (iter.hasNext())
        {
            numNominee++;
            inList = iter.next();

            // If matches, add to array
            if (searchNominee2(inList, nameFilter, filters))
            {
                searchResult[i] = inList;
                i++;
            }
        }

        fileContents = new String[numMatches];

        // Convert to csv file
        for (i = 0; i < numMatches; i++)
        {
            fileContents[i] = searchResult[i].toString();
        }

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        // Print table and prompt to save to file
        printCsvTable(fileContents, header);
        System.out.printf("\n%d/%d matches\n", numMatches, numNominee);
        System.out.printf("Took %sms\n\n", toMiliseconds(duration));

        saveCsvToFile(fileContents, "nominees_search.csv");
    }

    /**
     *  Name:     processFilterOptions
     *  Purpose:  Set an array of booleans true depending on user input
     *  Imports:
     *    - options : A boolean array containing options
     *    - input   : A String of integers inputted by the user
     *  Exports:
     *    - none
     **/

    public static void processFilterOptions(boolean[] options, String[] input)
    {
        String option;
        int optionSwitch;

        // Loop through each user selected input
        for (int i = 0; i < input.length; i++)
        {
            option = input[i];
            try
            {
                // Attempt to convert from string to int
                optionSwitch = Integer.parseInt(option);

                // If user input is within range of the number of options
                if (optionSwitch > 0 && optionSwitch <= options.length)
                {
                    options[optionSwitch - 1] = true;
                }
                else
                {
                    // Print error message
                    System.out.println(
                        "Invalid option: " + option + ". Ignoring"
                    );
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println(
                    "Invalid option: " + option + ". Ignoring"
                );
            }
        }
    }

    /**
     *  Name:     getFilter
     *  Purpose:  A wrapper function to get user input and convert to
     *            a regex string
     *  Imports:
     *    - msg : A string for the message to prompt the user with
     *  Exports:
     *    - String : A regex string for the search term
     **/

    public static String getFilter(String msg)
    {
        String userInput;
        userInput = Input.string(msg);
        // If user did not enter anything, use "match all" regex
        return userInput.isEmpty() ? ".*" : userInput;
    }

    /**
     *  Name:     searchNominee1
     *  Purpose:  Search a Nominee's classfields, iteration 1
     *  Imports:
     *    - inNominee : A nominee object used to search
     *    - filters   : A string array of user defined filters
     *  Exports:
     *    - Boolean
     **/

    public static boolean searchNominee1(Nominee inNominee, String[] filters)
    {
        /**
         * Attempts to match the nominee's State, the name or abbreviation
         * of the Party name, and the Division's name or ID
         *
         * We can match all of them at the same time because if user
         * did not specify a filter, the "match all" regex is used
         **/
        return (inNominee.getState().matches(filters[0])) &&
               ((inNominee.getNameParty().matches(filters[1])) ||
                (inNominee.getAbvParty().matches(filters[1]))) &&
               ((inNominee.getNameDivision().matches(filters[2])) ||
                (Integer.toString(inNominee.getIdDivision()).matches(
                    filters[2]
                )));
    }

    /**
     *  Name:     searchNominee2
     *  Purpose:  Search a Nominee's classfields, iteration 2
     *  Imports:
     *    - inNominee : A nominee object used to search
     *    - name      : A string for searching the name
     *    - filters   : A string array of user defined filters
     *  Exports:
     *    - none
     **/

    public static boolean searchNominee2(
        Nominee inNominee,
        String name,
        String[] filters)
    {
        return (inNominee.getSurname().startsWith(name)) &&
               (inNominee.getState().matches(filters[0])) &&
               ((inNominee.getNameParty().matches(filters[1])) ||
                (inNominee.getAbvParty().matches(filters[1])));
    }

    /**
     *  Name:     generateSortLine
     *  Purpose:  Given the order of the user defined sorts, generate a
     *            sort line used as the priority when inserting to the heap
     *  Imports:
     *    - inNominee : A nominee object used to generate the sort line
     *    - order     : A String array of the user defined sort order
     *  Exports:
     *    - sortLine : A String containing the sort line
     **/

    public static String generateSortLine(Nominee inNominee, String[] order)
    {
        String sortLine = "";
        String choice;

        // For each option in sort order
        for (int i = 0; i < order.length; i++)
        {
            choice = order[i];
            try
            {
                // Determine Nominee classfield and add
                // the contents in the classfield to the
                // sort line
                switch (Integer.parseInt(choice))
                {
                    case 1:
                        sortLine += inNominee.getSurname() + ",";
                        break;
                    case 2:
                        sortLine += inNominee.getState() + ",";
                        break;
                    case 3:
                        sortLine += inNominee.getNameParty() + ",";
                        break;
                    case 4:
                        sortLine += inNominee.getNameDivision() + ",";
                        break;
                    default:
                        System.out.printf(
                            "Invalid order option: %s. Ignoring\n",
                            choice
                        );
                        break;
                }
            }
            catch (NumberFormatException e)
            {
                System.out.printf(
                    "Invalid order option: %s. Ignoring\n",
                    choice
                );
            }
        }

        return sortLine;
    }

    /**
     *  Name:     listPartyMargin
     *  Purpose:  Third option in the menu. List division's of a
     *            selected party whose margin is below a user
     *            defined threshold
     *  Imports:
     *    - prefList : A list of house preference objects
     *  Exports:
     *    - none
     **/

    public static void listPartyMargin(
        DSALinkedList<HousePreference> prefList)
    {
        // Header for outputing to file
        String headerFile[] = {
            "Num", "DivisionID", "DivisionNm", "StateAb", "PartyAb",
            "PartyNm", "VotesFor", "VotesAgainst", "VotesTotal",
            "Percent", "Margin"
        };

        DSALinkedList<VoteStats> voteResults;
        DSALinkedList<String> fileList;
        Iterator<VoteStats> iter;
        Iterator<String> iter2;
        VoteStats inStats;
        String header, line;
        String fileContents[];
        String csvLine;

        int count, total;

        String userInput;
        String partyFilter;
        double marginLimit;

        long timeStart, timeEnd, duration;

        count = 0;
        total = 0;

        csvLine = "";
        partyFilter = "";

        // If user did not enter a party name
        if ((partyFilter = getPartyFilter()) == null)
        {
            throw new IllegalArgumentException(
                "No party found: Empty search field"
            );
        }

        // Get the margin limit
        marginLimit = getMarginLimit();

        timeStart = System.nanoTime();

        // Calculate the votes into each a voteStats object
        // and return as a list, distinguished by division
        voteResults = calcVotes(prefList, partyFilter);
        fileList = new DSALinkedList<String>();

        iter = voteResults.iterator();

        // For each division's vote stats
        while (iter.hasNext())
        {
            total++;
            inStats = iter.next();

            // If margin is below the threshold
            if (Math.abs(inStats.getMargin()) < marginLimit)
            {
                // Append to csv file list
                count++;
                csvLine = count + "," + inStats.toString();
                fileList.insertLast(csvLine);
            }
        }

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        // If there are no results recorded
        if (count == 0)
        {
            throw new IllegalArgumentException(
                "No party found: " + partyFilter
            );
        }
        else
        {
            // Begin converting csv list to a string array
            fileContents = new String[count];
            count = 0;

            iter2 = fileList.iterator();
            while (iter2.hasNext())
            {
                fileContents[count] = iter2.next();
                count++;
            }

            // Print the csv table and save to file
            printCsvTable(fileContents, headerFile);

            System.out.printf("\n%d/%d matches\n", count, total);
            System.out.printf("Took %sms\n\n", toMiliseconds(duration));

            saveCsvToFile(
                fileContents,
                String.format(
                    "%s_%03.2f_margin.csv",
                    partyFilter,
                    marginLimit
                )
            );
        }
    }

    /**
     *  Name:     createItinerary
     *  Purpose:  Create an itinerary on locations selected by the user
     *  Imports:
     *    - prefList : A list of house preference objects
     *    - map      : A graph containing all the locations
     *  Exports:
     *    - none
     **/

    public static void createItinerary(
        DSALinkedList<HousePreference> prefList,
        DSAGraph<Location,Trip> map)
    {
        // Header for output file
        String headerFile[] = {
            "Num", "DivisionID", "DivisionNm", "StateAb", "PartyAb",
            "PartyNm", "VotesFor", "VotesAgainst", "VotesTotal",
            "Percent", "Margin"
        };

        DSALinkedList<VoteStats> voteResults;
        DSALinkedList<String> fileList;
        Iterator<VoteStats> iter;
        Iterator<String> iter2;
        VoteStats inStats;
        String header, line;
        String fileContents[];
        String csvLine;

        int count, total;

        String userInput;
        String[] split;
        String partyFilter;
        double marginLimit;

        int[] visitDivisionIndex;
        String[] visitDivision;
        boolean allLocations, enableOptimise;

        DSAStack<Location> path;
        DSAStack<Location> tempStack;

        long timeStart, timeEnd, duration;

        count = 0;
        total = 0;

        csvLine = "";
        partyFilter = "";

        allLocations = false;
        enableOptimise = false;

        // If user did not enter a party filter
        if ((partyFilter = getPartyFilter()) == null)
        {
            throw new IllegalArgumentException(
                "No party found: Empty search field"
            );
        }

        // Get margin threshold
        marginLimit = getMarginLimit();

        timeStart = System.nanoTime();

        // Get the list of calculated votes in the form of
        // a linked list of voteStats objects
        voteResults = calcVotes(prefList, partyFilter);
        fileList = new DSALinkedList<String>();

        iter = voteResults.iterator();

        // For each vote result in each division
        while (iter.hasNext())
        {
            total++;
            inStats = iter.next();

            // If margin is below the threshold
            if (Math.abs(inStats.getMargin()) < marginLimit)
            {
                count++;
                // Append to csv linked list
                csvLine = count + "," + inStats.toString();
                fileList.insertLast(csvLine);
            }
        }

        fileContents = new String[count];

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        // If party did not match anything
        if (count == 0)
        {
            throw new IllegalArgumentException(
                "No party found: " + partyFilter
            );
        }

        fileContents = new String[count];
        count = 0;

        // Convert linked list to array
        iter2 = fileList.iterator();
        while (iter2.hasNext())
        {
            fileContents[count] = iter2.next();
            count++;
        }

        // Print table and time statistics
        printCsvTable(fileContents, headerFile);
        System.out.printf("\n%d/%d matches\n", count, total);
        System.out.printf("Took %sms\n\n", toMiliseconds(duration));

        // Get the locations the user wants to visit
        userInput = Input.string(
            "Input Division indexes to visit (eg: 1 2 3): "
        );

        // Empty user input indicates that user wants to visit all locations
        if (userInput.isEmpty())
        {
            allLocations = true;
            System.out.println(
                "No locations selected, using all locations"
            );

            visitDivisionIndex = new int[count];

            // For each id in csv file, set to an array
            // containing all the divisions to visit
            //
            // Offset is 1 because 0 index arrays
            for (int i = 1; i <= count; i++)
            {
                visitDivisionIndex[i - 1] = i;
            }
        }
        else
        {
            allLocations = false;

            // Get the id in csv file and set to an array
            // containing the divisions to visit
            split = userInput.split(WHITESPACE_REGEX);
            visitDivisionIndex = new int[split.length];

            try
            {
                for (int i = 0; i < split.length; i++)
                {
                    visitDivisionIndex[i] = Integer.parseInt(split[i]);
                }
            }
            catch (NumberFormatException e)
            {
                throw new IllegalArgumentException(
                    "Inputed Division index is invalid"
                );
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                throw new IllegalArgumentException(
                    "Inputed Division index is invalid"
                );
            }
        }

        // If all locations are selected, do not attempt locations
        // Assuming that the states are already in order before hand
        if (! allLocations)
        {
            // Prompt user
            userInput = Input.string(
                "Attempt optimisation? " +
                "Note: Starting location may change [Y/n]: "
            );

            // If user selected yes or did not enter anything
            if ((userInput.matches("^[yY]$")) ||
                (userInput.isEmpty()))
            {
                enableOptimise = true;
            }
            else
            {
                enableOptimise = false;
            }
        }

        // Restart timer
        timeStart = System.nanoTime();
        count = 0;
        visitDivision = new String[visitDivisionIndex.length];

        // Sort by division id if optimisation is enabled
        if (enableOptimise)
        {
            DSAMergeSort.mergeSort(visitDivisionIndex);
        }

        // For each selected divisions, get the division name
        for (int i = 0; i < visitDivisionIndex.length; i++)
        {
            split = fileContents[visitDivisionIndex[i] - 1].split(
                SPLIT_REGEX
            );

            visitDivision[count] = split[2];
            count++;
        }

        path = new DSAStack<Location>();
        path.push(map.getVertexValue(visitDivision[0]));

        // For each division
        for (int i = 1; i < visitDivision.length; i++)
        {
            // Get the shortest route from previous element
            // to current element
            //
            // Could be better optimised as the scope is only
            // two locations at a time, not the entire locations
            // selected
            tempStack = map.dijkstra(
                visitDivision[i - 1],
                visitDivision[i]
            );

            // Aggregate stacks
            while (! tempStack.isEmpty())
            {
                path.push(tempStack.pop());
            }
        }

        // Reverse the stack
        tempStack = path;
        path = new DSAStack<Location>();

        while (! tempStack.isEmpty())
        {
            path.push(tempStack.pop());
        }

        System.out.print("\n");

        // Print path
        printItinerary(
            path,
            map,
            partyFilter,
            marginLimit,
            visitDivision.length,
            timeStart
        );
    }

    /**
     *  Name:     getPartyFilter
     *  Purpose:  Prompt user for the party filter
     *  Imports:
     *    - none
     *  Exports:
     *    - userInput : A String for the user input
     **/

    public static String getPartyFilter()
    {
        String userInput;
        userInput = Input.string("Input party: ");

        // Set userInput to null if user did not enter anything
        if (userInput.isEmpty())
        {
            userInput = null;
        }
        return userInput;
    }

    /**
     *  Name:     getMarginLimit
     *  Purpose:  Prompt user for margin limit
     *  Imports:
     *    - none
     *  Exports:
     *    - marginLimit : A double for the user defined margin limit
     **/

    public static double getMarginLimit()
    {
        String userInput;
        double marginLimit;

        userInput = Input.string("Input margin [6.0]: ");

        // Set the default value for margin limit if user did not
        // input anything
        if (userInput.isEmpty())
        {
            marginLimit = 6.0;
        }
        else
        {
            // Attempt to convert from string to double
            try
            {
                marginLimit = Double.parseDouble(userInput);
            }
            catch (NumberFormatException e)
            {
                System.out.println(
                    "Margin is invalid. Using 6.0 instead"
                );
                marginLimit = 6.0;
            }
        }

        return marginLimit;
    }

    /**
     *  Name:     calcVotes
     *  Purpose:  Calculate votes in a division within a house preference
     *            and stores it into a linked list
     *  Imports:
     *    - prefList    : A list of House Preference objects
     *    - partyFilter : A string of the user defined party name
     *  Exports:
     *    - voteResultList : A linked list containing all the voting results
     **/

    public static DSALinkedList<VoteStats> calcVotes(
        DSALinkedList<HousePreference> prefList, String partyFilter)
    {
        DSALinkedList<VoteStats> voteResultList;
        VoteStats pollStats;

        Nominee tempNominee;
        HousePreference tempPref;

        Iterator<Nominee> iterNominee;
        Iterator<HousePreference> iterPref;

        iterPref = prefList.iterator();

        String nameDivision, abvParty, nameParty, state;
        int idDivision, votesFor, votesAgainst, votesTotal;
        double percent, margin;

        nameDivision = "";
        idDivision = 0;
        state = "";
        abvParty = "";
        nameParty = "";

        voteResultList = new DSALinkedList<VoteStats>();

        // For each house preference object
        while (iterPref.hasNext())
        {
            state = "";

            votesFor = 0;
            votesAgainst = 0;
            votesTotal = 0;

            tempPref = iterPref.next();
            iterNominee = tempPref.getListNominee().iterator();

            nameDivision = tempPref.getPrefNameDivision();
            idDivision = tempPref.getPrefIdDivision();

            // For each nominee in the house preference
            while (iterNominee.hasNext())
            {
                tempNominee = iterNominee.next();

                // Check if the nominee is in the user defined party
                if ((tempNominee.getAbvParty().matches(partyFilter)) ||
                    (tempNominee.getNameParty().matches(partyFilter)))
                {
                    // Calculate votes for that party
                    votesFor += tempNominee.getNumVotes();

                    // If these variables are not set, set them from
                    // the nominee object
                    if (abvParty.isEmpty() &&
                        nameParty.isEmpty())
                    {
                        abvParty = tempNominee.getAbvParty();
                        nameParty = tempNominee.getNameParty();
                    }

                    if (state.isEmpty())
                    {
                        state = tempNominee.getState();
                    }
                }
                else
                {
                    // Calculate votes for against that party
                    votesAgainst += tempNominee.getNumVotes();
                }
            }

            // Create VoteStat object with the calculated votes
            votesTotal += tempPref.getNumTotalVotes();
            pollStats = new VoteStats(nameDivision,
                                      Integer.toString(idDivision),
                                      state, abvParty, nameParty, votesFor,
                                      votesAgainst, votesTotal);

            // Add to the end of the vote result list
            voteResultList.insertLast(pollStats);
        }

        return voteResultList;
    }

    /**
     *  Name:     printItinerary
     *  Purpose:  Print the path of the itinerary
     *  Imports:
     *    - path         : The path for the itinerary
     *    - map          : A graph containing all locations connected by edges
     *    - partyFilter  : A string for the user defined party filter
     *    - marginLimit  : A double for the user defined margin limit
     *    - numLocations : The number of locations user has inputed
     *    - timeStart    : A long int for the start time of the algorithm
     *  Exports:
     *    - none
     **/

    public static void printItinerary(
        DSAStack<Location> path,
        DSAGraph<Location,Trip> map,
        String partyFilter,
        double marginLimit,
        int numLocations,
        long timeStart)
    {
        String[] fileContents;

        // Header for outputing to file
        String[] header = {
            "from_State", "from_Division", "from_Latitude",
            "from_Longitude", "to_State", "to_Division",
            "to_Latitude", "to_Longitude",
            "distance_metres", "time_hours_minutes",
            "trans_type"
        };

        String fromState, fromDivision, toState, toDivision, trans;
        double fromLat, fromLong, toLat, toLong;
        int distance, count, time, totalTime;
        long timeEnd, duration;

        Location from, to;
        Trip tripInfo;

        fileContents = new String[path.getCount() - 1];
        totalTime = numLocations * (3 * 60 * 60);

        from = path.pop();
        count = 0;

        // Get information from the starting point before the loop started
        fromState = from.getState();
        fromDivision = from.getDivision();
        fromLat = from.getLatitude();
        fromLong = from.getLongitude();

        // While the path stack is not empty and the count is within range
        while (! path.isEmpty() && count < fileContents.length)
        {
            // Get the next location
            to = path.pop();
            tripInfo = map.getEdgeValue(
                from.getDivision(),
                to.getDivision()
            );

            // Get information of the next location
            toState = to.getState();
            toDivision = to.getDivision();
            toLat = to.getLatitude();
            toLong = to.getLongitude();

            distance = tripInfo.getDistance();
            time = tripInfo.getDuration();
            trans = tripInfo.getTransportType();

            // Format csv line
            fileContents[count] = String.format(
                "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                fromState, fromDivision, fromLat, fromLong,
                toState, toDivision, toLat, toLong, distance,
                time, trans
            );

            // Calculate total time
            totalTime += time;
            count++;

            // Set next location to the current location for the next
            // iteration
            from = to;
            fromState = toState;
            fromDivision = toDivision;
            fromLat = toLat;
            fromLong = toLong;
        }

        // Time statistics
        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        // Print csv table and save to file
        printCsvTable(fileContents, header);

        System.out.printf(
            "Total Time: %s (%1.2f hours)\n",
            convertTimeToString(totalTime),
            (double)totalTime / (60 * 60)
        );

        System.out.printf("Took %sms\n\n", toMiliseconds(duration));

        saveCsvToFile(
            fileContents,
            String.format(
                "%s_%03.2f_margin_itinerary.csv",
                partyFilter,
                marginLimit
            )
        );
    }

    /**
     *  Name:     convertTimeToString
     *  Purpose:  Convert seconds to day, hours, minutes and seconds format
     *  Imports:
     *    - totalSecs : An integer for the total number of seconds
     *  Exports:
     *    - none
     **/

    public static String convertTimeToString(int totalSecs)
    {
        int days, hours, mins, secs;

        // Calculate days, hours, minutes and seconds
        days = (totalSecs / 3600) / 24;
        hours = (totalSecs / 3600) % 24;
        mins = (totalSecs / 60) % 60;
        secs = (totalSecs % 60) % 60;

        // Format the string
        return String.format("%dd %dh %dm %ds", days, hours, mins, secs);
    }

    /**
     *  Name:     printCsvTable
     *  Purpose:  Print a csv formatted string array into a table
     *  Imports:
     *    - csvArr : The csv array
     *    - header : The header of the csv table
     *  Exports:
     *    - none
     **/

    public static void printCsvTable(
        String[] csvArr,
        String[] header)
    {
        String padding, headerStr, sep, out;

        String[][] fields = new String[csvArr.length][header.length];
        String[] tempArr;
        String table;

        int[] paddingArr;

        fields = new String[csvArr.length][header.length];
        tempArr = new String[csvArr.length + 1];

        padding = "";
        headerStr = "";
        sep = "";
        out = "";

        paddingArr = new int[header.length];

        // Split csv into a 2D String array
        for (int i = 0; i < csvArr.length; i++)
        {
            fields[i] = csvArr[i].split(SPLIT_REGEX);
        }

        // Get the maximum string length for each column
        for (int i = 0; i < paddingArr.length; i++)
        {
            tempArr[0] = header[i];

            for (int j = 0; j < csvArr.length; j++)
            {
                tempArr[j + 1] = fields[j][i];
            }

            paddingArr[i] = calcMaxStringArrLenght(tempArr);
        }

        // Format the header
        for (int i = 0; i < paddingArr.length - 1; i++)
        {
            padding = "%-" + (paddingArr[i] + 2) + "s";
            sep += "+" + formatPadding(padding, '-');
            padding = "%-" + paddingArr[i] + "s";
            headerStr += "| " + String.format(padding, header[i]) + " ";
        }

        padding = "%-" + (paddingArr[paddingArr.length - 1] + 2) + "s";
        sep += "+" + formatPadding(padding, '-') + "+";
        padding = "%-" + paddingArr[paddingArr.length - 1] + "s";
        headerStr += "| " + String.format(padding, header[header.length - 1])
                  + " |";

        // Format each cell in the csv table
        for (int i = 0; i < fields.length; i++)
        {
            for (int j = 0; j < fields[i].length - 1; j++)
            {
                padding = "%-" + paddingArr[j] + "s";
                out += "| " + String.format(padding, fields[i][j]) + " ";
            }

            padding = "%-" + paddingArr[paddingArr.length - 1] + "s";
            out += "| " + 
                   String.format(padding, fields[i][fields[i].length - 1]) +
                  " |\n";
        }

        // Print table
        if (csvArr.length != 0)
        {
            table = String.format(
                "%s\n%s\n%s\n%s%s", sep, headerStr, sep, out, sep
            );

            System.out.println(table);
        }
    }

    /**
     *  Name:     calcMaxStringArrLenght
     *  Purpose:  Find the longest string in an array of string
     *  Imports:
     *    - arr : An array of string
     *  Exports:
     *    - maxLength : An integer for the longest string
     **/

    public static int calcMaxStringArrLenght(String[] arr)
    {
        int length, maxLength;
        length = 0;
        maxLength = 0;

        // For each element in the string array
        for (int i = 0; i < arr.length; i++)
        {
            length = arr[i].length();

            // Check if the current string's length is larger
            // than the current max length
            maxLength = (length > maxLength) ? length : maxLength;
        }

        return maxLength;
    }

    /**
     *  Name:     saveCsvToFile
     *  Purpose:  Wrapper function to get filename from user input
     *            and save csv array to a file
     *  Imports:
     *    - fileContents : A String array containing csv entries
     *    - defaultName  : A String for the default filename if user
     *                     did not enter anything
     *  Exports:
     *    - none
     **/

    public static void saveCsvToFile(
        String[] fileContents,
        String defaultName)
    {
        String userInput;

        // Prompt user for saving to file
        userInput = Input.string("Save report to file? [Y/n]: ");

        // If user selected yes or did not enter anything
        if ((userInput.matches("^[yY]$")) ||
            (userInput.isEmpty()))
        {
            // Get filename
            userInput = Input.string(
                String.format(
                    "Enter filename [%s]: ", defaultName
                )
            );

            if (userInput.isEmpty())
            {
                userInput = defaultName;
            }

            // Write file
            FileIO.writeText(userInput, fileContents);
        }
    }

    /**
     *  Name:     formatPadding
     *  Purpose:  Wrapper function for printing whitespace padding
     *  Imports:
     *    - padding : The format string
     *  Exports:
     *    - A string containing padding
     **/

    public static String formatPadding(String padding)
    {
        return String.format(padding, " ");
    }

    /**
     *  Name:     formatPadding
     *  Purpose:  An alternative function where instead of using
     *            whitespace, use a user specified character
     *  Imports:
     *    - padding : The format string
     *    - line    : The character to replace whitespace with
     *  Exports:
     *    - A string containing padding
     **/

    public static String formatPadding(String padding, char line)
    {
        return String.format(padding, " ").replace(' ', line);
    }

    /**
     *  Name:     generateLine
     *  Purpose:  Generates a line of length size
     *  Imports:
     *    - size : An int for the size of the line
     *  Exports:
     *    - A String line of length size
     **/

    public static String generateLine(int size)
    {
        return String.format("%" + size + "s", " ").replace(' ', '=');
    }

    /**
     *  Name:     isUnique
     *  Purpose:  Check if search is not in list
     *  Imports:
     *    - list   : The list to check
     *    - search : The search term to compare with
     *  Exports:
     *    - isUnique : A boolean
     **/

    public static boolean isUnique(DSALinkedList<String> list, String search)
    {
        Iterator<String> iter;
        String inList;
        boolean isUnique = true;

        iter = list.iterator();

        // While search is still unique and haven't
        // iterate through the list
        while (isUnique && iter.hasNext())
        {
            inList = iter.next();
            isUnique = ! search.equals(inList);
        }

        return isUnique;
    }

    /**
     *  Name:     compareIntString
     *  Purpose:  Wrapper function to compare a string int with an int
     *  Imports:
     *    - num : An integer to compare with
     *    - str : A String for an integer
     *  Exports:
     *    - Boolean
     **/

    public static boolean compareIntString(int num, String str)
    {
        return Integer.toString(num).equals(str);
    }

    /**
     *  Name:     toMiliseconds
     *  Purpose:  Convert nanoseconds to miliseconds
     *  Imports:
     *    - nano : A long for the amount of nanoseconds
     *  Exports:
     *    - The formatted string
     **/

    public static String toMiliseconds(long nano)
    {
        return String.format("%1.3f", (double)nano / 1000000);
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
