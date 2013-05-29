package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import domain.Doctor;
import domain.Hospital;
import domain.OpSlot;
import domain.Patient;
import domain.Person;

/**
 * Handles requests for the application home page.
 */
@Controller
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
	@RequestMapping(value = "/public", method = RequestMethod.GET)
	public String publicInfo(Model model) {
		
		List<OpSlot> opSlots = mongoTemplate.findAll(OpSlot.class);

		// TODO for testing only
		if (opSlots.size() >= 10) {
			mongoTemplate.dropCollection(OpSlot.class);
			
		}

		List<Hospital> hospitals = mongoTemplate.findAll(Hospital.class);
		Hospital hospital = hospitals.get(0);
		
		DateTime dateTime = new DateTime("2013-12-13T21:39:45.618-08:00");

		OpSlot opSlot = new OpSlot(hospital, 1, OpSlot.Type.AUGEN);
		opSlot.setDate(dateTime.toDate());

		mongoTemplate.save(opSlot);
		model.addAttribute("opSlots", opSlots);

		return "public";
	}
	
	

}
