package controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles requests for the allocator home page.
 */
@Controller
public class HomeController {

	@RequestMapping(value = "")
	public String home(Model model) {
		return "WEB-INF/views/home.jsp";
	}

}
