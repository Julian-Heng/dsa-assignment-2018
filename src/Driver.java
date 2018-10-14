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
                        System.out.println("Selected List by Margin");
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

        list = FileIO.readText(file);
        iter = list.iterator();

        nomineeList = new DSALinkedList<Nominee>();
        invalidEntries = new DSALinkedList<String>();
        line = "";

        if (iter.hasNext())
        {
            iter.next();

            while (iter.hasNext())
            {
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

        list = FileIO.readText(file);
        divisionIdList = new DSALinkedList<String>();
        uniqueDivisionId = new DSALinkedList<String>();

        for (String i : list)
        {
            split = i.split(",");
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
                if (! inInList.contains("Informal"))
                {
                    split = inInList.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

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
            }

            for (Nominee inNomineeList : tempNomineeList)
            {
                tempHousePref.addNomineeToList(inNomineeList);
            }

            preferenceList.insertLast(tempHousePref);
            break;
        }

        return preferenceList;
    }

    public static DSALinkedList<Nominee> getNomineeFromDivisionId(
        DSALinkedList<Nominee> nomineeList, String inDivisionId)
    {
        DSALinkedList<Nominee> returnList;
        returnList = new DSALinkedList<Nominee>();

        for (Nominee inList : nomineeList)
        {
            if (Integer.toString(inList.getIdDivision()).equals(inDivisionId))
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
            if (Integer.toString(inList.getIdCandidate()).equals(inCandidateId))
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

        String userInput;
        String[] split;
        int optionSwitch;

        String stateFilter = "";
        String partyFilter = "";
        String divisionFilter = "";
        String sortLine = "";

        int numMatches = 0;
        int numNominee = 0;
        int i = 0;

        DSAHeap heap;

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
            for (String option : split)
            {
                try
                {
                    optionSwitch = Integer.parseInt(option);
                    if (optionSwitch > 0 &&
                        optionSwitch <= filterOptions.length)
                    {
                        filterOptions[optionSwitch - 1] = true;
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

        if (filterOptions[0])
        {
            stateFilter = Input.string("Input state filter: ");

            if (stateFilter.isEmpty())
            {
                stateFilter = ".*";
            }
        }
        else
        {
            stateFilter = ".*";
        }

        if (filterOptions[1])
        {
            partyFilter = Input.string("Input party filter: ");

            if (partyFilter.isEmpty())
            {
                partyFilter = ".*";
            }
        }
        else
        {
            partyFilter = ".*";
        }

        if (filterOptions[2])
        {
            divisionFilter = Input.string("Input division filter: ");

            if (divisionFilter.isEmpty())
            {
                divisionFilter = ".*";
            }
        }
        else
        {
            divisionFilter = ".*";
        }

        orderMenu.printMenu();
        userInput = Input.string();
        split = userInput.split(" ");

        if (! userInput.isEmpty())
        {
            for (String option : split)
            {
                try
                {
                    optionSwitch = Integer.parseInt(option);
                }
                catch (NumberFormatException e)
                {
                    throw new IllegalArgumentException(
                        "Invalid option: " + option + "."
                    );
                }
            }
        }

        while (iter.hasNext())
        {
            numNominee++;
            inList = iter.next();
            if (inList.getState().matches(stateFilter) &&
                inList.getNameParty().matches(partyFilter) &&
                inList.getNameDivision().matches(divisionFilter))
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

        while (iter.hasNext())
        {
            inList = iter.next();
            if (inList.getState().matches(stateFilter) &&
                inList.getNameParty().matches(partyFilter) &&
                inList.getNameDivision().matches(divisionFilter))
            {
                searchResult[i] = inList;
                i++;
            }
        }

        if (! userInput.isEmpty())
        {
            for (Nominee inResult : searchResult)
            {
                sortLine = "";

                for (String option : split)
                {
                    switch (Integer.parseInt(option))
                    {
                        case 1:
                            sortLine += inResult.getSurname() + "|";
                            break;
                        case 2:
                            sortLine += inResult.getState() + "|";
                            break;
                        case 3:
                            sortLine += inResult.getNameParty() + "|";
                            break;
                        case 4:
                            sortLine += inResult.getNameDivision() + "|";
                            break;
                    }
                }

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
    }

    public static void searchNominees(DSALinkedList<Nominee> nomineeList)
    {
        Menu filterMenu;
        Iterator<Nominee> iter;
        Nominee inList;
        Nominee searchResult[];

        iter = nomineeList.iterator();

        boolean filterOptions[] = {false, false};

        String userInput;
        String[] split;
        int optionSwitch;

        String surnameFilter = "";
        String stateFilter = "";
        String partyFilter = "";

        int numMatches = 0;
        int numNominee = 0;
        int i = 0;

        filterMenu = new Menu(3);

        filterMenu.addOption("Select filter type (eg: 1 2 3):");
        filterMenu.addOption("    1. State");
        filterMenu.addOption("    2. Party");

        surnameFilter = Input.string("Input surname search term: ");

        filterMenu.printMenu();
        userInput = Input.string();
        split = userInput.split(" ");

        if (! userInput.isEmpty())
        {
            for (String option : split)
            {
                try
                {
                    optionSwitch = Integer.parseInt(option);
                    if (optionSwitch > 0 && optionSwitch < 4)
                    {
                        filterOptions[optionSwitch - 1] = true;
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

        if (filterOptions[0])
        {
            stateFilter = Input.string("Input state filter: ");

            if (stateFilter.isEmpty())
            {
                stateFilter = ".*";
            }
        }
        else
        {
            stateFilter = ".*";
        }

        if (filterOptions[1])
        {
            partyFilter = Input.string("Input party filter: ");

            if (partyFilter.isEmpty())
            {
                partyFilter = ".*";
            }
        }
        else
        {
            partyFilter = ".*";
        }

        while (iter.hasNext())
        {
            inList = iter.next();
            if (inList.getSurname().startsWith(surnameFilter) &&
                inList.getState().matches(stateFilter) &&
                inList.getNameParty().matches(partyFilter))
            {
                numMatches++;
            }
        }

        searchResult = new Nominee[numMatches];
        iter = null;
        inList = null;
        iter = nomineeList.iterator();

        while (iter.hasNext())
        {
            numNominee++;
            inList = iter.next();
            if (inList.getSurname().startsWith(surnameFilter) &&
                inList.getState().matches(stateFilter) &&
                inList.getNameParty().matches(partyFilter))
            {
                searchResult[i] = inList;
                i++;
            }
        }

        printNomineesTable(searchResult);
        System.out.println("\n" + numMatches + "/" + numNominee + " matches");
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
            fields[i] = nomineeArr[i].toString().split(",");
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
