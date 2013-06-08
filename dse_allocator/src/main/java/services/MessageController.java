package services;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


import dse_domain.DTO.ReservationDTO;


@Controller
public class MessageController {
	static final Logger logger = Logger.getLogger(MessageController.class);
	
	@Autowired
	AmqpTemplate amqpTemplate;
	
	
	public void handleReservationRequest(Object message){
		logger.info("received reservation request " +message);
		
		
		if(message instanceof ReservationDTO){
			
			
			
			//get info
			
			//do shit
			
			logger.info("message is of instance ReservationDTO! " +message);
			
			amqpTemplate.convertAndSend("messenger", message);
			
		}
		
		
	}
	
	
}
