package com.bookify.books.Bookify.email;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EmailController {

    /*@Autowired
    private EmailSenderService emailSenderService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestParam String to) {
        emailSenderService.setJavaMailSender(to);
        return new ResponseEntity<>("Email sent", HttpStatus.OK);
    }*/
}
