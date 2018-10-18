package metrics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import metrics.service.MetricsCollectorService;

@Controller
public class ApplicationMericsCollector {

	@Autowired
	private MetricsCollectorService metricsCollectorService;
	
	@GetMapping
	public ResponseEntity<HttpStatus> startCollectingMetrics() {
		metricsCollectorService.start();
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<HttpStatus> stopCollectingMetrics() {
		metricsCollectorService.stop();
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
}
