package edu.eci.tdse.parcial;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MathController {
	private final MathService mathService;
	private final ProxyService proxyService;

	public MathController(MathService mathService, ProxyService proxyService) {
		this.mathService = mathService;
		this.proxyService = proxyService;
	}

	@GetMapping("/health")
	public Map<String, String> health() {
		return Map.of("status", "ok", "service", "math-service");
	}

	@GetMapping("/api/lucasseq")
	public MathResponse lucasSequenceGet(@RequestParam("value") long value) {
		return mathService.calculate(value);
	}

	@PostMapping("/api/lucasseq")
	public MathResponse lucasSequencePost(@RequestParam("value") long value) {
		return mathService.calculate(value);
	}

	@GetMapping("/lucasseq")
	public ResponseEntity<String> lucasProxyGet(@RequestParam("value") long value) {
		return proxyService.delegateGet(value);
	}

	@PostMapping("/lucasseq")
	public ResponseEntity<String> lucasProxyPost(@RequestParam("value") long value) {
		return proxyService.delegatePost(value);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public Map<String, String> handleIllegalArgument(IllegalArgumentException ex) {
		return Map.of("error", ex.getMessage());
	}
}
