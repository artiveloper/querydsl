package study.querydsl.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    EntityManager em;

    @Test
    void testEntity() {
        Team teamA = new Team("TEAM A");
        Team teamB = new Team("TEAM B");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("userA", 10, teamA);
        Member member2 = new Member("userB", 29, teamA);
        Member member3 = new Member("userC", 12, teamB);
        Member member4 = new Member("userD", 34, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m left join fetch m.team", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member = " + member.getTeam());
        }

    }

}