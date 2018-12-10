package io.github.marwlod.memoapp.service;

import io.github.marwlod.memoapp.entity.UniMemo;
import io.github.marwlod.memoapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UniMemoService {

    void saveUniMemo(UniMemo uniMemo);

    UniMemo getUniMemoById(Long uniMemoId);

    void deleteUniMemo(Long uniMemoId);

    Page<UniMemo> getUniMemosByOwner(User user, Pageable pageable);
}
