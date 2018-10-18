package metrics.model;

public class Metrics {
	
	public int numOfLoginFailures;
	
	public int numOfHttpErrors;
	
	public int latency;
	
	public int throughput;
	
	public Metrics(int numOfLoginFailures, int numOfHttpErrors, int latency, int throughput) {
		this.numOfLoginFailures = numOfLoginFailures;
		this.numOfHttpErrors = numOfHttpErrors;
		this.latency = latency;
		this.throughput = throughput;
	}

}
