import java.util.*;

public class Driver
{
    public static void main(String[] args)
    {
        if (args.length < 3 ||
            args[0].equals("--help") ||
            args[0].equals("-h"))
        {
            printHelp();
        }
        else
        {
            try
            {
                menu(args);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Exiting...");
            }
        }
    }

    public static void menu(String[] files)
    {
        Menu menu;
        boolean exit;
        long timeStart, timeEnd, duration;
        DSAGraph<Location,Trip> locations;
        DSALinkedList<Nominee> nomineesList;
        DSALinkedList<HousePreference> preferenceList;
        DSALinkedList<HousePreference> tempPrefList;
        Iterator<HousePreference> iter;

        menu = new Menu(6);
        menu.addOption("Please select an option:");
        menu.addOption("    1. List Nominees");
        menu.addOption("    2. Nominee Search");
        menu.addOption("    3. List by Margin");
        menu.addOption("    4. Itinerary by Margin");
        menu.addOption("    5. Exit");
        exit = false;

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
                System.out.println(e.getMessage());
            }
        }
    }

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
        int count = 0;

        Trip tripInfo;

        int distance;
        int duration;
        String transportType;

        String[] split;
        String[] timeSplit;

        timeStart = System.nanoTime();
        list = FileIO.readText(file);
        iter = list.iterator();

        System.out.printf("Processing %s... ", file);

        locationGraph = new DSAGraph<Location,Trip>();

        while (iter.hasNext())
        {
            line = iter.next();
            printSpinner(spinner, count++);

            split = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

            startLocation = new Location(split[0], split[1],
                                         split[2], split[3]);
            endLocation = new Location(split[4], split[5],
                                       split[6], split[7]);

            try
            {
                if (split[8].equals("NONE"))
                {
                    distance = 0;
                }
                else
                {
                    distance = Integer.parseInt(split[8]);
                }

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

                transportType = split[10];
                tripInfo = new Trip(transportType, distance, duration);

                locationGraph.addVertex(
                    startLocation.getDivision(),
                    startLocation
                );

                locationGraph.addVertex(
                    endLocation.getDivision(),
                    endLocation
                );

                locationGraph.addEdge(
                    startLocation.getDivision(),
                    endLocation.getDivision(),
                    tripInfo.getDuration(),
                    tripInfo
                );
            }
            catch (NumberFormatException e)
            {
                throw new IllegalArgumentException(
                    "Invalid Distance File: " + e.getMessage()
                );
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException(
                    "Error: " + e.getMessage()
                );
            }
        }

        timeEnd = System.nanoTime();
        funcDuration = timeEnd - timeStart;

        System.out.printf("%sms\n", toMiliseconds(funcDuration));

        return locationGraph;
    }

    public static DSALinkedList<Nominee> processNominees(String file)
    {
        DSALinkedList<String> list;
        DSALinkedList<Nominee> nomineeList;
        Iterator<String> iter;
        DSALinkedList<String> invalidEntries;
        String line;

        long timeStart, timeEnd, duration;

        String spinner[] = {"\\", "|", "/", "-"};
        int count = 0;

        timeStart = System.nanoTime();
        list = FileIO.readText(file);
        iter = list.iterator();

        System.out.printf("Processing %s... ", file);

        nomineeList = new DSALinkedList<Nominee>();
        invalidEntries = new DSALinkedList<String>();
        line = "";

        if (iter.hasNext())
        {
            iter.next();

            while (iter.hasNext())
            {
                printSpinner(spinner, count++);
                try
                {
                    line = iter.next();
                    nomineeList.insertLast(new Nominee(line));
                }
                catch (IllegalArgumentException e)
                {
                    invalidEntries.insertLast(line);
                }
            }
        }
        else
        {
            throw new IllegalArgumentException(
                "House candidate file is invalid"
            );
        }

        iter = invalidEntries.iterator();

        while (iter.hasNext())
        {
            System.out.printf(
                "Invalid entry in %s: %s, ignoring\n",
                file, iter.next()
            );
        }

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        System.out.printf("%sms\n", toMiliseconds(duration));

        return nomineeList;
    }

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
        int count = 0;

        timeStart = System.nanoTime();
        list = FileIO.readText(file);
        iter = list.iterator();

        divisionIdList = new DSALinkedList<String>();

        System.out.printf("Processing %s... ", file);

        while (iter.hasNext())
        {
            line = iter.next();
            split = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            if (! split[1].isEmpty() &&
                ! split[1].equals("DivisionID") &&
                isUnique(divisionIdList, split[1]))
            {
                divisionIdList.insertLast(split[1]);
            }
        }

        preferenceList = new DSALinkedList<HousePreference>();
        iter = divisionIdList.iterator();

        while (iter.hasNext())
        {
            line = iter.next();
            printSpinner(spinner, count++);

            tempNomineeList = getNomineeFromDivisionId(nomineeList, line);
            inList = getPreferenceFromDivisionId(list, line);

            split = inList.peekFirst().split(
                ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"
            );

            divisionName = split[2];
            divisionId = split[1];

            tempHousePref = new HousePreference(divisionName, divisionId);

            iter2 = inList.iterator();
            while (iter2.hasNext())
            {
                split = iter2.next().split(
                    ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"
                );

                try
                {
                    if (! split[6].equals("Informal") &&
                        ! split[7].equals("Informal"))
                    {

                        tempNominee = getNomineeFromCandidateId(
                            tempNomineeList, split[5]
                        );

                        tempNominee.setNumVotes(
                            Integer.toString(
                                tempNominee.getNumVotes() + Integer.parseInt(
                                    split[13]
                                )
                            )
                        );
                    }
                    else
                    {
                        tempHousePref.setNumInformalVotes(
                            Integer.toString(
                                tempHousePref.getNumInformalVotes() +
                                    Integer.parseInt(split[13])
                                )
                        );
                    }
                }
                catch (NumberFormatException e)
                {
                    throw new IllegalArgumentException(
                        "Invalid Number of Votes: " + split[13]
                    );
                }
                catch (ArrayIndexOutOfBoundsException e)
                {
                    throw new IllegalArgumentException(
                        "Invalid House Preference File"
                    );
                }
            }

            iterNominee = tempNomineeList.iterator();
            while (iterNominee.hasNext())
            {
                tempHousePref.addNomineeToList(iterNominee.next());
            }

            tempHousePref.updateTotalVotes();
            preferenceList.insertLast(tempHousePref);
        }

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        System.out.printf("%sms\n", toMiliseconds(duration));

        return preferenceList;
    }

    public static DSALinkedList<Nominee> getNomineeFromState(
        DSALinkedList<Nominee> nomineeList, String inState)
    {
        DSALinkedList<Nominee> returnList;
        Iterator<Nominee> iter;
        Nominee inList;

        returnList = new DSALinkedList<Nominee>();
        iter = nomineeList.iterator();

        while (iter.hasNext())
        {
            inList = iter.next();
            if (inList.getState().equals(inState))
            {
                returnList.insertLast(inList);
            }
        }

        return returnList;
    }

    public static DSALinkedList<Nominee> getNomineeFromDivisionId(
        DSALinkedList<Nominee> nomineeList, String inDivisionId)
    {
        DSALinkedList<Nominee> returnList;
        Iterator<Nominee> iter;
        Nominee inList;
        returnList = new DSALinkedList<Nominee>();

        iter = nomineeList.iterator();

        while (iter.hasNext())
        {
            inList = iter.next();
            if (compareIntString(inList.getIdDivision(), inDivisionId))
            {
                returnList.insertLast(inList);
            }
        }

        return returnList;
    }

    public static Nominee getNomineeFromCandidateId(
        DSALinkedList<Nominee> nomineeList, String inCandidateId)
    {
        Nominee inList = null;
        Nominee result = null;
        Iterator<Nominee> iter = nomineeList.iterator();
        boolean isFound = false;

        while (! isFound && iter.hasNext())
        {
            inList = iter.next();
            if (compareIntString(inList.getIdCandidate(), inCandidateId))
            {
                result = inList;
                isFound = true;
            }
        }

        return result;
    }

    public static DSALinkedList<String> getPreferenceFromDivisionId(
        DSALinkedList<String> list, String inDivisionId)
    {
        DSALinkedList<String> returnList;
        Iterator<String> iter;
        String inList;
        String[] split;
        returnList = new DSALinkedList<String>();

        iter = list.iterator();
        while (iter.hasNext())
        {
            inList = iter.next();
            split = inList.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            if (! split[1].isEmpty() && split[1].equals(inDivisionId))
            {
                returnList.insertLast(inList);
            }
        }

        return returnList;
    }

    public static void listNominees(DSALinkedList<Nominee> nomineeList)
    {
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

        boolean filterOptions[] = {false, false, false};
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
        split = userInput.split(" ");

        if (! userInput.isEmpty())
        {
            processFilterOptions(filterOptions, split);
        }

        for (i = 0; i < filters.length; i++)
        {
            filters[i] = filterOptions[i] ? getFilter(filterMsg[i]) : ".*";
        }

        orderMenu.printMenu();
        userInput = Input.string();
        split = userInput.split(" ");

        timeStart = System.nanoTime();

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

        while (iter.hasNext())
        {
            inList = iter.next();

            if (searchNominee1(inList, filters))
            {
                searchResult[i] = inList;
                i++;
            }
        }

        if (! userInput.isEmpty())
        {
            for (int j = 0; j < searchResult.length; j++)
            {
                inResult = searchResult[j];
                sortLine = generateSortLine(inResult, split);
                heap.add(sortLine, inResult);
            }

            heap.heapSort();
            sorted = heap.toObjArray();

            for (int j = 0; j < numMatches; j++)
            {
                searchResult[j] = (Nominee)sorted[j];
            }
        }

        fileContents = new String[numMatches];

        for (i = 0; i < numMatches; i++)
        {
            fileContents[i] = searchResult[i].toString();
        }

        printCsvTable(fileContents, header);
        System.out.printf("\n%d/%d matches\n", numMatches, numNominee);

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        System.out.printf("Took %sms\n\n", toMiliseconds(duration));

        saveCsvToFile(fileContents, "nominees_list.csv");
    }

    public static void searchNominees(DSALinkedList<Nominee> nomineeList)
    {
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

        boolean filterOptions[] = {false, false};
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

        filterMenu = new Menu(3);

        filterMenu.addOption("Select filter type (eg: 1 2 3):");
        filterMenu.addOption("    1. State");
        filterMenu.addOption("    2. Party");

        nameFilter = Input.string("Input surname search term: ");

        filterMenu.printMenu();
        userInput = Input.string();
        split = userInput.split(" ");

        if (! userInput.isEmpty())
        {
            processFilterOptions(filterOptions, split);
        }

        for (i = 0; i < filters.length; i++)
        {
            filters[i] = filterOptions[i] ? getFilter(filterMsg[i]) : ".*";
        }

        while (iter.hasNext())
        {
            if (searchNominee2(iter.next(), nameFilter, filters))
            {
                numMatches++;
            }
        }

        timeStart = System.nanoTime();

        searchResult = new Nominee[numMatches];
        iter = null;
        inList = null;
        iter = nomineeList.iterator();
        i = 0;

        while (iter.hasNext())
        {
            numNominee++;
            inList = iter.next();

            if (searchNominee2(inList, nameFilter, filters))
            {
                searchResult[i] = inList;
                i++;
            }
        }

        fileContents = new String[numMatches];

        for (i = 0; i < numMatches; i++)
        {
            fileContents[i] = searchResult[i].toString();
        }

        printCsvTable(fileContents, header);
        System.out.printf("\n%d/%d matches\n", numMatches, numNominee);

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        System.out.printf("Took %sms\n\n", toMiliseconds(duration));

        saveCsvToFile(fileContents, "nominees_search.csv");
    }

    public static void processFilterOptions(boolean[] options, String[] input)
    {
        String option;
        int optionSwitch;

        for (int i = 0; i < input.length; i++)
        {
            option = input[i];
            try
            {
                optionSwitch = Integer.parseInt(option);
                if (optionSwitch > 0 && optionSwitch <= options.length)
                {
                    options[optionSwitch - 1] = true;
                }
                else
                {
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

    public static String getFilter(String msg)
    {
        String userInput;
        userInput = Input.string(msg);
        return ! userInput.isEmpty() ? userInput : ".*";
    }

    public static boolean searchNominee1(Nominee inNominee, String[] filters)
    {
        return (inNominee.getState().matches(filters[0])) &&
               ((inNominee.getNameParty().matches(filters[1])) ||
                (inNominee.getAbvParty().matches(filters[1]))) &&
               ((inNominee.getNameDivision().matches(filters[2])) ||
                (Integer.toString(inNominee.getIdDivision()).matches(
                    filters[2]
                )));
    }

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

    public static String generateSortLine(Nominee inNominee, String[] order)
    {
        String sortLine = "";
        String choice;

        for (int i = 0; i < order.length; i++)
        {
            choice = order[i];
            try
            {
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

    public static void listPartyMargin(
        DSALinkedList<HousePreference> prefList)
    {
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

        if ((partyFilter = getPartyFilter()) == null)
        {
            throw new IllegalArgumentException(
                "No party found: Empty search field"
            );
        }

        marginLimit = getMarginLimit();

        timeStart = System.nanoTime();
        voteResults = calcVotes(prefList, partyFilter);
        fileList = new DSALinkedList<String>();

        iter = voteResults.iterator();

        while (iter.hasNext())
        {
            total++;
            inStats = iter.next();
            if (Math.abs(inStats.getMargin()) < marginLimit)
            {
                count++;
                csvLine = count + "," + inStats.toString();
                fileList.insertLast(csvLine);
            }
        }

        if (count == 0)
        {
            System.out.println("No party found.");
        }
        else
        {
            fileContents = new String[count];
            count = 0;

            iter2 = fileList.iterator();
            while (iter2.hasNext())
            {
                fileContents[count] = iter2.next();
                count++;
            }

            printCsvTable(fileContents, headerFile);

            System.out.printf("\n%d/%d matches\n", count, total);

            timeEnd = System.nanoTime();
            duration = timeEnd - timeStart;

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

    public static void createItinerary(
        DSALinkedList<HousePreference> prefList,
        DSAGraph<Location,Trip> map)
    {
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

        DSAStack<Location> path;
        DSAStack<Location> tempStack;

        long timeStart, timeEnd, duration;

        count = 0;
        total = 0;

        csvLine = "";
        partyFilter = "";

        if ((partyFilter = getPartyFilter()) == null)
        {
            throw new IllegalArgumentException(
                "No party found: Empty search field"
            );
        }

        marginLimit = getMarginLimit();

        timeStart = System.nanoTime();
        voteResults = calcVotes(prefList, partyFilter);
        fileList = new DSALinkedList<String>();

        iter = voteResults.iterator();

        while (iter.hasNext())
        {
            total++;
            inStats = iter.next();
            if (Math.abs(inStats.getMargin()) < marginLimit)
            {
                count++;
                csvLine = count + "," + inStats.toString();
                fileList.insertLast(csvLine);
            }
        }

        fileContents = new String[count];

        if (count == 0)
        {
            throw new IllegalArgumentException(
                "No party found."
            );
        }

        fileContents = new String[count];
        count = 0;

        iter2 = fileList.iterator();
        while (iter2.hasNext())
        {
            fileContents[count] = iter2.next();
            count++;
        }

        printCsvTable(fileContents, headerFile);

        System.out.printf("\n%d/%d matches\n", count, total);

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        System.out.printf("Took %sms\n\n", toMiliseconds(duration));

        if (count == 0)
        {
            throw new IllegalArgumentException(
                "No party found: Search field doesn't match anything"
            );
        }

        userInput = Input.string(
            "Input Division indexes to visit (eg: 1 2 3): "
        );

        if (userInput.isEmpty())
        {
            System.out.println(
                "No locations selected, using all locations"
            );

            visitDivisionIndex = new int[count];

            for (int i = 1; i <= count; i++)
            {
                visitDivisionIndex[i - 1] = i;
            }
        }
        else
        {
            split = userInput.split(" ");
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

        timeStart = System.nanoTime();
        count = 0;
        visitDivision = new String[visitDivisionIndex.length];

        for (int i = 0; i < visitDivisionIndex.length; i++)
        {
            split = fileContents[visitDivisionIndex[i] - 1].split(
                ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"
            );

            visitDivision[count] = split[2];
            count++;
        }

        path = new DSAStack<Location>();
        path.push(map.getVertexValue(visitDivision[0]));

        for (int i = 1; i < visitDivision.length; i++)
        {
            tempStack = map.dijkstra(
                visitDivision[i - 1],
                visitDivision[i]
            );

            while (! tempStack.isEmpty())
            {
                path.push(tempStack.pop());
            }
        }

        tempStack = path;
        path = new DSAStack<Location>();

        while (! tempStack.isEmpty())
        {
            path.push(tempStack.pop());
        }

        printItinerary(
            path,
            map,
            partyFilter,
            marginLimit,
            visitDivision.length,
            timeStart
        );
    }

    public static String getPartyFilter()
    {
        String userInput;
        userInput = Input.string("Input party: ");
        if (userInput.isEmpty())
        {
            userInput = null;
        }
        return userInput;
    }

    public static double getMarginLimit()
    {
        String userInput;
        double marginLimit;

        userInput = Input.string("Input margin [6.0]: ");

        if (userInput.isEmpty())
        {
            marginLimit = 6.0;
        }
        else
        {
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

            while (iterNominee.hasNext())
            {
                tempNominee = iterNominee.next();

                if ((tempNominee.getAbvParty().matches(partyFilter)) ||
                    (tempNominee.getNameParty().matches(partyFilter)))
                {
                    votesFor += tempNominee.getNumVotes();

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
                    votesAgainst += tempNominee.getNumVotes();
                }
            }

            votesTotal += tempPref.getNumTotalVotes();
            pollStats = new VoteStats(nameDivision,
                                      Integer.toString(idDivision),
                                      state, abvParty, nameParty, votesFor,
                                      votesAgainst, votesTotal);

            voteResultList.insertLast(pollStats);
        }

        return voteResultList;
    }

    public static void printItinerary(
        DSAStack<Location> path,
        DSAGraph<Location,Trip> map,
        String partyFilter,
        double marginLimit,
        int numLocations,
        long timeStart)
    {
        String[] fileContents;
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
        totalTime = numLocations * 10800;

        from = path.pop();
        count = 0;

        fromState = from.getState();
        fromDivision = from.getDivision();
        fromLat = from.getLatitude();
        fromLong = from.getLongitude();

        while (! path.isEmpty() && count < fileContents.length)
        {
            to = path.pop();
            tripInfo = map.getEdgeValue(
                from.getDivision(),
                to.getDivision()
            );

            toState = to.getState();
            toDivision = to.getDivision();
            toLat = to.getLatitude();
            toLong = to.getLongitude();

            distance = tripInfo.getDistance();
            time = tripInfo.getDuration();
            trans = tripInfo.getTransportType();

            fileContents[count] = String.format(
                "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                fromState, fromDivision, fromLat, fromLong,
                toState, toDivision, toLat, toLong, distance,
                time, trans
            );

            totalTime += time;
            count++;

            from = to;
            fromState = toState;
            fromDivision = toDivision;
            fromLat = toLat;
            fromLong = toLong;
        }

        printCsvTable(fileContents, header);

        System.out.printf(
            "Total Time: %s (%1.2f hours)\n",
            convertTimeToString(totalTime),
            (double)totalTime / 3600
        );

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

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

    public static String convertTimeToString(int totalSecs)
    {
        int days, hours, mins, secs;

        days = (totalSecs / 3600) / 24;
        hours = (totalSecs / 3600) % 24;
        mins = (totalSecs / 60) % 60;
        secs = (totalSecs % 60) % 60;

        return String.format("%dd %dh %dm %ds", days, hours, mins, secs);
    }

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

        for (int i = 0; i < csvArr.length; i++)
        {
            fields[i] = csvArr[i].split(
                ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"
            );
        }

        for (int i = 0; i < paddingArr.length; i++)
        {
            tempArr[0] = header[i];

            for (int j = 0; j < csvArr.length; j++)
            {
                tempArr[j + 1] = fields[j][i];
            }

            paddingArr[i] = calcMaxStringArrLenght(tempArr);
        }

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

        if (csvArr.length != 0)
        {
            table = String.format(
                "%s\n%s\n%s\n%s%s", sep, headerStr, sep, out, sep
            );

            System.out.println(table);
        }
    }

    public static int calcMaxStringArrLenght(String[] arr)
    {
        int length, maxLength;
        length = 0;
        maxLength = 0;

        for (int i = 0; i < arr.length; i++)
        {
            length = arr[i].length();
            maxLength = (length > maxLength) ? length : maxLength;
        }

        return maxLength;
    }

    public static void saveCsvToFile(
        String[] fileContents,
        String defaultName)
    {
        String userInput;

        userInput = Input.string("Save report to file? [Y/n]: ");

        if ((userInput.matches("^[yY]$")) ||
            (userInput.isEmpty()))
        {
            userInput = Input.string(
                String.format(
                    "Enter filename [%s]: ", "Enter filename", defaultName
                )
            );

            if (userInput.isEmpty())
            {
                userInput = defaultName;
            }

            FileIO.writeText(userInput, fileContents);
        }
    }

    public static String formatPadding(String padding)
    {
        return String.format(padding, " ");
    }

    public static String formatPadding(String padding, char line)
    {
        return String.format(padding, " ").replace(' ', line);
    }

    public static String generateLine(int size)
    {
        return String.format("%" + size + "s", " ").replace(' ', '=');
    }

    public static boolean isUnique(DSALinkedList<String> list, String search)
    {
        Iterator<String> iter;
        String inList;
        boolean isUnique = true;

        iter = list.iterator();

        while (isUnique && iter.hasNext())
        {
            inList = iter.next();
            isUnique = ! search.equals(inList);
        }

        return isUnique;
    }

    public static boolean compareIntString(int num, String str)
    {
        return Integer.toString(num).equals(str);
    }

    public static void printSpinner(String[] pool, int step)
    {
        System.out.printf("%s\u0008", pool[step % pool.length]);
    }

    public static String toMiliseconds(long nano)
    {
        return String.format("%1.3f", (double)nano / 1000000);
    }

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
