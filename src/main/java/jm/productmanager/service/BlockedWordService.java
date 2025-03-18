package jm.productmanager.service;

import jm.productmanager.dto.BlockedWordDTO;
import jm.productmanager.exception.MessageResponse;
import jm.productmanager.model.BlockedWord;

import java.util.List;
import java.util.UUID;

public interface BlockedWordService {
    List<BlockedWord> getBlockedWords();

    BlockedWord getBlockedWord(UUID id);

    boolean containsBlockedWord(String name);

    BlockedWord createBlockedWord(BlockedWordDTO blockedWordDTO);

    BlockedWord updateBlockedWord(UUID id, BlockedWordDTO blockedWordDTO);

    MessageResponse deleteBlockedWord(UUID id);
}
