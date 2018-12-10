package io.github.marwlod.memoapp.service;

import io.github.marwlod.memoapp.entity.UniMemo;
import io.github.marwlod.memoapp.entity.User;
import io.github.marwlod.memoapp.repository.UniMemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UniMemoServiceImpl implements UniMemoService {


    private UniMemoRepository uniMemoRepository;

    @Autowired
    public UniMemoServiceImpl(UniMemoRepository uniMemoRepository) {
        this.uniMemoRepository = uniMemoRepository;
    }

    @Override
    @Transactional
    public void saveUniMemo(UniMemo uniMemo) {
        uniMemoRepository.save(uniMemo);
    }

    @Override
    @Transactional
    public UniMemo getUniMemoById(Long uniMemoId) {
        return uniMemoRepository.findById(uniMemoId).orElse(null);
    }

    @Override
    @Transactional
    public void deleteUniMemo(Long uniMemoId) {
        uniMemoRepository.deleteById(uniMemoId);
    }

    @Override
    @Transactional
    public Page<UniMemo> getUniMemosByOwner(User user, Pageable pageable) {
        return uniMemoRepository.getUniMemosByOwner(user, pageable);
    }
}
