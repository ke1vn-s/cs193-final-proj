import java.util.List;

public class Meal {
    private String Type;
    private List<Station> Stations;
    private String Status;
    private Hours Hours;

    public String getType() {
        return Type;
    }

    public List<Station> getStations() {
        return Stations;
    }

    public String getStatus() {
        return Status;
    }

    public Hours getHours() {
        return Hours;
    }
        
}
