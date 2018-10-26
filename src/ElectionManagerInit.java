import java.util.*;

/**
 *  Name:     ElectionManagerInit
 *  Purpose:  Process distance, candidates and preference files
 *            before the user can select an option. Used for
 *            initialising the program with objects
 *
 *  Author:   Julian Heng (19473701)
 **/

public class ElectionManagerInit
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

        System.out.printf("\u0008%sms\n", Commons.toMiliseconds(funcDuration));

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

        System.out.printf("\u0008%sms\n", Commons.toMiliseconds(duration));

        // Print invalid entries
        if (invalidEntries.getCount() != 0)
        {
            iter = invalidEntries.iterator();
            while (iter.hasNext())
            {
                System.out.printf(
                    "Invalid entry in %s: \"%s\", ignoring...\n",
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
        DSAHashTable hashTable;

        long timeStart, timeEnd, duration;

        String[] split;

        String spinner[] = {"\\", "|", "/", "-"};
        int count = 1;

        timeStart = System.nanoTime();

        // Read the file
        list = sortByDivisionId(FileIO.readText(file));
        iter = list.iterator();

        hashTable = new DSAHashTable();
        divisionIdList = new DSALinkedList<String>();

        while (iter.hasNext())
        {
            line = iter.next();
            split = line.split(SPLIT_REGEX);
            try
            {
                if (! split[1].isEmpty() &&
                    ! split[1].equals("DivisionID"))
                {
                    hashTable.put(split[1], split[1]);
                }
            }
            catch (IllegalArgumentException e)
            {
                if (! e.getMessage().equals("Keys must be unique"))
                {
                    throw new IllegalArgumentException(
                        "Unexpected exception occurred when getting unique " +
                        "Division ID"
                    );
                }
            }
        }

        divisionIdList = hashTable.convertKeyToLinkedList();
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

        System.out.printf("\u0008%sms\n", Commons.toMiliseconds(duration));

        return preferenceList;
    }

    /**
     *  Name:     sortByDivisionId
     *  Purpose:  Sort the preference file by Division ID
     *  Imports:
     *    - list : An unsorted linked list of strings
     *  Exports:
     *    - returnList : A sorted linked list of strings
     **/

    public static DSALinkedList<String> sortByDivisionId(
        DSALinkedList<String> list)
    {
        DSAMaxHeap heap;
        String entry;
        String[] split;

        DSALinkedList<String> returnList;
        DSALinkedList<Object> sorted;
        Iterator<String> iter = list.iterator();
        Iterator<Object> iter2;

        heap = new DSAMaxHeap(list.getCount());
        returnList = new DSALinkedList<String>();

        // Insert all entries in linked list to heap
        while (iter.hasNext())
        {
            entry = iter.next();
            split = entry.split(SPLIT_REGEX);

            heap.add(split[1], entry);
        }

        // Sort and convert out to arrays
        heap.heapSort();
        sorted = heap.toObjList();

        // Convert back to a linked list
        iter2 = sorted.iterator();
        while (iter2.hasNext())
        {
            returnList.insertLast((String)iter2.next());
        }

        return returnList;
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
            if (Commons.compareIntString(inList.getIdDivision(), inDivisionId))
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
            if (Commons.compareIntString(inList.getIdCandidate(), inCandidateId))
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
        String inList = "";
        String[] split;
        boolean start, end;

        returnList = new DSALinkedList<String>();

        start = false;
        end = false;

        // For all entries in preference file
        iter = list.iterator();

        while (iter.hasNext() && ! start)
        {
            inList = iter.next();
            split = inList.split(SPLIT_REGEX);
            if (! split[1].isEmpty() && split[1].equals(inDivisionId))
            {
                start = true;
            }
        }

        returnList.insertLast(inList);

        while (iter.hasNext() && ! end)
        {
            inList = iter.next();
            split = inList.split(SPLIT_REGEX);

            // If Division ID matches, add to return list
            if (! split[1].isEmpty() && split[1].equals(inDivisionId))
            {
                returnList.insertLast(inList);
            }
            else
            {
                end = true;
            }
        }

        return returnList;
    }
}
