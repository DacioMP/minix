package com.pedrosa.minix.controllers;

import com.pedrosa.minix.controllers.dto.CreateTweetDto;
import com.pedrosa.minix.controllers.dto.FeedDto;
import com.pedrosa.minix.controllers.dto.FeedItemDto;
import com.pedrosa.minix.services.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class TweetController {

    @Autowired
    private TweetService tweetService;

    public TweetController() {}

    @GetMapping("/feed")
    public ResponseEntity<FeedDto> feed(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){

        Page<FeedItemDto> tweets = tweetService.findAll(page, pageSize);

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

        UUID id = UUID.fromString(token.getName());
        String content = dto.content();

        tweetService.createTweet(id, content);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("tweets/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable Long id, JwtAuthenticationToken token) {

        UUID userId = UUID.fromString(token.getName());

        if (tweetService.deleteTweet(userId, id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
