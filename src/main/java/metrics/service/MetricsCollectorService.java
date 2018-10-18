package metrics.service;

import java.nio.ByteBuffer;

import com.amazonaws.services.kinesis.producer.KinesisProducer;
import com.fasterxml.jackson.databind.ObjectMapper;

import metrics.model.Metrics;


public class MetricsCollectorService {

	private static final String STREAM_NAME = "ApplicationMetricsStream";
	private static final String PARTITION_KEY = "partition";
	private static final int SLEEP_PERIOD = 2000;
	private boolean stop = true;
	
	private KinesisProducer metricsProducer;
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public void start() {
		stop = true;
		while(stop) {
			double rand = Math.random();
			Metrics metrics;
			
			if(rand < 0.05 || rand > 0.995) {
				metrics = getAbNormalOperation();
			} else {
				metrics = getNormalOperation();
			}
			try {
			String userRecord = objectMapper.writeValueAsString(metrics);
			ByteBuffer buffer = ByteBuffer.wrap(userRecord.getBytes("UTF-8")); 
			metricsProducer.addUserRecord(STREAM_NAME, PARTITION_KEY, buffer);
			
			Thread.sleep(SLEEP_PERIOD);
			} catch(Exception e) {
			}
		}
	}
	
	public void stop() {
		stop = false;
	}
	
	private Metrics getNormalOperation() {
		int numOfLoginFailures = 1 + (int)(Math.random() * ((3 - 1) + 1)) ;
		int numOfHttpErrors = 1 + (int)(Math.random() * ((20 - 1) + 1));
		int latency = 20 + (int)(Math.random() * ((50 - 20) + 1));
		int throughput = 40 + (int)(Math.random() * ((80 - 40) + 1));
		return new Metrics(numOfLoginFailures, numOfHttpErrors, latency, throughput);
	}
	
	private Metrics getAbNormalOperation() {
		int numOfLoginFailures = 1 + (int)(Math.random() * ((50 - 1) + 1)) ;
		int numOfHttpErrors = 1 + (int)(Math.random() * ((50 - 20) + 1));
		int latency = 50 + (int)(Math.random() * ((80 - 50) + 1));
		int throughput = 10 + (int)(Math.random() * ((20 - 10) + 1));
		return new Metrics(numOfLoginFailures, numOfHttpErrors, latency, throughput);
	}
	
}
