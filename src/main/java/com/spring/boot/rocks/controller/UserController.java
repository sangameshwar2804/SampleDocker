package com.spring.boot.rocks.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.spring.boot.rocks.model.AppRole;
import com.spring.boot.rocks.model.AppUser;
import com.spring.boot.rocks.model.GenerateCSVReport;
import com.spring.boot.rocks.model.GenerateExcelReport;
import com.spring.boot.rocks.model.GeneratePdfReport;
import com.spring.boot.rocks.repository.RoleRepository;
import com.spring.boot.rocks.service.UserService;
import com.spring.boot.rocks.validator.UserAddValidator;
import com.spring.boot.rocks.validator.UserEditValidator;

@Controller
@RequestMapping("/")
@SessionAttributes({ "roles", "programareas" })
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private UserAddValidator useraddValidator;

	@Autowired
	private UserEditValidator usereditValidator;

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public String root(Model model) {
		return "redirect:userlist";
	}

	@RequestMapping(value = { "home" }, method = RequestMethod.GET)
	public String home(Model model) {
		return "home";
	}

	@RequestMapping(value = { "userlist" }, method = RequestMethod.GET)
	public String listUsers(ModelMap model) {
		List<AppUser> users = userService.findAllUsers();
		model.addAttribute("users", users);
		return "userlist";
	}

	@RequestMapping(value = "registration", method = RequestMethod.GET)
	public String registration(Model model) {
		model.addAttribute("userForm", new AppUser());

		return "userregistration";
	}

	@RequestMapping(value = "registration", method = RequestMethod.POST)
	public String registration(@ModelAttribute("userForm") AppUser userForm, BindingResult bindingResult, Model model) {
		useraddValidator.validate(userForm, bindingResult);

		if (bindingResult.hasErrors()) {
			return "userregistration";
		}

		userService.save(userForm);

		model.addAttribute("success", "User " + userForm.getUsername() + " created successfully");
		return "success";

	}

	@RequestMapping(value = { "edit-user-{username}" }, method = RequestMethod.GET)
	public String editUser(@PathVariable String username, Model model) {
		AppUser user = userService.findByUsername(username);
		model.addAttribute("userForm", user);
		model.addAttribute("edit", true);
		return "userregistration";
	}

	@RequestMapping(value = { "edit-user-{username}" }, method = RequestMethod.POST)
	public String updateUser(@ModelAttribute("userForm") @Valid AppUser userForm, BindingResult bindingResult,
			Model model, @PathVariable String username) {
		usereditValidator.validate(userForm, bindingResult);
		if (bindingResult.hasErrors()) {
			return "userregistration";
		}

		userService.updateUser(userForm);

		model.addAttribute("success", "User " + userForm.getUsername() + " updated successfully");
		return "success";
	}

	@RequestMapping(value = { "view-user-{username}" }, method = RequestMethod.GET)
	public String viewUser(@PathVariable String username, Model model) {
		AppUser user = userService.findByUsername(username);
		model.addAttribute("userForm", user);
		return "userview";
	}

	@RequestMapping(value = { "delete-user-{username}" }, method = RequestMethod.GET)
	public String deleteUser(@PathVariable String username) {
		userService.deleteUserByUsername(username);
		return "redirect:userlist";
	}

	@ModelAttribute("roles")
	public List<AppRole> initializeRoles() {
		return (List<AppRole>) roleRepo.findAll();
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login(Model model, String error, String logout) {
		if (error != null)
			model.addAttribute("error", "Your username and password is invalid.");

		if (logout != null)
			model.addAttribute("message", "You have been logged out successfully.");

		return "login";
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);

			SecurityContextHolder.getContext().setAuthentication(null);
		}
		return "redirect:login?logout";
	}

	@RequestMapping(value = "Access_Denied", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model) {
		model.addAttribute("loggedinuser", getPrincipal());
		return "useraccessDenied";
	}

	@RequestMapping(value = "/alluserreportPDF", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> allusersReport() throws IOException {

		List<AppUser> users = (List<AppUser>) userService.findAllUsers();

		ByteArrayInputStream bis = GeneratePdfReport.userReport(users);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=UsersReport.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@RequestMapping(value = { "export-user-pdf-{username}" }, method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> exportUser(@PathVariable String username, Model model) {
		AppUser user = userService.findByUsername(username);
		model.addAttribute("userForm", user);
		ByteArrayInputStream bis = GeneratePdfReport.oneuserReport(user);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=" + username + ".pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@RequestMapping(value = "/alluserreportCSV", method = RequestMethod.GET)
	public void csvUsers(HttpServletResponse response) throws IOException {
		List<AppUser> users = (List<AppUser>) userService.findAllUsers();
		GenerateCSVReport.writeUsers(response.getWriter(), users);
		response.setHeader("Content-Disposition", "attachment; filename=AllUsersCSVReport.csv");
	}

	@RequestMapping(value = "/export-user-csv-{username}", method = RequestMethod.GET)
	public void usercsvReport(@PathVariable String username, HttpServletResponse response) throws IOException {
//    	 HttpHeaders headers = new HttpHeaders();
//	        headers.add("Content-Disposition", "inline; filename=" +username+".csv");
		response.setHeader("Content-Disposition", "attachment; filename=" + username + "CSVReport.csv");
		AppUser user = userService.findByUsername(username);
		GenerateCSVReport.writeUser(response.getWriter(), user);
	}

	@RequestMapping(value = "/export-user-xml-{username}", method = RequestMethod.GET)
	public @ResponseBody AppUser getUser(@PathVariable String username) {
		AppUser user = userService.findByUsername(username); // or set your own fields
		// user.setId(userid);
		// user.setUsername(username);
		// and so on....
	    
		return user;
	}

	@GetMapping(value = "/alluserreportExcel")
	public ResponseEntity<InputStreamResource> excelCustomersReport() throws IOException {
		List<AppUser> users = (List<AppUser>) userService.findAllUsers();
		ByteArrayInputStream in = GenerateExcelReport.usersToExcel(users);
		// return IO ByteArray(in);
		HttpHeaders headers = new HttpHeaders();
		// set filename in header
		headers.add("Content-Disposition", "attachment; filename=users.xlsx");
		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}

	@RequestMapping("/alluserreportJSON")
	public @ResponseBody String getusersJSON() {
		ObjectMapper objectMapper = new ObjectMapper();
		// Set pretty printing of json
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		List<AppUser> userlist = null;
		@SuppressWarnings("unused")
		String exception = null;
		String arrayToJson = null;
		try {
			userlist = userService.findAllUsers();
			arrayToJson = objectMapper.writeValueAsString(userlist);
		} catch (Exception ex) {
			ex.printStackTrace();
			exception = ex.getMessage();
		}
		return arrayToJson;
	}

	@RequestMapping("/export-user-json-{username}")
	public @ResponseBody String getuserJSON(@PathVariable String username, HttpServletResponse response) {

		ObjectMapper objectMapper = new ObjectMapper();
		// Set pretty printing of json
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		@SuppressWarnings("unused")
		String exception = null;
		String arrayToJson = null;
		try {
			AppUser user = userService.findByUsername(username);
			arrayToJson = objectMapper.writeValueAsString(user);
		} catch (Exception ex) {
			ex.printStackTrace();
			exception = ex.getMessage();
		}
		return arrayToJson;
	}

	private String getPrincipal() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}

	public String getTimeStamp() {
		TimeZone mytimeZone = TimeZone.getTimeZone("EST");
		Calendar calendar = Calendar.getInstance(mytimeZone);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		simpleDateFormat.setTimeZone(mytimeZone);
		String setTimeStamp = simpleDateFormat.format(calendar.getTime());
		return setTimeStamp;
	}

}
