/**
 *  Name:     Trip
 *  Purpose:  Provide a class that contains information that is relevant
 *            the trip in between two Location objects. Used as the object
 *            stored in the edges of the graph
 *
 *  Author:   Julian Heng (19473701)
 **/

public class Trip
{
    String transportType;
    int distance;
    int duration;

    // Alternate Constructor
    public Trip(String inTransport, int inDistance, int inDuration)
    {
        this.setTransportType(inTransport);
        this.setDistance(inDistance);
        this.setDuration(inDuration);
    }

    // Setters
    public void setTransportType(String inTransport)
    {
        transportType = inTransport;
    }

    public void setDistance(int inDistance)
    {
        distance = inDistance;
    }

    public void setDuration(int inDuration)
    {
        duration = inDuration;
    }

    // Getters
    public String getTransportType() { return transportType; }
    public int getDistance() { return distance; }
    public int getDuration() { return duration; }
}
