package com.example.shoppingmall.domain.item.application;

import com.example.shoppingmall.domain.item.dao.ItemRepository;
import com.example.shoppingmall.domain.item.dto.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    public Page<ItemResponse> getItemMainPage(int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 12, Sort.by(DESC, "created_at"));

        return itemRepository
                .findAll(pageRequest).map(ItemResponse::new);
    }
}
