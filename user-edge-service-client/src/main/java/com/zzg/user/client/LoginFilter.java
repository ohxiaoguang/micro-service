package com.zzg.user.client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zzg.thrift.user.dto.UserDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;



import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @Author ZZG
 * @Date 2019/10/8 14:33
 * @Version v1.0.0
 */
public abstract class LoginFilter implements Filter {

    private static Cache<String,UserDTO> cache =
            CacheBuilder.newBuilder().maximumSize(10000)
            .expireAfterWrite(3, TimeUnit.MINUTES).build();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String token = request.getParameter("token");
        if (StringUtils.isBlank(token)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("token")) {
                        token = cookie.getValue();
                    }
                }
            }
        }

        UserDTO userDTO =null;
        if (StringUtils.isNotBlank(token)) {
            userDTO = cache.getIfPresent(token);
            if (userDTO == null) {
                userDTO = requestUserInfo(token);
                cache.put(token,userDTO);
            }

        }
        if (userDTO==null){
            response.getWriter().write("login please");
            return;
        }

        login(request,response,userDTO);

        filterChain.doFilter(request,response);

    }

    protected abstract void login(HttpServletRequest request, HttpServletResponse response, UserDTO userDTO);

    private UserDTO requestUserInfo(String token) {
        String url = "http://127.0.0.1:9091/user/authentication";

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.addHeader("token",token);
        InputStream inputStream = null;
        try {
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode()!= HttpStatus.SC_OK) {
                throw new RuntimeException("request user info failed! StatusLine:"+response.getStatusLine());
            }
            inputStream = response.getEntity().getContent();
            byte[] temp = new byte[1024];
            StringBuilder sb = new StringBuilder();
            int len = 0;
            while ((len = inputStream.read(temp))>0){
                sb.append(new String(temp,0,len));
            }
            UserDTO userDTO = new ObjectMapper().readValue(sb.toString(), UserDTO.class);
            return userDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    public void destroy() {

    }
}
