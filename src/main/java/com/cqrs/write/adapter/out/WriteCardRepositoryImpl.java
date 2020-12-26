package com.cqrs.write.adapter.out;

import com.cqrs.write.domain.core.Card;
import com.cqrs.write.domain.core.WriteCardRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class WriteCardRepositoryImpl implements WriteCardRepository {
    private NamedParameterJdbcTemplate jdbcTemplate;

    public WriteCardRepositoryImpl(DataSource ds){
        this.jdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }


    @Override
    public void save(Card card) {
        jdbcTemplate.update("update into card (id, external_id, name) values (:_id, :_externalId, :_name)",
                new MapSqlParameterSource()
                        .addValue("_id", card.getId())
                        .addValue("_externalId", card.getExternalId())
                        .addValue("_name", card.getName())
                );
    }

    @Override
    public void update(Card card) {
        jdbcTemplate.update("update into card(external_id, name, updatedAt) values (:_externalId, :_name, :_updatedAt) where external_id = : _externalId",
                new MapSqlParameterSource()
                        .addValue("_externalId", card.getExternalId())
                        .addValue("_name", card.getName())
                        .addValue("_updatedAt", card.getUpdatedAt())
                        .addValue("_externalId", card.getExternalId()));
    }
}
