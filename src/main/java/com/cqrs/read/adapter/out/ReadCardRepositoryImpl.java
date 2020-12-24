package com.cqrs.read.adapter.out;

import com.cqrs.read.domain.core.CardDto;
import com.cqrs.read.domain.core.ReadCardRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReadCardRepositoryImpl implements ReadCardRepository {

    NamedParameterJdbcTemplate jdbcTemplate;

    public ReadCardRepositoryImpl(DataSource ds){
        this.jdbcTemplate = new NamedParameterJdbcTemplate(ds);
    }

    public List<CardDto> findAll(){
        //Read data from external source
        List<CardDto> cardList = jdbcTemplate
                .query("select * from cards", new BeanPropertyRowMapper<>(CardDto.class));
        return cardList;
    }
}
