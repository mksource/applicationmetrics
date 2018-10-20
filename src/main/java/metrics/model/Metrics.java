package metrics.model;

public class Metrics {
	
	public int numOfLoginFailures;
	
	public int numOfHttpErrors;
	
	public int latency;
	
	public int throughput;
	
	public String timestamp;
	
	public Metrics(int numOfLoginFailures, int numOfHttpErrors, int latency, int throughput, String timestamp) {
		this.numOfLoginFailures = numOfLoginFailures;
		this.numOfHttpErrors = numOfHttpErrors;
		this.latency = latency;
		this.throughput = throughput;
		this.timestamp = timestamp;
	}

}
