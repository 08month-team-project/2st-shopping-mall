//package com.example.shoppingmall.domain.item.api;
//
//import com.example.shoppingmall.domain.item.application.ItemService;
//import com.example.shoppingmall.domain.item.dto.ItemDetailResponse;
//import com.example.shoppingmall.domain.item.dto.ItemImageResponse;
//import com.example.shoppingmall.domain.item.dto.StockResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static com.example.shoppingmall.domain.item.type.ClothingSizeName.L;
//import static com.example.shoppingmall.domain.item.type.ClothingSizeName.M;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//class ItemControllerTest {
//
//    @InjectMocks
//    private ItemController itemController;
//
//    @Mock
//    private ItemService itemService;
//
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper;
//
//
//    @BeforeEach
//    void init() {
//        objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        //objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        //objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 배열 형식 대신 ISO 형식 사용
//
//        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
//    }
//
//    @Test
//    void getItemDetail() throws Exception {
//
//        // when
//        long itemId = 1L;
//
//        List<ItemImageResponse> itemImageResponses = List.of(
//                new ItemImageResponse(1, "이미지1"),
//                new ItemImageResponse(1, "이미지2"));
//
//        List<StockResponse> StockResponses = List.of(
//                new StockResponse(M, 2),
//                new StockResponse(L, 1));
//
//        ItemDetailResponse res = ItemDetailResponse.builder()
//                .itemId(itemId)
//                .itemName("황금바지")
//                .itemPrice(100000)
//                .description("당신도 입을 수 있어요!")
//                .hits(0)
//                .sellerId(1)
//                .sellerNickname("판매자1")
//                .createdAt(LocalDateTime.of(2024,5,10,0,0,0))
//                .expiredAt(LocalDateTime.of(2024,10,1,0,0,0))
//                .itemImageList(itemImageResponses)
//                .stockList(StockResponses)
//                .build();
//
//        when(itemService.getItemDetail(itemId)).thenReturn(res);
//
//
//        // perform: POST 요청을 보내고 결과를 검증
//        ResultActions resultActions = mockMvc.perform(get("/items/1")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(res))); // JSON 요청 바디 설정
//
//        // then: 응답 상태 및 반환된 JSON 필드 검증
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.item_id").value(itemId))
//                .andExpect(jsonPath("$.item_name").value(res.getItemName()))
//                .andExpect(jsonPath("$.item_price").value(res.getItemPrice()))
//                .andExpect(jsonPath("$.description").value(res.getDescription()))
//                .andExpect(jsonPath("$.hits").value(res.getHits()))
//                .andExpect(jsonPath("$.seller_id").value(res.getSellerId()))
//                .andExpect(jsonPath("$.seller_nickname").value(res.getSellerNickname()))
//                .andExpect(jsonPath("$.created_at").value(String.valueOf(res.getCreatedAt()).trim()))
//                .andExpect(jsonPath("$.expired_at").value(String.valueOf(res.getExpiredAt()).trim()))
//
//                .andExpect(jsonPath("$.item_image_list").isArray()) // 배열인지 검증
//                .andExpect(jsonPath("$.item_image_list.length()").value(2)) // 배열 사이즈가 동일한지
//
//                .andExpect(jsonPath("$.stock_list").isArray())
//                .andExpect(jsonPath("$.stock_list.length()").value(2));
//
//    }
//}