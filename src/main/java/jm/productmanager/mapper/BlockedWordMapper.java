package jm.productmanager.mapper;

import jm.productmanager.dto.BlockedWordDTO;
import jm.productmanager.model.BlockedWord;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BlockedWordMapper {
    @Mapping(target = "id", ignore = true)
    BlockedWord mapToEntity(BlockedWordDTO blockedWordDTO);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBlockedWord(@MappingTarget BlockedWord blockedWord, BlockedWordDTO blockedWordDTO);
}
