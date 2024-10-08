package edu.example.restz.dto;

import edu.example.restz.entity.Product;
import edu.example.restz.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReviewDTO {
    // 리뷰꺼
    private Long rno;
    private String content;
    private String reviewer;
    private int star;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    // 상품꺼
    private Long pno;

    public ReviewDTO(Review review){
        this.rno = review.getRno();
        this.content = review.getContent();
        this.reviewer = review.getReviewer();
        this.star = review.getStar();
        this.regDate = review.getRegDate();
        this.modDate = review.getModDate();
        this.pno = review.getProduct().getPno();
    } // 뷰로

    // 디비로
    public Review toEntity(){
        Product product = Product.builder().pno(pno).build();

        return Review.builder().rno(rno)
                .content(content)
                .reviewer(reviewer)
                .star(star)
                .product(product)
                .build();
    }
}
