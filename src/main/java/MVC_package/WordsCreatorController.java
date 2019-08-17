package MVC_package;

import data_base.UserRepository;
import entities.User;
import entities.WordTranslate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/create")
@SessionAttributes("user")
@PreAuthorize("hasRole('ADMIN')")
public class WordsCreatorController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TableController tableController;

    //Todo: refactor for accepting a user from the authentication mechanism
    @ModelAttribute("user")
    private User getUser( ){
        return userService.getUsersList().get(0);
    }

    @GetMapping
    public String showForm(Model model) {
        log.info(">>> wordsCreatorController in action");
        log.info(">>> current user service(must be singletone): " + userService.toString());
        model.addAttribute("word", new WordTranslate());
        return "words_creator";
    }


    @PostMapping
    public String saveWord(@Valid @ModelAttribute("word") WordTranslate word, Errors errors, Model model) {
        if (errors.hasFieldErrors()) return "words_creator";
        log.info(word.toString());
        User user = (User)model.asMap().get("user");
        log.info(">>> saving new word in user: " + user);
        userService.addWordToUserDictionary(user.getId(), word);
        return "redirect:/table";
    }
}