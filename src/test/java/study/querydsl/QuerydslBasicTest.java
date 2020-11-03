package study.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

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
    void startJPQL() {
        String sql = "select m from Member m left join fetch m.team " +
                "where m.username = :username";

        Member findMember = em.createQuery(sql, Member.class)
                .setParameter("username", "memberA")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("memberA");
    }

    @Test
    void startQuerydsl() {
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("memberA"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("memberA");
    }

    @Test
    void sort() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.gt(10))
                .orderBy(member.age.desc(), member.username.asc())
                .fetch();

        assertThat(result.get(0).getUsername()).isEqualTo("memberD");
        assertThat(result.get(1).getUsername()).isEqualTo("memberE");
        assertThat(result.get(2).getUsername()).isEqualTo("memberB");
        assertThat(result.get(3).getUsername()).isEqualTo("memberC");
    }

    @Test
    void paging1() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.age.desc())
                .offset(1)
                .limit(2)
                .fetch();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void paging2() {
        QueryResults<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.age.desc())
                .offset(1)
                .limit(2)
                .fetchResults();

        assertThat(result.getTotal()).isEqualTo(5);
        assertThat(result.getLimit()).isEqualTo(2);
        assertThat(result.getOffset()).isEqualTo(1);
        assertThat(result.getResults().size()).isEqualTo(2);
    }

}
