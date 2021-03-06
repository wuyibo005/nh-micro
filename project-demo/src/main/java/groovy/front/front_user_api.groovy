package groovy.front

import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GroovyExecUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.awt.event.ItemEvent;
import java.sql.PreparedStatement;
import groovy.json.*;
import com.nh.micro.db.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.nh.micro.cache.base.*;
import com.nh.micro.db.Cutil;
import com.nh.micro.db.Cobj;
import com.nh.micro.db.MicroDbHolder;


import org.springframework.jdbc.support.rowset.*;
import groovy.template.MicroMvcTemplate;
import javax.servlet.http.HttpSession;

class FrontProduct extends MicroMvcTemplate{
public String pageName="listDictionaryInfo";
public String tableName="t_front_user";


public String getPageName(HttpServletRequest httpRequest){
	return pageName;
}
public String getTableName(HttpServletRequest httpRequest){
	return tableName;
}

public void queryInfoByCodeGo(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
	HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
	HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
	HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
	String nhUserName=GroovyExecUtil.execGroovyRetObj("front_user_login", "getUserCode", 
		gInputParam,gOutputParam,gContextParam);
	Map infoMap=getInfoByBizIdService(nhUserName,"t_front_user","user_code");
	httpRequest.setAttribute("userInfo", infoMap);
	
	Map cardMap=getInfoByBizIdService(nhUserName,"t_front_user_bankcard","user_code");
	httpRequest.setAttribute("cardInfo", cardMap);
	
	httpRequest.getRequestDispatcher("/front-page/personal_center.jsp").forward(httpRequest, httpResponse);
	httpRequest.setAttribute("forwardFlag", "true");

}

public void modifyPassword(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
	HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
	HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
	HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
	String nhUserName=GroovyExecUtil.execGroovyRetObj("front_user_login", "getUserCode",
		gInputParam,gOutputParam,gContextParam);
	String oldpwd=httpRequest.getParameter("oldpwd");
	String newpwd=httpRequest.getParameter("newpwd");
	Map userInfo=getInfoByBizIdService(nhUserName,"t_front_user","user_code");
	String password=userInfo.get("password");
	if(!oldpwd.equals(password)){
		gOutputParam.setResultStatus(1);
		return;
	}
	Map paramMap=new HashMap();
	paramMap.put("password", newpwd);
	updateInfoByBizIdService(nhUserName,"t_front_user","user_code",paramMap);
	

}

public void resetPassword(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
	HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
	HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
	HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");


	String phoneNo=httpRequest.getParameter("phoneNo");
	String msgCode=httpRequest.getParameter("msgCode");
	String newPassword=httpRequest.getParameter("newPassword");
	String confirmPassword=httpRequest.getParameter("confirmPassword");

	Map paramMap=new HashMap();
	paramMap.put("password", newPassword);
	updateInfoByBizIdService(phoneNo,"t_front_user","user_code",paramMap);
	

}
}
