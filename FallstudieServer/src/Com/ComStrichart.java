package Com;

public class ComStrichart {

    private int idStrichart;
    private String StrichBez;
    private boolean Zustand;
    
    public ComStrichart(){}
    
    public ComStrichart(int idStrichart, String StrichBez, boolean Zustand){
    	this.idStrichart = idStrichart;
    	this.StrichBez = StrichBez;
    	this.Zustand = Zustand;
    }

	public int getIdStrichart() {
		return idStrichart;
	}

	public String getStrichBez() {
		return StrichBez;
	}

	public boolean isZustand() {
		return Zustand;
	}

	public void setIdStrichart(int idStrichart) {
		this.idStrichart = idStrichart;
	}

	public void setStrichBez(String strichBez) {
		StrichBez = strichBez;
	}

	public void setZustand(boolean zustand) {
		Zustand = zustand;
	}
    
}