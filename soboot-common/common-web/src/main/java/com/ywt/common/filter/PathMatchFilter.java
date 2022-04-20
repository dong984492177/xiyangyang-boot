package com.ywt.common.filter;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class PathMatchFilter extends CommonFilter {

    private final List<String> includePatterns = new ArrayList<>();
    private final List<String> excludePatterns = new ArrayList<>();

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public PathMatchFilter addPathPatterns(String... patterns) {
        return this.addPathPatterns(Arrays.asList(patterns));
    }

    private PathMatchFilter addPathPatterns(List<String> patterns) {
        this.includePatterns.addAll(patterns);
        return this;
    }

    public PathMatchFilter excludePathPatterns(String... patterns) {
        return this.excludePathPatterns(Arrays.asList(patterns));
    }

    private PathMatchFilter excludePathPatterns(List<String> patterns) {
        this.excludePatterns.addAll(patterns);
        return this;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
       return !this.matches(request.getRequestURI());
    }

    private boolean matches(String lookupPath) {
        if (!ObjectUtils.isEmpty(this.excludePatterns)) {
            for (String pattern : this.excludePatterns) {
                if (this.pathMatcher.match(pattern, lookupPath)) {
                    return false;
                }
            }
        }

        if (ObjectUtils.isEmpty(this.includePatterns)) {
            return true;
        } else {
            for (String pattern : this.includePatterns) {
                if (this.pathMatcher.match(pattern, lookupPath)) {
                    return true;
                }
            }
            return false;
        }
    }
}
