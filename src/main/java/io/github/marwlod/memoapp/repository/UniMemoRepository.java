package io.github.marwlod.memoapp.repository;

import io.github.marwlod.memoapp.entity.UniMemo;
import io.github.marwlod.memoapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniMemoRepository extends PagingAndSortingRepository<UniMemo, Long> {

    Page<UniMemo> getUniMemosByOwner(User user, Pageable pageable);
}
