package com.siemens.internship;

import com.siemens.internship.model.Item;
import com.siemens.internship.repository.ItemRepository;
import com.siemens.internship.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemServiceTest {

    private ItemService itemService;
    private ItemRepository itemRepository;

    @BeforeEach
    void setup(){
        itemRepository = mock(ItemRepository.class);
        itemService = new ItemService(itemRepository);
    }

    @Test
    void testFindById(){
        Item item = new Item(1L,"Name","A","ACCEPT","test@gmail.com");
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Optional<Item> found = itemService.findById(1L);
        assertTrue(found.isPresent());
        assertEquals("Name",found.get().getName());
    }

    @Test
    void testSave(){
        Item item = new Item(null,"Name","A","ACCEPT","test@gmail.com");
        Item saved = new Item(1L,"Name","A","ACCEPT","test@gmail.com");

        when(itemRepository.save(item)).thenReturn(saved);
        Item savedSaved = itemService.save(item);
        assertEquals("Name",saved.getName());
    }
}
