package io.github.marwlod.memoapp.service;

import io.github.marwlod.memoapp.entity.UniMemo;
import io.github.marwlod.memoapp.entity.User;

import java.util.List;

public interface UniMemoService {

    void saveUniMemo(UniMemo uniMemo);

    UniMemo getUniMemoById(Long uniMemoId);

    void deleteUniMemo(Long uniMemoId);

    List<UniMemo> getUniMemosByOwner(User user);
}
