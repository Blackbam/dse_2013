package controller;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dao.IUserInterfaceDAO;
import dse_domain.domain.Person;

@Controller
@RequestMapping(value = "/notification/")
public class NotificationController {
	static final Logger logger = Logger.getLogger(DoctorController.class);

	@Autowired
	AmqpTemplate amqpTemplate;

	@Autowired
	IUserInterfaceDAO uiDAO;

	// All notifications of the whole system
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String test(Model model, @RequestParam("id") String id) {

		Person person = uiDAO.findPatient(id);

		if (person == null) {
			person = uiDAO.findDoctor(id);
		}

		// find the person to get notifications for
		if (person != null) {
			model.addAttribute("notifications", uiDAO.findNotificationsForPerson(person));
		}
		return "notification";
	}

}