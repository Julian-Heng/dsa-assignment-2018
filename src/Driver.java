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
        DSAGraph<Location> locations;
        DSALinkedList<Nominee> nomineesList;
        DSALinkedList<HousePreference> preferenceList;
        DSALinkedList<HousePreference> tempPrefList;

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
            for (HousePreference inTemp : tempPrefList)
            {
                preferenceList.insertLast(inTemp);
            }
        }

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;
        System.out.println("Reading preference file took " +
                           toMiliseconds(duration) + "ms"
        );

        /*
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
                        System.out.println("Selected Itinerary by Margin");
                        break;
                    case 5:
                        System.out.println("Selected Exit");
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
        */
    }

    public static DSAGraph<Location> processDistances(String file)
    {
        DSALinkedList<String> list;
        DSAGraph<Location> locationGraph;
        Iterator<String> iter;
        Location startLocation, endLocation;

        long timeStart, timeEnd, funcDuration;
        timeStart = System.nanoTime();

        String spinner[] = {"\\", "|", "/", "-"};
        int count = 0;

        double distance;
        int tempTimeInt;
        String tempTimeStr;
        Time duration;
        String transportType;

        String[] split;

        list = FileIO.readText(file);
        System.out.print("Reading " + file + "... ");

        for (String line : list)
        {
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
                    distance = 0.0;
                }
                else
                {
                    distance = Double.parseDouble(split[8]);
                }

                if (split[9].contains(":"))
                {
                    duration = new Time(split[9]);
                }
                else if (split[9].equals("NONE"))
                {
                    duration = new Time("00:00:00");
                }
                else
                {
                    tempTimeInt = Integer.parseInt(split[9]);
                    tempTimeStr = String.format(
                            "%02d", tempTimeInt / 60 % 24
                        ) + ":" + String.format(
                            "%02d", tempTimeInt % 60
                        ) + ":" + String.format(
                            "%02d", ((tempTimeInt * 60) % 60) % 60
                        );
                    duration = new Time(tempTimeStr);
                }

                transportType = split[10];
            }
            catch (NumberFormatException e)
            {
                throw new IllegalArgumentException(
                    "Invalid Distance File: " + e.getMessage()
                );
            }
        }

        locationGraph = new DSAGraph<Location>();

        timeEnd = System.nanoTime();
        funcDuration = timeEnd - timeStart;

        System.out.println(toMiliseconds(funcDuration) + "ms");

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

        timeStart = System.nanoTime();

        String spinner[] = {"\\", "|", "/", "-"};
        int count = 0;

        list = FileIO.readText(file);
        iter = list.iterator();

        System.out.print("Reading " + file + "... ");

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
            System.out.println(
                "Invalid entry in " + file +
                ": " + iter.next()
            );
        }

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        System.out.println(toMiliseconds(duration) + "ms");

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

        Nominee tempNominee;
        HousePreference tempHousePref;
        String divisionId, divisionName;

        DSALinkedList<String> divisionIdList;
        DSALinkedList<String> uniqueDivisionId;

        DSALinkedList<String> stateList;
        DSALinkedList<String> uniqueStateList;

        long timeStart, timeEnd, duration;
        timeStart = System.nanoTime();

        String[] split;

        String spinner[] = {"\\", "|", "/", "-"};
        int count = 0;

        list = FileIO.readText(file);

        divisionIdList = new DSALinkedList<String>();
        uniqueDivisionId = new DSALinkedList<String>();

        System.out.print("Reading " + file + "... ");

        for (String i : list)
        {
            split = i.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            if (! split[1].isEmpty())
            {
                divisionIdList.insertLast(split[1]);
            }
        }

        for (String i : divisionIdList)
        {
            if (! i.equals("DivisionID") && isUnique(uniqueDivisionId, i))
            {
                uniqueDivisionId.insertLast(i);
            }
        }

        preferenceList = new DSALinkedList<HousePreference>();

        for (String i : uniqueDivisionId)
        {
            printSpinner(spinner, count++);

            tempNomineeList = getNomineeFromDivisionId(nomineeList, i);
            inList = getPreferenceFromDivisionId(list, i);

            split = inList.peekFirst().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            divisionName = split[2];
            divisionId = split[1];

            tempHousePref = new HousePreference(divisionName, divisionId);

            for (String inInList : inList)
            {
                split = inInList.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                if (! split[6].equals("Informal") &&
                    ! split[7].equals("Informal"))
                {

                    tempNominee = getNomineeFromCandidateId(
                        tempNomineeList, split[5]
                    );

                    try
                    {
                        tempNominee.setNumVotes(
                            Integer.toString(
                                tempNominee.getNumVotes() + Integer.parseInt(
                                    split[13]
                                )
                            )
                        );
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
                else
                {
                    try
                    {
                        tempHousePref.setNumInformalVotes(
                            Integer.toString(
                                tempHousePref.getNumInformalVotes() +
                                    Integer.parseInt(split[13])
                                )
                        );
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
            }

            for (Nominee inNomineeList : tempNomineeList)
            {
                tempHousePref.addNomineeToList(inNomineeList);
            }

            tempHousePref.updateTotalVotes();

            preferenceList.insertLast(tempHousePref);
        }

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        System.out.println(toMiliseconds(duration) + "ms");

        return preferenceList;
    }

    public static DSALinkedList<Nominee> getNomineeFromState(
        DSALinkedList<Nominee> nomineeList, String inState)
    {
        DSALinkedList<Nominee> returnList;
        returnList = new DSALinkedList<Nominee>();

        for (Nominee inList : nomineeList)
        {
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
        returnList = new DSALinkedList<Nominee>();

        for (Nominee inList : nomineeList)
        {
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
        String[] split;
        returnList = new DSALinkedList<String>();

        for (String inList : list)
        {
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

        DSAHeap heap;

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
        heap = new DSAHeap(numMatches);
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
            for (Nominee inResult : searchResult)
            {
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
        System.out.println("\n" + numMatches + "/" + numNominee + " matches");

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        System.out.println("Took " + toMiliseconds(duration) + "ms\n");

        userInput = Input.string("Save report to file? [Y/n]: ");

        if ((userInput.equals("Y")) ||
            (userInput.equals("y")) ||
            (userInput.isEmpty()))
        {
            userInput = Input.string("Enter filename: ");
            FileIO.writeText(userInput, fileContents);
        }
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
        System.out.println("\n" + numMatches + "/" + numNominee + " matches");

        timeEnd = System.nanoTime();
        duration = timeEnd - timeStart;

        System.out.println("Took " + toMiliseconds(duration) + "ms\n");

        saveCsvToFile(fileContents);
    }

    public static void processFilterOptions(boolean[] options, String[] input)
    {
        int optionSwitch;

        for (String option : input)
        {
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

        for (String choice : order)
        {
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
                        System.out.println(
                            "Invalid order option: " + choice + ". Ignoring"
                        );
                        break;
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println(
                    "Invalid order option: " + choice + ". Ignoring"
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
        String header, line;
        DSALinkedList<String> fileList;
        String fileContents[];
        String csvLine;

        int count;

        String userInput;
        String partyFilter;
        double marginLimit;

        long timeStart, timeEnd, duration;

        count = 0;

        csvLine = "";
        partyFilter = "";

        userInput = Input.string("Input party: ");

        if (userInput.isEmpty())
        {
            System.out.println(
                "No party found: Empty search field"
            );
        }
        else
        {
            partyFilter = userInput;

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

            timeStart = System.nanoTime();

            voteResults = calcVotes(prefList, partyFilter);
            fileList = new DSALinkedList<String>();

            for (VoteStats stats : voteResults)
            {
                if (Math.abs(stats.getMargin()) < marginLimit)
                {
                    count++;
                    csvLine = count + "," + stats.toString();
                    fileList.insertLast(csvLine);
                }
            }

            if (count != 0)
            {
                fileContents = new String[count];
                count = 0;

                for (String i : fileList)
                {
                    fileContents[count] = i;
                    count++;
                }

                printCsvTable(fileContents, headerFile);

                timeEnd = System.nanoTime();
                duration = timeEnd - timeStart;

                System.out.println("Took " + toMiliseconds(duration) + "ms\n");

                saveCsvToFile(fileContents);
            }
        }
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
                        nameParty.isEmpty() &&
                        state.isEmpty())
                    {
                        abvParty = tempNominee.getAbvParty();
                        nameParty = tempNominee.getNameParty();
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

    public static void printCsvTable(String[] csvArr, String[] header)
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
            table = sep + "\n" + headerStr + "\n" + sep + "\n" +
                    out + sep;

            System.out.println(table);
        }

    }

    public static int calcMaxStringArrLenght(String[] arr)
    {
        int length, maxLength;
        length = 0;
        maxLength = 0;

        for (String i : arr)
        {
            length = i.length();
            if (length > maxLength)
            {
                maxLength = length;
            }
        }

        return maxLength;
    }

    public static void saveCsvToFile(String[] fileContents)
    {
        String userInput;

        userInput = Input.string("Save report to file? [Y/n]: ");

        if ((userInput.equals("Y")) ||
            (userInput.equals("y")) ||
            (userInput.isEmpty()))
        {
            userInput = Input.string("Enter filename: ");
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
        System.out.print(pool[step % pool.length] + "\u0008");
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

        for (String inMsg : msg)
        {
            System.out.println(inMsg);
        }
    }
}
