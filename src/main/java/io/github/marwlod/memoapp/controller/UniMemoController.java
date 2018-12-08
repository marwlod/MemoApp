package io.github.marwlod.memoapp.controller;

import io.github.marwlod.memoapp.entity.UniMemo;
import io.github.marwlod.memoapp.entity.User;
import io.github.marwlod.memoapp.service.UniMemoService;
import io.github.marwlod.memoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/uniMemo")
public class UniMemoController {

    private UniMemoService uniMemoService;

    private UserService userService;

    @Autowired
    public UniMemoController(UniMemoService uniMemoService, UserService userService) {
        this.uniMemoService = uniMemoService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public String listMemos(Model model) {
        List<UniMemo> uniMemos = uniMemoService.getUniMemosByOwner(getCurrentUser());
        model.addAttribute("uniMemos", uniMemos);
        return "uni-memo-list";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("uniMemo", new UniMemo());
        return "uni-memo-form";
    }

    @PostMapping("/saveMemo")
    public String saveMemo(@Valid @ModelAttribute("uniMemo") UniMemo uniMemo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "uni-memo-form";
        }
        // add current user as owner of the memo
        uniMemo.setOwner(getCurrentUser());

        // save memo to db
        uniMemoService.saveUniMemo(uniMemo);
        return "redirect:/uniMemo/list";
    }

    @GetMapping("/showUpdateForm")
    public String showUpdateForm(@RequestParam("uniMemoId") Long uniMemoId, Model model) {
        UniMemo uniMemo = uniMemoService.getUniMemoById(uniMemoId);
        model.addAttribute("uniMemo", uniMemo);
        return "uni-memo-form";
    }

    @GetMapping("/delete")
    public String deleteMemo(@RequestParam("uniMemoId") Long uniMemoId) {
        uniMemoService.deleteUniMemo(uniMemoId);
        return "redirect:/uniMemo/list";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findUserByEmail(authentication.getName());
    }
}
