package com.thinkconstructive.rest_demo.mapper;

import com.thinkconstructive.rest_demo.model.Product;
import com.thinkconstructive.rest_demo.request.ProductRequest;
import com.thinkconstructive.rest_demo.responses.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "product.productName",target = "productName")
    @Mapping(source = "product.sellerId",target = "sellerId")
    @Mapping(source = "product.quantity",target = "quantity")
    @Mapping(source = "product.productId",target = "productId")
    ProductResponse getMappedProduct(Product product);
    @Mapping(source = "productRequest.productName",target = "productName")
    @Mapping(source = "productRequest.sellerId",target = "sellerId")
    @Mapping(source = "productRequest.quantity",target = "quantity")
    @Mapping(source = "productRequest.hiddenPrice",target = "hiddenPrice")
    Product updateProductFromProductRequest(Product product, ProductRequest productRequest);
}
