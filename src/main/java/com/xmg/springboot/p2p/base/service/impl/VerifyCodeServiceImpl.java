package com.xmg.springboot.p2p.base.service.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.xmg.springboot.p2p.base.service.IVerifyCodeService;
import com.xmg.springboot.p2p.base.vo.VerifyCodeVO;
import com.xmg.springboot.p2p.util.Consts;
import com.xmg.springboot.p2p.util.DateUtil;

@Service
public class VerifyCodeServiceImpl implements IVerifyCodeService {
	@Value("${sms.url}")
	private String url;
	@Value("${sms.username}")
	private String username;
	@Value("${sms.password}")
	private String password;
	@Value("${sms.apikey}")
	private String apikey;

	public VerifyCodeVO sendVerifCode(String phoneNumber, VerifyCodeVO vo) {
		//获取session中的发送成功时间
		if (vo == null
				|| DateUtil.getSecondBetween(new Date(), vo.getSendDate()) > Consts.VERIFY_CODE_INTERVAL) {

			//如果没有获取到或者时间超过10秒,则重新发送

			//执行发送

			//1生成一个验证码
			String code = UUID.randomUUID().toString().substring(0, 4);
			//2执行发送
			/*	System.out.println("执行发送,验证码为:" + code + "请在"
						+ Consts.VERIFY_CODE_VALID_MIN + "分钟之内使用");*/
			try {
				//创建一个url对象
				URL url = new URL(this.url);
				//使用url创建一个连接对象
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				//设置相关属性
				conn.setRequestMethod("POST");
				//设置要输出实体内容
				conn.setDoOutput(true);
				//设置输出的内容
				//拼接内容
				StringBuilder sb = new StringBuilder(100).append("username=")
						.append(this.username).append("&password=")
						.append(password).append("&apikey=").append(apikey)
						.append("&phoneNumber=").append(phoneNumber)
						.append("&content=").append("你收到的验证码为:").append(code)
						.append(",请及时使用,有效时间为")
						.append(Consts.VERIFY_CODE_VALID_MIN).append("分钟");
				conn.getOutputStream().write(sb.toString().getBytes());
				//发送
				conn.connect();
				//获取响应内容
				String ret = StreamUtils.copyToString(conn.getInputStream(),
						Charset.forName("UTF-8"));
				if (ret.indexOf("success:") == 0) {
					return new VerifyCodeVO(new Date(), code, phoneNumber);
					//UserContext
				} else {

				}
			} catch (Exception e) {

			}

			//3把发送时间,验证码和手机号码放到session中
			//return new VerifyCodeVO(new Date(), code, phoneNumber);
		}

		return null;
	}
}
