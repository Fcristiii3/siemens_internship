package com.siemens.internship;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.internship.controller.ItemController;
import com.siemens.internship.model.Item;
import com.siemens.internship.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper objectMapper;



    @Test
    void allItems() throws Exception {
        Item item = new Item(1L,"Name","A","ACCEPT","test@gmail.com");
        Mockito.when(itemService.findAll()).thenReturn(List.of(item));

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Name"));
    }
    @Test
    void create() throws Exception {
        Item item = new Item(null,"Name","A","ACCEPT","test@gmail.com");
        Item saved = new Item(1L,"Name2","A","ACCEPT2","test@gmail.com");

        Mockito.when(itemService.save(Mockito.any(Item.class))).thenReturn(saved);

        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void itemId() throws Exception {
        Item item = new Item(1L,"Name","A","ACCEPT","test@gmail.com");
        Mockito.when(itemService.findById(1L)).thenReturn(Optional.of(item));

        mockMvc.perform(get("/api/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@gmail.com"));
    }
    /*
    @Test
    void processItem() throws Exception {
        Item item = new Item(1L, "Name", "A", "CEVA", "test@gmail.com");
        itemService.save(item);
        Mockito.when(itemService.processItemsAsync())
                .thenReturn(CompletableFuture.completedFuture(List.of(item)));



        mockMvc.perform(get("/api/items/process"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PROCESSED"));
    }
    */

}
