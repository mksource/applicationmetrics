package metrics.service;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
	private static final int SLEEP_PERIOD_1 = 1;
	private static final int SLEEP_PERIOD_2 = 20;
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
				
				if(rand < 0.01) {
					metrics = getHighLoadOperationMetrics();
				} else if (rand > 0.998) {
					metrics = getMiddleLoadOperationsMetrics();
				}
				else {
					metrics = getNormalOperationMetrics();
				}
				try {
				String userRecord = objectMapper.writeValueAsString(metrics);
				ByteBuffer buffer = ByteBuffer.wrap(userRecord.getBytes("UTF-8")); 
				metricsProducer.addUserRecord(STREAM_NAME, PARTITION_KEY, buffer);
				
				TimeUnit.SECONDS.sleep(SLEEP_PERIOD_1);
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
				
				if(rand < 0.50) {
					metrics = getHighLoadOperationMetrics();
				} else {
					metrics = getMiddleLoadOperationsMetrics();
				}
				try {
				String userRecord = objectMapper.writeValueAsString(metrics);
				ByteBuffer buffer = ByteBuffer.wrap(userRecord.getBytes("UTF-8")); 
				metricsProducer.addUserRecord(STREAM_NAME, PARTITION_KEY, buffer);
				
				TimeUnit.SECONDS.sleep(SLEEP_PERIOD_2);
				} catch(Exception e) {
				}
			}
		};
		
		executors.execute(runntableTask);
	}
	
	
	
	public void stop() {
		stop = false;
	}
	
	private Metrics getNormalOperationMetrics() {
		int numOfLoginFailures = 1 + (int)(Math.random() * ((3 - 1) + 1)) ;
		int numOfHttpErrors = 1 + (int)(Math.random() * ((10 - 1) + 1));
		int latency = 10 + (int)(Math.random() * ((40 - 10) + 1));
		int throughput = 40 + (int)(Math.random() * ((80 - 40) + 1));
		Date date = new Date();
		String formatedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return new Metrics(numOfLoginFailures, numOfHttpErrors, latency, throughput, formatedDate);
	}
	
	private Metrics getHighLoadOperationMetrics() {
		int numOfLoginFailures = 20 + (int)(Math.random() * ((40 - 20) + 1)) ;
		int numOfHttpErrors = 20 + (int)(Math.random() * ((50 - 20) + 1));
		int latency = 50 + (int)(Math.random() * ((80 - 50) + 1));
		int throughput = 20 + (int)(Math.random() * ((40 - 20) + 1));
		Date date = new Date();
		String formatedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return new Metrics(numOfLoginFailures, numOfHttpErrors, latency, throughput, formatedDate);
	}
	
	private Metrics getMiddleLoadOperationsMetrics() {
		int numOfLoginFailures = 5 + (int)(Math.random() * ((20 - 5) + 1)) ;
		int numOfHttpErrors = 10 + (int)(Math.random() * ((20 - 10) + 1));
		int latency = 20 + (int)(Math.random() * ((50 - 20) + 1));
		int throughput = 5 + (int)(Math.random() * ((20 - 5) + 1));
		Date date = new Date();
		String formatedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return new Metrics(numOfLoginFailures, numOfHttpErrors, latency, throughput, formatedDate);
	}
	
	private KinesisProducer getKinesisProducer() {
		KinesisProducerConfiguration config = new KinesisProducerConfiguration();

		 config.setRegion(REGION);
		 config.setMaxConnections(1);
		 KinesisProducer producer = new KinesisProducer(config);
		 return producer;
	}
	
}
