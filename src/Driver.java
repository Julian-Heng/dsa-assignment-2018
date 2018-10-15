import java.util.*;

public class Driver
{
    public static void main(String[] args)
    {
        if (args.length < 2 ||
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
        DSALinkedList<Nominee> nomineesList;
        DSALinkedList<HousePreference> preferenceList;

        menu = new Menu(6);
        menu.addOption("Please select an option:");
        menu.addOption("    1. List Nominees");
        menu.addOption("    2. Nominee Search");
        menu.addOption("    3. List by Margin");
        menu.addOption("    4. Itinerary by Margin");
        menu.addOption("    5. Exit");
        exit = false;

        nomineesList = processNominees(files[0]);
        preferenceList = processPreference(files[1], nomineesList);

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
                        //System.out.println("Selected List by Margin");
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
    }

    public static DSALinkedList<Nominee> processNominees(String file)
    {
        DSALinkedList<String> list;
        DSALinkedList<Nominee> nomineeList;
        Iterator<String> iter;
        DSALinkedList<String> invalidEntries;
        String line;

        String spinner[] = {"\\", "\\", "|", "|", "/", "/", "-", "-"};
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

        System.out.print("\n");

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
        String[] split;

        String spinner[] = {"\\", "\\", "|", "|", "/", "/", "-", "-"};
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
            tempNomineeList = getNomineeFromDivisionId(nomineeList, i);
            inList = getPreferenceFromDivisionId(list, i);

            split = inList.peekFirst().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            divisionName = split[2];
            divisionId = split[1];

            tempHousePref = new HousePreference(divisionName, divisionId);

            for (String inInList : inList)
            {
                printSpinner(spinner, count++);

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
                }
            }

            for (Nominee inNomineeList : tempNomineeList)
            {
                tempHousePref.addNomineeToList(inNomineeList);
            }

            tempHousePref.updateTotalVotes();

            preferenceList.insertLast(tempHousePref);
        }

        System.out.print("\n");

        return preferenceList;
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
        Menu filterMenu;
        Menu orderMenu;
        Iterator<Nominee> iter;
        Nominee inList;
        Nominee searchResult[];
        Object sorted[];

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

        printNomineesTable(searchResult);
        System.out.println("\n" + numMatches + "/" + numNominee + " matches");

        userInput = Input.string("Save report to file? [Y/n]: ");

        if ((userInput.equals("Y")) ||
            (userInput.equals("y")) ||
            (userInput.isEmpty()))
        {
            fileContents = new String[numMatches];

            for (i = 0; i < numMatches; i++)
            {
                fileContents[i] = searchResult[i].toString();
            }

            userInput = Input.string("Enter filename: ");
            FileIO.writeText(userInput, fileContents);
        }
    }

    public static void searchNominees(DSALinkedList<Nominee> nomineeList)
    {
        Menu filterMenu;
        Iterator<Nominee> iter;
        Nominee inList;
        Nominee searchResult[];

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

        printNomineesTable(searchResult);
        System.out.println("\n" + numMatches + "/" + numNominee + " matches");

        userInput = Input.string("Save report to file? [Y/n]: ");

        if ((userInput.equals("Y")) ||
            (userInput.equals("y")) ||
            (userInput.isEmpty()))
        {
            fileContents = new String[numMatches];

            for (i = 0; i < numMatches; i++)
            {
                fileContents[i] = searchResult[i].toString();
            }

            userInput = Input.string("Enter filename: ");
            FileIO.writeText(userInput, fileContents);
        }
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
                        sortLine += inNominee.getSurname() + "|";
                        break;
                    case 2:
                        sortLine += inNominee.getState() + "|";
                        break;
                    case 3:
                        sortLine += inNominee.getNameParty() + "|";
                        break;
                    case 4:
                        sortLine += inNominee.getNameDivision() + "|";
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
        HousePreference tempPref;
        Nominee tempNominee;
        Iterator<HousePreference> iterPref;
        Iterator<Nominee> iterNominee;
        HousePreference prefDivision;

        String header, line;

        String userInput;
        String partyFilter = "";
        double marginLimit;

        int count, numMatches;
        int votesFor, votesAgainst, votesTotal;
        double percent, margin;

        tempPref = null;
        tempNominee = null;
        iterPref = null;
        iterNominee = null;
        prefDivision = null;

        count = 0;
        numMatches = 0;

        votesFor = 0;
        votesAgainst = 0;
        votesTotal = 0;

        percent = 0.0;
        margin = 0.0;

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

            iterPref = prefList.iterator();

            while (iterPref.hasNext())
            {
                votesFor = 0;
                votesAgainst = 0;
                votesTotal = 0;
                count++;

                tempPref = iterPref.next();
                iterNominee = tempPref.getListNominee().iterator();

                while (iterNominee.hasNext())
                {
                    tempNominee = iterNominee.next();

                    if ((tempNominee.getAbvParty().matches(partyFilter)) ||
                        (tempNominee.getNameParty().matches(partyFilter)))
                    {
                        numMatches++;
                        votesFor += tempNominee.getNumVotes();
                    }
                    else
                    {
                        votesAgainst += tempNominee.getNumVotes();
                    }
                }

                votesTotal += tempPref.getNumTotalVotes();

                percent = ((double)votesFor / (double)votesTotal) * 100;
                margin = percent - 50.0;

                if (Math.abs(margin) < marginLimit)
                {
                    header = " " + count + " | " +
                             "Division: " + tempPref.getPrefNameDivision() +
                             " | ID: " + tempPref.getPrefIdDivision();
                    line = generateLine(header.length());

                    System.out.println(line);
                    System.out.println(header);
                    System.out.println(line);
                    System.out.println("For:         " + votesFor);
                    System.out.println("Against:     " + votesAgainst);
                    System.out.println("Total votes: " + votesTotal);
                    System.out.println(line);
                    System.out.printf("%s %.2f%s\n", "Percent For:", percent, "%");
                    System.out.printf("%s      %.2f\n", "Margin:", margin);
                    System.out.println(line);
                    System.out.print("\n");
                }
            }

            if (numMatches == 0)
            {
                System.out.println(
                    "No party found: " + partyFilter
                );
            }
        }
    }

    public static void printNomineesTable(Nominee[] nomineeArr)
    {
        String padding, headerStr, sep, out;

        String header[] = {
            "StateAb", "DivisionID", "DivisionNm", "PartyAb", "PartyNm",
            "CandidateID", "Surname", "GivenNm", "Elected", "HistoricElected"
        };

        String fields[][] = new String[nomineeArr.length][header.length];
        String[] tempArr = new String[nomineeArr.length + 1];

        int paddingArr[] = new int[header.length];

        padding = "";
        headerStr = "";
        sep = "";
        out = "";

        for (int i = 0; i < nomineeArr.length; i++)
        {
            fields[i] = nomineeArr[i].toString().split(
                    ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"
            );
        }

        for (int i = 0; i < paddingArr.length; i++)
        {
            tempArr[0] = header[i];

            for (int j = 0; j < nomineeArr.length; j++)
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

        if (nomineeArr.length != 0)
        {
            System.out.println("\n" + sep);
            System.out.println(headerStr);
            System.out.println(sep);
            System.out.print(out);
            System.out.println(sep);
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

    public static void printHelp()
    {
        String msg[] = {
            "Usage: java Driver [House candidates file] [House preference file]",
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
