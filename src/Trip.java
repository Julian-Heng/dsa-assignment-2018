public class Trip
{
    String transportType;
    Time duration;

    public Trip(String inTransport, Time inDuration)
    {
        this.setTransportType(inTransport);
        this.setDuration(inDuration);
    }

    public void setTransportType(String inTransport)
    {
        transportType = inTransport;
    }

    public void setDuration(Time inDuration)
    {
        duration = inDuration;
    }

    public String getTransportType() { return transportType; }
    public Time getDuration() { return duration; }
}
