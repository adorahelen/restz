package edu.example.restz.repository.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import edu.example.restz.dto.ProductDTO;
import edu.example.restz.dto.ProductListDTO;
import edu.example.restz.entity.Product;
import edu.example.restz.entity.QProduct;
import edu.example.restz.entity.QProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.List;

public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {
    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public Page<ProductListDTO> list(Pageable pageable) {
        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<Product> query
                = from(product).leftJoin(product.images, productImage)  //조인
                .where( productImage.ino.eq(0) );   //WHERE 조건 = ino가 0인 이미지 파일

        JPQLQuery<ProductListDTO> dtoQuery
                = query.select(Projections.bean(
                ProductListDTO.class,
                product.pno,
                product.pname,
                product.price,
                product.registerId,
                productImage.filename.as("pimage")));

        getQuerydsl().applyPagination(pageable, dtoQuery);      //페이징
        List<ProductListDTO> productList = dtoQuery.fetch();    //쿼리 실행
        long count = dtoQuery.fetchCount();         //레코드 수 조회

        return new PageImpl<>(productList, pageable, count);
    }

    @Override
    public Page<ProductDTO> listWithAllImages(Pageable pageable) {
        QProduct product = QProduct.product;
        JPQLQuery<Product> query = from(product);
        getQuerydsl().applyPagination(pageable, query);      //페이징

        List<Product> products = query.fetch();    //쿼리 실행
        long count = query.fetchCount();         //레코드 수 조회

        List<ProductDTO> dtoList = products.stream()
                .map(ProductDTO::new)
                .toList();

        return new PageImpl<>(dtoList, pageable, count);
    }

    @Override
    public Page<ProductDTO> listWithAllImagesFetch(Pageable pageable) {
        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<Product> query
                = from(product).leftJoin(product.images, productImage)  //조인
                .fetchJoin();

        getQuerydsl().applyPagination(pageable, query);      //페이징
        List<Product> products = query.fetch();    //쿼리 실행
        long count = query.fetchCount();         //레코드 수 조회

        List<ProductDTO> dtoList = products.stream()
                .map(ProductDTO::new)
                .toList();

        return new PageImpl<>(dtoList, pageable, count);
    }
}

