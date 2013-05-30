package controller;

import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import domain.OpSlot;

/**
 * Handles requests for the public access page.
 * 
 * @author Taylor
 */
@Controller
@RequestMapping(value = "/public")
public class PublicController {

	@Autowired(required = false)
	MongoDbFactory mongoDbFactory;

	@Autowired(required = false)
	MongoTemplate mongoTemplate;

	@Autowired(required = false)
	@Qualifier(value = "serviceProperties")
	Properties serviceProperties;

	// get the full list of operations
	// Werte: Datum von bis Typ KH Arzt Status
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String publicInfo(Model model) {

		List<OpSlot> opSlots = mongoTemplate.findAll(OpSlot.class);
		
		model.addAttribute("opSlots", opSlots);

		return "public";
	}

}
