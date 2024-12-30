package bookit.backend.service;

import bookit.backend.model.dto.MessageDto;
import bookit.backend.model.dto.user.UserDto;
import bookit.backend.model.entity.Message;
import bookit.backend.model.entity.user.User;
import bookit.backend.repository.MessageRepository;
import bookit.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public HttpStatus sendMessage(long senderId, long receiverId, String body) {
        User sender = userRepository.findById(senderId).orElse(null);
        User receiver = userRepository.findById(receiverId).orElse(null);
        if(sender == null || receiver == null) return HttpStatus.NOT_FOUND;

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .body(body)
                .date(LocalDateTime.now())
                .build();

        messageRepository.save(message);
        return HttpStatus.CREATED;
    }

    public List<UserDto> getConversations(long userId) {
        var receivedFrom = messageRepository.findAllByReceiverId(userId)
                .stream()
                .map(Message::getSender)
                .map(m -> modelMapper.map(m, UserDto.class))
                .toList();
        var sentTo = messageRepository.findAllBySenderId(userId)
                .stream()
                .map(Message::getReceiver)
                .map(m -> modelMapper.map(m, UserDto.class))
                .toList();
        return Stream.concat(receivedFrom.stream(), sentTo.stream()).toList();
    }

    public List<MessageDto> getMessages(long userId, long currentUserId) {
        var received = messageRepository.findAllBySenderIdAndReceiverId(userId, currentUserId)
                .stream()
                .map(m -> modelMapper.map(m, MessageDto.class))
                .toList();
        var sent = messageRepository.findAllBySenderIdAndReceiverId(currentUserId, userId)
                .stream()
                .map(m -> modelMapper.map(m, MessageDto.class))
                .toList();
        return Stream.concat(received.stream(), sent.stream())
                .sorted(Comparator.comparing(MessageDto::getDate))
                .toList();
    }
}
