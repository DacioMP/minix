package com.pedrosa.minix.services;

import com.pedrosa.minix.dto.FeedItemDto;
import com.pedrosa.minix.entities.Tweet;
import com.pedrosa.minix.entities.User;
import com.pedrosa.minix.entities.enums.RoleValue;
import com.pedrosa.minix.repositories.TweetRepository;
import com.pedrosa.minix.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<FeedItemDto> findAll(int page, int pageSize) {

        return tweetRepository.findAll(
                PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"))
                .map(tweet -> new FeedItemDto(
                        tweet.getTweetId(),
                        tweet.getContent(),
                        tweet.getUser().getUsername())
                );
    }

    public void createTweet(UUID id, String content) {

        Optional<User> user = userRepository.findById(id);

        Tweet tweet = new Tweet(content, user.get());

        tweetRepository.save(tweet);
    }

    public boolean deleteTweet(UUID userId, Long tweetId) {

        Optional<User> user = userRepository.findById(userId);
        Optional<Tweet> tweet = tweetRepository.findById(tweetId);

        boolean isAdmin = user.get()
                .getRoles()
                .stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(RoleValue.ADMIN.name()));

        if (tweet.get().getUser().getUserId().equals(userId) || isAdmin) {
            tweetRepository.delete(tweet.get());
            return true;
        }

        return false;
    }
}
