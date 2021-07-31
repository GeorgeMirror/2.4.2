package app.controller;

import app.model.Role;
import app.model.User;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView pageForAdmin() {
        ModelAndView mav = new ModelAndView("admin/page");
        List<User> userList = userService.getAllUsers();
        mav.addObject("userList", userList);
        return mav;
    }

    @GetMapping("/search")
    public ModelAndView getUserById(@RequestParam(name = "id") long id) {
        ModelAndView mav = new ModelAndView("/admin/userById");
        User userById = userService.getUserById(id);
        mav.addObject("userById", userById);
        return mav;
    }

    @GetMapping("/userAdding")
    public String userAddingForm(@ModelAttribute("user") User user) {
        return "admin/userAdding";
    }

    @PostMapping(value = "/save")
    public String addUser(@ModelAttribute("user") User user,
                          @RequestParam("role") String...role) {
        Set<Role> roles = userService.setRoles(role);
        user.setRolesOfUser(roles);
        userService.createUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.getUserById(id));
        return "admin/userUpdating";
    }

    @PatchMapping()
    public String update(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable(name = "id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}
