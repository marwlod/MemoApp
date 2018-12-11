package io.github.marwlod.memoapp.controller;

import io.github.marwlod.memoapp.entity.UniMemo;
import io.github.marwlod.memoapp.entity.User;
import io.github.marwlod.memoapp.service.UniMemoService;
import io.github.marwlod.memoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public String listMemos(@PageableDefault(
            size = 5,
            sort = {"uniMemoDetails.dueDate", "shortDescription"},
            direction = Sort.Direction.ASC)
                                        Pageable pageable, Model model) {
        Page<UniMemo> page = uniMemoService.getUniMemosByOwner(getCurrentUser(), pageable);
        model.addAttribute("page", page);
        return "unimemo-list";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("uniMemo", new UniMemo());
        return "unimemo-form";
    }

    @PostMapping("/saveMemo")
    public String saveMemo(@Valid @ModelAttribute("uniMemo") UniMemo uniMemo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "unimemo-form";
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
        return "unimemo-form";
    }

    @GetMapping("/delete")
    public String deleteMemo(@RequestParam("uniMemoId") Long uniMemoId) {
        uniMemoService.deleteUniMemo(uniMemoId);
        return "redirect:/uniMemo/list";
    }

    @GetMapping("/showDetails")
    public String showDetails(@RequestParam("uniMemoId") Long uniMemoId, Model model) {
        UniMemo uniMemo = uniMemoService.getUniMemoById(uniMemoId);
        model.addAttribute("uniMemo", uniMemo);
        return "unimemo-details";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findUserByEmail(authentication.getName());
    }
}
