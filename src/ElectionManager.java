import java.util.*;

/**
 *  Name:     ElectionManager
 *  Purpose:  Manage election by viewing stats and calculations
 *
 *  Author:   Julian Heng (19473701)
 **/

public class ElectionManager
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
        Nominee inList;
        Nominee inResult;
        DSALinkedList<Nominee> searchResult;
        DSALinkedList<Object> sorted;
        Iterator<Nominee> iter;
        Iterator<Object> iter2;
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
        searchResult = new DSALinkedList<Nominee>();

        // Insert any matches to a linked list
        while (iter.hasNext())
        {
            inList = iter.next();
            if (searchNominee1(inList, filters))
            {
                searchResult.insertLast(inList);
            }
        }

        // If order options were selected
        if (! userInput.isEmpty())
        {
            // For each search result, generate the sort line
            // and add to the heap as the priority
            heap = new DSAMaxHeap(searchResult.getCount());
            iter = searchResult.iterator();

            while (iter.hasNext())
            {
                inResult = iter.next();
                sortLine = generateSortLine(inResult, split);
                heap.add(sortLine, inResult);
            }

            // Sort
            heap.heapSort();
            sorted = heap.toObjList();

            iter2 = sorted.iterator();
            searchResult = new DSALinkedList<Nominee>();

            while (iter2.hasNext())
            {
                searchResult.insertLast((Nominee)iter2.next());
            }
        }

        // Convert the list to a csv format
        fileContents = new String[searchResult.getCount()];

        i = 0;
        iter = searchResult.iterator();

        while (iter.hasNext())
        {
            fileContents[i++] = iter.next().toString();
        }

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        // Print table and prompt for saving the results as a file
        Commons.printCsvTable(fileContents, header);

        System.out.printf(
                "\n%d/%d matches\nTook %sms\n\n",
                searchResult.getCount(),
                nomineeList.getCount(),
                Commons.toMiliseconds(duration)
        );

        Commons.saveCsvToFile(fileContents, "nominees_list.csv");
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
        Nominee inList;
        DSALinkedList<Nominee> searchResult;
        Iterator<Nominee> iter;
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

        split = userInput.split(WHITESPACE_REGEX);
        timeStart = System.nanoTime();

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

        searchResult = new DSALinkedList<Nominee>();

        // Get matches to linked list
        while (iter.hasNext())
        {
            inList = iter.next();
            if (searchNominee2(inList, nameFilter, filters))
            {
                searchResult.insertLast(inList);
            }
        }

        fileContents = new String[searchResult.getCount()];

        // Convert to csv file
        iter = searchResult.iterator();
        i = 0;

        while (iter.hasNext())
        {
            fileContents[i++] = iter.next().toString();
        }

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        // Print table and prompt to save to file
        Commons.printCsvTable(fileContents, header);

        System.out.printf(
            "\n%d/%d matches\nTook %sms\n\n",
            searchResult.getCount(),
            nomineeList.getCount(),
            Commons.toMiliseconds(duration)
        );

        Commons.saveCsvToFile(fileContents, "nominees_search.csv");
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
            Commons.printCsvTable(fileContents, headerFile);

            System.out.printf("\n%d/%d matches\n", count, total);
            System.out.printf("Took %sms\n\n", Commons.toMiliseconds(duration));

            Commons.saveCsvToFile(
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

        // Some recycled code from listPartyMargin
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

        DSALinkedList<Location> path;
        DSALinkedList<Location> tempStack;

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
        Commons.printCsvTable(fileContents, headerFile);
        System.out.printf("\n%d/%d matches\n", count, total);
        System.out.printf("Took %sms\n\n", Commons.toMiliseconds(duration));

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
        try
        {
            for (int i = 0; i < visitDivisionIndex.length; i++)
            {
                split = fileContents[visitDivisionIndex[i] - 1].split(
                    SPLIT_REGEX
                );

                visitDivision[count] = split[2];
                count++;
            }

            path = new DSALinkedList<Location>();
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            throw new IllegalArgumentException(
                "Inputed Division index is invalid"
            );
        }

        // Insert starting location to path
        path.insertLast(map.getVertexValue(visitDivision[0]));

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
                path.insertLast(tempStack.removeLast());
            }
        }

        // Reverse the stack
        tempStack = path;
        path = new DSALinkedList<Location>();

        while (! tempStack.isEmpty())
        {
            path.insertLast(tempStack.removeLast());
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
        DSALinkedList<Location> path,
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

        from = path.removeLast();
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
            to = path.removeLast();
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
        Commons.printCsvTable(fileContents, header);

        System.out.printf(
            "Total Time: %s (%1.2f hours)\n",
            Commons.convertTimeToString(totalTime),
            (double)totalTime / (60 * 60)
        );

        System.out.printf("Took %sms\n\n", Commons.toMiliseconds(duration));

        Commons.saveCsvToFile(
            fileContents,
            String.format(
                "%s_%03.2f_margin_itinerary.csv",
                partyFilter,
                marginLimit
            )
        );
    }
}
