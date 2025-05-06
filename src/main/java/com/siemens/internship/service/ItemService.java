package com.siemens.internship.service;

import com.siemens.internship.model.Item;
import com.siemens.internship.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;

    private AtomicInteger processedCount = new AtomicInteger(0);


    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public Item save(Item item) {
        return itemRepository.save(item);
    }

    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }


    /**
     * Your Tasks
     * Identify all concurrency and asynchronous programming issues in the code
     * Fix the implementation to ensure:
     * All items are properly processed before the CompletableFuture completes
     * Thread safety for all shared state
     * Proper error handling and propagation
     * Efficient use of system resources
     * Correct use of Spring's @Async annotation
     * Add appropriate comments explaining your changes and why they fix the issues
     * Write a brief explanation of what was wrong with the original implementation
     *
     * Hints
     * Consider how CompletableFuture composition can help coordinate multiple async operations
     * Think about appropriate thread-safe collections
     * Examine how errors are handled and propagated
     * Consider the interaction between Spring's @Async and CompletableFuture
     */
    //the data structures for counting the processed elements and for storing them wasn't good for concurrent operations. the atomic and concurrent linked queue have safety for concurrent usage(
    //like a lock, only one unit can access it and edit)
    //
    //the return did not wait for all the threads to finish.
    //
    //so, thread safety is ensured by using ConcurrentLinkedQueue, AtomicInteger and completableFuture
    //error messages are documented but are not intrerupting.
    @Async
    public CompletableFuture<List<Item>> processItemsAsync() {

        List<Long> itemIds = itemRepository.findAllIds();
        ConcurrentLinkedQueue<Item> processedItems = new ConcurrentLinkedQueue<>();


        List<CompletableFuture<Void>> futures = itemIds.stream().map(id ->
            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(100);

                    Optional<Item> item = itemRepository.findById(id);
                    item.ifPresent(items -> {
                        items.setStatus("PROCESSED");
                        itemRepository.save(items);
                        processedCount.incrementAndGet();
                        processedItems.add(items);
                    });

                } catch (InterruptedException e) {
                    System.out.println("Error for id: " + id +" with msg:"+ e.getMessage());
                    log.error("Error for id: " + id +" with msg:"+ e.getMessage());
                }
            })).toList();


        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenApply(v -> List.copyOf(processedItems));
    }

}

