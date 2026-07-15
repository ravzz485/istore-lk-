package com.istore.api.order;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.mongodb.client.result.UpdateResult;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {

    private final MongoTemplate mongoTemplate;

    /**
     * ⭐ ATOMIC stock decrement.
     * "stock >= qty නම් විතරක් stock එක අඩු කරන්න" —
     * check එකයි update එකයි MongoDB එක ඇතුළේ එකම operation එකක්.
     * දෙන්නෙක් එකපාර ආවොත් — MongoDB එක serialize කරනවා,
     * දෙවෙනි එකාට modifiedCount = 0 එනවා.
     */
    public boolean tryDecrementStock(String sku, int qty) {

        Query query = new Query(Criteria.where("variants")
                .elemMatch(Criteria.where("sku").is(sku)
                        .and("stock").gte(qty)));      // stock ඇති නම් විතරක් match!

        Update update = new Update().inc("variants.$.stock", -qty);

        UpdateResult result = mongoTemplate.updateFirst(query, update, "products");

        return result.getModifiedCount() > 0;   // true = ලැබුනා, false = out of stock
    }

    /** Order cancel වුනාම stock ආපහු දෙනවා */
    public void restoreStock(String sku, int qty) {
        Query query = new Query(Criteria.where("variants.sku").is(sku));
        Update update = new Update().inc("variants.$.stock", qty);
        mongoTemplate.updateFirst(query, update, "products");
    }
}