package metrics.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApplicationMericsCollector {

	
	@GetMapping
	public ResponseEntity<HttpStatus> startCollectingMetrics() {
		
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<HttpStatus> stopCollectingMetrics() {
		
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
}
