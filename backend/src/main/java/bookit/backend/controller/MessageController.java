package bookit.backend.controller;

import bookit.backend.service.AccountService;
import bookit.backend.service.LoggedUserInfo;
import bookit.backend.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("messages")
public class MessageController {

    private final AccountService accountService;
    private final MessageService messageService;

    @GetMapping
    @Operation(summary = "Get list of user's conversations")
    public ResponseEntity<?> getConversations() {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        return ResponseEntity.ok(messageService.getConversations(userInfo.getId()));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get messages from given conversation")
    public ResponseEntity<?> getMessages(@PathVariable long userId) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        return ResponseEntity.ok(messageService.getMessages(userId, userInfo.getId()));
    }

    @PostMapping("/{userId}")
    @Operation(summary = "Send message")
    public ResponseEntity<?> sendMessage(@PathVariable long userId, @RequestBody String body) {
        LoggedUserInfo userInfo = accountService.getLoggedUserInfo();
        return ResponseEntity.status(messageService.sendMessage(userInfo.getId(), userId, body)).build();
    }
}
