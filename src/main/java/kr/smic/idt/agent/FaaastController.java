package kr.smic.idt.agent;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.fraunhofer.iosb.ilt.faaast.service.assetconnection.AssetConnectionException;
import de.fraunhofer.iosb.ilt.faaast.service.exception.ConfigurationException;
import de.fraunhofer.iosb.ilt.faaast.service.exception.EndpointException;
import de.fraunhofer.iosb.ilt.faaast.service.exception.MessageBusException;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.net.MalformedURLException;

@RestController
public class FaaastController {

	private FaaastService faaastService;

	public FaaastController(FaaastService faaastService) {
		this.faaastService = faaastService;
	}

	@RequestMapping(value = "/api/hello", method = RequestMethod.GET)
	public Mono<String> hello() {
		return Mono.just("hello");
	}

	@RequestMapping(value = "/api/faaast", method = RequestMethod.POST)
	public Mono<String> start() {
		try {
			this.faaastService.init();
			this.faaastService.start();
			return Mono.just("started");
		} catch (ConfigurationException | AssetConnectionException | MalformedURLException e) {
			return Mono.error(e);
		} catch (MessageBusException | EndpointException e) {
			return Mono.error(e);
		}
	}

	@RequestMapping(value = "/api/faaast", method = RequestMethod.DELETE)
	public Mono<String> stop() {
		this.faaastService.stop();
		return Mono.just("stopped");
	}
}
