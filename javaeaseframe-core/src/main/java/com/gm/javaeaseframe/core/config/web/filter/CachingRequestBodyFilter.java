package com.gm.javaeaseframe.core.config.web.filter;

import java.io.IOException;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

public class CachingRequestBodyFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest requestToUse = request;
        if (!(request instanceof ContentCachingRequestWrapper)) {
        	requestToUse = new ContentCachingRequestWrapper(request);
        }
        HttpServletResponse responseToUse = response;
        if (!(response instanceof ContentCachingResponseWrapper)) {
        	responseToUse =  new ContentCachingResponseWrapper(response);
        }
        try {
        	filterChain.doFilter(requestToUse, responseToUse);
        } finally {
        	if (responseToUse instanceof ContentCachingResponseWrapper) {
        		final ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper)responseToUse;
        		if (request.isAsyncStarted()) { // 异步场景的支持
        	        request.getAsyncContext().addListener(new AsyncListener() {
        	            public void onComplete(AsyncEvent asyncEvent) throws IOException {
        	                // 4. 注意返回之前一定要调用这行代码，将缓存写入输出流
        	            	responseWrapper.copyBodyToResponse();
        	            }
        	            public void onTimeout(AsyncEvent asyncEvent) throws IOException {
        	            }

        	            public void onError(AsyncEvent asyncEvent) throws IOException {
        	            }

        	            public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
        	            }
        	        });
        	    } else {
        	        // 4. 注意返回之前一定要调用这行代码，将缓存写入输出流
        	    	responseWrapper.copyBodyToResponse();
        	    }
        	}
        }
	}

}
