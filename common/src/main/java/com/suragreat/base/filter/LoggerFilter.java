package com.suragreat.base.filter;

import com.alibaba.fastjson.JSONObject;
import com.suragreat.base.constant.ServerErrorEnum;
import com.suragreat.base.exception.ServerException;
import com.suragreat.base.model.ResponseContainer;
import com.suragreat.base.util.IpUtil;
import com.suragreat.base.util.LogUtil;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.util.NestedServletException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class LoggerFilter implements Filter {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(LoggerFilter.class);
    private final String SESSIONID_KEY = "sid";

    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("LoggerFilter init");
    }

    /**
     * 接口执行时间
     * 参数输出
     * 错误的统一处理
     *
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     * 创建时间：2015-6-30  上午11:02:20
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            MDC.put(SESSIONID_KEY, UUID.randomUUID().toString());
            long startTime = System.currentTimeMillis();
            LogUtil.logStartTimeStamp(startTime);

            HttpServletRequest httprequest = (HttpServletRequest) request;
            HttpServletResponse httpresponse = (HttpServletResponse) response;
            String method = req.getMethod();
            String ip = IpUtil.getIpAddr(httprequest);
            Map<String, String[]> params = request.getParameterMap();
            StringBuilder queryString = new StringBuilder();
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    queryString.append(key);
                    queryString.append("=");
                    queryString.append(values[i]);
                    queryString.append("&");
                }
            }

            if (queryString.length() > 1) {// 去掉最后一个 '&'
                queryString.deleteCharAt(queryString.length() - 1);
            }
            logger.info("Request received from [{}], Method[{}], RequestURI:{}, Parameter: {}", ip, method,
                    req.getRequestURI(), queryString);

            PrintWriter out = null;
            try {
                chain.doFilter(request, response);
            } catch (Throwable e) {
                httpresponse.setCharacterEncoding("UTF-8");
                httpresponse.setContentType("application/json;charset=utf-8");
                httpresponse.addHeader("Content-Type", "application/json;charset=utf-8");
                ServerException serverException = getServerException(e);
                ResponseContainer resObject = new ResponseContainer(serverException);
                resObject.setTcost(LogUtil.getTimeCost());
                String responseJsonString = JSONObject.toJSONString(resObject);

                LogUtil.logInThreadLocal(responseJsonString);
                LogUtil.logInThreadLocal(e);

                out = httpresponse.getWriter();
                out.print(responseJsonString);
            } finally {
                StringBuilder logBuilder = new StringBuilder();
                logBuilder.append(
                        "Request finished with time: " + LogUtil.getTimeCost() + "ms");
                logBuilder.append("\n================================BEGIN=====================================");
                logBuilder.append("\nMethod[" + method + "];RequestURI:" + req.getRequestURI());
                List<String> logs = LogUtil.getLogs();
                for (String log : logs) {
                    logBuilder.append("\n").append(log);
                }
                logBuilder.append("\n********************************END***************************************");
                logger.info(logBuilder.toString());
                if (out != null) {
                    out.flush();
                    out.close();
                }
            }
        } catch (Exception e) {
            logger.error("log filter exception.", e);
        } finally {
            MDC.clear();
        }
    }

    private ServerException getServerException(Throwable e) {
        if (e instanceof ServerException) {
            return (ServerException) e;
        } else if (e instanceof NestedServletException) {
            return getServerException(((NestedServletException) e).getCause());
        } else {
            return new ServerException(ServerErrorEnum.INTERNAL_ERROR, e);
        }
    }

    public void destroy() {
        logger.info("LoggerFilter destory");
    }

}