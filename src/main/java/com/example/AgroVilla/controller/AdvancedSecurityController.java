package com.example.AgroVilla.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/secured")
public class AdvancedSecurityController {

    /**
     * Demonstrates method-level security with @PreAuthorize This method is only
     * accessible to users with REGULAR_USER role or higher
     */
    @GetMapping("/resources")
    @PreAuthorize("hasRole('REGULAR_USER')")
    public Map<String, String> getResources() {
        Map<String, String> resources = new HashMap<>();
        resources.put("resource1", "Resource data 1");
        resources.put("resource2", "Resource data 2");
        return resources;
    }

    /**
     * Demonstrates method-level security with SpEL expressions This method requires
     * both MANAGER role and a specific authority
     */
    @GetMapping("/reports/{reportId}")
    @PreAuthorize("hasRole('MANAGER') and hasAuthority('GENERATE_REPORT')")
    public Map<String, Object> getReport(@PathVariable String reportId) {
        Map<String, Object> report = new HashMap<>();
        report.put("id", reportId);
        report.put("title", "Quarterly Performance Report");
        report.put("content", "This report contains sensitive business data...");
        report.put("generated", "2025-04-10");
        return report;
    }

    /**
     * Demonstrates access to the currently authenticated user and method-level
     * security with SpEL expressions
     */
    @GetMapping("/account")
    public Map<String, String> getAccountInfo(@AuthenticationPrincipal UserDetails userDetails) {
        Map<String, String> accountInfo = new HashMap<>();
        accountInfo.put("username", userDetails.getUsername());
        accountInfo.put("authorities", userDetails.getAuthorities().toString());
        return accountInfo;
    }

    /**
     * Demonstrates method security with principal-based checks Only allows access
     * if the authenticated user matches the requested username
     */
    @GetMapping("/users/{username}/details")
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    public Map<String, String> getUserDetails(@PathVariable String username) {
        // In a real app, you would fetch this from a database
        Map<String, String> userDetails = new HashMap<>();
        userDetails.put("username", username);
        userDetails.put("email", username + "@example.com");
        userDetails.put("joinDate", "2024-01-15");
        userDetails.put("status", "Active");
        return userDetails;
    }

    /**
     * POST endpoint with role-based security Only managers can create new projects
     */
    @PostMapping("/projects")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public Map<String, String> createProject(@RequestBody Map<String, String> projectData) {
        // In a real app, you would save to a database
        Map<String, String> response = new HashMap<>();
        response.put("projectId", "PRJ-" + System.currentTimeMillis());
        response.put("name", projectData.get("name"));
        response.put("status", "Created");
        response.put("message", "Project successfully created");
        return response;
    }

    /**
     * PUT endpoint with combined role and expression-based security Only admins can
     * delete resources, or the owner of the resource
     */
    @DeleteMapping("/resources/{resourceId}")
    @PreAuthorize("hasRole('ADMIN') or @resourceOwnershipService.isOwner(authentication.principal, #resourceId)")
    public Map<String, String> deleteResource(@PathVariable String resourceId) {
        // In a real app, you would delete from a database
        Map<String, String> response = new HashMap<>();
        response.put("resourceId", resourceId);
        response.put("status", "Deleted");
        response.put("message", "Resource successfully deleted");
        return response;
    }

    /**
     * Demonstrates a method that requires multiple roles
     */
    @GetMapping("/system/config")
    @PreAuthorize("hasRole('REGULAR_USER') and hasAuthority('SENIOR')")
    public Map<String, String> getSystemConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("maintenance", "false");
        config.put("version", "2.5.3");
        config.put("maxUploadSize", "100MB");
        config.put("auditEnabled", "true");
        return config;
    }
}