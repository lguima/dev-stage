package br.com.nlw.devstage.controllers;

import br.com.nlw.devstage.dto.ErrorMessage;
import br.com.nlw.devstage.dto.SubscriptionResponse;
import br.com.nlw.devstage.exceptions.EventNotFoundException;
import br.com.nlw.devstage.exceptions.IndicationUserNotFoundException;
import br.com.nlw.devstage.exceptions.SubscriptionConflictException;
import br.com.nlw.devstage.models.Subscription;
import br.com.nlw.devstage.models.User;
import br.com.nlw.devstage.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
public class SubscriptionController {
  @Autowired
  private SubscriptionService subscriptionService;

  @PostMapping({
    "/subscriptions/{eventName}",
    "/subscriptions/{eventName}/{indicationUserId}"
  })
  public ResponseEntity<?> createSubscription(
    @PathVariable String eventName,
    @RequestBody User subscriber,
    @PathVariable(required = false) Integer indicationUserId
  ) {
    try {
      SubscriptionResponse subscription = subscriptionService.createSubscription(eventName, subscriber, indicationUserId);

      if (subscription == null) {
        return ResponseEntity.badRequest().build();
      }

      return ResponseEntity.ok().body(subscription);
    } catch (EventNotFoundException | IndicationUserNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
    } catch (SubscriptionConflictException e) {
      return ResponseEntity.status(409).body(new ErrorMessage(e.getMessage()));
    }
  }
}
