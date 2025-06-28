package com.jowety.expenseapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This controller acts as a fallback for Single Page Applications (SPAs).
 * It ensures that any request not handled by a Spring REST endpoint or a static resource
 * will be forwarded to the Angular application's index.html, allowing the Angular router to take over.
 */
@Controller
public class SpaFallbackController {

    /**
     * Catches all requests that are not explicitly matched by other Spring MVC controllers
     * (e.g., your @RestController APIs) or by Spring Boot's static resource handling.
     * Requests for static files (which typically contain a dot in their name, like .js, .css, .png)
     * and API endpoints (starting with /api/) are explicitly excluded.
     *
     * @return A "forward:" view name that directs the request to the Angular application's index.html.
     */
    @GetMapping(value = "{path:^(?!api|.*\\..*$).*$}") // Regex to exclude /api paths and paths with a file extension
    public String redirect() {
        return "forward:/index.html";
    }
}
