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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    public String listMemos(@PageableDefault(sort = {"uniMemoDetails.dueDate", "shortDescription"},
                                            direction = Sort.Direction.ASC) Pageable pageable,
                            Model model,
                            HttpSession session) {
        // get page of logged user's memos
        Page<UniMemo> page = uniMemoService.getUniMemosByOwner(getCurrentUser(), pageable);
        // add page and sortProperties session attributes <- used for getting back to this exact url
        session.setAttribute("page", page);
        List<Sort.Order> sortOrders = page.getSort().stream().collect(Collectors.toList());

        // extract sort parameters and add them to the model for processing
        if (sortOrders.size() > 1) {
            Sort.Order firstOrder = sortOrders.get(0);
            Sort.Order secondOrder = sortOrders.get(1);
            session.setAttribute("sortProperties", firstOrder.getProperty() + "," + secondOrder.getProperty() + "," + firstOrder.getDirection());
            model.addAttribute("sortAsc", firstOrder.getDirection() == Sort.Direction.ASC);
        }
        return "unimemo-list";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("uniMemo", new UniMemo());
        return "unimemo-form";
    }

    @PostMapping("/saveMemo")
    public String saveMemo(@Valid @ModelAttribute("uniMemo") UniMemo uniMemo,
                           BindingResult bindingResult,
                           HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "unimemo-form";
        }
        // add current user as owner of the memo
        uniMemo.setOwner(getCurrentUser());
        uniMemoService.saveUniMemo(uniMemo);

        // add last used url parameters for memo list page
        String params = constructParamList(session);
        return "redirect:/uniMemo/list" + params;
    }

    @GetMapping("/showUpdateForm")
    public String showUpdateForm(@RequestParam("uniMemoId") Long uniMemoId,
                                 Model model) {
        // update form for one memo that was clicked at
        UniMemo uniMemo = uniMemoService.getUniMemoById(uniMemoId);
        model.addAttribute("uniMemo", uniMemo);
        return "unimemo-form";
    }

    @GetMapping("/delete")
    public String deleteMemo(@RequestParam("uniMemoId") Long uniMemoId,
                             HttpSession session) {
        uniMemoService.deleteUniMemo(uniMemoId);
        String params = constructParamList(session);
        return "redirect:/uniMemo/list" + params;
    }

    @GetMapping("/showDetails")
    public String showDetails(@RequestParam("uniMemoId") Long uniMemoId,
                              Model model) {
        UniMemo uniMemo = uniMemoService.getUniMemoById(uniMemoId);
        model.addAttribute("uniMemo", uniMemo);
        return "unimemo-details";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findUserByEmail(authentication.getName());
    }

    // create param list to append to url from updated session attributes
    private String constructParamList(HttpSession session) {
        Page page = (Page) session.getAttribute("page");
        return "?page=" + page.getNumber() + "&size=" + page.getSize() + "&sort=" + session.getAttribute("sortProperties").toString();
    }
}
