package kr.smic.idt.agent;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import de.fraunhofer.iosb.ilt.faaast.service.Service;
import de.fraunhofer.iosb.ilt.faaast.service.assetconnection.AssetConnectionException;
import de.fraunhofer.iosb.ilt.faaast.service.exception.ConfigurationException;
import de.fraunhofer.iosb.ilt.faaast.service.exception.EndpointException;
import de.fraunhofer.iosb.ilt.faaast.service.exception.MessageBusException;
import kr.smic.idt.agent.config.FaaastConfiguration;

@Component
public class FaaastService {

	private FaaastConfiguration config;

	private Service faaastService;
	
	private Boolean isRunning = false;

	public FaaastService(FaaastConfiguration config) {
		this.config = config;
	}

	@PostConstruct
	public void init() throws ConfigurationException, AssetConnectionException {
		this.faaastService = new Service(config.getFaaastServiceConfig());
	}

	public void start() throws MessageBusException, EndpointException {
		if (!this.isRunning) {
			try {
				this.faaastService.start();
				this.isRunning = true;
			} catch (MessageBusException | EndpointException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	@PreDestroy
	public void stop() {
		if (this.isRunning) {
			this.faaastService.stop();
			this.isRunning = false;
		}
	}
}
