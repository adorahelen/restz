package edu.example.restz.repository;

import edu.example.restz.dto.ProductListDTO;
import edu.example.restz.dto.ReviewDTO;
import edu.example.restz.entity.Product;
import edu.example.restz.entity.Review;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;
import static org.junit.jupiter.api.Assertions.*;


// @DataJpaTest 시큐리티 때문에 X
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Log4j2
public class ReviewRepositoryTest {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testInsert(){
        // tbl_review 테이블에 상품번호 1번의 테스트 데이터 10개를 추가한다.
        Long pno = 1L;
        Product product = Product.builder().pno(pno).build();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            Review review = Review.builder()
                    .content("리뷰 테스트" + i)
                    .reviewer("user" + i)
                    .star(5)
                    .product(product)
                    .build();

            // 저장된 리뷰 객체를 로깅하거나 검증할 필요가 있다면 이곳에서 처리
            Review savedReview = reviewRepository.save(review);
            log.info("Saved Review: " + savedReview);
            assertNotNull(savedReview);
            assertEquals(i, savedReview.getRno());
        });
    }

    @Test
    @Transactional(readOnly = true)// 읽기 전용 트랜잭션 모드 설정
    public void testRead() {
        Long rno = 5L;

        Optional<Review> foundReview = reviewRepository.findById(rno);
        assertTrue(foundReview.isPresent(), "Review not found");

        System.out.println("=========");
        Product product = foundReview.get().getProduct();
        assertNotNull(product);
        assertEquals(1, product.getPno());
        assertEquals(1000, product.getPrice());
    } // => 셀렉트를 두 번 해서, 값을 찾음

    @Test
    public void testGetReviewProd(){
        Long rno = 5L;
        Optional<Review> foundReview = reviewRepository.getReviewProduct(rno);
        assertTrue(foundReview.isPresent(), "Review not found");
        Product product = foundReview.get().getProduct();
        assertNotNull(product);
        assertEquals(1, product.getPno());
        assertEquals(1000, product.getPrice());
    } // => 조인을 적용하여, 한번에 셀렉트로 값을 찾아옴

    @Test
    public void testGetReviewProdImg() {
        //리뷰 조회 시 상품과 상품 이미지도 가져오기 테스트
        Long rno = 5L;

        Optional<Review> foundReview
                = reviewRepository.getWithProductImage(rno);
        assertTrue(foundReview.isPresent(), "foundReview should be present");

        System.out.println("-----------------------");
        Product product = foundReview.get().getProduct();
        assertNotNull(product);
        assertEquals(1, product.getPno());
        assertEquals(1000, product.getPrice());
        assertEquals(0, product.getImages().first().getIno());
    }


    @Test
    @Transactional
    @Commit
    public void testUpdate(){
        Long rno = 5L;
        String content = "리뷰 수정 테스트";
        int star = 1;

        Optional<Review> foundReview = reviewRepository.findById(rno);
        assertTrue(foundReview.isPresent(), "Review not found");

        Review review = foundReview.get();
        review.ChangeContent(content);
        review.ChangeStar(star);

        foundReview = reviewRepository.findById(rno);
       assertEquals(content, foundReview.get().getContent());
       assertEquals(star, foundReview.get().getStar());
    }

    @Test
    public void testDelete(){
        Long rno = 4L;
        assertTrue(reviewRepository.existsById(rno), "Review not found");
        reviewRepository.deleteById(rno);
        assertFalse(reviewRepository.existsById(rno), "Review not found");
    }

    @Test
    public void testList(){
        Long pno = 1L;

        // 한 페이지에 5개씩 , 첫번째 페이지를 오름차순 정렬로 가져오기
        Pageable pageable = PageRequest.of(0, 5, Sort.by("rno").descending());


        Page<ReviewDTO> reviewDTOS = reviewRepository.List(pno, pageable);

        assertNotNull(reviewDTOS);
        assertEquals(9, reviewDTOS.getTotalElements());
        assertEquals(2, reviewDTOS.getTotalPages());
        assertEquals(0, reviewDTOS.getNumber()); // 현재 페이지 번호 0
        assertEquals(5, reviewDTOS.getSize());
        assertEquals(5, reviewDTOS.getContent().size());
//        reviewRepository.List(pno, pageable).getContent().forEach(reviewDTO ->{
//            System.out.println(reviewDTO);
//        });
        // 결과가 널이 아님을 검증

//        assertEquals(10, pageable.get)
    }

    @Test // 페이징테스트2, 리뷰 개수 추가
    public void testListWithReviewCount(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").ascending());

        Page<ProductListDTO> productListDTOS = productRepository.listWithReviewCount(pageable);
        assertNotNull(productListDTOS);
        productListDTOS.getContent().forEach(productListDTO -> {
            System.out.println(productListDTO);
        });
    }
}

