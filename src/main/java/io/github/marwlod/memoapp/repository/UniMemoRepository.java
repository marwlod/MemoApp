package io.github.marwlod.memoapp.repository;

import io.github.marwlod.memoapp.entity.UniMemo;
import io.github.marwlod.memoapp.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniMemoRepository extends PagingAndSortingRepository<UniMemo, Long> {
    List<UniMemo> getUniMemosByOwner(User user);
}
