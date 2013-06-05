package services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import dse_domain.domain.Notification;

@Controller
public class MessageController implements MessageListener {

	@Autowired(required = false)
	MongoDbFactory mongoDbFactory;

	@Autowired(required = false)
	MongoTemplate mongoTemplate;

	@Autowired(required = false)
	RabbitTemplate rabbitTemplate;

	@Override
	public void onMessage(Message message) {

		System.out.println("TEST MESSENGERcontroller - received message: " 
				+ message.toString());
		
		Notification fuckingShit = new Notification(null, null, null);
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {

		List<String> services = new ArrayList<String>();
		if (mongoDbFactory != null) {
			services.add("MongoDB: "
					+ mongoDbFactory.getDb().getMongo().getAddress());
		}
		if (rabbitTemplate != null) {
			services.add("RabbitMQ: "
					+ rabbitTemplate.getConnectionFactory().toString());
		}
		
		model.addAttribute("services", services);
		Notification fuckingShit = new Notification(null, null, null);

		return "home";
	}

}
