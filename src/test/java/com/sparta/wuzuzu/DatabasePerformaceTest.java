package com.sparta.wuzuzu;


import com.sparta.wuzuzu.domain.community_posts.repository.CommunityPostRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@SpringBootTest
@Service
public class DatabasePerformaceTest {

    @Autowired
    private CommunityPostRepository communityPostRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    @Test
//    public void testSearchPostsNoIndex() {
//        long startTime = System.currentTimeMillis();
//        communityPostRepository.findByTitleContaining("dog", Pageable.unpaged());
//        long endTime = System.currentTimeMillis();
//        System.out.println("Execution time without index: " + (endTime - startTime) + "ms");
//    }

    @Test
    public void testSearchPostsWithIndex() {
        long startTime = System.currentTimeMillis();
        communityPostRepository.findByTitleWithFullTextSearch("dog", Pageable.unpaged());
        long endTime = System.currentTimeMillis();
        System.out.println("Execution time with index: " + (endTime - startTime) + "ms");
    }

//    @Test
//    public void explainSearch() {
//        jdbcTemplate.query("EXPLAIN SELECT * FROM community_posts WHERE title LIKE '%dog%'", rs -> {
//            while (rs.next()) {
//                System.out.println("Type: " + rs.getString("type"));
//                System.out.println("Possible_keys: " + rs.getString("possible_keys"));
//                System.out.println("Key: " + rs.getString("key"));
//                System.out.println("Rows: " + rs.getString("rows"));
//                System.out.println("Extra: " + rs.getString("Extra"));
//            }
//        });
//    }

    @Test
    public void explainSearchFullText() {
        jdbcTemplate.query("EXPLAIN SELECT * FROM community_posts WHERE MATCH(title) AGAINST('+dog*' IN BOOLEAN MODE)", rs -> {
            while (rs.next()) {
                System.out.println("Type: " + rs.getString("type"));
                System.out.println("Possible_keys: " + rs.getString("possible_keys"));
                System.out.println("Key: " + rs.getString("key"));
                System.out.println("Rows: " + rs.getString("rows"));
                System.out.println("Extra: " + rs.getString("Extra"));
            }
        });
    }



    private void executeQueryAndLogTime(String query, String scenario) {
        long startTime = System.currentTimeMillis();
        List<Map<String, Object>> results = jdbcTemplate.queryForList(query);
        long endTime = System.currentTimeMillis();
        System.out.println("Scenario: " + scenario);
        System.out.println("Result count: " + results.size());
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
        System.out.println("--------------------------------");
    }

    @Test
    public void testPagingWithIndexOnLikeCounts() {
        // 인덱스가 적용된 상황에서의 likeCount 기준 정렬 테스트
        String queryWithIndex = "SELECT SQL_NO_CACHE * FROM community_posts WHERE title LIKE '%dog%' AND category = 1 ORDER BY like_count LIMIT 500, 10";
        executeQueryAndLogTime(queryWithIndex, "With Index on LikeCount");
    }

    @Test
    public void testPagingWithoutIndexOnLikeCounts() {
        // 인덱스가 적용되지 않은 상황에서의 likeCount 기준 정렬 테스트
        String queryWithoutIndex = "SELECT SQL_NO_CACHE * FROM community_posts WHERE title LIKE '%dog%' AND category = 1 ORDER BY like_count LIMIT 500, 10";
        executeQueryAndLogTime(queryWithoutIndex, "Without Index on LikeCount");
    }

    @Test
    public void testPagingWithIndexOnViews() {
        // 인덱스가 적용된 상태에서 조회수 기준 정렬 테스트
        String queryWithIndex = "SELECT SQL_NO_CACHE * FROM community_posts WHERE title LIKE '%dog%' ORDER BY views LIMIT 500, 10";
        executeQueryAndLogTime(queryWithIndex, "With Index on Views");
    }

    @Test
    public void testPagingWithoutIndexOnViews() {
        // 인덱스가 적용되지 않은 상태에서 조회수 기준 정렬 테스트
        String queryWithoutIndex = "SELECT SQL_NO_CACHE * FROM community_posts WHERE title LIKE '%dog%' ORDER BY views LIMIT 500, 10";
        executeQueryAndLogTime(queryWithoutIndex, "Without Index on Views");
    }

}