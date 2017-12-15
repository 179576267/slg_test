package com.douqu.game.main.listener;

import com.alibaba.fastjson.JSONObject;
import com.douqu.game.core.e.ReturnMessage;
import com.douqu.game.main.util.MsgUtils;
import com.douqu.game.core.web.request.BaseRequestDto;
import com.douqu.game.core.web.response.BaseResponseDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(urlPatterns = "/*", filterName = "controllerFilter")
public class ControllerListener implements Filter {

    Logger logger = Logger.getLogger(ControllerListener.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("init ControllerFilter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        logger.debug("Request Uri: " + request.getRequestURI()+"  "+request.getContentType());
        if(request.getContentType() != null && request.getContentType().startsWith("application/json"))
        {
            BufferedRequestWrapper webRequest = new BufferedRequestWrapper(request);
            boolean error = false;
            String resultJson = webRequest.getInputStreamString();
            if(resultJson.length() < 100)
                logger.debug("Request Param:" + resultJson);
            if(!StringUtils.isNullOrEmpty(resultJson))
            {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                BaseRequestDto baseRequestDto = objectMapper.readValue(resultJson, BaseRequestDto.class);
                if(baseRequestDto.getBaseParam() != null)
                {
//                    PlayerService playerService = SpringContext.getBean(PlayerService.class);
//                    PlayerModel playerModel = playerService.getPlayerById(baseRequestDto.getBaseParam().getUid());
//                    if(playerModel == null || playerModel.getToken() == null)
//                    {
//                        logger.info("用户不存在:"+baseRequestDto.getBaseParam());
//                        error = true;
//                    }
//                    else
//                    {
//                        error = !playerModel.getToken().equals(baseRequestDto.getBaseParam().getKey());
//                        if(error)
//                        {
//                            logger.info("localToken:" + playerModel.getToken()+" paramToken:" + baseRequestDto.getBaseParam().getKey());
//                        }
//                    }
                }
                else
                {
                    error = true;
                }
            }
            else
            {
                error = true;
            }

            if(error)
            {
                logger.info("非法请求:" + request.getRequestURI());
                PrintWriter out = servletResponse.getWriter();
                BaseResponseDto response = MsgUtils.createMsg(ReturnMessage.ILLEGAL);
                out.write(JSONObject.toJSONString(response));
                out.flush();
                return;
            }

            filterChain.doFilter(webRequest, servletResponse);
        }
        else
        {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}