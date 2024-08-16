package com.pedrosa.minix.controllers;

import com.pedrosa.minix.controllers.dto.CreateTweetDto;
import com.pedrosa.minix.controllers.dto.FeedDto;
import com.pedrosa.minix.controllers.dto.FeedItemDto;
import com.pedrosa.minix.entities.Tweet;
import com.pedrosa.minix.entities.enums.RoleValue;
import com.pedrosa.minix.repositories.TweetRepository;
import com.pedrosa.minix.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class TweetController {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    public TweetController() {}

    public TweetController(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/feed")
    public ResponseEntity<FeedDto> feed(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){

        Page<FeedItemDto> tweets = tweetRepository.findAll(
                PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"))
                .map(tweet -> new FeedItemDto(
                        tweet.getTweetId(),
                        tweet.getContent(),
                        tweet.getUser().getUsername())
                );

        return ResponseEntity.ok().body(new FeedDto(
                tweets.toList(),
                page,
                pageSize,
                tweets.getTotalPages(),
                tweets.getTotalElements())
        );
    }

    @PostMapping("/tweets")
    public ResponseEntity<Void> createTweet(@RequestBody CreateTweetDto dto, JwtAuthenticationToken token) {

        var user = userRepository.findById(UUID.fromString(token.getName()));

        var tweet = new Tweet();
        tweet.setUser(user.get());
        tweet.setContent(dto.content());

        tweetRepository.save(tweet);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("tweets/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable Long id, JwtAuthenticationToken token) {


        var user = userRepository.findById(UUID.fromString(token.getName()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase(RoleValue.ADMIN.name()));

        if (tweet.getUser().getUserId().equals(UUID.fromString(token.getName())) || isAdmin){
            tweetRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
