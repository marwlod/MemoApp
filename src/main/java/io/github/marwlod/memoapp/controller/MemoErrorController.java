package io.github.marwlod.memoapp.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MemoErrorController implements ErrorController {

    // map error types to appropriate error pages
    @GetMapping(value = "/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int code = Integer.valueOf(status.toString());
            if (code == HttpStatus.FORBIDDEN.value()) {
                return "error/403";
            } else if (code == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            } else if (code == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/500";
            }
        }
        return "error/default";
    }

    // default error path
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
