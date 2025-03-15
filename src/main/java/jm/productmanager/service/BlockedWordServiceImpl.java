package jm.productmanager.service;

import jm.productmanager.dto.BlockedWordDTO;
import jm.productmanager.exception.BlockedWordNotFoundException;
import jm.productmanager.exception.MessageResponse;
import jm.productmanager.mapper.BlockedWordMapper;
import jm.productmanager.model.BlockedWord;
import jm.productmanager.repository.BlockedWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlockedWordServiceImpl implements BlockedWordService {
    private final BlockedWordRepository blockedWordRepository;
    private final BlockedWordMapper blockedWordMapper;

    @Override
    public List<BlockedWord> getBlockedWords() {
        return blockedWordRepository.findAll();
    }

    @Override
    public BlockedWord getBlockedWord(UUID id) {
        return blockedWordRepository.findById(id)
                       .orElseThrow(() -> new BlockedWordNotFoundException(id));
    }

    @Override
    public boolean existsByName(String name) {
        return blockedWordRepository.existsByName(name);
    }

    @Override
    public BlockedWord createBlockedWord(BlockedWordDTO blockedWordDTO) {
        validateBlockedWordDTO(blockedWordDTO);
        return blockedWordRepository.save(blockedWordMapper.mapToEntity(blockedWordDTO));
    }

    @Override
    public BlockedWord updateBlockedWord(UUID id, BlockedWordDTO blockedWordDTO) {
        BlockedWord blockedWord = getBlockedWord(id);
        validateBlockedWordDTO(blockedWordDTO);
        blockedWordMapper.updateBlockedWord(blockedWord, blockedWordDTO);
        return blockedWordRepository.save(blockedWord);
    }

    @Override
    public MessageResponse deleteBlockedWord(UUID id) {
        blockedWordRepository.deleteById(id);
        return new MessageResponse("Blocked word with id: " + id + " has been deleted");
    }

    private void validateBlockedWordDTO(BlockedWordDTO categoryDTO) {
        if (categoryDTO.getName() == null || !categoryDTO.getName().matches("^[a-zA-Z0-9]{3,20}$")) {
            throw new IllegalArgumentException("Product name should be between 3 and 20 characters and contain only letters and numbers");
        }
        if (blockedWordRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Blocked word with name " + categoryDTO.getName() + " already exists");
        }
    }
}
