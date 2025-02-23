package br.com.nlw.devstage.repositories;

import br.com.nlw.devstage.dto.SubscriptionRankingItem;
import br.com.nlw.devstage.models.Event;
import br.com.nlw.devstage.models.Subscription;
import br.com.nlw.devstage.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
  public Subscription findByEventAndSubscriber(Event event, User subscriber);

  public Subscription findBySubscriptionNumber(Integer subscriptionNumber);

  @Query(value = "select" +
            " s.indication_user_id," +
            " u.user_name," +
            " count(s.subscription_number) as qty" +
          " from " +
            " app.subscriptions s" +
          " inner join" +
            " app.users u " +
              " on s.indication_user_id = u.user_id" +
          " where " +
            " s.indication_user_id is not null" +
              " and" +
                " s.event_id = :eventId" +
          " group by " +
            " s.indication_user_id, u.user_name" +
          " order by" +
            " qty desc;", nativeQuery = true)
  public List<SubscriptionRankingItem> generateRankingByEvent(@Param("eventId") Integer eventId);
}
