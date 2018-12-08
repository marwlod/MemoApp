package io.github.marwlod.memoapp.service;

import io.github.marwlod.memoapp.entity.UniMemo;
import io.github.marwlod.memoapp.entity.User;
import io.github.marwlod.memoapp.repository.UniMemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniMemoServiceImpl implements UniMemoService {


    private UniMemoRepository uniMemoRepository;

    @Autowired
    public UniMemoServiceImpl(UniMemoRepository uniMemoRepository) {
        this.uniMemoRepository = uniMemoRepository;
    }

    @Override
    public List<UniMemo> getUniMemos() {
        return (List<UniMemo>) uniMemoRepository.findAll();
    }

    @Override
    public void saveUniMemo(UniMemo uniMemo) {
        uniMemoRepository.save(uniMemo);
    }

    @Override
    public UniMemo getUniMemoById(Long uniMemoId) {
        return uniMemoRepository.findById(uniMemoId).orElse(null);
    }

    @Override
    public void deleteUniMemo(Long uniMemoId) {
        uniMemoRepository.deleteById(uniMemoId);
    }

    @Override
    public List<UniMemo> getUniMemosByOwner(User user) {
        return uniMemoRepository.getUniMemosByOwner(user);
    }
}
