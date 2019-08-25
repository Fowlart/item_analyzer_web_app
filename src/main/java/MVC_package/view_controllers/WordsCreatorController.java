package MVC_package.view_controllers;

import MVC_package.UserService;
import MVC_package.rest_consumers.TestRestConsumer;
import data_base.UserRepository;
import entities.User;
import entities.WordTranslate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/create")
@SessionAttributes("user")
public class WordsCreatorController {
    @Autowired
    private UserService userService;

    @Autowired
    private TestRestConsumer testRestConsumer;

    //Todo: refactor for accepting a user from the authentication mechanism
    @ModelAttribute("user")
    private User getUser() {
        log.info(">>> user is added to the session attribute");
        return userService.getUsersList().get(0);
    }

    @GetMapping
    public String showForm(Model model) {
        log.info(">>> wordsCreatorController in action");
        log.info(">>> current user service(must be singletone): " + userService.toString());

        //for test rest API consumer
        log.info(">>> testing rest-consumers API: "+testRestConsumer.getWords());

        model.addAttribute("word", new WordTranslate());
        return "words_creator";
    }

    @PostMapping
    public String saveWord(@Valid @ModelAttribute("word") WordTranslate word, Errors errors, Model model) {
        if (errors.hasFieldErrors()) return "words_creator";
        log.info(word.toString());
        User user = (User) model.asMap().get("user");
        log.info(">>> saving new word in user: " + user);
        userService.addWordToUserDictionary(user.getId(), word);
        return "redirect:/table";
    }
}