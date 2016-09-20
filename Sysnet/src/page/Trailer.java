package page;

public class Trailer {
	private String SCAC;
	private String TrailerNb;
	private String Dest;
	private String CurTerminal;
	private String lastReportTime;

	public String getTrailerNb() {
		return TrailerNb;
	}

	public void setTrailerNb(String trailerNb) {
		this.TrailerNb = trailerNb;
	}

	public String getSCAC() {
		return SCAC;
	}

	public void setSCAC(String sCAC) {
		this.SCAC = sCAC;
	}

	public String getDest() {
		return Dest;
	}

	public void setDest(String dest) {
		this.Dest = dest;
	}

	public String getCurTerminal() {
		return CurTerminal;
	}

	public void setCurTerminal(String curTerminal) {
		this.CurTerminal = curTerminal;
	}

	public String getLastReportTime() {
		return lastReportTime;
	}

	public void setLastReportTime(String lastReportTime) {
		this.lastReportTime = lastReportTime;
	}

}
