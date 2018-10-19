package metrics.service;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.amazonaws.services.kinesis.producer.KinesisProducer;
import com.amazonaws.services.kinesis.producer.KinesisProducerConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import metrics.model.Metrics;

@Service
public class MetricsCollectorService {

	private static final String STREAM_NAME = "ApplicationMetricsStream";
	private static final String PARTITION_KEY = "partition";
	private static final int SLEEP_PERIOD = 2000;
	private static final String REGION = "us-east-1";
	private boolean stop = true;
	
	private KinesisProducer metricsProducer = getKinesisProducer();;
	private ObjectMapper objectMapper = new ObjectMapper();
	ExecutorService executors = Executors.newFixedThreadPool(10);
	
	public void start() throws JsonProcessingException, UnsupportedEncodingException {
		stop = true;
		
		Runnable runntableTask = () -> {
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
		};
		
		executors.execute(runntableTask);
	}
	
	public void error() throws JsonProcessingException, UnsupportedEncodingException {
		stop = true;
		
		Runnable runntableTask = () -> {
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
		};
		
		executors.execute(runntableTask);
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
		int numOfLoginFailures = 20 + (int)(Math.random() * ((50 - 20) + 1)) ;
		int numOfHttpErrors = 20 + (int)(Math.random() * ((50 - 20) + 1));
		int latency = 50 + (int)(Math.random() * ((80 - 50) + 1));
		int throughput = 10 + (int)(Math.random() * ((20 - 10) + 1));
		return new Metrics(numOfLoginFailures, numOfHttpErrors, latency, throughput);
	}
	
	private KinesisProducer getKinesisProducer() {
		KinesisProducerConfiguration config = new KinesisProducerConfiguration();

		 config.setRegion(REGION);
		 config.setMaxConnections(1);
		 KinesisProducer producer = new KinesisProducer(config);
		 return producer;
	}
	
}
