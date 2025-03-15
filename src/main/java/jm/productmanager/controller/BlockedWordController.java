package jm.productmanager.controller;

import jm.productmanager.dto.BlockedWordDTO;
import jm.productmanager.exception.MessageResponse;
import jm.productmanager.model.BlockedWord;
import jm.productmanager.service.BlockedWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blocked-words")
@RequiredArgsConstructor
public class BlockedWordController {
    private final BlockedWordService blockedWordService;

    @GetMapping
    public ResponseEntity<List<BlockedWord>> getBlockedWords() {
        return ResponseEntity.ok(blockedWordService.getBlockedWords());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlockedWord> getBlockedWord(@PathVariable UUID id) {
        return ResponseEntity.ok(blockedWordService.getBlockedWord(id));
    }

    @PostMapping
    public ResponseEntity<BlockedWord> createBlockedWord(@RequestBody BlockedWordDTO blockedWordDTO) {
        return ResponseEntity.ok(blockedWordService.createBlockedWord(blockedWordDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BlockedWord> updateBlockedWord(@PathVariable UUID id, @RequestBody BlockedWordDTO blockedWordDTO) {
        return ResponseEntity.ok(blockedWordService.updateBlockedWord(id, blockedWordDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteBlockedWord(@PathVariable UUID id) {
        return ResponseEntity.ok(blockedWordService.deleteBlockedWord(id));
    }
}
