package com.storix.storix_api.domains.hashtag.adaptor;

import com.storix.storix_api.domains.hashtag.dto.HashtagInfo;
import com.storix.storix_api.domains.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HashtagAdaptor {

    private final HashtagRepository hashtagRepository;

    public Map<Long, List<String>> findHashTagsByWorksIds (List<Long> worksIds){
        if (worksIds == null || worksIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<HashtagInfo> hashtags = hashtagRepository.findAllByWorksIds(worksIds);

        return hashtags.stream()
                .collect(Collectors.groupingBy(
                        HashtagInfo::worksId,
                        LinkedHashMap::new,
                        Collectors.mapping(HashtagInfo::hashtagName, Collectors.toList())
                ));
    }
}
