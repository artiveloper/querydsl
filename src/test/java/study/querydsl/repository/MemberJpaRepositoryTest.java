package study.querydsl.repository;

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    EntityManager em;

    QueryFactory queryFactory;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @BeforeEach
    void before() {
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("TEAM A");
        Team teamB = new Team("TEAM B");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("memberA", 10, teamA);
        Member member2 = new Member("memberB", 29, teamA);

        Member member3 = new Member("memberC", 12, teamB);
        Member member4 = new Member("memberD", 34, teamB);
        Member member5 = new Member("memberE", 34, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.persist(member5);
    }

    @Test
    void searchTest() {
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(12);
        condition.setAgeLoe(29);

        List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(condition);
        assertThat(result).extracting("username").contains("memberB");
    }

    @Test
    void searchTest2() {
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(12);
        condition.setAgeLoe(29);
        condition.setTeamName("TEAM B");

        List<MemberTeamDto> result = memberJpaRepository.search(condition);
        assertThat(result).extracting("username").contains("memberC");
    }

}