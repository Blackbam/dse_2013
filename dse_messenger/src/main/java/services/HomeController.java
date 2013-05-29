package services;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.amqp.core.AmqpTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	@Autowired(required = false)
	MongoDbFactory mongoDbFactory;

	@Autowired(required = false)
	MongoTemplate mongoTemplate;

	@Autowired
	AmqpTemplate amqpTemplate;

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

		model.addAttribute("services", services);
		// model.addAttribute("serviceProperties",
		// getServicePropertiesAsList());

		String environmentName = (System.getenv("VCAP_APPLICATION") != null) ? "Cloud"
				: "Local";
		model.addAttribute("environmentName", environmentName);
		return "home";
	}

	// TODO: remove - just for some properties info to debug for now
	@RequestMapping("/env")
	public void env(HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.println("System Properties:");
		for (Map.Entry<Object, Object> property : System.getProperties()
				.entrySet()) {
			out.println(property.getKey() + ": " + property.getValue());
		}
		out.println();
		out.println("System Environment:");
		for (Map.Entry<String, String> envvar : System.getenv().entrySet()) {
			out.println(envvar.getKey() + ": " + envvar.getValue());
		}
	}

	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public String get(Model model) {

		String message = (String) amqpTemplate.receiveAndConvert("messenger");
		if (message != null)
			model.addAttribute("got", message);
		else
			model.addAttribute("got_queue_empty", true);

		return home(model);
	}

}
